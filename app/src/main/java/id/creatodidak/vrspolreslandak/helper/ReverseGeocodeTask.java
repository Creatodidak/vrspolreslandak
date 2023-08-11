package id.creatodidak.vrspolreslandak.helper;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReverseGeocodeTask extends AsyncTask<LatLng, Void, String> {

    private static final String TAG = "ReverseGeocodeTask";
    private static final String GOOGLE_MAPS_API_KEY = "AIzaSyBbhErQDKRIVAy6te7yCzN8wGWrAe8AnT8";
    private final MarkerOptions markerOptions;

    public ReverseGeocodeTask(MarkerOptions markerOptions) {
        this.markerOptions = markerOptions;
    }

    @Override
    protected String doInBackground(LatLng... params) {
        if (params.length == 0) {
            return null;
        }

        LatLng latLng = params[0];

        String urlString = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" +
                latLng.latitude + "," + latLng.longitude + "&key=" + GOOGLE_MAPS_API_KEY;

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            bufferedReader.close();
            inputStream.close();
            connection.disconnect();

            return stringBuilder.toString();
        } catch (IOException e) {
            Log.e(TAG, "Error fetching data from Google API", e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            String locationAddress = parseAddressFromJson(result);
            if (locationAddress != null) {
                markerOptions.title(locationAddress);
            }
        }
    }

    private String parseAddressFromJson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray resultsArray = jsonObject.getJSONArray("results");
            if (resultsArray.length() > 0) {
                JSONObject addressObject = resultsArray.getJSONObject(0);
                return addressObject.getString("formatted_address");
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON", e);
        }
        return null;
    }
}
