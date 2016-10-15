package com.example.twitter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private static AccessToken token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Consumer Key (API Key)KpQrzxa3mQAKCuoHAoOt9rxHo
        //Consumer Secret (API Secret)doZz7HbuJUR7PCNEdZ0ZAqnQaMX8n7cLfIKQdf2DQvWiHSbWXE
        twitter = TwitterFactory.getSingleton();
        twitter.setOAuthConsumer("[KpQrzxa3mQAKCuoHAoOt9rxHo]", "[doZz7HbuJUR7PCNEdZ0ZAqnQaMX8n7cLfIKQdf2DQvWiHSbWXE]");
        RequestToken requestToken;
        try {
          requestToken = twitter.getOAuthRequestToken();
        } catch (TwitterException te){
            te.getStatusCode();
            requestToken = null;
        }

        AccessToken accessToken = null;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (null == accessToken) {
            System.out.println("Open the following URL and grant access to your account:");
            System.out.println(requestToken.getAuthorizationURL());
            System.out.print("Enter the PIN(if aviailable) or just hit enter.[PIN]:");
            try{
                String  pin;
                try {
                     pin = br.readLine();

                }catch (Exception e){
                    e.printStackTrace();
                    pin = "";
                }
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
        System.exit(0);
    }
    private static void storeAccessToken(int useId, AccessToken accessToken){
        //store accessToken.getToken
        userId =useId;
        //store accessToken.getTokenSecret()
        token=accessToken;
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
