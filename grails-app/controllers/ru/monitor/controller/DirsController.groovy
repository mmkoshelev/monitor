package ru.monitor.controller

import ru.monitor.model.EtalonDir
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

        def groupIds = serverItem.groups.id
        def edQuery = EtalonDir.where { group.id in groupIds }
        [dirs: edQuery.list(sort: "name")]
    }
}
