package ru.monitor.model

/**
 * Эталонный файл проверки
 *
 * @since 0.1
 */
class EtalonFile {

    String name
    Long etalonId
    String hash
    Integer size

    static belongsTo = [etalonDir: EtalonDir]
    static hasMany = [etalonAce: EtalonAce]

    static constraints = {
    }

    static mapping = {
        etalonAce lazy: false
        etalonAce sort: "trustee"
    }
}
