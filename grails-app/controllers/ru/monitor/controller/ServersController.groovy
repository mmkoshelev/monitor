package ru.monitor.controller

import org.springframework.web.multipart.MultipartHttpServletRequest
import ru.monitor.model.ServerItem

import java.text.MessageFormat

/**
 * Отображение информации по серверам прверки
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
            flash.ex = ex
            request.setAttribute("error", "Жопень")
        }
    }

    def newserver() {
        def server = new ServerItem(params)
        try {
            if (!server.save(flush: true)) {
                server.errors.getAllErrors().each {err ->
                    flash.error = MessageFormat.format(err.defaultMessage, err.arguments)
                }
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
            } finally{
                tmp.delete()
            }
        }

        return redirect(action: "index")
    }
}
