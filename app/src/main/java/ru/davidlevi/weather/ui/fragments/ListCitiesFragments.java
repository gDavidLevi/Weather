package ru.davidlevi.weather.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Comparator;
import java.util.List;

import retrofit2.Response;
import ru.davidlevi.weather.R;
import ru.davidlevi.weather.retrofit.data.DataManager;
import ru.davidlevi.weather.retrofit.data.IDataManager;
import ru.davidlevi.weather.retrofit.data.WeatherRequest;
import ru.davidlevi.weather.sqlite.HistoryTable;
import ru.davidlevi.weather.ui.BaseActivity;
import ru.davidlevi.weather.ui.Constants;
import ru.davidlevi.weather.ui.fragments.adapters.AdapterListCities;

/**
 * Класс-контроллер для макета fragment_list_cities.xml
 */
public class ListCitiesFragments extends BaseFragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_cities, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Activity activity = getBaseActivity();
        final Context context = activity.getApplicationContext();

        // Создадим RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.fragment_listcities_recyclerview);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        // Получим данные
        HistoryTable historyTable = new HistoryTable(context);
        historyTable.open();
        final List<String> list = historyTable.getListCities();
        list.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2); // A-Z
            }
        });
        historyTable.close();

        // Адаптер
        AdapterListCities adapter = new AdapterListCities(list);
        recyclerView.setAdapter(adapter);

        // Обработчик нажатия на элемент списка
        adapter.SetOnItemClickListener(new AdapterListCities.OnItemClickListener() {
            @Override
            public void onCityClick(final int position) {
                getBaseActivity().setCurrentCity(list.get(position));
                getBaseActivity().showWeatherFragment();
            }
        });
    }
}