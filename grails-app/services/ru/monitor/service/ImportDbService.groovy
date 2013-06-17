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
            def monitorGroups = importMonitorGroup(sql)
            def checkRuns = importCheckRun(sql)
        } finally {
            sql.close()
        }
    }

    private static importMonitorGroup(Sql sql) {
        def list = []
        sql.eachRow("SELECT grpid, grpname FROM mvz_group") {
            def row = it.toRowResult()
            row.groups = []
            sql.eachRow("SELECT g.grpname " +
                        "FROM mvz_link gl INNER JOIN  mvz_group g ON gl.grp2 = g.grpid " +
                        "WHERE gl.grp1 = ?", [row.grpid]) {
                row.groups << it.grpname
            }
            list << row
        }

        return list
    }

    private static importCheckRun(Sql sql) {
        def list = []
        sql.eachRow("SELECT runid, grpid FROM mvz_chkrun") {
            list << it.toRowResult()
        }

        return list
    }

}
