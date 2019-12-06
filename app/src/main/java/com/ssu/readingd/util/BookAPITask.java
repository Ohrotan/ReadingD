package com.ssu.readingd.util;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class BookAPITask extends AsyncTask<Integer, Void, JsonObject> {
    protected String mURL;

    public BookAPITask(String url) {
        mURL = url;
    }

    protected JsonObject doInBackground(Integer... params) {
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

            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(result);
            Log.v("REST_API", "Success! " + result);
            return je.getAsJsonObject();
        } catch (Exception e) {
            // Error calling the rest api
            Log.e("REST_API", "GET method failed: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

    }
}