package ru.monitor.model

/**
 * Конфигурационный элемент проверки
 *
 * @since 0.1
 */
class MonitorItem {

    String path

    static belongsTo = [group: MonitorGroup]
    static hasMany = [patts: Patt]

    static constraints = {
    }
}
