package com.example.cvolk.geocoding.view.main;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cvolk.geocoding.R;
import com.example.cvolk.geocoding.managers.LocationManager;
import com.example.cvolk.geocoding.managers.PermissionManager;

public class MainActivity extends AppCompatActivity implements MainContract.View, PermissionManager.IPermissionInteraction {

    private static final String TAG = MainActivity.class.getSimpleName() + "_TAG";

    private MainPresenter mainPresenter;
    private TextView tvLocation;
    private EditText etAddressLocation;
    private EditText etLocationLatitude;
    private EditText etLocationLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();

        mainPresenter = new MainPresenter(
                PermissionManager.getDefault(this),
                LocationManager.getDefault(this)
        );
    }

    @Override
    protected void onStart() {
        super.onStart();

        mainPresenter.attachView(this);
        mainPresenter.checkPermission();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mainPresenter.detachView();
    }

    private void bindViews() {
        tvLocation = findViewById(R.id.tvLocation);
        etAddressLocation = findViewById(R.id.etLocationAddress);
        etLocationLatitude = findViewById(R.id.etLocationLatitude);
        etLocationLongitude = findViewById(R.id.etLocationLongitude);
    }

    @Override
    public void onPermissionResult(boolean isGranted) {
        Log.d(TAG, "onPermissionResult: " + isGranted);
        if (mainPresenter != null && isGranted) {
            mainPresenter.getLocation();
        }
    }

    @Override
    public void onCheckPermission(boolean isEnabled) {
        if (isEnabled) {
            mainPresenter.getLocation();
        }
    }

    @Override
    public void onGetLocation(Location location) {
        String locationString = location.getLatitude() + " : " + location.getLongitude();
        //tvLocation.setText(locationString);
    }

    @Override
    public void onGeocode(double lat, double lon) {
        tvLocation.setText("Location Geocode : " + lat + " Lat / " + lon + " Lon");
        etLocationLatitude.setText(Double.toString(lat));
        etLocationLongitude.setText(Double.toString(lon));
    }

    @Override
    public void onReverseGeocode(String address) {
        tvLocation.setText(address);
        etAddressLocation.setText(address);
    }

    @Override
    public void onGeocodeAPI(double lat, double lon) {
        tvLocation.setText("Location Geocode : " + lat + " Lat / " + lon + " Lon");
        etLocationLatitude.setText(Double.toString(lat));
        etLocationLongitude.setText(Double.toString(lon));
    }

    @Override
    public void onReverseGeocodeAPI(String address) {
        tvLocation.setText(address);
        etAddressLocation.setText(address);
    }

    @Override
    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    public void geocodeLocation(View view) {
        mainPresenter.geocode(this, etAddressLocation.getText().toString());
    }

    public void reverseGeocodeLocation(View view) {
        mainPresenter.reverseGeocode(
            this,
            Double.parseDouble(etLocationLatitude.getText().toString()),
            Double.parseDouble(etLocationLongitude.getText().toString())
        );
    }

    public void geocodeLocationAPI(View view) {
        mainPresenter.geocodeAPI(this, etAddressLocation.getText().toString());
    }

    public void reverseGeocodeLocationAPI(View view) {
        mainPresenter.reverseGeocodeAPI(
            this,
            Double.parseDouble(etLocationLatitude.getText().toString()),
            Double.parseDouble(etLocationLongitude.getText().toString())
        );
    }
}
