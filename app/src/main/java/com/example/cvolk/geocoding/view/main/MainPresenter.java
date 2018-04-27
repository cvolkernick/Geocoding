package com.example.cvolk.geocoding.view.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.example.cvolk.geocoding.managers.LocationManager;
import com.example.cvolk.geocoding.managers.PermissionManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MainPresenter implements MainContract.Presenter{

    MainContract.View view;

    PermissionManager permissionManager;
    LocationManager locationManager;
    FusedLocationProviderClient locationProviderClient;
    Location currentLocation;
    private Geocoder geocoder;

    public MainPresenter(PermissionManager permissionManager, LocationManager locationManager) {

        permissionManager.attach(this);
        locationManager.attach(this);

        this.permissionManager = permissionManager;
        this.locationManager = locationManager;

        permissionManager.checkPermission();
    }

    @Override
    public void attachView(MainContract.View view) {

        this.view = view;
    }

    @Override
    public void detachView() {

        this.view = null;
    }

    @Override
    public void checkPermission() {

        permissionManager.checkPermission();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void getLocation() {

        Log.d(TAG, "getLocation: ");

        // think this is not good practice...? (casting as activity)
        locationProviderClient = LocationServices.getFusedLocationProviderClient((Activity)view);

        locationProviderClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {

                    @Override
                    public void onSuccess(Location location) {
                        Log.d(TAG, "onSuccess: " + location.toString());

                        currentLocation = location;

                        view.onGetLocation(location);
                    }
                });
    }

    @Override
    public void geocode(Context context, String address) {
        geocoder = new Geocoder(context);
        if (geocoder.isPresent()) {
            try {
                List<Address> list = geocoder.getFromLocationName(address, 1);
                Address result = list.get(0);

                double lat = result.getLatitude();
                double lon = result.getLongitude();

                view.onGeocode(lat, lon);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
                Toast.makeText(context, "No results.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void reverseGeocode(Context context, double lat, double lon) {
        geocoder = new Geocoder(context);
        if (geocoder.isPresent()) {
            try {
                List<Address> list = geocoder.getFromLocation(lat, lon, 1);
                Address address = list.get(0);

//                StringBuffer result = new StringBuffer();
//
//                result.append("Name: " + address.getLocality() + "\n");
//                result.append("Sub-Admin Area: " + address.getSubAdminArea() + "\n");
//                result.append("Admin Area: " + address.getAdminArea() + "\n");
//                result.append("Country: " + address.getCountryName() + "\n");
//                result.append("Country Code: " + address.getCountryCode() + "\n");
//
//                String resultString = result.toString();

                view.onReverseGeocode(address.getAddressLine(0));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
                Toast.makeText(context, "No results.", Toast.LENGTH_SHORT).show();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                Toast.makeText(context, "Invalid Coordinates.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
