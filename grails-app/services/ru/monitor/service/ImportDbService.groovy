package ru.monitor.service

import groovy.sql.Sql
import ru.monitor.db.SQLiteConnectionFactory
import ru.monitor.model.ServerItem

/**
 * Сервис импорта данных файла проверки
 *
 * @since 0.1
 */
class ImportDbService {

    static transactional = false

    def importDb(ServerItem serverItem, String filePath) {
        Sql sql = SQLiteConnectionFactory.getConnection(filePath)
        try {
            def monitorGroups = getMonitorGroups(sql)
            def monitorItems = getMonitorItems(sql)
            def patts = getPatts(sql)
            def etalonDirs = getEtalonDirs(sql)
            def etalonFiles = getEtalonFiles(sql)
            def etalonAces = getEtalonAces(sql)
            def checkRuns = getCheckRuns(sql)
            def checkFiles = getCheckFiles(sql)
            def checkAces = getCheckAces(sql)
        } finally {
            sql.close()
        }
    }

    private static getMonitorGroups(Sql sql) {
        def list = []
        sql.eachRow("SELECT grpid, grpname FROM mvz_group") {
            def row = it.toRowResult()
            row.groups = []
            sql.eachRow("""SELECT g.grpname
                           FROM mvz_link gl INNER JOIN  mvz_group g
                               ON gl.grp2 = g.grpid
                           WHERE gl.grp1 = ?""", [row.grpid]) {
                row.groups << it.grpname
            }
            list << row
        }

        return list
    }

    private static getCheckRuns(Sql sql) {
        def list = []
        sql.eachRow("SELECT runid, grpid FROM mvz_chkrun") {
            list << it.toRowResult()
        }

        return list
    }

    private static getMonitorItems(Sql sql) {
        def list = []
        sql.eachRow("SELECT grpid, ipos, path FROM mvz_item") {
            list << it.toRowResult()
        }

        return list
    }

    private static getPatts(Sql sql) {
        def list = []
        sql.eachRow("SELECT grpid, ipos, pattern, flags FROM mvz_patt") {
            list << it.toRowResult()
        }

        return list
    }

    private static getEtalonDirs(Sql sql) {
        def list = []
        sql.eachRow("SELECT dirid, grpid, dname FROM mvz_dir") {
            list << it.toRowResult()
        }

        return list
    }

    private static getEtalonFiles(Sql sql) {
        def list = []
        sql.eachRow("SELECT eid, dirid, fname, fhash, fsize FROM mvz_entry") {
            list << it.toRowResult()
        }

        return list
    }

    private static getEtalonAces(Sql sql) {
        def list = []
        sql.eachRow("""SELECT a.eid, a.acemode, 0 as diff, a.acevalue, t.uname, NULL as trustee
                       FROM mvz_eace a LEFT OUTER JOIN mvz_trustee t
                           ON a.trustee = t.uid""") {
            list << it.toRowResult()
        }

        return list
    }

    private static getCheckFiles(Sql sql) {
        def list = []
        sql.eachRow("SELECT ceid, dirid, fname, status FROM mvz_chkent") {
            list << it.toRowResult()
        }

        return list
    }

    private static getCheckAces(Sql sql) {
        def list = []
        sql.eachRow("""SELECT a.ceid, a.acemode, a.cdiff, a.acevalue, t.uname, ct.uname as trustee
                       FROM mvz_chkace a LEFT OUTER JOIN mvz_trustee t
                           ON a.trustee = t.uid
                       LEFT OUTER JOIN mvz_chktrust ct
                           ON a.trustee = ct.uid""") {
            list << it.toRowResult()
        }

        return list
    }
}
