package example.com.virtualpet.maps;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by mrlukasbos on 21/12/15.
 */
public class Place {

    String place_id, name, address;
    LatLng latlng;

    Place() {

    }

    Place(LatLng latlng, String place_id) {
        this.latlng = latlng;
        this.place_id = place_id;
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
