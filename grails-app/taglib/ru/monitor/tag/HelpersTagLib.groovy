package ru.monitor.tag

import java.text.MessageFormat

/**
 * Вспомогательные tags
 *
 * @since 0.1
 */
class HelpersTagLib {

    static namespace = "m"

    /**
     * Tag вывода сообщений из flash и request
     * по зарезирвированным типам сообщений: error, warn, success, message
     */
    def messages = { attrs, body ->
        def values = [error: "error", warn: "warning", success: "success", message: "hint"]
        String template = """
            <div class="aui-message {0} closeable">
                <p class="title">
                    <span class="aui-icon icon-{0}"></span>
                    {1}
                </p>
                <span class="aui-icon icon-close" role="button" tabindex="0"></span>
            </div>"""
        def renderTemplate = { String value, Object msg ->
            if (msg && msg instanceof String) {
                out << MessageFormat.format(template, value, msg)
            }
        }

        for (entry in values){
            renderTemplate(entry.value, flash["${entry.key}"])
            renderTemplate(entry.value, request.getAttribute("${entry.key}"))
        }
    }
}
