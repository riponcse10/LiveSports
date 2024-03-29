package com.sportsworld.cricket.everything.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.sportsworld.cricket.everything.R;
import com.sportsworld.cricket.everything.adapter.MatchDetailsViewPagerAdapter;
import com.sportsworld.cricket.everything.fragment.RankingFragment;
import com.sportsworld.cricket.everything.util.Constants;
import com.sportsworld.cricket.everything.util.FetchFromWeb;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import dmax.dialog.SpotsDialog;

/**
 * @author Ripon
 */
public class RankingActivity extends CommonAppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    Spinner spinner;
    AdView adView;
    MatchDetailsViewPagerAdapter matchDetailsViewPagerAdapter;
    RankingFragment testFragment;
    RankingFragment odiFragment;
    RankingFragment T20Fragment;
    JSONObject response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        tabLayout = (TabLayout) findViewById(R.id.tab_ranking);
        viewPager = (ViewPager) findViewById(R.id.view_pager_ranking);
        spinner = (Spinner) findViewById(R.id.spinner);
        adView = (AdView) findViewById(R.id.adViewMatchDetails);
        testFragment = new RankingFragment();
        odiFragment = new RankingFragment();
        T20Fragment = new RankingFragment();

        List<String> categories = new ArrayList<String>();
        categories.add("Top Teams");
        categories.add("Top Batsmen");
        categories.add("Top Bowlers");
        categories.add("Top Allrounders");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        setupViewPage(viewPager);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);
        String url = Constants.RANKING_URL;
        Log.d(Constants.TAG, url);

        final AlertDialog progressDialog = new SpotsDialog(RankingActivity.this, R.style.Custom);
        progressDialog.show();
        progressDialog.setCancelable(true);
        FetchFromWeb.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, final JSONObject response) {
                progressDialog.dismiss();

                try {
                    if (testFragment.isAdded())
                        testFragment.populateTeamList(response.getJSONObject("Team").getJSONObject("TEST"));
                    if (odiFragment.isAdded())
                        odiFragment.populateTeamList(response.getJSONObject("Team").getJSONObject("ODI"));
                    if (T20Fragment.isAdded())
                        T20Fragment.populateTeamList(response.getJSONObject("Team").getJSONObject("T20"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            try {
                                testFragment.populateTeamList(response.getJSONObject("Team").getJSONObject("TEST"));
                                odiFragment.populateTeamList(response.getJSONObject("Team").getJSONObject("ODI"));
                                T20Fragment.populateTeamList(response.getJSONObject("Team").getJSONObject("T20"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else if (position == 1) {
                            try {
                                testFragment.populatePlayerList(response.getJSONObject("Player").getJSONObject("TEST").getJSONObject("batting"));
                                odiFragment.populatePlayerList(response.getJSONObject("Player").getJSONObject("ODI").getJSONObject("batting"));
                                T20Fragment.populatePlayerList(response.getJSONObject("Player").getJSONObject("T20").getJSONObject("batting"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else if (position == 2) {
                            try {
                                testFragment.populatePlayerList(response.getJSONObject("Player").getJSONObject("TEST").getJSONObject("bowling"));
                                odiFragment.populatePlayerList(response.getJSONObject("Player").getJSONObject("ODI").getJSONObject("bowling"));
                                T20Fragment.populatePlayerList(response.getJSONObject("Player").getJSONObject("T20").getJSONObject("bowling"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else if (position == 3) {
                            try {
                                testFragment.populatePlayerList(response.getJSONObject("Player").getJSONObject("TEST").getJSONObject("allrounder"));
                                odiFragment.populatePlayerList(response.getJSONObject("Player").getJSONObject("ODI").getJSONObject("allrounder"));
                                T20Fragment.populatePlayerList(response.getJSONObject("Player").getJSONObject("T20").getJSONObject("allrounder"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                Log.d(Constants.TAG, response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                progressDialog.dismiss();
                Toast.makeText(RankingActivity.this, "Failed", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progressDialog.dismiss();
                Toast.makeText(RankingActivity.this, "Failed", Toast.LENGTH_LONG).show();
            }
        });

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(Constants.ONE_PLUS_TEST_DEVICE)
                .addTestDevice(Constants.XIAOMI_TEST_DEVICE).build();
        adView.loadAd(adRequest);
    }

    public final void setupViewPage(ViewPager viewPager) {
        this.matchDetailsViewPagerAdapter = new MatchDetailsViewPagerAdapter(getSupportFragmentManager());
        this.matchDetailsViewPagerAdapter.addFragment(testFragment, "Test");
        this.matchDetailsViewPagerAdapter.addFragment(odiFragment, "ODI");
        this.matchDetailsViewPagerAdapter.addFragment(T20Fragment, "T20I");
        viewPager.setAdapter(this.matchDetailsViewPagerAdapter);
    }
}
