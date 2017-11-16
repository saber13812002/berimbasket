package ir.berimbasket.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ir.berimbasket.app.R;

/**
 * Created by mohammad hosein on 7/21/2017.
 */

public class AdapterPlayerSpecification extends RecyclerView.Adapter<AdapterPlayerSpecification.PlayerSpecViewHolder> {

    private final Context context;
    private final ArrayList<String> playerSpecList;

    public AdapterPlayerSpecification(ArrayList<String> playerList, Context context) {
        this.playerSpecList = playerList;
        this.context = context;
    }

    @Override
    public AdapterPlayerSpecification.PlayerSpecViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_player_specification, parent, false);
        AdapterPlayerSpecification.PlayerSpecViewHolder holder = new AdapterPlayerSpecification.PlayerSpecViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(AdapterPlayerSpecification.PlayerSpecViewHolder holder, int position) {
        holder.setData(position);
    }


    @Override
    public int getItemCount() {
        return playerSpecList.size();
    }

    class PlayerSpecViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtSpecNum, txtSpecValue;

        PlayerSpecViewHolder(View itemView) {
            super(itemView);
            this.txtSpecNum = (TextView) itemView.findViewById(R.id.txtSpecKey);
            this.txtSpecValue = (TextView) itemView.findViewById(R.id.txtSpecValue);
        }

        public void setData(int position) {
            txtSpecNum.setText(String.valueOf(position));
            txtSpecValue.setText(playerSpecList.get(position));
        }

        @Override
        public void onClick(View view) {
//            switch (view.getId()) {
//            }
        }
    }
}
