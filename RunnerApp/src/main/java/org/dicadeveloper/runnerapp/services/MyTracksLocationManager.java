/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.dicadeveloper.runnerapp.services;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.dicadeveloper.runnerapp.util.GoogleLocationUtils;

//import com.google.android.gms.location.LocationClient;

/**
 * My Tracks Location Manager. Applies Google location settings before allowing
 * access to {@link LocationManager}.
 * 
 * @author Jimmy Shih
 */
public class MyTracksLocationManager {

  /**
   * Observer for Google location settings.
   * 
   * @author Jimmy Shih
   */
  private class GoogleSettingsObserver extends ContentObserver {

    public GoogleSettingsObserver(Handler handler) {
      super(handler);
    }

    @Override
    public void onChange(boolean selfChange) {
      isAllowed = GoogleLocationUtils.isAllowed(context);
    }
  }

  private final GoogleApiClient.ConnectionCallbacks connectionCallbacks = new GoogleApiClient.ConnectionCallbacks() {


      @Override
    public void onConnected(Bundle bunlde) {
      handler.post(new Runnable() {
          @Override
        public void run() {
          if (requestLastLocation != null && mGoogleApiClient.isConnected()) {
            requestLastLocation.onLocationChanged(LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient));
            requestLastLocation = null;
          }
          if (requestLocationUpdates != null && mGoogleApiClient.isConnected()) {
            LocationRequest locationRequest = new LocationRequest().setPriority(
                LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(requestLocationUpdatesTime)
                .setFastestInterval(requestLocationUpdatesTime)
                .setSmallestDisplacement(requestLocationUpdatesDistance);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                locationRequest, requestLocationUpdates, handler.getLooper());
          }
        }
      });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
  };

  private final GoogleApiClient.OnConnectionFailedListener
      onConnectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
          @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {}
      };

  private GoogleApiClient mGoogleApiClient;
  private final Context context;
  private final Handler handler;
  //private final LocationClient locationClient;
  private final LocationManager locationManager;
  private final ContentResolver contentResolver;
  private final GoogleSettingsObserver observer;

  private boolean isAllowed;
  private LocationListener requestLastLocation;
  private LocationListener requestLocationUpdates;
  private float requestLocationUpdatesDistance;
  private long requestLocationUpdatesTime;

  public MyTracksLocationManager(Context context, Looper looper, boolean enableLocaitonClient) {
    this.context = context;
    this.handler = new Handler(looper);

    if (enableLocaitonClient) {
      mGoogleApiClient = new GoogleApiClient.Builder(context)
              .addApi(LocationServices.API)
              .addConnectionCallbacks(connectionCallbacks)
              .addOnConnectionFailedListener(onConnectionFailedListener)
              .build();
      mGoogleApiClient.connect();
    } else {
      mGoogleApiClient = null;
    }

    locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    contentResolver = context.getContentResolver();
    observer = new GoogleSettingsObserver(handler);

    isAllowed = GoogleLocationUtils.isAllowed(context);

    contentResolver.registerContentObserver(
        GoogleLocationUtils.USE_LOCATION_FOR_SERVICES_URI, false, observer);
  }

  /**
   * Closes the {@link MyTracksLocationManager}.
   */
  public void close() {
    if (mGoogleApiClient != null) {
      mGoogleApiClient.disconnect();
    }
    contentResolver.unregisterContentObserver(observer);
  }

  /**
   * Returns true if allowed to access the location manager. Returns true if
   * there is no enforcement or the Google location settings allows access to
   * location data.
   */
  public boolean isAllowed() {
    return isAllowed;
  }

  /**
   * Returns true if gps provider is enabled.
   */
  public boolean isGpsProviderEnabled() {
    if (!isAllowed()) {
      return false;
    }
    String provider = LocationManager.GPS_PROVIDER;
    if (locationManager.getProvider(provider) == null) {
      return false;
    }
    return locationManager.isProviderEnabled(provider);
  }

  /**
   * Request last location.
   * 
   * @param locationListener location listener
   */
  public void requestLastLocation(final LocationListener locationListener) {
    handler.post(new Runnable() {
        @Override
      public void run() {
        if (!isAllowed()) {
          requestLastLocation = null;
          locationListener.onLocationChanged(null);
        } else {
          requestLastLocation = locationListener;
          connectionCallbacks.onConnected(null);
        }
      }
    });
  }

  /**
   * Requests location updates. This is an ongoing request, thus the caller
   * needs to check the status of {@link #isAllowed}.
   * 
   * @param minTime the minimal time
   * @param minDistance the minimal distance
   * @param locationListener the location listener
   */
  public void requestLocationUpdates(
      final long minTime, final float minDistance, final LocationListener locationListener) {
    handler.post(new Runnable() {
        @Override
      public void run() {
        requestLocationUpdatesTime = minTime;
        requestLocationUpdatesDistance = minDistance;
        requestLocationUpdates = locationListener;
        connectionCallbacks.onConnected(null);
      }
    });
  }

  /**
   * Removes location updates.
   * 
   * @param locationListener the location listener
   */
  public void removeLocationUpdates(final LocationListener locationListener) {
    handler.post(new Runnable() {
        @Override
      public void run() {
        requestLocationUpdates = null;
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
          LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,locationListener);
        }
      }
    });
  }
}