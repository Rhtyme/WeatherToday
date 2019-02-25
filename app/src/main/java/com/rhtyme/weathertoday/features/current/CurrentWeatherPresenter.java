package com.rhtyme.weathertoday.features.current;

import javax.inject.Inject;

import com.rhtyme.weathertoday.data.DataManager;
import com.rhtyme.weathertoday.features.base.BasePresenter;
import com.rhtyme.weathertoday.injection.ConfigPersistent;
import com.rhtyme.weathertoday.util.rx.scheduler.SchedulerUtils;

@ConfigPersistent
public class CurrentWeatherPresenter extends BasePresenter<CurrentWeatherMvpView> {

    private final DataManager dataManager;

    @Inject
    public CurrentWeatherPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public void loadCurrentWeather(boolean forceRefresh) {
        checkViewAttached();
        getView().showProgress(true, forceRefresh);
        addDisposable(dataManager
                .getCurrentWeather(forceRefresh)
                .compose(SchedulerUtils.ioToMain())
                .subscribe(
                        weatherTable -> {
                            getView().showProgress(false, forceRefresh);
                            getView().showCurrentWeather(weatherTable);
                        },
                        throwable -> {
                            getView().showProgress(false, forceRefresh);
                            getView().showError(throwable);
                        }));

    }
}
