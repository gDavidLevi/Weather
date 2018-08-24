package ru.davidlevi.weather.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.Closeable;

import ru.davidlevi.weather.sqlite.model.WeatherData;

/**
 * Класс описывает погодную таблицу
 */
public class WeatherTable implements Closeable {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    private Cursor cursor; // Курсор
    private String[] tmpAllColumns = { // Колонки таблицы 'tmp' в БД:
            DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_CITY,
            DatabaseHelper.COLUMN_TEMPERATURE,
            DatabaseHelper.COLUMN_PRESSURE,
            DatabaseHelper.COLUMN_HUMIDITY,
            DatabaseHelper.COLUMN_DESCRIPTION,
            DatabaseHelper.COLUMN_ICON
    };

    /**
     * Конструктор
     *
     * @param context Context
     */
    public WeatherTable(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    /**
     * Открывает базу данных
     *
     * @throws SQLException Exception
     */
    public void open() throws SQLException {
        // Открывает БД для чтения и записи
        database = dbHelper.getWritableDatabase();
        // Курсор
        query();
        cursor.moveToFirst();
    }

    /**
     * Создание запроса на курсор
     */
    private void query() {
        // Курсор фактически это подготовенный запрос "SELECT * FROM notes;",
        // но сами данные подчитываются только по необходимости
        cursor = database.query(DatabaseHelper.TABLE_TEMP, tmpAllColumns,
                null, null, null, null, null);
    }

    /**
     * Обновляем курсор
     */
    public void RefreshCursor() {
        int position = cursor.getPosition(); // получить текущую позицию
        query();
        cursor.moveToPosition(position); // поместить на это же позицию
    }

    /**
     * Преобразователь данных курсора в объект
     *
     * @return WeatherData
     */
    private WeatherData cursorToTemp() {
        WeatherData weatherData = new WeatherData();
        weatherData.setId(cursor.getLong(0));
        weatherData.setCity(cursor.getString(1));
        weatherData.setTemperature(cursor.getString(2));
        weatherData.setPressure(cursor.getString(3));
        weatherData.setHumidity(cursor.getString(4));
        weatherData.setDescription(cursor.getString(5));
        weatherData.setIcon(cursor.getString(6));
        return weatherData;
    }

    /**
     * Прочитать данные по определенной позиции
     *
     * @param position int
     * @return WeatherData
     */
    public WeatherData getPosition(int position) {
        cursor.moveToPosition(position);
        return cursorToTemp();
    }

    /**
     * Получить количество строк в таблице
     *
     * @return int
     */
    public int getCount() {
        return cursor.getCount();
    }

    /**
     * Закрыть базу данных
     */
    public void close() {
        cursor.close();
        dbHelper.close();
    }

    /**
     * Сохраняет запись в таблице
     */
    public long save(WeatherData weatherData) {
        deleteAll();
        //
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COLUMN_CITY, weatherData.getCity());
        contentValues.put(DatabaseHelper.COLUMN_TEMPERATURE, weatherData.getTemperature());
        contentValues.put(DatabaseHelper.COLUMN_PRESSURE, weatherData.getPressure());
        contentValues.put(DatabaseHelper.COLUMN_HUMIDITY, weatherData.getHumidity());
        contentValues.put(DatabaseHelper.COLUMN_DESCRIPTION, weatherData.getDescription());
        contentValues.put(DatabaseHelper.COLUMN_ICON, weatherData.getIcon());
        // Добавление записи
        return database.insert(DatabaseHelper.TABLE_TEMP, null, contentValues);
    }

    /**
     * Удалить запись (DELETE)
     *
     * @param weatherData WeatherData
     */
    private void deleteData(WeatherData weatherData) {
        long id = weatherData.getId();
        database.delete(DatabaseHelper.TABLE_TEMP, DatabaseHelper.COLUMN_ID + " = " + id, null);
    }

    /**
     * Очистить таблицу (DELETE FROM notes;)
     */
    private void deleteAll() {
        database.delete(DatabaseHelper.TABLE_TEMP, null, null);
    }
}