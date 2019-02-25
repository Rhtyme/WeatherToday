package com.rhtyme.weathertoday.injection.component;

import dagger.Subcomponent;

import com.rhtyme.weathertoday.features.current.CurrentWeatherActivity;
import com.rhtyme.weathertoday.injection.PerActivity;
import com.rhtyme.weathertoday.injection.module.ActivityModule;

@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(CurrentWeatherActivity currentPrayersActivity);
}
