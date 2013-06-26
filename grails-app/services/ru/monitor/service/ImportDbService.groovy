package ru.monitor.service

import groovy.sql.Sql
import org.hibernate.StatelessSession
import org.hibernate.Transaction
import ru.monitor.db.SQLiteConnectionFactory
import ru.monitor.model.EtalonDir
import ru.monitor.model.MonitorGroup
import ru.monitor.model.MonitorItem
import ru.monitor.model.Patt
import ru.monitor.model.ServerItem

/**
 * Сервис импорта данных файла проверки
 *
 * @since 0.1
 */
class ImportDbService {

    /** Сервис чтения данных SQLite */
    def readDbService
    /** Фабрика открытия сессий работы с БД */
    def sessionFactory


    def importDb(ServerItem serverItem, String filePath) {
        Sql sql = SQLiteConnectionFactory.getConnection(filePath)
        try {
            importMonitorGroups(serverItem, sql)
            importMonitorItems(sql)
            importPatts(sql)
            importEtalonDirs(sql)

//            def etalonFiles = readDbService.getEtalonFiles(sql)
//            def etalonAces = readDbService.getEtalonAces(sql)
//            def checkRuns = readDbService.getCheckRuns(sql)
//            def checkFiles = readDbService.getCheckFiles(sql)
//            def checkAces = readDbService.getCheckAces(sql)
        } finally {
            sql.close()
        }
    }

    /**
     * Импорт групп проверки для сервера
     *
     * @param serverItem Сервер проверки
     * @param sql БД
     * @return
     */
    def importMonitorGroups(ServerItem serverItem, Sql sql) {
        def monitorGroups = readDbService.getMonitorGroups(sql)
        // Создаем все группы
        def newGroups = []
        monitorGroups.each {
            newGroups << new MonitorGroup(name: it.grpname, etalonId: it.grpid, serverItem: serverItem)
        }

        // Связываем с подгруппами
        monitorGroups.each {
            def mg = (MonitorGroup) newGroups.find({g -> g.etalonId == it.grpid})
            newGroups.findAll({g -> it.groups.contains(g.name)}).each { MonitorGroup g ->
                mg.addToGroups(g)
            }
        }

        // Сохраняем
        newGroups*.save()
    }

    /**
     * Импорт элементов проверки
     *
     * @param serverItem Сервер проверки
     * @param sql БД
     */
    def importMonitorItems(Sql sql) {
        def monitorItems = readDbService.getMonitorItems(sql)
        monitorItems.each {
            MonitorGroup mg = MonitorGroup.findByEtalonId(it.grpid)
            MonitorItem mi = new MonitorItem(path: it.path, ipos: it.ipos, group: mg)
            mi.save()
        }
    }

    /**
     * Импорт шаблонов проверки
     *
     * @param sql БД
     */
    def importPatts(Sql sql) {
        def patts = readDbService.getPatts(sql)
        patts.each {
            MonitorGroup mg = MonitorGroup.findByEtalonId(it.grpid)
            MonitorItem mi = MonitorItem.findByGroupAndIpos(mg, it.ipos)
            Patt p = new Patt(pattern: it.pattern, flags: it.flags, monitorItem: mi)
            p.save()
        }
    }

    /**
     * Импорт эталонных директорий
     *
     * @param sql
     */
    def importEtalonDirs(Sql sql) {
        def etalonDirs = readDbService.getEtalonDirs(sql)
        StatelessSession session = sessionFactory.openStatelessSession()
        try {
            Transaction tx = session.beginTransaction()
            etalonDirs.each {
                MonitorGroup mg = MonitorGroup.findByEtalonId(it.grpid)
                EtalonDir ed = new EtalonDir(name: it.dname, etalonId: it.dirid, group: mg)
                session.insert(ed)
            }
            tx.commit()
        } finally {
            session.close()
        }
    }
}
