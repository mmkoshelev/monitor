package ru.monitor

/**
 * Типы статуса проверки
 *
 * @since 0.1
 */
enum CheckStatusType {

    /*
  ConsVerifOk    =   0,   Различия отсутствуют
  ConsVerifNew   =   1,   Новый элемент
  ConsVerifDel   =   2,   Элемент удален
  ConsVerifHash  =   4,   Отличия в хэш-коде
  ConsVerifSize  =   8,   Отличия в размере
  ConsVerifType  =  16,   Отличия в типе элемента (файл/каталог/символьная ссылка)
  ConsVerifAttr  =  32,   Отличия в атрибутах (владелец, группа)
  ConsVerifAcl   =  64,   Отличия в установленном ACL
  ConsVerifErr   = 128,   Ошибка чтения данных либо атрибутов
   */

    Ok(0, "Ок"),
    New(1, "Новый файл"),
    Del(2, "Файл удален"),
    Hash(4, "Файл изменен, не совпал хэш"),
    Size(8, "Файл изменен, не совпал размер"),
    Type(16, "Отличия в типе элемента (файл/каталог/символьная ссылка)"),
    Attr(32, "Отличия в атрибутах (владелец, группа)"),
    Acl(64, "Отличия в установленном ACL"),
    Err(128, "Ошибка чтения данных либо атрибутов"),
    Unknown(256, "Статус не определен");

    int value
    String name

    private CheckStatusType(int value, String name) {
        this.value = value
        this.name = name
    }

    /**
     * Поиск статуса по коду
     *
     * @param value Код статуса
     * @return Значение
     */
    static CheckStatusType getByValue(int value) {
        return values().find({it.value == value}) ?: Unknown
    }

    /**
     * Поиск статусов по битовой маске
     *
     * @param value значение маски
     * @return Список элементов
     */
    static Set<CheckStatusType> getByValueSet(int value) {
        if (value == 0) {
          return EnumSet.of(Ok)
        } else {
            return values().findAll({(it.value != 0 && (it.value & value) == it.value )}).toSet()
        }
    }
}