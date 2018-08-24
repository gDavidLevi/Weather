package ru.davidlevi.weather.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

public class LocationService extends Service {
    // Logcat
    private static final String TAG = "LocationService";

    // Intent
    public static final String INTENT_LOCATION_NAME = "IntentLocationName";
    public static final String ACTION_BROADCAST_LOCATION = "ru.davidlevi.action.ACTION_BROADCAST_LOCATION";

    public LocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        run();
        return Service.START_STICKY;
        // Данный режим (Service.START_STICKY) обычно используется для сервисов, которые сами
        // обрабатывают свои состояния, явно стартуя и завершая свою работу при необходимости.
    }

    private void run() {
        final Coordinates currentCoordinates = new Coordinates();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String bestProvider = null;
        if (locationManager != null)
            bestProvider = locationManager.getBestProvider(criteria, true);
        if (bestProvider != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d("Finder", "Не получили разрешений для доступа к локации");
            }
            locationManager.requestLocationUpdates(bestProvider, 10000, 10, new LocationListener() { // каждые 10 секунд, растоянием 10 метров
                @Override
                public void onLocationChanged(Location location) {
                    String lat = Double.toString(location.getLatitude())
                            .replace(",", ".");
                    String lon = Double.toString(location.getLongitude())
                            .replace(",", ".");
                    currentCoordinates.setLatitude(lat);
                    currentCoordinates.setLongitude(lon);

                    // Отправляем бродкаст c координатами
                    Intent intent = new Intent(ACTION_BROADCAST_LOCATION);
                    intent.putExtra(INTENT_LOCATION_NAME, currentCoordinates);
                    sendBroadcast(intent);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                }
            });
        }
    }
}