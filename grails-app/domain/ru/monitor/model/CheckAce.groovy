package ru.monitor.model

/**
 * Результат проверки ace
 *
 * @since 0.1
 */
class CheckAce {

    String value
    Integer mode;
    String trustee;
    Integer diff;

    static belongsTo = [checkFile: CheckFile]

    static constraints = {
    }
}
