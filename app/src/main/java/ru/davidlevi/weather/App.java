package ru.davidlevi.weather;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import es.dmoral.toasty.Toasty;
import ru.davidlevi.weather.ui.Constants;

/**
 * Топовые настройки, инициализаторы...
 */
public class App extends Application {
    /**
     * Called when the application is starting, before any activity, service,
     * or receiver objects (excluding content providers) have been created.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        // Настройка Toasty для всех классов данного приложения
        // См. https://github.com/GrenderG/Toasty
        Toasty.Config.getInstance()
                .setTextColor(getResources().getColor(R.color.colorBackground, null))
                .apply();
    }
}
