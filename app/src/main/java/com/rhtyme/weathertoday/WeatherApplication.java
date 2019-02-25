package com.rhtyme.weathertoday;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.singhajit.sherlock.core.Sherlock;
import com.squareup.leakcanary.LeakCanary;

import org.greenrobot.greendao.database.Database;

import io.reactivex.plugins.RxJavaPlugins;
import com.rhtyme.weathertoday.data.model.response.DaoMaster;
import com.rhtyme.weathertoday.data.model.response.DaoSession;
import com.rhtyme.weathertoday.injection.component.AppComponent;
import com.rhtyme.weathertoday.injection.component.DaggerAppComponent;
import com.rhtyme.weathertoday.injection.module.AppModule;
import com.rhtyme.weathertoday.injection.module.NetworkModule;
import timber.log.Timber;

public class WeatherApplication extends Application {

    private AppComponent appComponent;


    private DaoSession daoSession;

    public static WeatherApplication get(Context context) {
        return (WeatherApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            Stetho.initializeWithDefaults(this);
            LeakCanary.install(this);
            Sherlock.init(this);
        }
        RxJavaPlugins.setErrorHandler(Timber::d); // nothing or some logging

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "weather-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();

    }

    public AppComponent getComponent() {
        if (appComponent == null) {
            appComponent = DaggerAppComponent.builder()
                    .networkModule(new NetworkModule(this))
                    .appModule(new AppModule(this))
                    .build();
        }
        return appComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(AppComponent appComponent) {
        this.appComponent = appComponent;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

}
