package ru.davidlevi.weather.retrofit.data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Интерфейс для взаимодействия с Retrofit.
 * Будем его передавать как параметр для retrofit.create(IRetrofit.class)
 * <p>
 * API call https://openweathermap.org/current#name:
 * api.openweathermap.org/data/2.5/weather?q={city name}
 * api.openweathermap.org/data/2.5/weather?q={city name},{country code}
 * <p>
 * <p>
 * icons https://openweathermap.org/weather-conditions
 */
public interface IRetrofit {
    /**
     * Метод loadWeather() интерфейса предназначен для
     * Из ссылки https://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=b6907d289e10d714a6e88b30761fae22
     * извлекаем "data/2.5/weather" и прописываем как параметр в GET.
     *
     * @param cityCountry @Query("q") String, например: London или London,uk
     * @param units       @Query("units") String units, например: metric
     * @param keyApi      @Query("appid") String, например: b6907d289e10d714a6e88b30761fae22
     * @return Call<WeatherRequest>, возвращаемый объект
     */
    @GET("data/2.5/weather")
    Call<WeatherRequest> loadWeather(@Query("q") String cityCountry, @Query("units") String units, @Query("appid") String keyApi);

    /**
     * http://api.openweathermap.org/data/2.5/weather?lat=55.75222&lon=37.61555&units=metric&appid=0ca79a6baefe36fad02032fa007a7861
     *
     * @param lat
     * @param lon
     * @param units
     * @param keyApi
     * @return Call<WeatherRequest>, возвращаемый объект
     */
    @GET("data/2.5/weather")
    Call<WeatherRequest> loadWeather(@Query("lat") String lat, @Query("lon") String lon, @Query("units") String units, @Query("appid") String keyApi);
}