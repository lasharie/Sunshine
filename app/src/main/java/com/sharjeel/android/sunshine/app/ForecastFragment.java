package com.sharjeel.android.sunshine.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {


    public static ArrayAdapter <String> arrayAdapter_weather;


    public ForecastFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.forecastfragment,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            FetchWeatherTask fetchWeatherTask = new FetchWeatherTask();
            fetchWeatherTask.execute("10010");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ArrayList<String> forecasts = new ArrayList<String>(
                Arrays.asList(
                        "Today - Sunny - 88/63",
                        "Tomorrow - Foggy - 70/46",
                        "Wed - Cloudy - 72/63",
                        "Thu - Rainy - 70/50",
                        "Fri - Sunny - 70/63",
                        "Sat - Thunders - 50/30",
                        "Sun - Showers 50/34")
        );
        arrayAdapter_weather =
            new ArrayAdapter<String>(
                    getActivity(),
                    R.layout.list_item_forecast,
                    R.id.list_item_forecast_textview,
                    forecasts
            );
        final ListView mListView = (ListView) rootView.findViewById(R.id.listview_forecast);
        mListView.setAdapter(arrayAdapter_weather);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String weatherToast = arrayAdapter_weather.getItem(position);
                Toast.makeText(getActivity(),weatherToast,Toast.LENGTH_LONG).show();
            }
        });

        return rootView;
    }


}
