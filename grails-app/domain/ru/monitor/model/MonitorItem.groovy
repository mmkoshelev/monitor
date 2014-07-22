package ru.monitor.model

/**
 * Конфигурационный элемент проверки
 *
 * @since 0.1
 */
class MonitorItem {

    String path
    Long ipos

    static belongsTo = [group: MonitorGroup]
    static hasMany = [patts: Patt]

    static constraints = {
    }

    static mapping = {
        patts lazy: false
    }
}
