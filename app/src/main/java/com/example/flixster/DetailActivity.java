package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;

import okhttp3.Headers;

public class DetailActivity extends YouTubeBaseActivity {

    private static final String YOUTUBE_API_KEY = "AIzaSyCYtYuaJj7apVZ29GyeBFrNWF1rXfDzeko";
    public static final String VIDEOS_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";


    TextView tvTitle;
    TextView tvOverview;
    RatingBar ratingBar;
    YouTubePlayerView youTubePlayerView ;
    //ArrayList<Double> ratingArray = new ArrayList<Double>();

    //Movie movie = Parcels.unwrap(getIntent().getParcelableExtra("movie"));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvTitle = findViewById(R.id.tvTitle);
        tvOverview = findViewById(R.id.tvOverview);
        ratingBar = findViewById(R.id.ratingBar);
        youTubePlayerView = findViewById(R.id.player);


        // no longer need as this would have to be one by one   String title = getIntent().getStringExtra("title");
        // this was replaced with our movie object as it contains the title, overview , stars , etc..
        Movie movie = Parcels.unwrap(getIntent().getParcelableExtra("movie"));
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        ratingBar.setRating((float)movie.getRating());



        // need to down cast it bc the method setRating utilizes floats
        // this also  was  no longer need. Up above so below tvTitle.setText(title);


        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(VIDEOS_URL, movie.getMovieId()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {

                try {
                    JSONArray results = json.jsonObject.getJSONArray("results");
                    if (results.length() == 0)
                    {
                        return;
                    }
                    String youtubeKey =  results.getJSONObject(0).getString("key");
                    //double ratingValue = results.getJSONObject(0).getDouble("vote_average");
                    Log.d("DetailActivity",youtubeKey);
                    intializeYoutube(youtubeKey);//,ratingValue);
                    //JSONArray ratingArray = json.jsonObject.getJSONArray("vote_average");

                    //ratingArray.add(movie.getRating());





                } catch (JSONException e) {
                    Log.e("DetailActivity", "Failed to parse JSON", e);
                    //e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

            }
        });



    }

    private void intializeYoutube(final String youtubeKey)//, final double ratingValue)
    {
        youTubePlayerView.initialize(YOUTUBE_API_KEY , new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b)
            {
                Log.d("DetailActivity","onInitializationSuccess");





                /* if (ratingValue > 5.0)
                {
                    youTubePlayer.loadVideo(youtubeKey);
                }
                else
                {
                    youTubePlayer.cueVideo(youtubeKey);
                }

                 */

                youTubePlayer.cueVideo(youtubeKey);


            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult)
            {
                Log.d("DetailActivity","onInitializationFailure");

            }
        });


    }
}