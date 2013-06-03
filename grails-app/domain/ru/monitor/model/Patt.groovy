package ru.monitor.model

/**
 * Павила проверки
 *
 * @since 0.1
 */
class Patt {

    String pattern
    Integer flags

    static belongsTo = [monitorItem: MonitorItem]

    static constraints = {
    }
}
