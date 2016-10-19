package com.example.twitter;

import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String BASE_URL = "https://api.twitter.com/1.1/";
    private static Twitter twitter;
    private static int userId;
    private static AccessToken accessToken;
    private static RequestToken requestToken;
    private static String consumerKey;
    private static String consumerSecret;
    private static String pin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Consumer Key (API Key)KpQrzxa3mQAKCuoHAoOt9rxHo
        //Consumer Secret (API Secret)doZz7HbuJUR7PCNEdZ0ZAqnQaMX8n7cLfIKQdf2DQvWiHSbWXE
        if (android.os.Build.VERSION.SDK_INT > 4.0) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        twitter = TwitterFactory.getSingleton();
        consumerKey = "KpQrzxa3mQAKCuoHAoOt9rxHo";
        consumerSecret = "doZz7HbuJUR7PCNEdZ0ZAqnQaMX8n7cLfIKQdf2DQvWiHSbWXE";
        onHandTwitter(null);
        //System.exit(0);
    }
    private static void storeAccessToken(int useId, AccessToken inAccessToken){
        //store accessToken.getToken
        userId =useId;
        //store accessToken.getTokenSecret()
        accessToken=inAccessToken;
    }
    private static void storeRequestToken(RequestToken inRequestToken){
        requestToken = inRequestToken;
    }
    public boolean onHandTwitter(View view){
        if(consumerKey.isEmpty()&&consumerSecret.isEmpty()){
            twitter.setOAuthConsumer(consumerKey, consumerSecret);
        }
        RequestToken requestToken;
        try {
            requestToken = twitter.getOAuthRequestToken();
        } catch (TwitterException te){
            System.out.println("CAUGHT EXCEPTION");
            te.getStatusCode();
            System.out.println("END EXCEPTION");
            System.out.println(te.getMessage());
            requestToken = null;
        }

        accessToken = null;
        TextView text = (TextView) findViewById(R.id.headerTextView);
        TextView pass = (TextView) findViewById(R.id.passwordEditText);
        storeRequestToken(requestToken);
        text.setText("Go");
        text.setText("Please Open this URL to Allow Access\n"+requestToken.getAuthorizationURL()+"\nEnter pin if available or just hit enter.");
        System.out.println("In while Loop");
        pin = pass.getText() + "";

       /* String authUrl=requestToken.getAuthorizationURL();
        System.out.println("Open the following URL and grant access to your account:");
        System.out.println(authUrl);

        System.out.print("Enter the PIN(if available) or just hit enter.[PIN]:");
        */

        System.out.println("Calling getToken");
        return true;
    }
    public boolean onGoToAuth(View view){
        Uri uri = Uri.parse(requestToken.getAuthorizationURL()); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
        System.out.println(intent.getAction());
        return true;
    }
    public boolean onSetAccessToken(View view) {
        try{
            if(pin.length() > 0){
                accessToken = twitter.getOAuthAccessToken(requestToken, pin);
            }else{
                accessToken = twitter.getOAuthAccessToken();
            }
        } catch (TwitterException te) {
            if(401 ==  te.getStatusCode()){
                System.out.println("Unable to get the access token.");
            }else{
                te.printStackTrace();
            }
        }

        //persist to the accessToken for future reference.
        try {
            storeAccessToken((int) twitter.verifyCredentials().getId() , accessToken);
        }catch (Exception e){
            e.getMessage();
        }
        Status status;
        try {
            status= twitter.updateStatus("Status");
        }catch (TwitterException te){
            te.getMessage();
            status=null;
        }
        System.out.println("Successfully updated the status to [" + status.getText() + "].");
        return true;
    }

    public HashMap<String, String> download_tweets(String screen_name, int number_of_tweets, String  max_id) {
        String api_url = "%sstatuses/user_timeline.json?" + BASE_URL;
        api_url += "screen_name="+ screen_name;
        api_url += "count=" +number_of_tweets;
        Twitter twitter = TwitterFactory.getSingleton();
        HashMap<String,String> tweets = null;

        try {
            List<Status> statuses = twitter.getHomeTimeline();
            System.out.println("Showing home timeline.");
            String curStatus = "";
            String curUser = "";
            tweets = new HashMap<>();
            for (Status status : statuses) {
                curUser=status.getUser().getName();
                curStatus = status.getText();
                tweets.put(curUser,curStatus);
                System.out.println(curUser+" : "+curStatus);
            }
            return tweets;
        }catch (Exception e ){
            System.out.println(e.getMessage());
        }
        return tweets;
    }

}
