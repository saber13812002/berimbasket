package ir.berimbasket.app.activity.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.ronash.pushe.Pushe;
import ir.berimbasket.app.R;
import ir.berimbasket.app.adapter.AdapterPlayerSpecification;
import ir.berimbasket.app.entity.EntityPlayer;
import ir.berimbasket.app.network.HttpFunctions;
import ir.berimbasket.app.util.PrefManager;

public class FragmentPlayerSpecification extends Fragment {

    private static String PLAYER_URL = "http://berimbasket.ir/bball/getPlayers.php";
    private EntityPlayer entityPlayer;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_specification, container, false);
        this.view = view;
        new GetPlayers().execute();
        return view;
    }


    private class GetPlayers extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            HttpFunctions sh = new HttpFunctions(HttpFunctions.RequestType.GET);
            PrefManager pref = new PrefManager(getContext());
            String pusheId = Pushe.getPusheId(getContext());
            String userName = pref.getUserName();
            int userId = pref.getUserId();
            String urlParams = String.format("id=%s&pusheid=%s&username=%s", userId, pusheId, userName);
            String jsonStr = sh.makeServiceCall(PLAYER_URL + "?" + urlParams);
            if (jsonStr != null) {
                try {
                    JSONArray locations = new JSONArray(jsonStr);

                    for (int i = 0; i < locations.length(); i++) {
                        JSONObject c = locations.getJSONObject(i);

                        String id = c.getString("id");
                        String username = c.getString("username");
                        String namefa = c.getString("namefa");
                        String address = c.getString("address");
                        String uImage = c.getString("uImages");
                        String uInstagramId = c.getString("uInstagramId");
                        String uTelegramId = c.getString("uTelegramlId");
                        String height = c.getString("height");
                        String weight = c.getString("weight");
                        String city = c.getString("city");
                        String age = c.getString("age");
                        String coach = c.getString("coach");
                        String teamname = c.getString("teamname");
                        String experience = c.getString("experience");
                        String post = c.getString("post");
                        String telegramPhone = c.getString("telegramphone");

                        Log.i("name", id);

                        entityPlayer = new EntityPlayer();
                        // adding each child node to HashMap key => value

                        entityPlayer.setId(id != "null" ? Integer.parseInt(id) : -1);
                        entityPlayer.setUsername(username);
                        entityPlayer.setName(namefa);
                        entityPlayer.setAddress(address);
                        entityPlayer.setProfileImage(uImage);
                        entityPlayer.setInstagramId(uInstagramId);
                        entityPlayer.setTelegramId(uTelegramId);
                        entityPlayer.setHeight(height != "null" ? Integer.parseInt(height) : -1);
                        entityPlayer.setWeight(weight != "null" ? Integer.parseInt(weight) : -1);
                        entityPlayer.setCity(city);
                        entityPlayer.setAge(age != "null" ? Integer.parseInt(age) : -1);
                        entityPlayer.setCoachName(coach);
                        entityPlayer.setTeamName(teamname);
                        entityPlayer.setExperience(experience);
                        entityPlayer.setPhone(telegramPhone);

                    }
                } catch (final JSONException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            initRecyclerPlayerSpec(getPlayerSpec(entityPlayer));
        }
    }

    private void initRecyclerPlayerSpec(ArrayList<String> playerSpecList) {

        RecyclerView recyclerView = view.findViewById(R.id.recyclerPlayerSpec);
        AdapterPlayerSpecification adapterPlayerSpecification = new AdapterPlayerSpecification(playerSpecList, getActivity());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapterPlayerSpecification);

        GridLayoutManager glm = new GridLayoutManager(getActivity(), 2);
        glm.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(glm);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private ArrayList<String> getPlayerSpec(EntityPlayer entityPlayer) {

        ArrayList<String> playerSpecList = new ArrayList<>();
        String specSeparator = getString(R.string.fragment_player_spec_separator);
        playerSpecList.add(getString(R.string.fragment_player_spec_name) + " " + specSeparator + " " + entityPlayer.getName());
        playerSpecList.add(getString(R.string.fragment_player_spec_age) + " " + specSeparator + " " + String.valueOf(entityPlayer.getAge()));
        playerSpecList.add(getString(R.string.fragment_player_spec_city) + " " + specSeparator + " " + entityPlayer.getCity());
        playerSpecList.add(getString(R.string.fragment_player_spec_height) + " " + specSeparator + " " + String.valueOf(entityPlayer.getHeight()));
        playerSpecList.add(getString(R.string.fragment_player_spec_weight) + " " + specSeparator + " " + String.valueOf(entityPlayer.getWeight()));
        playerSpecList.add(getString(R.string.fragment_player_spec_address) + " " + specSeparator + " " + entityPlayer.getAddress());
        playerSpecList.add(getString(R.string.fragment_player_spec_experience) + " " + specSeparator + " " + entityPlayer.getExperience());
        playerSpecList.add(getString(R.string.fragment_player_spec_head_coach) + " " + specSeparator + " " + entityPlayer.getCoachName());
        playerSpecList.add(getString(R.string.fragment_player_spec_team) + " " + specSeparator + " " + entityPlayer.getTeamName());
        playerSpecList.add(getString(R.string.fragment_player_spec_user_name) + " " + specSeparator + " " + entityPlayer.getUsername());
        playerSpecList.add(getString(R.string.fragment_player_spec_post) + " " + specSeparator + " " + String.valueOf(entityPlayer.getPost()));
//        playerSpecList.add("" + entityPlayer.getProfileImage());
        playerSpecList.add(getString(R.string.fragment_player_spec_telegram) + " " + specSeparator + " " + entityPlayer.getTelegramId());
        playerSpecList.add(getString(R.string.fragment_player_spec_instagram) + " " + specSeparator + " " + entityPlayer.getInstagramId());
        playerSpecList.add(getString(R.string.fragment_player_spec_phone_number) + " " + specSeparator + " " + entityPlayer.getPhone());
        return playerSpecList;
    }


}
