package ru.monitor.util

import org.apache.commons.logging.Log
import org.codehaus.groovy.grails.web.servlet.FlashScope
import org.springframework.validation.Errors

import javax.servlet.http.HttpServletRequest
import java.text.MessageFormat

/**
 * Вспомогательные методы логирования
 *
 * @since 0.1
 */
class LogUtil {

    /**
     * Вывод информации об ошибке
     *
     * @param flash Flash scope
     * @param msg Сообщение
     * @param ex Исключение
     * @param log Логгер
     */
    static error(FlashScope flash, String msg, Exception ex = null, Log log = null) {
        log?.error(msg, ex)
        addMessage(flash, "error", messageExceptionToString(msg, ex))
    }

    /**
     * Вывод информации об ошибке
     *
     * @param flash Flash scope
     * @param errors Ошибки валидации объекта
     * @param log Логгер
     */
    static error(FlashScope flash, Errors errors, Log log = null) {
        def msg = errorsToString(errors)
        log?.error(msg)
        error(flash, msg)
    }

    /**
     * Вывод информации об ошибке
     *
     * @param request Запрос
     * @param msg Сообщение
     * @param ex Исключение
     * @param log Логгер
     */
    static error(HttpServletRequest request, String msg, Exception ex = null, Log log = null) {
        log?.error(msg, ex)
        addMessage(request, "error", messageExceptionToString(msg, ex))
    }

    /**
     * Вывод информации об ошибке
     *
     * @param request Запрос
     * @param errors Ошибки валидации объекта
     * @param log Логгер
     */
    static error(HttpServletRequest request, Errors errors, Log log = null) {
        def msg = errorsToString(errors)
        log?.error(msg)
        error(request, msg)
    }

    /**
     * Вывод информации об успехе операции
     *
     * @param flash Flash scope
     * @param msg Сообщение
     * @param log Логгер
     */
    static success(FlashScope flash, String msg, Log log = null) {
        log?.info(msg)
        addMessage(flash, "success", msg)
    }

    /**
     * Вывод информации об успехе операции
     *
     * @param request Запрос
     * @param msg Сообщение
     * @param log Логгер
     */
    static success(HttpServletRequest request, String msg, Log log = null) {
        log?.info(msg)
        addMessage(request, "success", msg)
    }

    /**
     * Вывод варнинга
     *
     * @param flash Flash scope
     * @param msg Сообщение
     * @param log Логгер
     */
    static warn(FlashScope flash, String msg, Log log = null) {
        log?.warn(msg)
        addMessage(flash, "warn", msg)
    }

    /**
     * Вывод варнинга
     *
     * @param request Запрос
     * @param msg Сообщение
     * @param log Логгер
     */
    static warn(HttpServletRequest request, String msg, Log log = null) {
        log?.warn(msg)
        addMessage(request, "warn", msg)
    }

    /**
     * Вывод общего сообщения
     *
     * @param flash Flash scope
     * @param msg Сообщение
     * @param log Логгер
     */
    static message(FlashScope flash, String msg, Log log = null) {
        log?.info(msg)
        addMessage(flash, "message", msg)
    }

    /**
     * Вывод общего сообщения
     *
     * @param request Запрос
     * @param msg Сообщение
     * @param log Логгер
     */
    static message(HttpServletRequest request, String msg, Log log = null) {
        log?.info(msg)
        addMessage(request, "message", msg)
    }

    /**
     * Сбор ошибок валидации в строковое представление
     *
     * @param errors Ошибки валидации
     * @return Строковое преставлении ошибок
     */
    private static String errorsToString(Errors errors) {
        def sb = new StringBuilder()
        errors.getAllErrors().each {
            sb.append(MessageFormat.format(it.defaultMessage, it.arguments)).append(" ")
        }

        return sb.toString()
    }

    /**
     * Вывод сообщения с исключением
     *
     * @param msg Сообщение
     * @param ex Исключение
     * @return Описание сообщения
     */
    private static String messageExceptionToString(String msg, Exception ex) {
        return msg + (ex != null ? ": ${ex.message}" : "")
    }

    /**
     * Добавление сообщения в заданный контекст
     * с учетом новое сообщение или расширить до списка
     *
     * @param flash Flash scope
     * @param name Название параметра (error, success, warn, message)
     * @param msg Сообщение
     */
    private static addMessage(FlashScope flash, String name, String msg) {
        if (!flash[name]) {
            flash[name] = msg
        } else {
            if (flash[name] instanceof String) {
                flash[name] = [flash[name]]
            }
            flash[name] << msg
        }
    }

    /**
     * Добавление сообщения в заданный контекст
     * с учетом новое сообщение или расширить до списка
     *
     * @param request Запрос
     * @param name Название параметра (error, success, warn, message)
     * @param msg Сообщение
     */
    private static addMessage(HttpServletRequest request, String name, String msg) {
        if (!request.getAttribute(name)) {
            request.setAttribute(name, msg)
        } else {
            if (request.getAttribute(name) instanceof String) {
                request.setAttribute(name, [request.getAttribute(name)])
            }
            request.getAttribute(name) << msg
        }
    }
}
