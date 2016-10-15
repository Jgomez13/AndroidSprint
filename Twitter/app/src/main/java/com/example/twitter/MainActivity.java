package com.example.twitter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.*;

public class MainActivity extends AppCompatActivity {

    public static final String BASE_URL = "https://api.twitter.com/1.1/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        }catch (){

        }
    }


    public Array download_tweets(String screen_name,int number_of_tweets,String  max_id) {
        String api_url = "%sstatuses/user_timeline.json?" + BASE_URL;
        api_url += "screen_name="+ screen_name;
        api_url += "count=" +number_of_tweets;
        if (max_id != null) {
            api_url += "&max_id="+ max_id;
        }
        //send request to Twitter
        response = requests.get(api_url, auth = oauth)

        if response.status_code == 200:

        tweets = json.loads(response.content)

        return tweets


        return None
        try {
            int responseCode;
            InputStream responseIn = null;

            // URL to be forged.
            URL url = new URL(BASE_URL);

            // URLConnection instance is created to further parameterize a
            // resource request past what the state members of URL instance
            // can represent.
            URLConnection urlConn = url.openConnection();
            if (urlConn instanceof HttpURLConnection) {
                urlConn.setConnectTimeout(60000);
                urlConn.setReadTimeout(90000);

            }
            //POST /1/statuses/update.json?include_entities=true HTTP/1.1
            //Accept: */*
            /*Connection: close
            User-Agent: OAuth gem v0.4.4
            Content-Type: application/x-www-form-urlencoded
            Content-Length: 76
            Host: api.twitter.com
                    */
            urlConn.addRequestProperty("User-Agent", "OAuth gem v0.4.4");
            urlConn.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConn.addRequestProperty("Content-Length","76");
            urlConn.addRequestProperty("Host","api.twitter.com");
            urlConn.addRequestProperty("Authorization",""+
                    "OAuth oauth_consumer_key=\"xvz1evFS4wEEPTGEFPHBog\", \n" +
                    "oauth_nonce=\"kYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg\", \n" +
                    "oauth_signature=\"tnnArxj06cWHq44gCs1OSKk%2FjLY%3D\", \n" +
                    "oauth_signature_method=\"HMAC-SHA1\", \n" +
                    "oauth_timestamp=\"1318622958\", \n" +
                    "oauth_token=\"370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb\", \n" +
                    "oauth_version=\"1.0\"");
            urlConn.setDoOutput(true);
            String data = "";
    }
}
