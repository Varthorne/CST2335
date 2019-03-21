package com.example.androidlabs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherForecast extends AppCompatActivity {

    private static final String weatherURL = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric";
    private static final String UVURL = "http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389";
    ProgressBar progressBar;
    TextView currentTempField;
    TextView minTempField;
    TextView maxTempField;
    TextView uvRatingField;
    ImageView weatherIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        ForecastQuery forecast = new ForecastQuery();
        forecast.execute(weatherURL,UVURL);

        currentTempField = findViewById(R.id.currentTempDisplay);
        minTempField = findViewById(R.id.minTempDisplay);
        maxTempField = findViewById(R.id.maxTempDisplay);
        uvRatingField = findViewById(R.id.uvRatingDisplay);
        weatherIcon = findViewById(R.id.weatherImage);

    }


    private class ForecastQuery extends AsyncTask<String, Integer, String>{

        private String uvRating;
        private String currentTemp;
        private String minTemp;
        private String maxTemp;
        private String icon;
        private Bitmap weatherImage;

        @Override
        protected String doInBackground(String... strings) {

            try {
                /* Establish Connection and Instantiate Parser */
                String tempURL = strings[0];
                URL url = new URL(tempURL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream stream = connection.getInputStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);

                XmlPullParser parser = factory.newPullParser();
                parser.setInput(stream, "UTF-8");

                /* Retrieve data from the XML */
                while(parser.getEventType() != XmlPullParser.END_DOCUMENT){

                    /* Look at each start tag */
                    if(parser.getEventType() == XmlPullParser.START_TAG){

                        String tagName = parser.getName();

                        if(tagName.equals("temperature")){
                            currentTemp = parser.getAttributeValue(null, "value");

                            publishProgress(25);
                            Log.i("currentTemp", "Current temperature is " + currentTemp);

                            Thread.sleep(750);

                            minTemp = parser.getAttributeValue(null, "min");

                            publishProgress(50);
                            Log.i("minTemp", "Minimum temperature is " + minTemp);

                            Thread.sleep(750);

                            maxTemp = parser.getAttributeValue(null, "max");

                            publishProgress(75);
                            Log.i("maxTemp", "Maximum temperature is " + maxTemp);

                            Thread.sleep(750);

                        } else if(tagName.equals("weather")){

                            icon = parser.getAttributeValue(null, "icon") + ".png";
                            Log.i("Image is:", icon);
                        }
                    }

                    parser.next();
                }

                /* Check to see if the weather icon is already on the disk, download it if false */
                if(!fileExistence(icon)) {

                    Bitmap image = null;
                    URL imageUrl = new URL("http://openweathermap.org/img/w/" + icon);
                    connection = (HttpURLConnection) imageUrl.openConnection();
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        image = BitmapFactory.decodeStream(connection.getInputStream());
                    }

                    FileOutputStream outputStream = openFileOutput(icon, Context.MODE_PRIVATE);
                    image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                    outputStream.flush();
                    outputStream.close();

                    weatherImage = image;
                    Log.i("File doesn't exist", "Created new file: "+ icon);

                } else {

                    FileInputStream fis = null;

                    try {
                        File file = getBaseContext().getFileStreamPath(icon);
                        Log.i("File path is: ", file.getPath());

                        fis = new FileInputStream(file);
                        weatherImage = BitmapFactory.decodeStream(fis);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }


                    Log.i("File exists", "Retrieved file: "+ icon);
                }


                /* Establishing connection to second URL */
                tempURL = strings[1];
                url = new URL(tempURL);
                connection = (HttpURLConnection) url.openConnection();
                stream = connection.getInputStream();

                /* Read UV Rating from JSON */
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while((line = reader.readLine()) != null){

                    sb.append(line + "\n");
                }

                String result = sb.toString();

                JSONObject uvData = new JSONObject(result);

                uvRating = uvData.getString("value");
                Log.i("UV Rating is: ", uvData.getString("value"));

            } catch (IOException | XmlPullParserException e) {
                Log.e("Connection failed", e.getMessage());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return "";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setVisibility(ProgressBar.VISIBLE);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            currentTempField.setText(currentTemp);
            minTempField.setText(minTemp);
            maxTempField.setText(maxTemp);
            weatherIcon.setImageBitmap(weatherImage);
            uvRatingField.setText(uvRating);
            progressBar.setVisibility(View.INVISIBLE);
        }

        private boolean fileExistence(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }
    }

}
