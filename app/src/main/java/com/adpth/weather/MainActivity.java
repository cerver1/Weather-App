package com.adpth.weather;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.adpth.weather.databinding.ActivityMainBinding;
import com.androdocs.httprequest.HttpRequest;
import com.google.android.material.snackbar.Snackbar;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    String API = "3dc8a8be15b581b5a6e240a35e2b647a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Calendar calendar = Calendar.getInstance();
        String currentData = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        binding.time.setText(currentData);


        binding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager ConnectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                assert ConnectionManager != null;
                NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    new WeatherData().execute();

                } else {
                    Snackbar snackbar = Snackbar.make(binding.constraintLayout, "check your Internet connection", Snackbar.LENGTH_LONG);
                    snackbar.show();

                }
            }
        });
    }

   private class WeatherData extends AsyncTask<String, Void, String> {
        String City = binding.yourCity.getText().toString();
        @Override
        protected String doInBackground(String... strings) {
            return HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?q=" + City + "&units=metric&appid=" + API);
        }

        @Override
        protected void onPostExecute(String s) {

                try {
                    JSONObject jsonObj = new JSONObject(s);
                    JSONObject main = jsonObj.getJSONObject("main");
                    JSONObject coor = jsonObj.getJSONObject("coord");
                    JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);
                    JSONObject sys = jsonObj.getJSONObject("sys");
                    JSONObject wind = jsonObj.getJSONObject("wind");

                    String latitude = coor.getString("lat");
                    String longitude = coor.getString("lon");
                    String temperature = main.getString("temp");
                    String humidity = main.getString("humidity");
                    String pres = main.getString("pressure");
                    String location_details = jsonObj.getString("name") + ", " + sys.getString("country");

                    long rise = sys.getLong("sunrise");
                    String sunrise = new SimpleDateFormat("HH:mm", Locale.US).format(new Date(rise * 1000));

                    long set = sys.getLong("sunset");
                    String sunset = new SimpleDateFormat("HH:mm", Locale.US).format(new Date(set * 1000));
                    String status = weather.getString("description");
                    String speed = wind.getString("speed");

                    binding.pressureAnim.setVisibility(View.VISIBLE);
                    binding.pressureAnim.playAnimation();
                    binding.pressure.setVisibility(View.VISIBLE);
                    binding.pressure.setText(pres);

                    binding.tempData.setVisibility(View.VISIBLE);
                    binding.temp.setText(temperature);

                    binding.humidityImg.setVisibility(View.VISIBLE);
                    binding.humidity.setVisibility(View.VISIBLE);
                    binding.humidity.setText(humidity);

                    binding.location.setVisibility(View.VISIBLE);
                    binding.location.setText(location_details);

                    binding.sunriseImg.setVisibility(View.VISIBLE);
                    binding.sunrise.setVisibility(View.VISIBLE);
                    binding.sunrise.setText(sunrise);

                    binding.sunsetImg.setVisibility(View.VISIBLE);
                    binding.sunset.setVisibility(View.VISIBLE);
                    binding.sunset.setText(sunset);

                    binding.conditionData.setVisibility(View.VISIBLE);
                    binding.desc.setText(status);

                    binding.windAnim.playAnimation();
                    binding.wind.setVisibility(View.VISIBLE);
                    binding.wind.setText(speed);

                    binding.globeAnim.setVisibility(View.VISIBLE);
                    binding.globeAnim.playAnimation();
                    binding.lat.setVisibility(View.VISIBLE);
                    binding.lat.setText(latitude);

                    binding.lon.setVisibility(View.VISIBLE);
                    binding.lon.setText(longitude);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
    }
}