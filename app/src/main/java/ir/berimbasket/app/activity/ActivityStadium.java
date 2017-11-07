package ir.berimbasket.app.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.berimbasket.app.R;
import ir.berimbasket.app.activity.fragment.FragmentStadiumMap;
import ir.berimbasket.app.adapter.AdapterStadiumGallery;
import ir.berimbasket.app.entity.EntityStadium;
import ir.berimbasket.app.entity.EntityStadiumGallery;
import ir.berimbasket.app.util.ApplicationLoader;
import ir.berimbasket.app.util.SendTo;
import ir.berimbasket.app.util.TypefaceManager;

public class ActivityStadium extends AppCompatActivity {

    TextView txtStadiumName, txtStadiumTel, txtRateNo, txtStadiumAddress, txtStadiumRound, txtTelegramChannel, txtInstagramId, txtDetailSection;
    AppCompatButton btnCompleteStadiumDetail;
    CircleImageView imgStadiumLogo;
    Typeface typeface;
    EntityStadium entityStadium;
    String stadiumLogoUrl;
    private static final String UPDATE_STADIUM_INFO_BOT = "https://t.me/berimbasketProfilebot";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stadium);
        entityStadium = (EntityStadium) getIntent().getSerializableExtra("stadiumDetail");
        stadiumLogoUrl = getIntent().getStringExtra("stadiumLogoUrlPath");
        initToolbar();
        initViewsAndListeners();
        initStadiumMap();
        initGalleryRecycler();
        getStadiumInfo(entityStadium);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tracking the screen view (Analytics)
        ApplicationLoader.getInstance().trackScreenView("Stadium Screen");
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // FIXME: 21/09/2017  setDisplayHomeAsUp show warning for nullpointer exception cause
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void getStadiumInfo(EntityStadium entityStadium) {
        txtStadiumName.setText(entityStadium.getTitle());
        txtStadiumTel.setText("-");
        txtStadiumAddress.setText(entityStadium.getAddress());
        txtStadiumRound.setText("-");
        txtTelegramChannel.setText(entityStadium.getTelegramChannelId());
        txtInstagramId.setText(entityStadium.getInstagramId());
    }

    private void initViewsAndListeners() {

        typeface = TypefaceManager.get(getApplicationContext(), getString(R.string.font_yekan));

        txtStadiumName = (TextView) findViewById(R.id.txtStadiumName);
        txtStadiumTel = (TextView) findViewById(R.id.txtStadiumTel);
        txtRateNo = (TextView) findViewById(R.id.txtRateNo);
        txtStadiumAddress = (TextView) findViewById(R.id.txtStadiumAddress);
        txtStadiumRound = (TextView) findViewById(R.id.txtStadiumRound);
        txtTelegramChannel = (TextView) findViewById(R.id.txtTelegramChannel);
        txtInstagramId = (TextView) findViewById(R.id.txtInstagramId);
        txtDetailSection = (TextView) findViewById(R.id.txtDetailSection);
        imgStadiumLogo = (CircleImageView) findViewById(R.id.imgStadiumLogo);
        Picasso.with(ActivityStadium.this)
                .load("https://berimbasket.ir/" + stadiumLogoUrl)
                .resize(100, 100)
                .placeholder(R.drawable.stadium1)
                .error(R.drawable.stadium1)
                .centerInside()
                .into(imgStadiumLogo);

        btnCompleteStadiumDetail = (AppCompatButton) findViewById(R.id.btnCompleteStadiumDetail);

        txtStadiumName.setTypeface(typeface);
        txtStadiumTel.setTypeface(typeface);
        txtRateNo.setTypeface(typeface);
        txtStadiumAddress.setTypeface(typeface);
        txtStadiumRound.setTypeface(typeface);
        txtTelegramChannel.setTypeface(typeface);
        txtInstagramId.setTypeface(typeface);
        btnCompleteStadiumDetail.setTypeface(typeface);
        txtDetailSection.setTypeface(typeface);

        btnCompleteStadiumDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendTo.sendToTelegramChat(ActivityStadium.this, UPDATE_STADIUM_INFO_BOT);
            }
        });
    }


    private void initStadiumMap() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentStadiumMap fragmentStadiumMap = new FragmentStadiumMap();
        Bundle bundle = new Bundle();
        bundle.putSerializable("stadiumDetail", entityStadium);
        fragmentStadiumMap.setArguments(bundle);
        fragmentTransaction.replace(R.id.mapContainer, fragmentStadiumMap);
        fragmentTransaction.commit();
    }


    private void initGalleryRecycler() {
        int[] galleryIdes = {R.drawable.slider1, R.drawable.slider2, R.drawable.slider3};

        ArrayList<EntityStadiumGallery> stadiumGalleryList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            EntityStadiumGallery entityStadiumGallery = new EntityStadiumGallery();
            entityStadiumGallery.setId(i);
            entityStadiumGallery.setUrl(galleryIdes[i]);
            stadiumGalleryList.add(entityStadiumGallery);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerStadiumGallery);
        AdapterStadiumGallery adapterStadiumGallery = new AdapterStadiumGallery(stadiumGalleryList, this);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapterStadiumGallery);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
