package ru.monitor.model

/**
 * Группы проверки
 *
 * @since 0.1
 */
class MonitorGroup {

    String name
    Long etalonId

    static hasMany = [groups: MonitorGroup, monitorItems: MonitorItem, etalonDirs: EtalonDir]
    static belongsTo = [serverItem: ServerItem]

    static constraints = {
        name blank: false, unique: ["serverItem"]
    }

    static mapping = {
        name index: "mg_name_si_idx"
        serverItem index:  "mg_name_si_idx"
        monitorItems lazy: false
        groups lazy: false
        monitorItems sort: "path"
    }
}
