package ru.monitor.model

/**
 * Результат проверки файла
 *
 * @since 0.1
 */
class CheckFile {

    String name
    Integer status
    Long etalonId

    static belongsTo = [checkRun: CheckRun, etalonDir: EtalonDir]
    static hasMany = [checkAces: CheckAce]

    static constraints = {
    }

    static mapping = {
        checkAces sort: "trustee"
    }
}
