package example.com.virtualpet.maps;

import android.content.Context;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import example.com.virtualpet.R;


public class MapsActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMarkerClickListener {

    public static final String TAG = MapsActivity.class.getSimpleName();
     /* Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    public GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location previouslocation;
    private Location location;
    private Marker currentlocationmarker;
    private float TotalDistance = 1234; //500 meter
    private float distancewalked = 0;
    int radius = 3000;
    private float DistanceToWalk = TotalDistance;
    float checkdistance = 0;
    boolean firstLoad = true;
    ArrayList<Place> places = new ArrayList<Place>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        startAPIclient();
        createLocationRequest();

        // Setting a custom info window adapter for the google map
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker marker) {

                // Getting view from the layout file info_window_layout
                View v = getLayoutInflater().inflate(R.layout.infowindowlayout, null);

                // Getting reference to the TextView to set latitude
                TextView tvaddress = (TextView) v.findViewById(R.id.tv_address);
                TextView tvopen = (TextView) v.findViewById(R.id.tv_open);
                TextView tvtitle = (TextView) v.findViewById(R.id.tv_title);

                for (Place place : places) {
                    if (marker.getId().equals(place.getPlaceMarker().getId())) {

                        if (place.isOpen()) {
                            tvopen.setText("Open now");
                            tvopen.setTextColor(Color.GREEN);
                        } else if(place.isOpen() == false) {
                            tvopen.setText("closed now");
                            tvopen.setTextColor(Color.RED);
                        } else {
                            tvopen.setText("openingstimes unknown");
                            tvopen.setTextColor(Color.GRAY);
                        }
                        tvtitle.setText(place.getName());
                        tvaddress.setText(place.getAddress());
                    }
                }

                // Returning the view containing InfoWindow contents
                return v;

            }
        });

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
         if (!mGoogleApiClient.isConnected()) {
             mGoogleApiClient.connect();
         }
    }

    @Override
    protected void onPause() {
        super.onPause();

       // if (mGoogleApiClient.isConnected()) {
         //   LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
          //  mGoogleApiClient.disconnect();
      //  }
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
                firstLoad = true;
            }
        }
    }

    //This should only be called once and when we are sure that {@link #mMap} is not null.
    private void setUpMap() {
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    private void handleNewLocation(Location location) {

        drawCurrentLocation(location);
        updateDistance();

        // not update every time the supermarkets locations, but only when user significanlty moves
        if (distancewalked > checkdistance + (radius/2) || firstLoad) {
            // we will using AsyncTask during parsing
            new AsyncTaskParseJson(this, mMap).execute();
            checkdistance = distancewalked;
            firstLoad = false;
        }

        // store current location in previous location
        previouslocation = location;
    }

    @Override
    public void onConnected(Bundle bundle) {

        location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        handleNewLocation(location);


        checkIfAtGroceryStore();
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




    public void setMarkers(Place place) {
        place.setPlaceMarker(mMap);
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        Log.i(TAG, "onmarkerclick is called. now try to find the place where it is attached to");

        for (Place place : places) { // look in all places

            Log.i(TAG, "the ids that are compared are: " + marker.getId() + " " + place.getPlaceMarker().getId());

            if (marker.getId().equals(place.getPlaceMarker().getId())) { // check for the place where this marker belongs to
                //handle click here
                if (place.details_updated) {
                    marker.showInfoWindow(); // if we already know the details there is no need to parse Json
                } else {
                    new AsyncTaskParseJson(this, mMap, place).execute(); //parse request with a place so it knows to get details instead of place. (otherwise we could not have known te place)
                    Log.i(TAG, "onmarkerclick is called, and a asynctask has been requested with the place with id: " + place.getId());
                    return true; // returning true means the normal behaviour is overwritten
                }
            }
        }
        return false; // returning false means the normal behaviour is not overwritten.
    }

    public void HideProgressCircle() {
        ProgressBar p = (ProgressBar) findViewById(R.id.marker_progress);
        p.setVisibility(View.INVISIBLE);;
    }

    public void ShowProgressCircle() {
        ProgressBar p = (ProgressBar) findViewById(R.id.marker_progress);
        p.setVisibility(View.VISIBLE);;
    }

    private void startAPIclient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
    }

    private void createLocationRequest() {
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5 * 1000)        // 5 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
        mLocationRequest.setSmallestDisplacement(5); //only trigger after 5 meter
    }

    private void checkIfAtGroceryStore() {
        if (places != null) {
            for (Place place : places) {
                Location place_loc = new Location("placelocation");
                place_loc.setLatitude(place.getLocation().latitude);
                place_loc.setLongitude(place.getLocation().longitude);

                if(location.distanceTo(place_loc) < 50 && place.isOpen()) { // if within range of 50 meters of a known place, and if the place is open off course muhaha!
                    //TODO trigger things. but hey! you are at a grocery store. Awesome! Lets show a toast instead.

                    Context context = getApplicationContext();
                    CharSequence text = "you are close to a supermarket!";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        }
    }

    private void drawCurrentLocation(Location location) {

        // Log.d(TAG, location.toString());

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

            distancewalked += (int) location.distanceTo(previouslocation);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    private void updateDistance() {
        DistanceToWalk = TotalDistance - distancewalked;

        // get textviews
        TextView distancetowalktext_tv = (TextView) findViewById(R.id.distancetowalktext);
        TextView distance_m_or_km = (TextView) findViewById(R.id.m_or_km);

        // make nice notation (400 meter, 1.44 kilomter)
        if (DistanceToWalk < 1000) {

            distancetowalktext_tv.setText(String.valueOf(DistanceToWalk));
            distance_m_or_km.setText("meter");
        } else {
            double dist_to_walk = DistanceToWalk / 1000;
            //format to two decimals and set text
            distancetowalktext_tv.setText(String.format("%.2f", dist_to_walk));
            distance_m_or_km.setText("kilometer");
        }
    }

    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

        String supermarketUrl;
        String detailsUrl;
        final String TAG;
        JSONArray dataJsonArr;

        JSONArray GeometryJsonArr;
        GoogleMap map;
        String location_lat, location_lng, place_id;
        private String key = "AIzaSyBpgUXiJgnGDnfJ6eR-Nf_W3BzJX4jtcrg";
        Place place;

        public AsyncTaskParseJson(Context context, GoogleMap map) {
            TAG = "AsyncTaskParseJson.java";
            this.map = map;

            // results JSONArray
            dataJsonArr = null;
            GeometryJsonArr = null;
        }

        public AsyncTaskParseJson(Context context, GoogleMap map, Place place) {
            TAG = "AsyncTaskParseJson.java";
            this.map = map;

            // results JSONArray
            dataJsonArr = null;
            GeometryJsonArr = null;
            this.place = place;
        }



        @Override
        protected void onPreExecute() {
            MapsActivity.this.ShowProgressCircle();
        }

        @Override
        protected String doInBackground(String... arg0) {

            if (place != null) {
                parseGroceryDetails(place);
                Log.i(TAG, "the place is not null and we try to do the function parsegrocerydetails");
            } else {
                parseGroceryStore();
                Log.i(TAG, "the place is null and we try to do the function paresgrocerystore");

            }
            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg) {
            if (place != null) {
                place.updatePlaceMarker();
            } else {
                for (int i = 0; i < places.size(); i++) {
                    Place myPlace = places.get(i);
                    //set locations and id's in markers
                    MapsActivity.this.setMarkers(myPlace);
                }
            }
            MapsActivity.this.HideProgressCircle();
        }

        private void parseGroceryStore() {
            try {

                // set your json string url here
                supermarketUrl = "https://maps.googleapis.com/maps/api/place/radarsearch/json?location="
                        + String.valueOf(location.getLatitude())
                        + ","
                        + String.valueOf(location.getLongitude())
                        + "&radius="
                         + String.valueOf(radius)
                        + "&types=grocery_or_supermarket&key="
                        + key;

                JsonParser jParser = new JsonParser();                 // instantiate our json parser

                // get json string from url
                JSONObject json = jParser.getJSONFromUrl(supermarketUrl);
                //Log.d("tag", json.toString(4));

                // get the array of users
                dataJsonArr = json.getJSONArray("results");

                // loop through all results
                for (int i = 0; i < dataJsonArr.length(); i++) {

                    JSONObject c = dataJsonArr.getJSONObject(i);

                    JSONObject geo_object = c.getJSONObject("geometry");
                    JSONObject location_object = geo_object.getJSONObject("location");

                    // Storing each json item in variable
                    location_lat = location_object.getString("lat");
                    location_lng = location_object.getString("lng");
                    place_id = c.getString("place_id");

                    // show the values in our logcat
                    Log.i(TAG, "id: " + place_id + ", lat: " + location_lat + ", lng: " + location_lng);


                    // check if place exists. if it does do not add place.
                    boolean placeExists = false;
                    for (Place place : places) {
                        if (place.place_id == place_id) {
                            placeExists = true;
                        }
                    }
                    if (!placeExists) {
                        //add values to a place object
                        places.add((new Place(new LatLng(Double.parseDouble(location_lat), Double.parseDouble(location_lng)), place_id)));
                    }

                  }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        void parseGroceryDetails(Place place) {
            String place_id = place.getId();
            try {
                detailsUrl = "https://maps.googleapis.com/maps/api/place/details/json?placeid="
                        + place_id
                        + "&key="
                        + key;

                JsonParser detailsParser = new JsonParser();                 // instantiate our json parser

                // get json string from url
                JSONObject details_json = detailsParser.getJSONFromUrl(detailsUrl);
                Log.d(TAG, details_json.toString(4));

                    JSONObject c = details_json.getJSONObject("result");
                    String name = c.getString("name");
                    String address = c.getString("formatted_address");

                    try {
                        JSONObject openinghours = c.getJSONObject("opening_hours");
                        String open = openinghours.getString("open_now");
                        place.setOpen(open);
                    } catch(JSONException e) {
                        place.setOpen(null);
                    }

                place.setName(name);
                place.setAddress(address);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}


