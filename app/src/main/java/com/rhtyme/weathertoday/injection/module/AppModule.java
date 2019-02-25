package com.rhtyme.weathertoday.injection.module;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;
import com.rhtyme.weathertoday.WeatherApplication;
import com.rhtyme.weathertoday.data.model.response.DaoSession;
import com.rhtyme.weathertoday.injection.ApplicationContext;

import static com.rhtyme.weathertoday.Constants.PREF_FILE_NAME;

@Module(includes = {ApiModule.class})
public class AppModule {
    private final Application application;
    private final DaoSession daoSession;

    public AppModule(Application application) {
        this.application = application;
        this.daoSession = ((WeatherApplication) application).getDaoSession();
    }

    @Provides
    Application provideApplication() {
        return application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return application;
    }

    @Provides
    @ApplicationContext
    SharedPreferences provideSharedPreference(@ApplicationContext Context context) {
        return context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    @Provides
    DaoSession provideDaoSession() {
        return daoSession;
    }
}
