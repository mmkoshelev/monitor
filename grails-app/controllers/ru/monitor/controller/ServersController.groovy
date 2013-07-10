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
            def servers = ServerItem.list(params)

            def lastChecks = [:]
            for (server in servers) {
                lastChecks[server.code] = server.checkRuns.sort({a, b -> b.runDate <=> a.runDate }).find()?.runDate
            }

            return [servers: servers, lastChecks: lastChecks]
        } catch (Exception ex) {
            LogUtil.error(request, "Ошибка отображения серверов проверки", ex, log)
        }
    }

    def newserver() {
        try {
            def server = new ServerItem(params)
            if (!server.save(flush: true)) {
                LogUtil.error(flash, server.errors)
            } else {
                LogUtil.success(flash, "Сервер проверки успешно добавлен")
            }
        } catch (Exception ex) {
            LogUtil.error(flash, "Ошибка добавления нового сервера проверки", ex, log)
        }

        return redirect(action: "index")
    }

    def importdb() {
        def dbfile = ((MultipartHttpServletRequest)request).getFile("dbfile")
        if (dbfile.empty) {
            LogUtil.error(flash, "Не указан файл базы")
        } else {
            File tmp = new File(UUID.randomUUID().toString() + ".db")
            try {
                dbfile.transferTo(tmp)
                def serverItem = ServerItem.findByCode(params.server)
                if (serverItem) {
                    importDbService.importDb(serverItem, tmp.absolutePath)
                } else {
                    LogUtil.warn(flash, "Сервер проверки ${params.server} не найден", log)
                }
            } catch (IOException ex) {
                LogUtil.error(flash, "Ошибка работы с файлами", ex, log)
            } catch (MonitorException ex) {
                LogUtil.error(flash, "Ошибка импорта", ex, log)
            } finally {
                tmp.delete()
            }
        }

        return redirect(action: "index")
    }
}
