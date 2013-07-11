package ru.monitor.service

import groovy.sql.Sql
import org.apache.commons.lang.time.DateUtils
import org.hibernate.StatelessSession
import org.hibernate.Transaction
import ru.monitor.db.SQLiteConnectionFactory
import ru.monitor.exception.MonitorException
import ru.monitor.model.*

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

    /**
     * Импорт данных файла для серера
     *
     * @param serverItem Сервер проверки
     * @param filePath Путь к файлу данных проверки
     * @throws MonitorException
     */
    def importDb(ServerItem serverItem, String filePath) throws MonitorException {
        Sql sql = SQLiteConnectionFactory.getConnection(filePath)
        try {
            log.info("Импортируем MonitorGroups для [${serverItem.code}]")
            def monitorGroups = importMonitorGroups(serverItem, sql)
            log.info("MonitorGroups импортированы для [${serverItem.code}]")

            log.info("Импортируем MonitorItems для [${serverItem.code}]")
            importMonitorItems(monitorGroups, sql)
            log.info("MonitorGroups импортированы для [${serverItem.code}]")

            log.info("Импортируем Patts для [${serverItem.code}]")
            importPatts(serverItem, sql)
            log.info("MonitorGroups импортированы для [${serverItem.code}]")

            log.info("Импортируем EtalonDirs для [${serverItem.code}]")
            def etalonDirs = importEtalonDirs(serverItem, sql)
            log.info("MonitorGroups импортированы для [${serverItem.code}]")

            log.info("Импортируем EtalonFiles для [${serverItem.code}]")
            def etalonFiles = importEtalonFiles(etalonDirs, sql)
            log.info("MonitorGroups импортированы для [${serverItem.code}]")

            log.info("Импортируем EtalonAces для [${serverItem.code}]")
            importEtalonAces(etalonFiles, sql)
            log.info("MonitorGroups импортированы для [${serverItem.code}]")

            log.info("Импортируем CheckRuns для [${serverItem.code}]")
            def checkRuns = importCheckRuns(serverItem, sql)
            log.info("MonitorGroups импортированы для [${serverItem.code}]")

            log.info("Импортируем CheckFiles для [${serverItem.code}]")
            def checkFiles = importCheckFiles(etalonDirs, checkRuns, sql)
            log.info("MonitorGroups импортированы для [${serverItem.code}]")

            log.info("Импортируем CheckAces для [${serverItem.code}]")
            importCheckAces(checkFiles, sql)
            log.info("MonitorGroups импортированы для [${serverItem.code}]")
            log.info("Импорт завершен для [${serverItem.code}]")
        } catch(Exception e) {
            throw new MonitorException("Ошибка импорта файла данных для сервера [$serverItem.name]", e)
        } finally {
            sql.close()
        }
    }

    /**
     * Импорт групп проверки для сервера
     *
     * @param serverItem Сервер проверки
     * @param sql БД
     * @return Импортированные группы проверок
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
        newGroups*.save(failOnError: true)

        return newGroups
    }

    /**
     * Импорт элементов проверки
     *
     * @param monitorGroups Группы проверки сервера
     * @param sql БД
     */
    def importMonitorItems(List<MonitorGroup> monitorGroups, Sql sql) {
        def monitorItems = readDbService.getMonitorItems(sql)
        monitorItems.each {
            MonitorGroup mg = monitorGroups.find({ g -> g.etalonId == it.grpid})
            MonitorItem mi = new MonitorItem(path: it.path, ipos: it.ipos, group: mg)
            mi.save(failOnError: true)
        }
    }

    /**
     * Импорт шаблонов проверки
     *
     * @param Сервер проверки
     * @param sql БД
     */
    def importPatts(ServerItem serverItem, Sql sql) {
        def patts = readDbService.getPatts(sql)
        patts.each {
            MonitorGroup mg = MonitorGroup.findByEtalonIdAndServerItem(it.grpid, serverItem)
            MonitorItem mi = MonitorItem.findByGroupAndIpos(mg, it.ipos)
            Patt p = new Patt(pattern: it.pattern, flags: it.flags, monitorItem: mi)
            p.save(failOnError: true)
        }
    }

    /**
     * Импорт эталонных директорий
     *
     * @param serverItem Сервер проверки
     * @param sql БД
     * @return Импортированные эталонные директории
     */
    def importEtalonDirs(ServerItem serverItem, Sql sql) {
        def etalonDirs = readDbService.getEtalonDirs(sql)
        def newDirs = []
        StatelessSession session = sessionFactory.openStatelessSession()
        try {
            Transaction tx = session.beginTransaction()
            def groups = MonitorGroup.findAllByServerItem(serverItem)
            etalonDirs.each {
                MonitorGroup mg = groups.find({g -> g.etalonId == it.grpid})
                EtalonDir ed = new EtalonDir(name: it.dname, etalonId: it.dirid, group: mg)
                session.insert(ed)
                newDirs << ed
            }
            tx.commit()
        } finally {
            session.close()
        }

        return newDirs
    }

    /**
     * Импорт эталонных файлов
     *
     * @param etalonDirs Эталонные директории
     * @param sql БД
     * @return Импортированные эталонные файлы
     */
    def importEtalonFiles(List<EtalonDir> etalonDirs, Sql sql) {
        def etalonFiles = readDbService.getEtalonFiles(sql)
        def newFiles = []
        StatelessSession session = sessionFactory.openStatelessSession()
        try {
            Transaction tx = session.beginTransaction()
            etalonFiles.each {
                EtalonDir ed = etalonDirs.find({d -> d.etalonId == it.dirid})
                EtalonFile ef = new EtalonFile(name: it.fname, hash: it.fhash, size: it.fsize,
                        etalonId: it.eid, etalonDir: ed)
                session.insert(ef)
                newFiles << ef
            }
            tx.commit()
        } finally {
            session.close()
        }

        return newFiles
    }

    /**
     * Импорт эталонных ACE
     *
     * @param etalonFiles Эталонные файлы
     * @param sql БД
     */
    def importEtalonAces(List<EtalonFile> etalonFiles, Sql sql) {
        def etalonAces = readDbService.getEtalonAces(sql)
        StatelessSession session = sessionFactory.openStatelessSession()
        try {
            Transaction tx = session.beginTransaction()
            etalonAces.each {
                EtalonFile ef = etalonFiles.find({f -> f.etalonId == it.eid})
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
     * @param serverItem Сервер проверки
     * @param sql БД
     * @return Импортированные проверки
     */
    def importCheckRuns(ServerItem serverItem, Sql sql) {
        def checkRuns = readDbService.getCheckRuns(sql)
        def newRuns = []
        checkRuns.each {
            MonitorGroup mg = MonitorGroup.findByEtalonIdAndServerItem(it.grpid, serverItem)
            def runDate = DateUtils.parseDate(it.runid, "yyyy-MM-dd_HH-mm-ss")
            CheckRun cr = new CheckRun(runDate: runDate, etalonId: it.runid, group: mg, serverItem: serverItem)
            cr.save(failOnError: true)
            newRuns << cr
        }

        return newRuns
    }

    /**
     * Импорт проверочных файлов
     *
     * @param etalonDirs Эталонные директории
     * @param checkRuns Проверки
     * @param sql БД
     * @return Импортированные файлы проверки
     */
    def importCheckFiles(List<EtalonDir> etalonDirs, List<CheckRun> checkRuns, Sql sql) {
        def checkFiles = readDbService.getCheckFiles(sql)
        def newFiles = []
        StatelessSession session = sessionFactory.openStatelessSession()
        try {
            Transaction tx = session.beginTransaction()
            checkFiles.each {
                EtalonDir ed = etalonDirs.find({d -> d.etalonId == it.dirid})
                CheckRun cr = checkRuns.find({r -> r.etalonId == it.runid})
                CheckFile cf = new CheckFile(name: it.fname, status: it.status,
                        etalonId: it.ceid, etalonDir: ed, checkRun: cr)
                session.insert(cf)
                newFiles << cf
            }
            tx.commit()
        } finally {
            session.close()
        }

        return newFiles
    }

    /**
     * Импорт проверочных ACE
     *
     * @param checkFiles Файлы проверки
     * @param sql БД
     */
    def importCheckAces(List<CheckFile> checkFiles, Sql sql) {
        def checkAces = readDbService.getCheckAces(sql)
        StatelessSession session = sessionFactory.openStatelessSession()
        try {
            Transaction tx = session.beginTransaction()
            checkAces.each {
                CheckFile cf = checkFiles.find({f -> f.etalonId == it.ceid})
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
