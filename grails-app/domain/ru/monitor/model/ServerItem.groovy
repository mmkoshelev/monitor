package ru.monitor.model

/**
 * Описание сервера проверки
 *
 * @since 0.1
 */
class ServerItem {

    String name
    String code

    static hasMany = [checkRuns: CheckRun, groups: MonitorGroup]

    static constraints = {
        code unique: true, blank: false
    }
}
