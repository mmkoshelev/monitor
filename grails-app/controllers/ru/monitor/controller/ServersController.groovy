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
            }
        } catch (Exception ex) {
            flash.error = ex.message
        }
        flash.success = "Сервер проверки успешно добавлен"

        return redirect(action: "index")
    }

    def importdb() {
        def dbfile = ((MultipartHttpServletRequest)request).getFile("dbfile")
        if(dbfile.empty) {
            flash.error = "Не указан файл базы"
        }

        return redirect(action: "index")
    }
}
