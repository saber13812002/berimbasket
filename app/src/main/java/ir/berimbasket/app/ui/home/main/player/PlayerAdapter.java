package ir.berimbasket.app.ui.home.main.player;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.berimbasket.app.R;
import ir.berimbasket.app.data.env.UrlConstants;
import ir.berimbasket.app.data.network.model.Player;

class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder> {

    private List<Player> dataSource;
    private PlayerListListener listener;

    interface PlayerListListener {
        void onPlayerItemClick(Player player);
    }

    PlayerAdapter(List<Player> items, PlayerListListener listener) {
        dataSource = items;
        this.listener = listener;
    }

    PlayerAdapter(PlayerListListener listener) {
        this(new ArrayList<Player>(), listener);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_player, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.imgPlayerProfile.setImageResource(R.drawable.profile_default);
        holder.txtPlayerName.setText(dataSource.get(position).getName());
        holder.txtTeam.setText(dataSource.get(position).getTeamName());
        holder.txtPost.setText(dataSource.get(position).getPost());
        String profilePic = dataSource.get(position).getProfileImage();
        Picasso.with(holder.view.getContext())
                .load(UrlConstants.Base.Root + "/" + profilePic)
                .resize(120, 120)
                .centerInside()
                .placeholder(R.drawable.profile_default)
                .error(R.drawable.profile_default)
                .into(holder.imgPlayerProfile);
        if (dataSource.get(position).getPriority() > 6) {
            holder.imgCoach.setVisibility(View.VISIBLE);
        }
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    listener.onPlayerItemClick(dataSource.get(holder.getAdapterPosition()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView txtPlayerName, txtPost, txtTeam;
        ImageView imgCoach;
        CircleImageView imgPlayerProfile;

        ViewHolder(View view) {
            super(view);
            this.view = view;
            this.txtPlayerName = view.findViewById(R.id.txtPlayerName);
            this.txtPost = view.findViewById(R.id.txtPlayerPost);
            this.txtTeam = view.findViewById(R.id.txtPlayerTeam);
            this.imgPlayerProfile = view.findViewById(R.id.imgPlayerProfile);
            this.imgCoach = view.findViewById(R.id.imgCoach);
        }
    }

    void swapDataSource(List<Player> list) {
        this.dataSource = list;
        notifyDataSetChanged();
    }

    void addItems(List<Player> list) {
        int lastPosition = dataSource.size();
        dataSource.addAll(list);
        notifyItemRangeInserted(lastPosition, list.size());
    }
}
