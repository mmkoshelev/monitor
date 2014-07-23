package ru.monitor.controller

import org.springframework.dao.DataIntegrityViolationException
import org.springframework.web.multipart.MultipartHttpServletRequest
import ru.monitor.exception.MonitorException
import ru.monitor.model.CheckFile
import ru.monitor.model.ServerItem
import ru.monitor.util.LogUtil

/**
 * Отображение информации по серверам проверки
 *
 * @since 0.1
 */
class ServersController {

    static allowedMethods = [newserver: "POST", delete: "POST", importdb: "POST", quicksearch: "POST"]

    def importDbService

    def index() {
        try {
            List<ServerItem> servers;
            if (flash.quicksearch == null) {
                if (!params.sort) {
                    params.sort = "name"
                }
                servers = ServerItem.list(params)
            } else {
                servers = flash.quicksearch
            }

            def lastChecks = [:]
            def serverStatuses = [:]
            for (server in servers) {
                def lastCheck = server.checkRuns.sort({a, b -> b.runDate <=> a.runDate }).find()
                lastChecks[server.code] = lastCheck?.runDate
                serverStatuses[server.code] = CheckFile.countByCheckRun(lastCheck) == 0
            }

            return [servers: servers, lastChecks: lastChecks, serverStatuses: serverStatuses]
        } catch (Exception ex) {
            LogUtil.error(request, "Ошибка отображения серверов проверки", ex, log)
        }
    }

    def quicksearch(String quicksearch) {
        def qs = ServerItem.findAllByCodeIlikeOrNameIlike("%${quicksearch}%", "%${quicksearch}%")
        flash.quicksearch = qs
        flash.quicksearchtext = quicksearch
        return redirect(action: "index")
    }

    def newserver() {
        try {
            def server = new ServerItem(params)
            if (!server.save(flush: true)) {
                LogUtil.error(flash, server.errors, log)
            } else {
                LogUtil.success(flash, "Сервер проверки [$server.code] успешно добавлен",log)
            }
        } catch (Exception ex) {
            LogUtil.error(flash, "Ошибка добавления нового сервера проверки", ex, log)
        }

        return redirect(action: "index")
    }

    def delete(Long id) {
        def serverItem = ServerItem.get(id)
        if (!serverItem) {
            LogUtil.error(flash, "Не найден сервер проверки с идентификатором [${id}]", null, log)
        } else {
            try {
                serverItem.delete(flush: true)
                LogUtil.success(flash, "Сервер проверки [${serverItem.code}] успешно удален", log)
            } catch (DataIntegrityViolationException ex) {
                LogUtil.error(flash, "Не удалось удалить сервер проверки [${serverItem.code}] с идентификатором [${id}]", ex, log)
            }
        }

        return redirect(action: "index")
    }

    def importdb() {
        def dbfile = ((MultipartHttpServletRequest)request).getFile("dbfile")
        if (dbfile.empty) {
            LogUtil.error(flash, "Не указан файл базы для импорта")
        } else {
            File tmp = new File(UUID.randomUUID().toString() + ".db")
            try {
                dbfile.transferTo(tmp)
                def serverItem = ServerItem.findByCode(params.server)
                if (serverItem) {
                    importDbService.importDb(serverItem, tmp.absolutePath)
                    LogUtil.success(flash, "Импорт для сервера [${serverItem.code}] успешно выполнен")
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
