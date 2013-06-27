package ru.monitor.service

import groovy.sql.Sql
import org.apache.commons.lang.time.DateUtils
import org.hibernate.StatelessSession
import org.hibernate.Transaction
import ru.monitor.db.SQLiteConnectionFactory
import ru.monitor.model.CheckAce
import ru.monitor.model.CheckFile
import ru.monitor.model.CheckRun
import ru.monitor.model.EtalonAce
import ru.monitor.model.EtalonDir
import ru.monitor.model.EtalonFile
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
            importEtalonFiles(sql)
            importEtalonAces(sql)
            importCheckRuns(serverItem, sql)
            importCheckFiles(sql)
            importCheckAces(sql)
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
     * @param sql БД
     */
    def importEtalonDirs(Sql sql) {
        def etalonDirs = readDbService.getEtalonDirs(sql)
        StatelessSession session = sessionFactory.openStatelessSession()
        try {
            Transaction tx = session.beginTransaction()
            def groups = MonitorGroup.list()
            etalonDirs.each {
                MonitorGroup mg = groups.find({g -> g.etalonId == it.grpid})
                EtalonDir ed = new EtalonDir(name: it.dname, etalonId: it.dirid, group: mg)
                session.insert(ed)
            }
            tx.commit()
        } finally {
            session.close()
        }
    }

    /**
     * Импорт эталонных файлов
     *
     * @param sql БД
     */
    def importEtalonFiles(Sql sql) {
        def etalonFiles = readDbService.getEtalonFiles(sql)
        StatelessSession session = sessionFactory.openStatelessSession()
        try {
            Transaction tx = session.beginTransaction()
            def dirs = EtalonDir.list()
            etalonFiles.each {
                EtalonDir ed = dirs.find({d -> d.etalonId == it.dirid})
                EtalonFile ef = new EtalonFile(name: it.fname, hash: it.fhash, size: it.fsize,
                        etalonId: it.eid, etalonDir: ed)
                session.insert(ef)
            }
            tx.commit()
        } finally {
            session.close()
        }
    }

    /**
     * Импорт эталонных ACE
     *
     * @param sql БД
     */
    def importEtalonAces(Sql sql) {
        def etalonAces = readDbService.getEtalonAces(sql)
        StatelessSession session = sessionFactory.openStatelessSession()
        try {
            Transaction tx = session.beginTransaction()
            def files = EtalonFile.list()
            etalonAces.each {
                EtalonFile ef = files.find({f -> f.etalonId == it.eid})
                EtalonAce ea = new EtalonAce(value: it.acevalue, mode: it.acemode,
                        trustee: it.uname ?: it.trustee, diff: it.diff, etalonFile: ef)
                session.insert(ea)
            }
            tx.commit()
        } finally {
            session.close()
        }
    }

    /**
     * Импорт проверок
     *
     * @param sql БД
     */
    def importCheckRuns(ServerItem serverItem, Sql sql) {
        def checkRuns = readDbService.getCheckRuns(sql)
        checkRuns.each {
            MonitorGroup mg = MonitorGroup.findByEtalonIdAndServerItem(it.grpid, serverItem)
            def runDate = DateUtils.parseDate(it.runid, "yyyy-MM-dd_HH-mm-ss")
            CheckRun cr = new CheckRun(runDate: runDate, etalonId: it.runid, group: mg, serverItem: serverItem)
            cr.save()
        }
    }

    /**
     * Импорт проверочных файлов
     *
     * @param sql БД
     */
    def importCheckFiles(Sql sql) {
        def checkFiles = readDbService.getCheckFiles(sql)
        StatelessSession session = sessionFactory.openStatelessSession()
        try {
            Transaction tx = session.beginTransaction()
            def dirs = EtalonDir.list()
            def checkRuns = CheckRun.list()
            checkFiles.each {
                EtalonDir ed = dirs.find({d -> d.etalonId == it.dirid})
                CheckRun cr = checkRuns.find({r -> r.etalonId == it.runid})
                CheckFile cf = new CheckFile(name: it.fname, status: it.status,
                        etalonId: it.ceid, etalonDir: ed, checkRun: cr)
                session.insert(cf)
            }
            tx.commit()
        } finally {
            session.close()
        }
    }

    /**
     * Импорт проверочных ACE
     *
     * @param sql БД
     */
    def importCheckAces(Sql sql) {
        def checkAces = readDbService.getCheckAces(sql)
        StatelessSession session = sessionFactory.openStatelessSession()
        try {
            Transaction tx = session.beginTransaction()
            def files = CheckFile.list()
            checkAces.each {
                CheckFile cf = files.find({f -> f.etalonId == it.ceid})
                CheckAce ca = new CheckAce(value: it.acevalue, mode: it.acemode,
                        trustee: it.uname ?: it.trustee, diff: it.cdiff, checkFile: cf)
                session.insert(ca)
            }
            tx.commit()
        } finally {
            session.close()
        }
    }
}
