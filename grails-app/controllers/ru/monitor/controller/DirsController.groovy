package ru.monitor.controller

import ru.monitor.model.EtalonDir
import ru.monitor.model.EtalonFile
import ru.monitor.model.ServerItem
import ru.monitor.util.LogUtil

/**
 * Отображение контрольных директирий серверов проверки
 *
 * @since 0.2
 */
class DirsController {

    def index(String id) {
        def serverItem = ServerItem.findByCode(id)
        if (!serverItem) {
            LogUtil.error(flash, "Не найден сервер проверки с идентификатором [${id}]", null, log)
            return redirect(controller: "servers", action: "index")
        }

        params.sort = "name"
        params.max = params.max ?: 10
        def edQuery = EtalonDir.where { group.id in serverItem.groups.id }
        def dirs = edQuery.list(params)

        def etalonCountsByDirs = EtalonFile.createCriteria().list {
            inList("etalonDir.id", dirs.id)
            projections {
                sqlGroupProjection "etalon_dir_id as etalondirid, count(*) as efcount", "etalon_dir_id", ["etalondirid", "efcount"], [INTEGER, INTEGER]
            }
        }.collectEntries({new MapEntry(it[0], it[1])})
        [dirs: dirs, dirsCount: edQuery.count(), etalonCountsByDirs: etalonCountsByDirs]
    }
}
