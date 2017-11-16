package ir.berimbasket.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ir.berimbasket.app.R;
import ir.berimbasket.app.entity.EntitySocialAcc;

/**
 * Created by mohammad hosein on 7/21/2017.
 */

public class AdapterSocialAcc extends RecyclerView.Adapter<AdapterSocialAcc.ViewHolderSocialAcc> {

    private final Context context;
    private final ArrayList<EntitySocialAcc> socialAccList;

    public AdapterSocialAcc(ArrayList<EntitySocialAcc> socialAccList, Context context) {
        this.socialAccList = socialAccList;
        this.context = context;
    }

    @Override
    public ViewHolderSocialAcc onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_social_acc, parent, false);
        ViewHolderSocialAcc holder = new ViewHolderSocialAcc(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolderSocialAcc holder, int position) {
        holder.setData(position);
    }


    @Override
    public int getItemCount() {
        return socialAccList.size();
    }

    class ViewHolderSocialAcc extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView btnSocialAcc;
        ViewHolderSocialAcc(View itemView) {
            super(itemView);
            this.btnSocialAcc = itemView.findViewById(R.id.btnSocialAcc);
        }

        public void setData(int position) {
            final EntitySocialAcc entitySocialAcc = socialAccList.get(position);
            btnSocialAcc.setImageResource(entitySocialAcc.getImageResId());
            btnSocialAcc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (entitySocialAcc.getType() == EntitySocialAcc.SOCIAL_TYPE_INSTAGRAM) {

                    } else if (entitySocialAcc.getType() == EntitySocialAcc.SOCIAL_TYPE_TELEGRAM_CHANNEL) {

                    } else if(entitySocialAcc.getType() == EntitySocialAcc.SOCIAL_TYPE_TELEGRAM_GROUP) {

                    } else {

                    }
                }
            });
        }

        @Override
        public void onClick(View view) {
        }
    }
}
