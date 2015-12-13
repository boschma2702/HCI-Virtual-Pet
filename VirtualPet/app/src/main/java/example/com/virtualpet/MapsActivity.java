package example.com.virtualpet;

import android.content.IntentSender;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


public class MapsActivity extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final String TAG = MapsActivity.class.getSimpleName();

    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location previouslocation;
    private Location location;
    private Marker currentlocationmarker;
    private float TotalDistance = 1234; //500 meter
    private float distancewalked = 0;
    private float DistanceToWalk = TotalDistance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5 * 1000)        // 5 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
        mLocationRequest.setSmallestDisplacement(1); //only trigger after 1 meter

        // we will using AsyncTask during parsing
        new AsyncTaskParseJson(this).execute();

    }

    @Override
    protected void onStart() {
        super.onStart();
        setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

     //This should only be called once and when we are sure that {@link #mMap} is not null.
    private void setUpMap() {
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());


        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();


        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        if (previouslocation != null) {

            double prevLatitude = previouslocation.getLatitude();
            double prevLongitude = previouslocation.getLongitude();


            // draw line and marker from previous to current location
            mMap.addPolyline(new PolylineOptions()
                    .add(new LatLng(prevLatitude, prevLongitude), new LatLng(currentLatitude, currentLongitude))
                    .width(5)
                    .color(Color.RED));
            if (currentlocationmarker != null) {
                currentlocationmarker.remove();
            }

            currentlocationmarker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(currentLatitude, currentLongitude))
                    .title("Current location!"));

             distancewalked += (int)location.distanceTo(previouslocation);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // get textviews
        TextView distancetowalktext_tv = (TextView)findViewById(R.id.distancetowalktext);
        TextView distance_m_or_km = (TextView)findViewById(R.id.m_or_km);

        // get distance to walk
        DistanceToWalk = TotalDistance - distancewalked;

        // make nice notation (400 meter, 1.44 kilomter)
        if (DistanceToWalk < 1000) {
            distancetowalktext_tv.setText(String.valueOf(DistanceToWalk));
            distance_m_or_km.setText("meter");
        } else {
            float dist_to_walk = DistanceToWalk/1000;
            distancetowalktext_tv.setText(String.valueOf(dist_to_walk));
            distance_m_or_km.setText("kilometer");
        }





        // store current location in previous location
        previouslocation = location;
    }

    @Override
    public void onConnected(Bundle bundle) {

        location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        handleNewLocation(location);
    }



    @Override
    public void onConnectionSuspended(int i) {


    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "ON LOCATION CHANGED");
        handleNewLocation(location);
    }
}

