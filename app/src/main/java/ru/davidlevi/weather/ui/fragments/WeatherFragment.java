package ru.davidlevi.weather.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.davidlevi.weather.R;
import ru.davidlevi.weather.sqlite.WeatherTable;
import ru.davidlevi.weather.sqlite.model.WeatherData;

/**
 * Класс-контроллер для макета fragment_weather.xml
 */
public class WeatherFragment extends BaseFragment {
    private Activity that;
    private Context context;
    private Resources resources;

    /**
     * Надуем макет fragment_weather.xml
     *
     * @param inflater           LayoutInflater
     * @param container          ViewGroup
     * @param savedInstanceState Bundle
     * @return View
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        that = getBaseActivity();
        context = that.getApplicationContext();
        resources = that.getResources();

        // Идентификация с элементами
        TextView city = view.findViewById(R.id.d_city);
        TextView icon = view.findViewById(R.id.d_weather_icon);
        TextView temperature = view.findViewById(R.id.d_temperature);
        TextView pressure = view.findViewById(R.id.d_pressure);
        TextView humidity = view.findViewById(R.id.d_humidity);
        TextView description = view.findViewById(R.id.d_description);

        // Получим доступ к таблице БД с данными загруженными retrofit'ом
        WeatherTable weatherTable = new WeatherTable(context);
        weatherTable.open();
        WeatherData data = weatherTable.getPosition(0);
        weatherTable.close();

        // Установка значений
        city.setText(data.getCity());
        for (String element : resources.getStringArray(R.array.string_array_icons))
            if (element.split(" ")[0].equals(data.getIcon()))
                icon.setText(element.split(" ")[1]);
        temperature.setText(data.getTemperature());
        pressure.setText(data.getPressure());
        humidity.setText(data.getHumidity());
        description.setText(data.getDescription());
    }
}