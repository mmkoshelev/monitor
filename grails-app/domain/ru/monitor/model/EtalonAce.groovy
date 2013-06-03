package ru.monitor.model

/**
 * Эталонные ace проверки
 *
 * @since 0.1
 */
class EtalonAce {

    String value
    Integer mode;
    String trustee;
    Integer diff;

    static belongsTo = [etalonFile: EtalonFile]

    static constraints = {
    }
}
