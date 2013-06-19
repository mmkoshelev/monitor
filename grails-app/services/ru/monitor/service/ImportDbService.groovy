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

    def readDbService

    def importDb(ServerItem serverItem, String filePath) {
        Sql sql = SQLiteConnectionFactory.getConnection(filePath)
        try {
            def monitorGroups = readDbService.getMonitorGroups(sql)
            def monitorItems = readDbService.getMonitorItems(sql)
            def patts = readDbService.getPatts(sql)
            def etalonDirs = readDbService.getEtalonDirs(sql)
            def etalonFiles = readDbService.getEtalonFiles(sql)
            def etalonAces = readDbService.getEtalonAces(sql)
            def checkRuns = readDbService.getCheckRuns(sql)
            def checkFiles = readDbService.getCheckFiles(sql)
            def checkAces = readDbService.getCheckAces(sql)
        } finally {
            sql.close()
        }
    }


}
