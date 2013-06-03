package ru.monitor.model

/**
 * Эталонные директории проверки
 *
 * @since 0.1
 */
class EtalonDir {

    String name

    static belongsTo = [group: MonitorGroup]
    static hasMany = [etalonFiles: EtalonFile]

    static constraints = {
    }
}
