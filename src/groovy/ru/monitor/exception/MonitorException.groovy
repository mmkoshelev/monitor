package ru.monitor.exception

import org.springframework.core.NestedRuntimeException

/**
 * Общее исключение монитора
 *
 * @since 0.1
 */
class MonitorException extends NestedRuntimeException {

    MonitorException(String msg) {
        super(msg)
    }

    MonitorException(String msg, Throwable cause) {
        super(msg, cause)
    }
}
