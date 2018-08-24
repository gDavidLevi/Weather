package ru.davidlevi.weather.ui;

import android.Manifest;

public class Constants {
    // Зарегистрированный мой APIkey c сайта openweathermap.org
    public static final String RETROFIT_APIKEY = "0ca79a6baefe36fad02032fa007a7861";
    public static final String RETROFIT_UNITS = "metric";

    // Разрешения
    public static final String PERMISSION1 = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String PERMISSION2 = Manifest.permission.ACCESS_COARSE_LOCATION;

    // Коды ответов для разрешений
    public static final int REQUEST_CODE_LOCATION = 1;

    // Константы SharedPreferences
    public static final String CURRENT_CITY = "CURRENT_CITY";
    public static final String CURRENT_CITY_DEFAULT_VALUE = "Moscow";
    public static final String CURRENT_USER = "CURRENT_USER";
    public static final String CURRENT_USER_DEFAULT_VALUE = "David Levi";
    public static final String CURRENT_USER_EMAIL = "CURRENT_USER_EMAIL";
    public static final String CURRENT_USER_EMAIL_DEFAULT_VALUE = "gdavidlevy@gmail.com";
}
