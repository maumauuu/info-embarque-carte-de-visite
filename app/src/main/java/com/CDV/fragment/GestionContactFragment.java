package com.CDV.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.CDV.R;

public class GestionContactFragment extends Fragment {

    private static final String TAG = GestionContactFragment.class.getSimpleName();
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int position;

    public GestionContactFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundl = getArguments();
        position = bundl.getInt("pos");
        View view = inflater.inflate(R.layout.gestion_contact, container, false);
        tabLayout = (TabLayout)view.findViewById(R.id.tabs);
        viewPager = (ViewPager)view.findViewById(R.id.view_pager);
        CustomFragmentPageAdapter custom = new CustomFragmentPageAdapter(getChildFragmentManager());
        //getActivity().setTitle(custom.getPageTitle(position));
        viewPager.setAdapter(custom);;
        tabLayout.setTabTextColors(Color.LTGRAY,Color.WHITE);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(position);

        return view;

    }
}
