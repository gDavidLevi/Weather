package ru.davidlevi.weather.ui.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;

import ru.davidlevi.weather.ui.BaseActivity;

/**
 * Базовый фрагмент для всех фрагментов нашего проекта
 */
public abstract class BaseFragment extends Fragment {
    private BaseActivity baseActivity;

    /**
     * Событие onAttach фрагмента
     *
     * @param context Context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Из контекста извлекаем ссылку на BaseActivity
        baseActivity = (BaseActivity) context;
        baseActivity.onFragmentAttached();
    }

    /**
     * Метод возвращает ссылку на базовую активити
     *
     * @return BaseActivity
     */
    public BaseActivity getBaseActivity() {
        return baseActivity;
    }

    /**
     * Событие onDetach фрагмента
     */
    @Override
    public void onDetach() {
        baseActivity = null;
        super.onDetach();
    }
}
