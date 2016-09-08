package com.sfuronlabs.ripon.cricketmania.videoplayers;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.sfuronlabs.ripon.cricketmania.R;
import com.sfuronlabs.ripon.cricketmania.util.Constants;
import com.sfuronlabs.ripon.cricketmania.util.DMWebVideoView;

/**
 * @author ripon
 */
public class DMPlayerActivity extends AppCompatActivity {

    AdView adView;
    private DMWebVideoView mVideoView;
    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dmplayer);

        Intent intent = getIntent();
        url = intent.getStringExtra("url");

        mVideoView = ((DMWebVideoView) findViewById(R.id.dmView));
        mVideoView.setVideoId(url);
        mVideoView.setAutoPlay(true);

        adView = (AdView) findViewById(R.id.adViewDMPlayer);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE).build();
        adView.loadAd(adRequest);
    }

    @Override
    public void onBackPressed() {
        mVideoView.handleBackPress(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mVideoView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mVideoView.onResume();
        }
    }
}