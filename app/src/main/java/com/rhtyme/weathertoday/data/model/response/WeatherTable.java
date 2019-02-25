package com.rhtyme.weathertoday.data.model.response;

import com.google.gson.Gson;
import com.rhtyme.weathertoday.data.model.response.w.Weather;
import com.rhtyme.weathertoday.data.model.response.w.WeatherCity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

@Entity(nameInDb = "weather_record")
public class WeatherTable {

    @Id(autoincrement = true)
    private Long id;

    String data;

    @Property(nameInDb = "created_at")
    private Long createdAt;

    @Transient
    WeatherCity weatherCity;

    private static final long serialVersionUID = 7526472295622776134L;

    @Generated(hash = 1162233405)
    public WeatherTable(Long id, String data, Long createdAt) {
        this.id = id;
        this.data = data;
        this.createdAt = createdAt;
    }

    @Generated(hash = 33035578)
    public WeatherTable() {
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public WeatherCity weatherCityObject() {
        if (weatherCity == null && data != null) {
            Gson gson = new Gson();
            weatherCity = gson.fromJson(data, WeatherCity.class);
        }
        return weatherCity;
    }

    public void setupWeatherCityData(WeatherCity weatherCity) throws Exception {
        if (weatherCity == null) {
            throw new Exception("null WeatherCity object");
        }
        Gson gson = new Gson();
        this.data = gson.toJson(weatherCity);
        this.weatherCity = weatherCity;
    }
}
