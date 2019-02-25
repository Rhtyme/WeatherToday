package com.rhtyme.weathertoday.data;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import retrofit2.Call;
import retrofit2.Response;

import com.rhtyme.weathertoday.Constants;
import com.rhtyme.weathertoday.data.model.response.DaoSession;
import com.rhtyme.weathertoday.data.model.response.WeatherTable;
import com.rhtyme.weathertoday.data.model.response.WeatherTableDao;
import com.rhtyme.weathertoday.data.model.response.w.WeatherCity;
import com.rhtyme.weathertoday.data.remote.WeatherService;
import com.rhtyme.weathertoday.util.Utils;

@Singleton
public class DataManager {

    private WeatherService weatherService;

    @Inject
    DaoSession daoSession;

    @Inject
    public DataManager(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    public Single<WeatherTable> getCurrentWeather(boolean forceRefresh) {

        return Single.create(new SingleOnSubscribe<WeatherTable>() {
            @Override
            public void subscribe(SingleEmitter<WeatherTable> emitter) throws Exception {
                WeatherTableDao weatherTableDao = daoSession.getWeatherTableDao();
                WeatherTable weatherTable = null;
                List<WeatherTable> weatherTables = weatherTableDao.queryBuilder()
                        .orderDesc(WeatherTableDao.Properties.Id)
                        .limit(1)
                        .build()
                        .list();

                if (weatherTables != null && !weatherTables.isEmpty() && !forceRefresh) {
                    weatherTable = weatherTables.get(0);
                    weatherTable.getCreatedAt();
                    if (!Utils.isExpired(weatherTable)) {
                        weatherTable.weatherCityObject();
                        emitter.onSuccess(weatherTable);
                        return;
                    }
                }

                Call<WeatherCity> weatherCall = weatherService.getCurrentWeather(Constants.MOSCOW_RU,
                        Constants.OPEN_WEATHER_MAP_API_KEY,
                        Constants.API_UNITS_METRIC);
                Response<WeatherCity> weatherCityResponse =  weatherCall.execute();
                WeatherCity weatherCity = weatherCityResponse.body();
                if (weatherCity == null) {
                    emitter.onError(new Throwable("Null WeatherCity"));
                    return;
                }
                weatherTable = new WeatherTable();
                weatherTable.setCreatedAt(System.currentTimeMillis());
                weatherTable.setupWeatherCityData(weatherCity);
                daoSession.getWeatherTableDao().deleteAll();
                daoSession.getWeatherTableDao().insert(weatherTable);
                emitter.onSuccess(weatherTable);

            }
        });


    }

    public Single<WeatherCity> getCurrentWeatherSingle(boolean forceRefresh) {

        return weatherService.getCurrentWeatherSingle(Constants.MOSCOW_RU,
                Constants.OPEN_WEATHER_MAP_API_KEY,
                Constants.API_UNITS_METRIC);
    }

}
