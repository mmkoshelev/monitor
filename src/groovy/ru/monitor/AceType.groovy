package ru.monitor

/**
 * Режимы правила контроля доступа
 *
 * @since 0.1
 */
enum AceType {
    /*
    osp_ace_invalid =  0,
    osp_ace_mask    =  1,        Простая маска доступа (UNIX, VMS), либо флаги (Windows)
    osp_ace_permit  =  2,        Разрешение
    osp_ace_deny    =  3,        Запрет
    osp_ace_audit   =  4,        Аудит доступа
    osp_ace_label   =  5         Метка безопасности
     */

    Mask(1, "Маска"),
    Permit(2, "Разрешено"),
    Deny(3, "Запрещено"),
    Audit(4, "Аудит"),
    Label(5, "Метка"),
    Unknown(1000, "Тип не определен");

    int value
    String name

    private AceType(int value, String name) {
        this.value = value
        this.name = name
    }

    /**
     * Поиск типа по коду
     * @param value Код статуса
     * @return Значение
     */
    static AceType getByValue(int value) {
        return values().find({ace -> ace.value == value}) ?: Unknown
    }
}