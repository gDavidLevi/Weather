package ru.davidlevi.weather.retrofit.data;

/**
 * Интерфейс взаимодействия с DataManager
 */
public interface IDataManager {
    /**
     * Инициализация Retrofit
     * <p>
     * Ициниализация Retrofit происходит базовым url (http://api.openweathermap.org/) и конвертером
     * преобразования JSON'а в объекты.
     * Далее происходит создание объекта (listener), при помощи которого будем выполнять
     * запросы.
     */
    void initRetrofit(DataManager.OnEvent listener);

    /**
     * Запрос Retrofit
     *
     * @param city   String город
     * @param units  String metric
     * @param keyApi String appid
     */
    void requestRetrofitCity(final String city, final String units, final String keyApi);

    /**
     *
     * @param lat
     * @param lon
     * @param units
     * @param keyApi
     */
    void requestRetrofitCoordinates(final String lat, final String lon, final String units, final String keyApi);
}
