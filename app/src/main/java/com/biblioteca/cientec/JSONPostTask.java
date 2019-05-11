package com.biblioteca.cientec;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JSONPostTask extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");///////////////////////////////////////////////////////////////////////////////
            connection.setRequestProperty("Content-type", "application/json");/////////////////////////////////////////////////
            connection.setRequestProperty("Accept", "application/json");///////////////////////////////////////////////////////
            connection.setDoOutput(true);//////////////////////////////////////////////////////////////////////////////////////
            String json = params[1];
            OutputStream os = connection.getOutputStream();
            os.write(json.getBytes("UTF-8"));

            connection.connect();

            //if (connection.getResponseCode() == 200)/////////////////////////////////////////////////////////////////////////////
                //Log.i("cientec", "Response code 200!");////////////////////////////////////////////////////////////////

            InputStream stream = connection.getInputStream();

            reader = new BufferedReader((new InputStreamReader(stream)));

            StringBuffer buffer = new StringBuffer();

            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            return  buffer.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {

        } finally {
            if (connection!= null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

    }
}
