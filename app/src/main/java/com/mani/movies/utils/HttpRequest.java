package com.mani.movies.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

class HttpRequest {
    private static String TAG = HttpRequest.class.getSimpleName();
    private URL requestUrl;
    private static String API_KEY = "Please update API Key";
    private HttpURLConnection connection = null;

    HttpRequest(String url) {
        try {
            requestUrl = new URL(url + "?api_key=" + API_KEY);
        } catch (MalformedURLException e) {
            Log.d(TAG, "Invalid URL");
        }
    }

    String makeHttpRequest() throws IOException {

        InputStream inputStream = null;
        String data = "";
        try {
            connection = (HttpURLConnection) requestUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int responceCode = connection.getResponseCode();
            if (responceCode == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                data = getStringFromInputStream(inputStream);
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return data;
    }

    private String getStringFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder data = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                data.append(line);
                line = reader.readLine();
            }
        }
        return data.toString();
    }
}
