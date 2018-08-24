package ru.davidlevi.weather.retrofit.data;

import android.support.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Данный класс наследует от интерфейса (имплементирует интерфейс IDataManager)
 */
public class DataManager implements IDataManager {

    private static final String BASE_URL = "http://api.openweathermap.org/";
    private static final String TAG = "DataManagerLog";

    private IRetrofit iRetrofit; // интерфейс IRetrofit

    /**
     * Интерфес для получения событий из DataManager
     */
    public interface OnEvent {
        void getData(@NonNull Response<WeatherRequest> response); // данные

        void getError(String failureMessage); // ошибка
    }

    private OnEvent listener; // слушатель событий

    /**
     * Инициализация Retrofit
     * <p>
     * Ициниализация Retrofit происходит базовым url (http://api.openweathermap.org/) и конвертером
     * преобразования JSON'а в объекты.
     * Далее происходит создание объекта (iRetrofit), при помощи которого будем выполнять
     * запросы.
     */
    @Override
    public void initRetrofit(OnEvent listener) {
        this.listener = listener;
        //
        Retrofit retrofit;
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                //Конвертер, необходимый для преобразования JSON'ов в объекты
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        iRetrofit = retrofit.create(IRetrofit.class);
    }

    /**
     * Запрос Retrofit
     *
     * @param city   String город
     * @param units  String metric
     * @param keyApi String appid
     */
    @Override
    public void requestRetrofitCity(final String city, final String units, final String keyApi) {
        iRetrofit.loadWeather(city, units, keyApi)
                // Ассинхронный запрос
                .enqueue(new Callback<WeatherRequest>() {
                    /**
                     * Событие "наОтвет"
                     *
                     * @param call Call<WeatherRequest>
                     * @param response Response<WeatherRequest>
                     */
                    @Override
                    public void onResponse(@NonNull Call<WeatherRequest> call, @NonNull Response<WeatherRequest> response) {
                        if (response.body() != null)
                            listener.getData(response);
                    }

                    /**
                     * Событие "наОтказ"
                     *
                     * @param call Call<WeatherRequest>
                     * @param t Throwable
                     */
                    @Override
                    public void onFailure(@NonNull Call<WeatherRequest> call, @NonNull Throwable t) {
                        listener.getError(t.getMessage());
                    }
                });
    }

    @Override
    public void requestRetrofitCoordinates(String lat, String lon, String units, String keyApi) {
        iRetrofit.loadWeather(lat, lon, units, keyApi)
                .enqueue(new Callback<WeatherRequest>() {
                    @Override
                    public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                        if (response.body() != null)
                            listener.getData(response);
                    }

                    @Override
                    public void onFailure(Call<WeatherRequest> call, Throwable t) {
                        listener.getError(t.getMessage());
                    }
                });
    }
}
