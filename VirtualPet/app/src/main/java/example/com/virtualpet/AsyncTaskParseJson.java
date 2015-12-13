package example.com.virtualpet;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mrlukasbos on 13/12/15.
 */
// you can make this class as another java file so it will be separated from your main activity.
public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

    String yourJsonStringUrl;
    final String TAG;
    JSONArray dataJsonArr;

    public AsyncTaskParseJson(Context context) {
        TAG = "AsyncTaskParseJson.java";

        // set your json string url here
         yourJsonStringUrl = "https://maps.googleapis.com/maps/api/place/radarsearch/json?location=48.859294,2.347589&radius=5000&types=food&key=AIzaSyBpgUXiJgnGDnfJ6eR-Nf_W3BzJX4jtcrg";

        // contacts JSONArray
         dataJsonArr = null;
    }


    @Override
    protected void onPreExecute() {}

    @Override
    protected String doInBackground(String... arg0) {

        try {

            // instantiate our json parser
            JsonParser jParser = new JsonParser();

            // get json string from url
            JSONObject json = jParser.getJSONFromUrl(yourJsonStringUrl);


            Log.d("tag", json.toString(4));


            // get the array of users
            dataJsonArr = json.getJSONArray("results");

            // loop through all results
            for (int i = 0; i < dataJsonArr.length(); i++) {

                JSONObject c = dataJsonArr.getJSONObject(i);

                // Storing each json item in variable
                String id = c.getString("id");
                String name = c.getString("name");
                String icon = c.getString("icon");

                // show the values in our logcat
                Log.i(TAG, "id: " + id
                        + ", name: " + name
                        + ", icon: " + icon);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String strFromDoInBg) {}
}