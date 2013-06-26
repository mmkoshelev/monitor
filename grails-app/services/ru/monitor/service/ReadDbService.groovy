package ru.monitor.service

import groovy.sql.Sql

/**
 * Чтение данных SQLite DB
 *
 * @since 0.1
 */
class ReadDbService {

    static transactional = false

    /**
     * Получение групп проверки
     *
     * @param sql БД
     * @return [grpid, grpname, [groups]]
     */
    def getMonitorGroups(Sql sql) {
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

    def getCheckRuns(Sql sql) {
        def list = []
        sql.eachRow("SELECT runid, grpid FROM mvz_chkrun") {
            list << it.toRowResult()
        }

        return list
    }

    /**
     * Получение элементов проверки
     *
     * @param sql БД
     * @return [grpid, ipos, path]
     */
    def getMonitorItems(Sql sql) {
        def list = []
        sql.eachRow("SELECT grpid, ipos, path FROM mvz_item") {
            list << it.toRowResult()
        }

        return list
    }

    /**
     * Получение шаблонов проверки
     *
     * @param sql БД
     * @return [grpid, ipos, pattern, flags]
     */
    def getPatts(Sql sql) {
        def list = []
        sql.eachRow("SELECT grpid, ipos, pattern, flags FROM mvz_patt") {
            list << it.toRowResult()
        }

        return list
    }

    /**
     * Получение эталонных директорий проверки
     *
     * @param sql БД
     * @return [dirid, grpid, dname]
     */
    def getEtalonDirs(Sql sql) {
        def list = []
        sql.eachRow("SELECT dirid, grpid, dname FROM mvz_dir") {
            list << it.toRowResult()
        }

        return list
    }

    /**
     * Поолучение эталонных файлов проверки
     *
     * @param sql БД
     * @return [eid, dirid, fname, fhash, fsize]
     */
    def getEtalonFiles(Sql sql) {
        def list = []
        sql.eachRow("SELECT eid, dirid, fname, fhash, fsize FROM mvz_entry") {
            list << it.toRowResult()
        }

        return list
    }

    def getEtalonAces(Sql sql) {
        def list = []
        sql.eachRow("""SELECT a.eid, a.acemode, 0 as diff, a.acevalue, t.uname, NULL as trustee
                       FROM mvz_eace a LEFT OUTER JOIN mvz_trustee t
                           ON a.trustee = t.uid""") {
            list << it.toRowResult()
        }

        return list
    }

    def getCheckFiles(Sql sql) {
        def list = []
        sql.eachRow("SELECT ceid, dirid, fname, status FROM mvz_chkent") {
            list << it.toRowResult()
        }

        return list
    }

    def getCheckAces(Sql sql) {
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
