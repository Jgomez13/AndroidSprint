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




    }


    public Array download_tweets(String screen_name,int number_of_tweets,String  max_id) {
        String api_url = "%sstatuses/user_timeline.json?" + BASE_URL;
        api_url += "screen_name="+ screen_name;
        api_url += "count=" +number_of_tweets;
        Twitter twitter = TwitterFactory.getSingleton();
        List<Status> statuses = twitter.getHomeTimeline();
        System.out.println("Showing home timeline.");
        for (Status status : statuses) {
            System.out.println(status.getUser().getName() + ":" +
                    status.getText());
        }

    }
}
