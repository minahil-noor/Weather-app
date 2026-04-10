package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    EditText edtCity;
    Button btnWeather;
    TextView txtWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtCity = findViewById(R.id.edtCity);
        btnWeather = findViewById(R.id.btnWeather);
        txtWeather = findViewById(R.id.txtWeather);

        btnWeather.setOnClickListener(v -> {
            new Thread(() -> {
                try {
                    String city = edtCity.getText().toString().trim();

                    if(city.isEmpty()){
                        runOnUiThread(() -> txtWeather.setText("Please enter a city!"));
                        return;
                    }

                    String apiKey = "0b8d56b6be8737367c573d2b61623f35"; // ?? Replace with OpenWeather API key
                    String urlString = "https://api.openweathermap.org/data/2.5/weather?q="
                            + city + "&appid=" + apiKey + "&units=metric";

                    URL url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    InputStream inputStream = connection.getInputStream();
                    Scanner scanner = new Scanner(inputStream);
                    StringBuilder result = new StringBuilder();
                    while (scanner.hasNext()) {
                        result.append(scanner.nextLine());
                    }

                    JSONObject jsonObject = new JSONObject(result.toString());

                    JSONObject main = jsonObject.getJSONObject("main");
                    double temp = main.getDouble("temp");
                    int humidity = main.getInt("humidity");

                    JSONObject wind = jsonObject.getJSONObject("wind");
                    double windSpeed = wind.getDouble("speed");

                    JSONArray weatherArray = jsonObject.getJSONArray("weather");
                    JSONObject weatherObj = weatherArray.getJSONObject(0);
                    String description = weatherObj.getString("description");

                    String displayText = "Temperature: " + temp + "°C\n"
                            + "Humidity: " + humidity + "%\n"
                            + "Wind Speed: " + windSpeed + " m/s\n"
                            + "Condition: " + description;

                    runOnUiThread(() -> txtWeather.setText(displayText));

                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() ->
                            txtWeather.setText("Error: " + e.getMessage())
                    );
                }
            }).start();
        });
    }
}
