package com.rhtyme.weathertoday.injection.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import com.rhtyme.weathertoday.data.remote.WeatherService;
import retrofit2.Retrofit;

/**
 * Created by shivam on 29/5/17.
 */
@Module(includes = {NetworkModule.class})
public class ApiModule {

    @Provides
    @Singleton
    WeatherService providePokemonApi(Retrofit retrofit) {
        return retrofit.create(WeatherService.class);
    }
}
