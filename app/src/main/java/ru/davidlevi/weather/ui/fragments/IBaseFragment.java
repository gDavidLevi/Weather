package ru.davidlevi.weather.ui.fragments;

/**
 * Базовый фрагмент имеет интерфейс Callback.
 * Реализуется в BaseActivity.
 * Используется для передачи данных от фрагмента в активити
 */
public interface IBaseFragment {
    /**
     * Событие onFragmentAttached
     */
    void onFragmentAttached();

    /**
     * Событие onFragmentDetached
     */
    void onFragmentDetached(String tag);

    /**
     * Запустить фрагмент
     *
     * @param fragment Class
     */
    void startFragment(Class fragment);

    /**
     * Установить настроки сессии работы приложения
     */
    void setSettings();
}
