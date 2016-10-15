package com.example.twitter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import twitter4j.*;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String BASE_URL = "https://api.twitter.com/1.1/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




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
