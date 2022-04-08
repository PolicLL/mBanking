package com.example.mbankingapp.service;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class DataDownloader
{

    public String getAccountAndTransactionsData()
    {

        return getJsonData("https://mportal.asseco-see.hr/builds/ISBD_public/Zadatak_1.json");
    }

    public String getJsonData(String url)
    {
        DownloadData downloadData = new DownloadData();

        String data = "";

        try
        {
            data = downloadData.execute
                    (url).get();
        }
        catch (ExecutionException | InterruptedException e)
        {
            e.printStackTrace();
        }

        Log.i("Test" , data);

        return data;
    }

}


class DownloadData extends AsyncTask<String , Void , String>
{

    @Override
    protected String doInBackground(String... strings)
    {
        String result = null;

        try
        {
            result = HTTPGetCall(strings[0]);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return result;
    }

    protected String HTTPGetCall(String WebMethodURL) throws IOException {
        StringBuilder response = new StringBuilder();

        //Prepare the URL and the connection
        URL u = new URL(WebMethodURL);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();

        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK)
        {
            //Get the Stream reader ready
            BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()),8192);

            //Loop through the return data and copy it over to the response object to be processed
            String line;

            while ((line = input.readLine()) != null)
                response.append(line);


            input.close();
        }

        return response.toString();
    }
}
