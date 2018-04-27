package com.example.cvolk.geocoding.view.main;

import android.content.Context;
import android.location.Location;

import com.example.cvolk.geocoding.view.base.BasePresenter;
import com.example.cvolk.geocoding.view.base.BaseView;

public interface MainContract {

    interface View extends BaseView {

        void onCheckPermission(boolean isEnabled);

        void onGetLocation(Location location);

        void onGeocode(double lat, double lon);

        void onReverseGeocode(String address);

        void onGeocodeAPI(double lat, double lon);

        void onReverseGeocodeAPI(String address);
    }

    interface Presenter extends BasePresenter<View> {

        void checkPermission();

        void getLocation();

        void geocode(Context context, String address);

        void reverseGeocode(Context context, double lat, double lon);

        void geocodeAPI(Context context, String address);

        void reverseGeocodeAPI(Context context, double lat, double lon);
    }
}
