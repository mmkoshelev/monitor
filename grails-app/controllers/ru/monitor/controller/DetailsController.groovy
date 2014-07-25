package ru.monitor.controller

import ru.monitor.model.CheckRun
import ru.monitor.util.LogUtil

/**
 * Отображение информации по проверке сервера
 *
 * @since 0.2
 */
class DetailsController {

    def index(String id) {
        def checkRun;
        try {
            checkRun = CheckRun.where {serverItem.code == id}.list([sort: "runDate", order: "desc"]).first()
        } catch (ex) {
            LogUtil.error(flash, "Не найдена проверка для сервера с идентификатором [${id}]", ex, log)
            return redirect(controller: "servers", action: "index")
        }

        [files: checkRun.checkFiles, checkRun: checkRun]
    }
}
