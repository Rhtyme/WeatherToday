package com.rhtyme.weathertoday.features.current;


import com.rhtyme.weathertoday.data.model.response.WeatherTable;
import com.rhtyme.weathertoday.data.model.response.w.WeatherCity;
import com.rhtyme.weathertoday.features.base.MvpView;

import java.util.List;

public interface CurrentWeatherMvpView extends MvpView {

    void showCurrentWeather(WeatherTable weatherTable);

    void showProgress(boolean show, boolean forceRefresh);

    void showError(Throwable error);


}
