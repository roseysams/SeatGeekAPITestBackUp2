package com.example.rsams4190.seatgeekapitest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private CurrentEvents mCurrentEvents;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        String clientID = "OTY3NjUxOHwxNTEwOTU0MjA2LjE0";
        //double latitude = 37.8267;
        //double longitude = -122.432;
        //String seatGeekUrl = "https://api.seatgeek.com/2/events?client_id=" + clientID + "/" + latitude + "," + longitude;
        final String seatGeekUrl = "https://api.seatgeek.com/2/events?client_id=" + clientID;

        if (isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(seatGeekUrl)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            if (response.isSuccessful()) {
                                mCurrentEvents = getCurrentDetails(jsonData);
                            }
                        } else {
                            alertUserAboutError();
                        }
                    }
                    catch (JSONException e){
                        Log.e(TAG, "Exception caught: ", e);
                    }
                }
            });
        }
        else{
            Toast.makeText(this, getString(R.string.network_unavailable_message),
                    Toast.LENGTH_LONG).show();
        }

        Log.d(TAG, "Main UI is running!");
    }

    private CurrentEvents getCurrentDetails(String jsonData) throws JSONException {
        JSONObject funevents = new JSONObject(jsonData);
        String meta = funevents.getString("meta");
        Log.i(TAG, "From JSON: " + meta );


        /*
        //this is not going to work. venue is not a double, there are doubles in venue, such as the lat and lon or the id inside the venue array but it itself is not a double
        JSONObject events = funevents.getJSONObject("events");
        CurrentEvents currentEvents = new CurrentEvents();
        currentEvents.setmVenue(events.getDouble("venue"));
        currentEvents.setmTime(events.getString("time")); //says getLong in Treehouse instead of getString //no "m" in the setmTime in Treehouse videos for this line, the line above, and the lines below.
        currentEvents.setmIcon(events.getString("icon"));
        currentEvents.setmPrecipChance(events.getDouble("precipProbability"));
        currentEvents.setmSummary(events.getString("summary"));
        currentEvents.setmTemperature(events.getDouble("temperature"));*/


        return new CurrentEvents();
    }

    private boolean isNetworkAvailable(){
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if(networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }
        return isAvailable;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }
}
