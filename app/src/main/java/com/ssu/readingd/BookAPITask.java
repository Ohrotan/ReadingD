package com.ssu.readingd;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;


public class BookAPITask extends AsyncTask<Integer, Void, JSONObject> {
    protected String mURL;

    public BookAPITask(String url) {
        mURL = url;
    }

    protected JSONObject doInBackground(Integer... params) {
        String result = null;

        try {
            URL url = new URL(mURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();

            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            result = builder.toString();
            XmlToJson xmlToJson = new XmlToJson.Builder(result).build();

            JSONObject jsonObject = xmlToJson.toJson();

            String jsonString = xmlToJson.toString();
            Log.v("REST_API","Success! "+jsonString);
            return jsonObject;
        }
        catch (Exception e) {
            // Error calling the rest api
            Log.e("REST_API", "GET method failed: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

    }
}