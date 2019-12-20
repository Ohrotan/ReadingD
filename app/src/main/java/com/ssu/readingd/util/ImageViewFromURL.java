package com.ssu.readingd.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageViewFromURL {

    public static Bitmap getBitmap(String url) {
        URL imgUrl = null;
        HttpURLConnection connection = null;
        InputStream is = null;
        Bitmap retBitmap = null;
        try {
            imgUrl = new URL(url);
            connection = (HttpURLConnection) imgUrl.openConnection();
            connection.setDoInput(true);
            connection.connect();
            is = connection.getInputStream();
            retBitmap = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            return retBitmap;
        }
    }

    public static void setImageView(final Activity activity, final ImageView imgSample1, final String imgUrl) {
        final Bitmap bitmapSample1 = getBitmap(imgUrl);
        new Thread(new Runnable() {
            public void run() {

                if (bitmapSample1 != null) {
                    activity.runOnUiThread(new Runnable() {
                        @SuppressLint("NewApi")
                        public void run() {
                            imgSample1.setImageBitmap(bitmapSample1);
                        }
                    });
                }

            }
        }).start();

    }

}
