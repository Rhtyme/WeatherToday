package com.rhtyme.weathertoday.data.remote;

import io.reactivex.Single;

import com.rhtyme.weathertoday.data.model.response.w.WeatherCity;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {

    @GET("weather")
    Single<WeatherCity> getCurrentWeatherSingle(@Query("q") String cityCountry,
                                          @Query("APPID") String appID,
                                          @Query("units") String units);

    @GET("weather")
    Call<WeatherCity> getCurrentWeather(@Query("q") String cityCountry,
                                       @Query("APPID") String appID,
                                       @Query("units") String units);


}
