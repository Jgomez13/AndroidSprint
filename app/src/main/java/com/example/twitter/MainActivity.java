package com.example.twitter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.HashMap;
import java.util.List;

import static android.R.id.*;


public class MainActivity extends Activity {

    public static final String BASE_URL = "https://api.twitter.com/1.1/";
    private static Twitter twitter;
    private static int userId;
    private static AccessToken accessToken = null;
    private static RequestToken requestToken = null;
    private static boolean setConsumer = false;
    private static String consumerKey;
    private static String consumerSecret;
    private static String pin;
    private static String oauth_url;
    private static String oauth_verifier;
    private Dialog auth_dialog;
    private ProgressDialog progress;
    public static WebView web;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Consumer Key (API Key)KpQrzxa3mQAKCuoHAoOt9rxHo
        //Consumer Secret (API Secret)doZz7HbuJUR7PCNEdZ0ZAqnQaMX8n7cLfIKQdf2DQvWiHSbWXE
        Login();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    void Login() {
        if (Build.VERSION.SDK_INT > 4.0) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {

            twitter = TwitterFactory.getSingleton();

            consumerKey = "KpQrzxa3mQAKCuoHAoOt9rxHo";
            consumerSecret = "doZz7HbuJUR7PCNEdZ0ZAqnQaMX8n7cLfIKQdf2DQvWiHSbWXE";
            twitter.setOAuthConsumer(consumerKey, consumerSecret);
            requestToken = twitter.getOAuthRequestToken("T4J_OAuth://callback_main");
            oauth_url = requestToken.getAuthenticationURL();
            Intent intent = new Intent(this,DisplayMessageActivity.class);

            startActivity(intent);
            this.startActivity(new Intent(intent.ACTION_VIEW, Uri.parse(oauth_url)));
            new TokenGet().execute();
        } catch (TwitterException ex) {
            System.out.println("in Login" + ex.getMessage());
        }


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Uri uri = intent.getData();
        try {
            String verifier = uri.getQueryParameter("oauth_verifier");
            AccessToken accessToken = twitter.getOAuthAccessToken(requestToken,
                    verifier);
            String token = accessToken.getToken(), secret = accessToken
                    .getTokenSecret();
            displayTimeLine(token, secret); //after everything, display the first tweet

        } catch (TwitterException ex) {
            Log.e("Main.onNewIntent", "" + ex.getMessage());
        }
    }

    @SuppressWarnings("deprecation")
    void displayTimeLine(String token, String secret) {
        if (null != token && null != secret) {
            List<Status> statuses = null;
            try {
                //twitter.setOAuthAccessToken(token, secret);
                statuses = twitter.getUserTimeline();
                Toast.makeText(this, statuses.get(0).getText(), Toast.LENGTH_LONG)
                        .show();
            } catch (Exception ex) {
                Toast.makeText(this, "Error:" + ex.getMessage(),
                        Toast.LENGTH_LONG).show();
                Log.d("Main.displayTimeline", "" + ex.getMessage());
            }

        } else {
            Toast.makeText(this, "Not Verified", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private class TokenGet extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {

            try {
                requestToken = twitter.getOAuthRequestToken();
                oauth_url = requestToken.getAuthorizationURL();
            } catch (TwitterException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return oauth_url;
        }

        @Override
        protected void onPostExecute(String oauth_url) {
            if (oauth_url != null) {
                Log.e("URL", oauth_url);
                auth_dialog = new Dialog(MainActivity.this);
                auth_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                auth_dialog.setContentView(R.layout.activity_display_message);
                web = new WebView(auth_dialog.getContext());
                web.getSettings().setJavaScriptEnabled(true);
                web.loadUrl(oauth_url);
                web.setWebViewClient(new WebViewClient() {
                    boolean authComplete = false;

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        if (url.contains("oauth_verifier") && authComplete == false) {
                            authComplete = true;
                            Log.e("Url", url);
                            Uri uri = Uri.parse(url);
                            oauth_verifier = uri.getQueryParameter("oauth_verifier");

                            auth_dialog.dismiss();
                            new AccessTokenGet().execute();
                        } else if (url.contains("denied")) {
                            auth_dialog.dismiss();
                            Toast.makeText(MainActivity.this, "Sorry !, Permission Denied", Toast.LENGTH_SHORT).show();


                        }
                    }
                });
                auth_dialog.show();
                auth_dialog.setCancelable(true);


            } else {

                Toast.makeText(MainActivity.this, "Sorry !, Network Error or Invalid Credentials", Toast.LENGTH_SHORT).show();


            }
        }
    }

    private class AccessTokenGet extends AsyncTask<String, String, Boolean> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress = new ProgressDialog(MainActivity.this);
            progress.setMessage("Fetching Data ...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.show();

        }


        @Override
        protected Boolean doInBackground(String... args) {

            try {


                accessToken = twitter.getOAuthAccessToken(requestToken, oauth_verifier);

                SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                edit.putString("ACCESS_TOKEN", accessToken.getToken());
                edit.putString("ACCESS_TOKEN_SECRET", accessToken.getTokenSecret());

                edit.commit();


            } catch (TwitterException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();


            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean response) {
            if (response) {
                Intent i = new Intent(MainActivity.this, MainActivity.class);
                i.putExtra("ACCESS_TOKEN", accessToken.getToken());
                i.putExtra("ACCESS_TOKEN_SECRET", accessToken.getTokenSecret());
                startActivity(i);
            }
        }


    }
}
/*
        onHandTwitter(null);
        //System.exit(0);
    }

    private static void storeAccessToken(int useId, AccessToken inAccessToken) {
        //store accessToken.getToken
        userId = useId;
        //store accessToken.getTokenSecret()
        accessToken = inAccessToken;
    }

    private void setRequestToken() {
        try {
            requestToken = twitter.getOAuthRequestToken();
        } catch (TwitterException te) {
            System.out.println("CAUGHT EXCEPTION");
            te.getStatusCode();
            System.out.println("END EXCEPTION");
            System.out.println(te.getMessage());
            requestToken = null;
        }
    }

    public boolean onHandTwitter(View view) {
        if (!setConsumer) {
            twitter.setOAuthConsumer(consumerKey, consumerSecret);
            setConsumer = true;
        }

        if (requestToken == null) setRequestToken();

        accessToken = null;
        TextView text = (TextView) findViewById(R.id.headerTextView);
        TextView pass = (TextView) findViewById(R.id.passwordEditText);
        text.setText("Go");
        oauth_url = requestToken.getAuthorizationURL() ;
        text.setText("Please Open this URL to Allow Access\n" + oauth_url + "\nEnter pin if available or just hit enter.");
        System.out.println("In while Loop");
        pin = pass.getText() + "";

       /* String authUrl=requestToken.getAuthorizationURL();
        System.out.println("Open the following URL and grant access to your account:");
        System.out.println(authUrl);

        System.out.print("Enter the PIN(if available) or just hit enter.[PIN]:");
        */

//System.out.println("Calling getToken");
//        return true;
//        }
//
//public boolean onGoToAuth(View view) {
//        Uri uri = Uri.parse(oauth_url); // missing 'http://' will cause crashed
//        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//        this.startActivity(intent);
//        System.out.println("Intent is Printing getAction");
//        System.out.println(intent.getAction());
//        new TokenGet().execute();
//        finish();
//
//        return true;
//        }
//
//public boolean onSetAccessToken(View view) {
//        try {
//        if (pin.length() > 0) {
//        accessToken = twitter.getOAuthAccessToken(requestToken, pin);
//        } else {
//        accessToken = twitter.getOAuthAccessToken();
//        }
//        } catch (TwitterException te) {
//        if (401 == te.getStatusCode()) {
//        System.out.println("Unable to get the access token.");
//        } else {
//        te.printStackTrace();
//        }
//        }
//
//        //persist to the accessToken for future reference.
//        try {
//        storeAccessToken((int) twitter.verifyCredentials().getId(), accessToken);
//        } catch (Exception e) {
//        e.getMessage();
//        }
//        Status status;
//        try {
//        status = twitter.updateStatus("Status");
//        } catch (TwitterException te) {
//        te.getMessage();
//        status = null;
//        }
//        System.out.println("Successfully updated the status to [" + status.getText() + "].");
//        return true;
//        }
//
//public HashMap<String, String> download_tweets(String screen_name, int number_of_tweets, String max_id) {
//        String api_url = "%sstatuses/user_timeline.json?" + BASE_URL;
//        api_url += "screen_name=" + screen_name;
//        api_url =api_url + "count=" + number_of_tweets;
//        Twitter twitter = TwitterFactory.getSingleton();
//        HashMap<String, String> tweets = null;
//
//        try {
//        List<Status> statuses = twitter.getHomeTimeline();
//        System.out.println("Showing home timeline.");
//        String curStatus = "";
//        String curUser = "";
//        tweets = new HashMap<>();
//        for (Status status : statuses) {
//        curUser = status.getUser().getName();
//        curStatus = status.getText();
//        tweets.put(curUser, curStatus);
//        System.out.println(curUser + " : " + curStatus);
//        }
//        return tweets;
//        } catch (Exception e) {
//        System.out.println(e.getMessage());
//        }
//        return tweets;
//        }

