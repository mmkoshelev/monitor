package ru.monitor.model

/**
 * Эталонный файл проверки
 *
 * @since 0.1
 */
class EtalonFile {

    String name
    String hash
    Integer size

    static belongsTo = [etalonDir: EtalonDir]
    static hasMany = [etalonAce: EtalonAce]

    static constraints = {
    }
}
