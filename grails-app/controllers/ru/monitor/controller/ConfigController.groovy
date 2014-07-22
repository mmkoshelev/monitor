package ru.monitor.controller

import ru.monitor.model.ServerItem
import ru.monitor.util.LogUtil

/**
 * Отображение информации по конфигурации серверов проверки
 *
 * @since 0.2
 */
class ConfigController {

    def index(String id) {
        def serverItem = ServerItem.findByCode(id)
        if (!serverItem) {
            LogUtil.error(flash, "Не найден сервер проверки с идентификатором [${id}]", null, log)
            return redirect(controller: "servers", action: "index")
        }

        return [groups: serverItem.groups]
    }
}
