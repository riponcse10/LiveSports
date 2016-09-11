package com.sfuronlabs.ripon.cricketmania.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.sfuronlabs.ripon.cricketmania.activity.PlayerProfileActivity;
import com.sfuronlabs.ripon.cricketmania.model.Bowler;
import com.sfuronlabs.ripon.cricketmania.R;
import com.sfuronlabs.ripon.cricketmania.util.Constants;
import com.sfuronlabs.ripon.cricketmania.util.FetchFromWeb;
import com.sfuronlabs.ripon.cricketmania.util.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * @author Ripon
 */
public class BowlerAdapter extends RecyclerView.Adapter<BowlerAdapter.BowlerViewHolder> {

    Context context;
    ArrayList<Bowler> bowlers;
    LayoutInflater layoutInflater;

    public BowlerAdapter(Context context, ArrayList<Bowler> bowlers) {
        this.context = context;
        this.bowlers = bowlers;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public BowlerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BowlerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.singlebowler, parent, false));
    }

    @Override
    public void onBindViewHolder(BowlerViewHolder holder, final int position) {
        holder.name.setText(bowlers.get(position).getName());
        holder.overs.setText(bowlers.get(position).getOver());
        holder.maidens.setText(bowlers.get(position).getMaiden());
        holder.runs.setText(bowlers.get(position).getRun());
        holder.wickets.setText(bowlers.get(position).getWicket());
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://apisea.xyz/Cricket/apis/v1/CricinfoToCricAPI.php?key=bl905577&cricinfo="+bowlers.get(position).getPlayerId();
                final ProgressDialog progressDialog;
                progressDialog = ProgressDialog.show(context, "", "Loading. Please wait...", true);
                progressDialog.setCancelable(true);

                FetchFromWeb.get(url, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        progressDialog.dismiss();
                        try {
                            if (response.getString("msg").equals("Successful")) {
                                String playerID = (response.getJSONArray("content").getJSONObject(0).getString("cricapiID"));
                                Intent intent = new Intent(context, PlayerProfileActivity.class);
                                intent.putExtra("playerID",playerID);
                                context.startActivity(intent);
                            } else {
                                Toast.makeText(context,"Profile Not Found",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d(Constants.TAG, response.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }


    @Override
    public int getItemCount() {
        return bowlers.size();
    }

    static class BowlerViewHolder extends RecyclerView.ViewHolder {
        protected TextView name;
        protected TextView overs;
        protected TextView maidens;
        protected TextView runs;
        protected TextView wickets;

        public BowlerViewHolder(View itemView) {
            super(itemView);

            name = ViewHolder.get(itemView, R.id.bowl_Name);
            overs = ViewHolder.get(itemView, R.id.bowl_overs);
            maidens = ViewHolder.get(itemView, R.id.bowl_maiden);
            runs = ViewHolder.get(itemView, R.id.bowl_runs);
            wickets = ViewHolder.get(itemView, R.id.bowl_wickets);
        }
    }
}
