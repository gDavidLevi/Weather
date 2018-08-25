package ru.davidlevi.weather.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.Closeable;

import ru.davidlevi.weather.sqlite.model.CityInformation;

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
            DatabaseHelper.COLUMN_ICON,
            DatabaseHelper.COLUMN_WINDSPEED,
            DatabaseHelper.COLUMN_CLOUDINESS
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
     * @return CityInformation
     */
    private CityInformation cursorToTemp() {
        CityInformation cityInformation = new CityInformation();
        cityInformation.setId(cursor.getLong(0));
        cityInformation.setCity(cursor.getString(1));
        cityInformation.setTemperature(cursor.getString(2));
        cityInformation.setPressure(cursor.getString(3));
        cityInformation.setHumidity(cursor.getString(4));
        cityInformation.setDescription(cursor.getString(5));
        cityInformation.setIcon(cursor.getString(6));
        cityInformation.setWindspeed(cursor.getString(7));
        cityInformation.setCloudiness(cursor.getString(8));
        return cityInformation;
    }

    /**
     * Прочитать данные по определенной позиции
     *
     * @param position int
     * @return CityInformation
     */
    public CityInformation getPosition(int position) {
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
    public long save(CityInformation cityInformation) {
        deleteAll();
        //
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COLUMN_CITY, cityInformation.getCity());
        contentValues.put(DatabaseHelper.COLUMN_TEMPERATURE, cityInformation.getTemperature());
        contentValues.put(DatabaseHelper.COLUMN_PRESSURE, cityInformation.getPressure());
        contentValues.put(DatabaseHelper.COLUMN_HUMIDITY, cityInformation.getHumidity());
        contentValues.put(DatabaseHelper.COLUMN_DESCRIPTION, cityInformation.getDescription());
        contentValues.put(DatabaseHelper.COLUMN_ICON, cityInformation.getIcon());
        contentValues.put(DatabaseHelper.COLUMN_WINDSPEED, cityInformation.getWindspeed());
        contentValues.put(DatabaseHelper.COLUMN_CLOUDINESS, cityInformation.getCloudiness());
        // Добавление записи
        return database.insert(DatabaseHelper.TABLE_TEMP, null, contentValues);
    }

    /**
     * Удалить запись (DELETE)
     *
     * @param cityInformation CityInformation
     */
    private void deleteData(CityInformation cityInformation) {
        long id = cityInformation.getId();
        database.delete(DatabaseHelper.TABLE_TEMP, DatabaseHelper.COLUMN_ID + " = " + id, null);
    }

    /**
     * Очистить таблицу (DELETE FROM notes;)
     */
    private void deleteAll() {
        database.delete(DatabaseHelper.TABLE_TEMP, null, null);
    }
}