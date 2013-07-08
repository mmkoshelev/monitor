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
        flash.error = messageExceptionToString(msg, ex)
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
        request.setAttribute("error", messageExceptionToString(msg, ex))
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
}
