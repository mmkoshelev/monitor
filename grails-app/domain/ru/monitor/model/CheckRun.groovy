package ru.monitor.model

/**
 * Описание задачи проверки
 *
 * @since 0.1
 */
class CheckRun {

    Date runDate
    String etalonId

    static belongsTo = [group: MonitorGroup, serverItem: ServerItem]
    static hasMany = [checkFiles: CheckFile]

    static constraints = {
    }

    static mapping = {
        checkFiles sort: "etalonDir.name"
    }
}
