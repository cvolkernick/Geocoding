package com.example.cvolk.geocoding.managers;

import android.app.Activity;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import static android.content.ContentValues.TAG;

public class LocationManager {

    Activity activity;
    private FusedLocationProviderClient locationProviderClient;
    ILocationInteraction locationListener;

    public static LocationManager instance = null;

    private LocationManager() {}

    public static LocationManager getDefault(Activity activity) {

        if (instance == null) {
            instance = new LocationManager(activity);
        }

        instance.attach(activity);

        return instance;
    }

    private LocationManager(Activity activity) {

        this.activity = activity;
        locationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
    }

    public void attach(Object object) {

        if (object instanceof ILocationInteraction) {
            Log.d(TAG, "attach: ");

            this.locationListener = (ILocationInteraction) object;
        }
    }

    public interface ILocationInteraction {

        void onGetLocation(Location location);
    }
}
