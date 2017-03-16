package com.example.particulartech;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    /**
     * declaring global variables
     */
    TextView result;    //text view letting user know if messages have been sent or not
    String city ="";    //city determining


    /**
     * onCreate - initializes important variables when the app starts
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = (TextView) findViewById(R.id.result);
        result.setText("");
    }


    /**
     * weatherLetter - triggered when the "Send Notifications" button is clicked,
     * this method in turn triggers the API call
     *
     * @param view in this case the Button view that calls the onClick method
     */
    public void weatherLetter(View view){
        apiCall apiCall_1 = new apiCall();
        apiCall apiCall_2 = new apiCall();
        result.setText("Sending emails to Kingston counterparts... \n");
        apiCall_1.execute("http://api.openweathermap.org/data/2.5/forecast?id=3489854&appid=6e35a5fe31bb218fcbd93431efd85475");
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        result.setText(result.getText() + "Sending emails to Montego Bay counterparts... \n");
        apiCall_2.execute("http://api.openweathermap.org/data/2.5/forecast?id=3489460&appid=6e35a5fe31bb218fcbd93431efd85475");

    }


    /**
     * internal class used to actually make the API call in the background
     */
    private class apiCall extends AsyncTask<String, Void, Boolean>{

        Calendar calendar = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");



        /**
         * doInBackground - takes a url and makes a call to the openweather API
         * @param urls the request sent to the openweather API
         * @return a string of raw data representing the information returned from the API
         */
        @Override
        protected Boolean doInBackground(String... urls) {
            String notice = "";
            String result = "";
            Mail mail;
            int rawData;

            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                rawData = inputStreamReader.read();

                while(rawData!=-1){
                    char letter = (char) rawData;
                    result += letter;
                    rawData = inputStreamReader.read();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonResult = new JSONObject(result);
                JSONArray list = jsonResult.getJSONArray("list");
                Log.i("length",String.valueOf(list.length()));
                JSONObject jCity = jsonResult.getJSONObject("city");
                Log.i("city",jCity.getString("name"));
                city = jCity.getString("name");

                for(int i=0;i < list.length(); i++){
                    JSONObject bulk = list.getJSONObject(i);
                    JSONObject weather = bulk.getJSONArray("weather").getJSONObject(0);
                    String dt_txt = bulk.getString("dt_txt");
                    Date date = df.parse(dt_txt);
                    calendar.setTime(date);

                    if(dt_txt.contains("06:00:00")){
                        Log.i("main",weather.getString("main"));
                        Log.i("dt_txt", dt_txt);
                        notice += buildNotice(weather,calendar);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            mail = new Mail(notice, buildRecipientList(city));
            try {
                if(mail.send()){
                    Log.i("Success", "Success");
                    return true;
                } else {
                    Log.i("Fail", "Fail");
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean bool) {
            super.onPostExecute(bool);
            if(bool){
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                result.setText(result.getText() + "Mesages sent to " + city +"\n");
            } else {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                result.setText(result.getText() + "Problems sending messages to " + city +"\n");
            }
        }

        /**
         * buildNotice - this method sends messages to the workforce based on certain conditions
         * @param weather - a JSON object detailing the weather
         * @param cal - a calendar object for the day of the respective forecast
         */
        private String buildNotice(JSONObject weather, Calendar cal){
            DateFormat day = new SimpleDateFormat("EEEE");
            int id=0;
            String main="";

            if(cal.get(Calendar.DAY_OF_WEEK)!=Calendar.SATURDAY && cal.get(Calendar.DAY_OF_WEEK)!=Calendar.SUNDAY){
                try {
                    id = weather.getInt("id");
                    main = weather.getString("main");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("id",String.valueOf(id));
                if(main.equals("Rain") && id==500){
                    return "On " + day.format(cal.getTime()) + " there's a four hour workday for regular workers and IT guys shouldn't hit the road \n";
                } else if(main.equals("Clear") || main.equals("Clouds")){
                    return "On " + day.format(cal.getTime()) + " there's an eight hour workday for regular workers and IT guys should hit the road \n";
                }
            }
            return "";
        }

        /**
         * buildRecipientList - constructs a list of email addresses based on the city the weather forecast is for
         * @param city - a string describing the city the current forecast is
         * @return an array of emails
         */
        private String[] buildRecipientList(String city){
            Database database = new Database();
            ArrayList<String> recipients = new ArrayList<>();

            for(Person worker : database.getWorkforce()){
                if(worker.getCity().equals(city)){
                    Log.i("add", worker.getEmail());
                    recipients.add(worker.getEmail());
                }
            }
            return recipients.toArray(new String[0]);
        }

    }
}
