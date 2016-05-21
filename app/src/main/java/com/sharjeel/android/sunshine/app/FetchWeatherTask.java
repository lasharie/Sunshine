package com.sharjeel.android.sunshine.app;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sharjeel on 5/10/16.
 */
public class FetchWeatherTask extends AsyncTask<String,Void, String[]> {
    private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

    @Override
    protected void onPostExecute(String[] strings) {

        if (strings != null){
            ForecastFragment.arrayAdapter_weather.clear();
            for (String mString : strings){
                ForecastFragment.arrayAdapter_weather.add(mString);
            }
        }
        else Log.e(LOG_TAG, "Error: Null string returned");
        super.onPostExecute(strings);
    }

    @Override
    protected String[] doInBackground(String... params) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String forecastJsonStr = null;
        String query = params[0];
        String mode = "json";
        String unit = "metric";
        int numDays = 7;
        String apikey = "57c3caef97ef1376a6df1855a99284ee";

        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            final String FORECAST_BASE_URI  = "http://api.openweathermap.org/data/2.5/forecast/daily?";
            final String QUERY_PARAM        = "q";
            final String MODE_PARAM         = "mode";
            final String UNIT_PARAM         = "unit";
            final String CNT_PARAM          ="cnt";
            final String API_PARAM          ="apikey";
            Uri builtUri = Uri.parse(FORECAST_BASE_URI).buildUpon()
                .appendQueryParameter(QUERY_PARAM,query)
                .appendQueryParameter(UNIT_PARAM,unit)
                .appendQueryParameter(CNT_PARAM,Integer.toString(numDays))
                .appendQueryParameter(API_PARAM,apikey)
                    .build();
            URL url = new URL(builtUri.toString());
            Log.v("Built URI:", builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            forecastJsonStr = buffer.toString();
//            Log.v(LOG_TAG,"Received:\n"+forecastJsonStr);

            try {
                String[] foreCasts = WeatherDataParser.getWeatherDataFromJson(forecastJsonStr);
//                for (String s : foreCasts) {
//                    Log.v(LOG_TAG, "Forecast entry: " + s);
//                }
                return foreCasts;
            }
            catch (JSONException e) {
                Log.e(LOG_TAG, "Error ", e);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        return null;
    }
}
