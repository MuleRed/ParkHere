package re.parkhe.parkhere.data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import re.parkhe.parkhere.model.ParkingLot;
import re.parkhe.parkhere.model.ParkingLotsList;

/**
 * Created by ryan on 10/28/17.
 */

public class GetParkingLotsRequest extends AsyncTask<String, Void, String> {
    private Context mContext;

    public GetParkingLotsRequest(Context context) {
        mContext = context;
    }

    @Override
    protected String doInBackground(String... strings) {

        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL(strings[0]);
            urlConnection = (HttpURLConnection) url.openConnection();

            int responseCode = urlConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String server_response = readStream(urlConnection.getInputStream());
                jsonToArrayList(server_response);
                Log.v("CatalogClient", server_response);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void jsonToArrayList(String server_response) {
        ArrayList<ParkingLot> parkingLots = new ArrayList<>();
        try {
            JSONArray jArray = new JSONArray(server_response);
            for (int i = 0; i < jArray.length(); i++) {

                JSONObject jObject = jArray.getJSONObject(i);

                ParkingLot parkingLot = new ParkingLot();
                String name = jObject.getString("name");
                String tab1_text = jObject.getString("tab1_text");
                int active = jObject.getInt("active");
                parkingLots.add(parkingLot);

            }
            ParkingLotsList.get(mContext).setParkingLotsList(parkingLots);
        } catch (JSONException e) {
            Toast.makeText(mContext, "Error getting Parking Lots", Toast.LENGTH_LONG).show();
            Log.e("JSONException", "Error: " + e.toString());
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}