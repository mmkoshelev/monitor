package ru.monitor.db

import groovy.sql.Sql
import org.sqlite.SQLiteDataSource

import java.text.MessageFormat

/**
 * Фабрика создания соединений для SQLite
 *
 * @since 0.1
 */
class SQLiteConnectionFactory {

    final static String URL_PATTERN = "jdbc:sqlite:{0}";

    /**
     * Получаем groovy sql соединение с базой sqlite
     *
     * @param filePath Пйть к файлу БД
     * @return Groovy SQL
     * @throws FileNotFoundException Файл не найден
     */
    static Sql getConnection(String filePath) throws FileNotFoundException {
        def file = new File(filePath)
        if (!file.exists()) {
            throw new FileNotFoundException("Файл $filePath не найден")
        }

        return Sql.newInstance(MessageFormat.format(URL_PATTERN, filePath), "org.sqlite.JDBC")
    }
}
