package com.rhtyme.weathertoday.injection.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import com.rhtyme.weathertoday.data.DataManager;
import com.rhtyme.weathertoday.injection.ApplicationContext;
import com.rhtyme.weathertoday.injection.module.AppModule;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    @ApplicationContext
    Context context();

    Application application();

    DataManager apiManager();
}
