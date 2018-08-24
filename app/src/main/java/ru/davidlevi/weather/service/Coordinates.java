package ru.davidlevi.weather.service;

import java.io.Serializable;

/**
 * Класс для хранения координат
 */
public class Coordinates implements Serializable {
    private String latitude;
    private String longitude;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public boolean isNotNull() {
        return !(latitude == null | longitude == null);
    }

    @Override
    public String toString() {
        return String.format("Coordinates [lat:%s lon:%s]", latitude, longitude);
    }
}
