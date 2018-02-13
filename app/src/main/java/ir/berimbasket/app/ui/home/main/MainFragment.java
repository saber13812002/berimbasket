package ir.berimbasket.app.ui.home.main;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.berimbasket.app.R;
import ir.berimbasket.app.util.AnalyticsHelper;
import ir.berimbasket.app.util.FontHelper;

/**
 * Created by mohammad hosein on 5/1/2017.
 */

public class MainFragment extends Fragment {

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ViewPager viewPager = view.findViewById(R.id.pagerMain);
        TabLayout tabLayout = view.findViewById(R.id.tabMain);

        // init viewPager
        MainPagerAdapter pagerAdapter = new MainPagerAdapter(getContext(), getActivity().getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);

        // init tabLayout
        tabLayout.setupWithViewPager(viewPager);
        FontHelper fontHelper = new FontHelper(getContext(), getString(R.string.font_yekan));
        fontHelper.tablayoutApplyFont(tabLayout);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Track screen view (Analytics)
        AnalyticsHelper.getInstance().trackScreenView(getContext(), this.getClass().getSimpleName());
    }

}
