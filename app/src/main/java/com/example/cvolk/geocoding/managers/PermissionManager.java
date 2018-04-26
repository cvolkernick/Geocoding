package com.example.cvolk.geocoding.managers;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

public class PermissionManager{

    private static final int PERMISSIONS_REQUEST_LOCATION = 1;
    public static final String TAG = PermissionManager.class.getSimpleName() + "_TAG";

    public static PermissionManager instance = null;

    Activity activity;
    public IPermissionInteraction permissionListener;

    private PermissionManager() {}

    public static PermissionManager getDefault(Activity activity) {

        if (instance == null) {
            instance = new PermissionManager(activity);
        }

        instance.attach(activity);

        return instance;
    }

    public PermissionManager(Activity activity) {
        this.activity = activity;
    }

    public void attach(Object object) {

        if (object instanceof IPermissionInteraction) {
            Log.d(TAG, "attach: ");

            permissionListener = (IPermissionInteraction) object;
        }
    }

    public void checkPermission() {


        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            Log.d(TAG, "checkPermission: Permission is not granted");

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                Log.d(TAG, "checkPermission: Show an explanation to the user");
                getExplanationDialog().show();
            }
            else {
                requestPermission();
            }
        }
        else {

            permissionListener.onPermissionResult(true);


            Log.d(TAG, "checkPermission: Permission has already been granted");
        }

    }

    private void requestPermission() {

        Log.d(TAG, "requestPermission: No explanation needed; request the permission");
        ActivityCompat.requestPermissions(activity,
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION },
                PERMISSIONS_REQUEST_LOCATION);
    }

    private AlertDialog getExplanationDialog() {

        return new AlertDialog.Builder(activity)
                .setTitle("Need the permission")
                .setMessage("Can you please allow this permission? I need it.")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(activity,
                                "Features disabled",
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        requestPermission();
                    }
                }).create();
    }

    public void checkResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {

            case PERMISSIONS_REQUEST_LOCATION: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.d(TAG, "checkResult: permission was granted");

                    permissionListener.onPermissionResult(true);
                }
                else {

                    permissionListener.onPermissionResult(false);

                    Log.d(TAG, "checkResult: permission denied");
                }

                return;
            }
        }
    }

    public interface IPermissionInteraction {

        public void onPermissionResult(boolean isGranted);
    }
}