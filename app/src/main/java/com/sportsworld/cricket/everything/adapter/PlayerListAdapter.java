package com.sportsworld.cricket.everything.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.sportsworld.cricket.everything.R;
import com.sportsworld.cricket.everything.activity.PlayerProfileActivity;
import com.sportsworld.cricket.everything.model.Player;
import com.sportsworld.cricket.everything.util.CircleImageView;
import com.sportsworld.cricket.everything.util.Constants;
import com.sportsworld.cricket.everything.util.FetchFromWeb;
import com.sportsworld.cricket.everything.util.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import dmax.dialog.SpotsDialog;

/**
 * @author Ripon
 */
public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.PlayerListViewHolder> {

    Context context;
    ArrayList<Player> players;
    Gson gson;

    public PlayerListAdapter(Context context, ArrayList<Player> players) {
        this.context = context;
        this.players = players;
        gson = new Gson();
    }

    @Override
    public PlayerListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singleplayeroflist, parent, false);
        return new PlayerListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlayerListViewHolder holder, final int position) {
        holder.pname.setText(players.get(position).getName());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "http://apisea.xyz/Cricket/apis/v1/YahooToCricAPI.php?key=bl905577&yahoo="+players.get(position).getPersonid();
                final AlertDialog progressDialog = new SpotsDialog(context, R.style.Custom);
                progressDialog.show();
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
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show();
                    }
                });


            }
        });
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    static class PlayerListViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView pname;
        LinearLayout linearLayout;

        public PlayerListViewHolder(View itemView) {
            super(itemView);
            circleImageView = ViewHolder.get(itemView, R.id.civSinglePlayer);
            pname = ViewHolder.get(itemView, R.id.tvPlayerName);
            linearLayout = ViewHolder.get(itemView, R.id.player_list_item_card);
        }
    }
}
