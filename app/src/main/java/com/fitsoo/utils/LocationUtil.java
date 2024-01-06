package com.fitsoo.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;

import com.fitsoo.activity.baseclass.BaseActivity;
import com.fitsoo.preference.FitsooPref;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by system  on 6/3/17.
 *
 * This is a location utility class. Everywhere where you need to get the location
 * you will need to use this class instead of Location listener or any other component.
 *
 */

public class LocationUtil extends BaseActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static LocationUtil locationUtil;
    private GoogleApiClient mApiClient;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 101;
    private LocationRequest mLocationRequest;
    private Context actContext;
    private long interval = 10000;
    private long fastestInterval = 5000;
    private int priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;
    private LocationManager locationManager;
    public boolean isGpsEnabled = false;

    private LocationUtil(Context actContext) {
        this.actContext = actContext;
        mApiClient = new GoogleApiClient.Builder(actContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        initializeLocationManager();
    }

    private void initializeLocationManager(){
        locationManager = (LocationManager) actContext.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(actContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(actContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) actContext,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocation);
    }

    public static LocationUtil getInstance(Context actContext) {
        if (locationUtil == null) {
            locationUtil = new LocationUtil(actContext);
        }
        return locationUtil;
    }

    public void setInterval(long interval) {
        if (interval > 1000) {
            this.interval = interval;
        }
    }

    public void setFastestInterval(long fastestInterval) {
        if (fastestInterval > 1000) {
            this.fastestInterval = fastestInterval;
        }
    }

    public GoogleApiClient getGoogleApiInstance() {
        return mApiClient;
    }


    // TO be called to get the Last known location
    public Location getLastKnownLocation() {
        Location tempLocation = null;
        if(locationManager == null){
            initializeLocationManager();
        }

        startGettingLocation();
        if (ActivityCompat.checkSelfPermission(actContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(actContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) actContext,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            return null;
        }

        isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        tempLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null ? locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) : locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(tempLocation != null){
            return tempLocation;
        } else {
            GPSTracker tracker = new GPSTracker(actContext);
            if(tracker.canGetLocation()){
                tempLocation = tracker.getLocation();
                tracker.getLatitude();
                tracker.getLongitude();
                return tempLocation;
            }
        }

        return tempLocation;
    }


    public void showGPSSettingAlert(final Context context){
        AlertDialog.Builder messageAlert = new AlertDialog.Builder(context);
        messageAlert.setTitle("Enable GPS!!");
        messageAlert.setMessage("We need you to enable your GPS to get data more accurately!");
        messageAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });

        messageAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        messageAlert.create().show();
    }

    public void setPriority(int priority){
        this.priority = priority;
    }

    // Should be called when want to start getting the Location
    public void startGettingLocation(){mApiClient.connect();}

    // Should be called when want to stop getting the location
    public void stopGettingLocation(){mApiClient.disconnect();
    locationManager.removeUpdates(mLocation);}

    // Should be called when want to remove any update in the Location
    public void removeLocationUpdates() {
        if (mApiClient != null && mApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mApiClient, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        FitsooPref.saveLocation(location.getLatitude() , location.getLongitude() , actContext);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(actContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(actContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(actContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) actContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            return;
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(interval);
        mLocationRequest.setFastestInterval(fastestInterval);
        mLocationRequest.setPriority(priority);

        if (ActivityCompat.checkSelfPermission(actContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(actContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mApiClient, mLocationRequest, this);
        }

    }


    android.location.LocationListener mLocation = new android.location.LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            FitsooPref.saveLocation(location.getLatitude() , location.getLongitude() , actContext);
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
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.

                return;
            }
        }
    }
}
