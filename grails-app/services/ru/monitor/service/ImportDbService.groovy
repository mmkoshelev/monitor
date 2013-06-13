package ru.monitor.service

import groovy.sql.Sql
import org.springframework.transaction.TransactionStatus
import ru.monitor.db.SQLiteConnectionFactory
import ru.monitor.model.MonitorGroup
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
            importMonitorGroup(serverItem, sql)
            importCheckRun(sql)
        } finally {
            sql.close()
        }
    }

    def importMonitorGroup(ServerItem serverItem, Sql sql) {
        MonitorGroup.withTransaction { TransactionStatus status ->
            try {
                sql.eachRow("SELECT grpname FROM mvz_group") {
                    MonitorGroup mg = new MonitorGroup(name: it.grpname)
                    serverItem.addToGroups(mg)
                    mg.save()
                }
            } catch (Exception e) {
                status.setRollbackOnly()
            }
        }
    }

    def importCheckRun(Sql sql) {

    }

}
