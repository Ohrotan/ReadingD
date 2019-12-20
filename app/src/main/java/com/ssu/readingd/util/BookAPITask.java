package com.ssu.readingd.util;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ssu.readingd.BookManualRegisterActivity;
import com.ssu.readingd.dto.BookSimpleDTO;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class BookAPITask extends AsyncTask<Integer, Void, JsonObject> {
    protected String mURL;
    Activity activity;

    public BookAPITask(String url, Activity activity) {
        mURL = url;
        this.activity = activity;
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

    @Override
    protected void onPostExecute(JsonObject jo) {
        super.onPostExecute(jo);
        JsonArray ja = jo.get("docs").getAsJsonArray();
        if (ja.size() > 0) {
            BookSimpleDTO book = BookSimpleDTO.parse(ja.get(0).getAsJsonObject());

            Intent intent = new Intent(activity, BookManualRegisterActivity.class);
            intent.putExtra("book", book);

            activity.startActivity(intent);
            activity.overridePendingTransition(0, 0);
            activity.finish();
        } else {
            Toast.makeText(activity, "검색결과 없음", Toast.LENGTH_SHORT).show();
            // activity.showToast("검색결과가 없습니다. 책 제목을 직접 입력해주세요.");
        }
    }
}