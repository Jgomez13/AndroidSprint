package com.example.zakti.twitter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import twitter4j.Twitter;
import twitter4j.Status;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class MainActivity extends AppCompatActivity {
    private String username;
    private String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void login(View view) {
        EditText userEntered = (EditText) findViewById(R.id.user);
        EditText passEntered = (EditText) findViewById(R.id.password);

        username = userEntered.getText().toString();
        password = passEntered.getText().toString();

        getTweets(view);
    }

    private void getTweets(View view) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("KpQrzxa3mQAKCuoHAoOt9rxHo")
                .setOAuthConsumerSecret("doZz7HbuJUR7PCNEdZ0ZAqnQaMX8n7cLfIKQdf2DQvWiHSbWXE")
                .setOAuthAccessToken(username)
                .setOAuthAccessTokenSecret(password);
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

        List<Status> statuses = twitter.getRetweets()
    }
}
