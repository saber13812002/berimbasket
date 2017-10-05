package ir.berimbasket.app.activity.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import ir.berimbasket.app.R;
import ir.berimbasket.app.activity.ActivityCreateStadium;
import ir.berimbasket.app.activity.ActivityHome;
import ir.berimbasket.app.activity.ActivitySetMarker;
import ir.berimbasket.app.activity.ActivityStadium;
import ir.berimbasket.app.entity.EntityStadium;
import ir.berimbasket.app.json.HttpFunctions;
import ir.berimbasket.app.util.ApplicationLoader;
import ir.berimbasket.app.util.GPSTracker;

public class FragmentMap extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private static String _URL = "http://berimbasket.ir/bball/get.php?id=0";
    double latitude = 35.723284;
    double longitude = 51.441968;
    GoogleMap map;
    private MapView mapView;
    private LocationManager locationManager;
    private ArrayList<EntityStadium> locationList;
    private String TAG = ActivityHome.class.getSimpleName();
    private ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fabAddLocation);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivitySetMarker.class);
                getActivity().startActivity(intent);
            }
        });

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationList = new ArrayList<>();
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        // Tracking the screen view (Analytics)
        ApplicationLoader.getInstance().trackScreenView("Map Fragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        try {

            this.map = map;
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            map.setMyLocationEnabled(true);
            new GetLocations().execute();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        // Check if a click count was set, then display the click count.
        try {
            Intent intent = new Intent(getActivity(), ActivityStadium.class);
            marker.getTag();
            intent.putExtra("stadiumDetail", "");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    private class GetLocations extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            GPSTracker gps = new GPSTracker(getActivity());

            // Check if GPS enabled
            if (gps.canGetLocation()) {

                latitude = gps.getLatitude();
                longitude = gps.getLongitude();

            } else {
                // Can't get location.
                // GPS or network is not enabled.
                // Ask user to enable GPS/network in settings.
                gps.showSettingsAlert();
            }
            pDialog.setMessage("لطفا صبر کنید ...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpFunctions sh = new HttpFunctions(HttpFunctions.RequestType.GET);

            // Making a request to _URL and getting response
            String jsonStr = sh.makeServiceCall(_URL);
            Log.e(TAG, "Response from _URL: " + jsonStr);

            if (jsonStr != null) {
                try {
                    // Getting JSON Array node
                    JSONArray locations = new JSONArray(jsonStr);

                    // looping through All Contacts
                    for (int i = 0; i < locations.length(); i++) {
                        JSONObject c = locations.getJSONObject(i);

                        String id = c.getString("id");
                        String title = c.getString("title");
                        String latitude = c.getString("PlaygroundLatitude");
                        String longitude = c.getString("PlaygroundLongitude");
                        String type = c.getString("PlaygroundType");

                        // tmp hash map for single contact
                        EntityStadium entityStadium = new EntityStadium();

                        // adding each child node to HashMap key => value
                        entityStadium.setId(Integer.parseInt(id));
                        entityStadium.setTitle(title);
                        entityStadium.setLatitude(latitude);
                        entityStadium.setLongitude(longitude);
                        entityStadium.setType(type);

                        // adding contact to contact list
                        locationList.add(entityStadium);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
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
                Log.e(TAG, "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            /**
             * Updating parsed JSON data into ListView
             * */
            if (pDialog.isShowing())
                pDialog.cancel();

            Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/yekan.ttf");
            for (int i = 0; i < locationList.size(); i++) {
                EntityStadium entityStadium = locationList.get(i);
                String id = String.valueOf(entityStadium.getId());
                String title = entityStadium.getTitle();
                String latitude = entityStadium.getLatitude();
                String longitude = entityStadium.getLongitude();

                View customMarkerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_map_marker, null);
                TextView txtMarkerTitle = (TextView) customMarkerView.findViewById(R.id.markerTitle);
                txtMarkerTitle.setText(title);
                txtMarkerTitle.setTypeface(typeface);
                IconGenerator generator = new IconGenerator(getActivity());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    generator.setBackground(null);
                } else {
                    generator.setBackground(null);
                }
                generator.setContentView(customMarkerView);
                Bitmap icon = generator.makeIcon();


                Marker marker = map.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)))
                        .icon(BitmapDescriptorFactory.fromBitmap(icon))
                        .title(title));

                marker.setTag(id);

            }

            map.setOnMarkerClickListener(FragmentMap.this);
            getCityLatLong();

        }

    }

    /**
     * Change map location based on city that selected in setting
     */
    private void getCityLatLong() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String latLong = prefs.getString("state_list", "35.111111a54545");

        if (Double.parseDouble(latLong.split("a")[0]) != 0) {
            FragmentMap.this.latitude = Double.parseDouble(latLong.split("a")[0]);
            FragmentMap.this.longitude = Double.parseDouble(latLong.split("a")[1]);
        }
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(FragmentMap.this.latitude, FragmentMap.this.longitude), 14.0f));
    }
}
