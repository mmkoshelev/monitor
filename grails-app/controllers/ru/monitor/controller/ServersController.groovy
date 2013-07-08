package ru.monitor.controller

import org.springframework.web.multipart.MultipartHttpServletRequest
import ru.monitor.exception.MonitorException
import ru.monitor.model.ServerItem
import ru.monitor.util.LogUtil

/**
 * Отображение информации по серверам проверки
 *
 * @since 0.1
 */
class ServersController {

    static allowedMethods = [newserver: "POST", importdb: "POST"]

    def importDbService

    def index() {
        try {
            def servers = ServerItem.list(sort: "name")
            return [servers: servers]
        } catch (Exception ex) {
            LogUtil.error(request, "Ошибка отображения серверов проверки", ex, log)
        }
    }

    def newserver() {
        def server = new ServerItem(params)
        try {
            if (!server.save(flush: true)) {
                LogUtil.error(flash, server.errors)
            } else {
                flash.success = "Сервер проверки успешно добавлен"
            }
        } catch (Exception ex) {
            flash.error = ex.message
        }


        return redirect(action: "index")
    }

    def importdb() {
        def dbfile = ((MultipartHttpServletRequest)request).getFile("dbfile")
        if (dbfile.empty) {
            flash.error = "Не указан файл базы"
        } else {
            File tmp = new File(UUID.randomUUID().toString() + ".db")
            try {
                dbfile.transferTo(tmp)
                def serverItem = ServerItem.findByCode(params.server)
                if (serverItem) {
                    importDbService.importDb(serverItem, tmp.absolutePath)
                }
            } catch (IOException ex) {
                log.error("Ошибка работы с файлами", ex)
            } catch (MonitorException ex) {
                log.error("Ошибка импорта", ex)
            } finally{
                tmp.delete()
            }
        }

        return redirect(action: "index")
    }
}
