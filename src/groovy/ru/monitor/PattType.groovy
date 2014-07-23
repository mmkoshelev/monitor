package ru.monitor
/**
 * Типы шаблонов
 *
 * @since 0.1
 */
enum PattType {
    /*
     ConsPattInverse = 1,          Инверсия - отказ от обработки подходящих
     ConsPattRegexp = 2,           Регулярное выражение
     ConsPattUpper = 4,            Перевод в верхний регистр
     ConsPattFull = 8,             Сравнение шаблона с полным путем
     ConsPattTypeA = 16,           Шаблон действует на все элементы без учета их типа
     ConsPattTypeD = 32,           Шаблон отбирает каталоги (по умолчанию отбираются файлы)
    */

    Inverse(1, "Инверсия - отказ от обработки подходящих"),
    Regexp(2, "Регулярное выражение"),
    Upper(4, "Перевод в верхний регистр"),
    Full(8, "Сравнение шаблона с полным путем"),
    TypeA(16, "Шаблон действует на все элементы без учета их типа"),
    TypeD(32, "Шаблон отбирает каталоги (по умолчанию отбираются файлы)");

    int value
    String name

    private PattType(int value, String name) {
        this.value = value
        this.name = name
    }

    /**
     * Поиск статусов по битовой маске
     *
     * @param value значение маски
     * @return Список элементов
     */
    static Set<PattType> getByValueSet(int value) {
        return values().findAll({patt -> (patt.value & value) == patt.value}).toSet()
    }
}