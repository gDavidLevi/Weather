package ru.davidlevi.weather.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Класс установки базы данных.
 * Создает БД, если ее нет; обновляет ее если версии не совпадают.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "weather.db"; // Имя БД
    private static final int DATABASE_VERSION = 2; // Версия БД

    // Названия столбцов (поля публичные) таблицы TABLE_TEMP:
    public static final String TABLE_TEMP = "tmp";
    // Названия столбцов (поля публичные) таблицы TABLE_HISTORY:
    public static final String TABLE_HISTORY = "history";
    // поля:
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_TEMPERATURE = "temperature";
    public static final String COLUMN_PRESSURE = "pressure";
    public static final String COLUMN_HUMIDITY = "humidity";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_ICON = "icon";
    public static final String COLUMN_WINDSPEED = "windspeed";
    public static final String COLUMN_CLOUDINESS = "cloudiness";


    /**
     * Конструктор
     *
     * @param context Context
     */
    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Вызывается при попытке доступа к базе данных, но когда еще эта база данных не создана
     *
     * @param db SQLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // tmp
        db.execSQL("CREATE TABLE " + TABLE_TEMP + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CITY + " TEXT,"
                + COLUMN_TEMPERATURE + " TEXT,"
                + COLUMN_PRESSURE + " TEXT, "
                + COLUMN_HUMIDITY + " TEXT, "
                + COLUMN_DESCRIPTION + " TEXT, "
                + COLUMN_ICON + " TEXT, "
                + COLUMN_WINDSPEED + " TEXT, "
                + COLUMN_CLOUDINESS + " TEXT);");

        // history
        db.execSQL("CREATE TABLE " + TABLE_HISTORY + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CITY + " TEXT,"
                + COLUMN_TEMPERATURE + " TEXT,"
                + COLUMN_PRESSURE + " TEXT, "
                + COLUMN_HUMIDITY + " TEXT, "
                + COLUMN_DESCRIPTION + " TEXT, "
                + COLUMN_ICON + " TEXT, "
                + COLUMN_WINDSPEED + " TEXT, "
                + COLUMN_CLOUDINESS + " TEXT);");
    }

    /**
     * Вызывается, когда необходимо обновление базы данных
     *
     * @param db         SQLiteDatabase
     * @param oldVersion int
     * @param newVersion int
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        if ((oldVersion == 1) && (newVersion == 2)) {
//            String upgradeQuery = "ALTER TABLE " + TABLE_NOTE + " ADD COLUMN " + NOTE_COLUMN_TITLE + " TEXT DEFAULT 'Title'";
//            db.execSQL(upgradeQuery);
//        }
    }
}
