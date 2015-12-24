package example.com.virtualpet.maps;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by mrlukasbos on 21/12/15.
 */
public class Place {

    public static final String TAG = MapsActivity.class.getSimpleName();

    String place_id, name, address;
    LatLng latlng;
    Marker place_marker;
    GoogleMap mMap;
    boolean details_updated;

    Place() {
    }

    Place(LatLng latlng, String place_id) {
        this.latlng = latlng;
        this.place_id = place_id;
        details_updated = false;
    }

    void setPlaceMarker(GoogleMap mMap) {
        place_marker = mMap.addMarker(new MarkerOptions()
                .position(latlng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        this.mMap = mMap;
    }

    void updatePlaceMarker() {
        place_marker.setTitle(name);
        place_marker.setSnippet(address);

        details_updated = true;

        place_marker.showInfoWindow();
    }

    Marker getPlaceMarker() {
        return place_marker;
    }

    void setName(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }

    void setId() {

    }

    String getId() {
        return place_id;
    }

    void setLocation() {

    }

    LatLng getLocation() {
        return latlng;
    }

    void setAddress(String address) {

        this.address = address;
    }

}
