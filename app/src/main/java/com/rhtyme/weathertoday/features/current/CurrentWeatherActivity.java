package com.rhtyme.weathertoday.features.current;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import timber.log.Timber;

import com.rhtyme.weathertoday.R;
import com.rhtyme.weathertoday.data.model.response.WeatherTable;
import com.rhtyme.weathertoday.features.base.BaseActivity;
import com.rhtyme.weathertoday.features.common.ErrorView;
import com.rhtyme.weathertoday.injection.component.ActivityComponent;
import com.rhtyme.weathertoday.util.Utils;

import java.util.Locale;

public class CurrentWeatherActivity extends BaseActivity
        implements CurrentWeatherMvpView {

    @Inject
    CurrentWeatherPresenter presenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.main_swipe_refresh)
    SwipeRefreshLayout mainSwipeableContent;

    @BindView(R.id.view_error)
    ErrorView errorView;

    @BindView(R.id.progress)
    ProgressBar progressBar;


    @BindView(R.id.main_weather_icon)
    TextView mIconWeatherView;

    @BindView(R.id.main_temperature)
    TextView mTemperatureView;

    @BindView(R.id.main_description)
    TextView mDescriptionView;

    @BindView(R.id.main_humidity)
    TextView mHumidityView;

    @BindView(R.id.main_wind_speed)
    TextView mWindSpeedView;


    @BindView(R.id.main_pressure)
    TextView mPressureView;

    @BindView(R.id.main_cloudiness)
    TextView mCloudinessView;

    @BindView(R.id.main_last_update)
    TextView mLastUpdateView;

    @BindView(R.id.main_sunrise)
    TextView mSunriseView;

    @BindView(R.id.main_sunset)
    TextView mSunsetView;

    @BindView(R.id.main_app_bar)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.main_wind_icon)
    TextView mIconWindView;

    @BindView(R.id.main_humidity_icon)
    TextView mIconHumidityView;

    @BindView(R.id.main_pressure_icon)
    TextView mIconPressureView;

    @BindView(R.id.main_cloudiness_icon)
    TextView mIconCloudinessView;

    @BindView(R.id.main_sunrise_icon)
    TextView mIconSunriseView;

    @BindView(R.id.main_sunset_icon)
    TextView mIconSunsetView;

    private String mSpeedScale;
    private String mIconWind;
    private String mIconHumidity;
    private String mIconPressure;
    private String mIconCloudiness;
    private String mIconSunrise;
    private String mIconSunset;
    private String mPercentSign;
    private String mPressureMeasurement;
    private WeatherTable weatherTable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.moscow);
        weatherConditionsIcons();
        initTypeface();
        mainSwipeableContent.setEnabled(false);
        presenter.loadCurrentWeather(false);
        errorView.setErrorListener(() -> presenter.loadCurrentWeather(true));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.weather_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_refresh:
                mainSwipeableContent.setRefreshing(true);
                presenter.loadCurrentWeather(true);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initTypeface() {
        Typeface weatherFontIcon = Typeface.createFromAsset(this.getAssets(),
                "fonts/weathericons-regular-webfont.ttf");

        mIconWeatherView.setTypeface(weatherFontIcon);
        mIconWindView.setTypeface(weatherFontIcon);
        mIconHumidityView.setTypeface(weatherFontIcon);
        mIconPressureView.setTypeface(weatherFontIcon);
        mIconCloudinessView.setTypeface(weatherFontIcon);
        mIconSunriseView.setTypeface(weatherFontIcon);
        mIconSunsetView.setTypeface(weatherFontIcon);

        /**
         * Initialize and configure weather icons
         */
        mIconWindView.setText(mIconWind);
        mIconHumidityView.setText(mIconHumidity);
        mIconPressureView.setText(mIconPressure);
        mIconCloudinessView.setText(mIconCloudiness);
        mIconSunriseView.setText(mIconSunrise);
        mIconSunsetView.setText(mIconSunset);

    }


    @Override
    public int getLayout() {
        return R.layout.current_weather;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void attachView() {
        presenter.attachView(this);
    }

    @Override
    protected void detachPresenter() {
        presenter.detachView();
    }

    private void updateCurrentWeather() {
        mSpeedScale = Utils.getSpeedScale(CurrentWeatherActivity.this);
        String temperature = String.format(Locale.getDefault(), "%.0f",
                weatherTable.weatherCityObject().getMain().getTemp());
        String pressure = String.format(Locale.getDefault(), "%.1f",
                weatherTable.weatherCityObject().getMain().getPressure());
        String wind = String.format(Locale.getDefault(), "%.1f", weatherTable.weatherCityObject().getWind().getSpeed());

        String lastUpdate = Utils.formatUpdateTime(CurrentWeatherActivity.this,
                weatherTable.getCreatedAt());
        String sunrise = Utils.unixTimeToFormatTime(
                CurrentWeatherActivity.this, weatherTable.weatherCityObject().getSys().getSunrise());
        String sunset = Utils.unixTimeToFormatTime(
                CurrentWeatherActivity.this, weatherTable.weatherCityObject().getSys().getSunset());

        mIconWeatherView.setText(
                Utils.getStrIcon(CurrentWeatherActivity.this, weatherTable.weatherCityObject().getWeather().get(0).getIcon()));
        mTemperatureView.setText(getString(R.string.temperature_with_degree, temperature));
        mDescriptionView.setText(weatherTable.weatherCityObject().getWeather().get(0).getDescription());
        mHumidityView.setText(getString(R.string.humidity_label,
                String.valueOf(weatherTable.weatherCityObject().getMain().getHumidity()),
                mPercentSign));
        mPressureView.setText(getString(R.string.pressure_label, pressure,
                mPressureMeasurement));
        mWindSpeedView.setText(getString(R.string.wind_label, wind, mSpeedScale));
        mCloudinessView.setText(getString(R.string.cloudiness_label,
                String.valueOf(weatherTable.weatherCityObject().getClouds().getAll()),
                mPercentSign));
        mLastUpdateView.setText(getString(R.string.last_update_label, lastUpdate));
        mSunriseView.setText(getString(R.string.sunrise_label, sunrise));
        mSunsetView.setText(getString(R.string.sunset_label, sunset));

    }

    private void weatherConditionsIcons() {
        mIconWind = getString(R.string.icon_wind);
        mIconHumidity = getString(R.string.icon_humidity);
        mIconPressure = getString(R.string.icon_barometer);
        mIconCloudiness = getString(R.string.icon_cloudiness);
        mPercentSign = getString(R.string.percent_sign);
        mPressureMeasurement = getString(R.string.pressure_measurement);
        mIconSunrise = getString(R.string.icon_sunrise);
        mIconSunset = getString(R.string.icon_sunset);
    }


    @Override
    public void showCurrentWeather(WeatherTable weatherTable) {
        mainSwipeableContent.setVisibility(View.VISIBLE);
        this.weatherTable = weatherTable;
        updateCurrentWeather();
    }

    @Override
    public void showProgress(boolean show, boolean forceRefresh) {
        if (show) {
            if (forceRefresh && errorView.getVisibility() == View.VISIBLE) {
                progressBar.setVisibility(View.VISIBLE);
                errorView.setVisibility(View.GONE);
                mainSwipeableContent.setRefreshing(false);
            } else if (!forceRefresh) {
                progressBar.setVisibility(View.VISIBLE);
                errorView.setVisibility(View.GONE);
                mainSwipeableContent.setVisibility(View.GONE);
            }
        } else {
            mainSwipeableContent.setRefreshing(false);
            progressBar.setVisibility(View.GONE);
        }

    }

    @Override
    public void showError(Throwable error) {
        errorView.setVisibility(View.VISIBLE);
        mainSwipeableContent.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        Timber.e(error, "There was an error retrieving the weather");
    }
}
