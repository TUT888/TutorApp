package com.example.studentapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.studentapp.search.SearchPostFragment;
import com.google.android.material.tabs.TabLayout;
import com.example.studentapp.R;
import com.example.studentapp.adapter.SearchViewPagerAdapter;

import java.util.List;

public class SearchFragment extends Fragment {

   TabLayout searchTabLayout;
   ViewPager searchViewPager;
   View mView;


    // TODO: Rename and change types of parameters

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_search, container, false);

        searchTabLayout = mView.findViewById(R.id.searchTabLayout);
        searchViewPager = mView.findViewById(R.id.searchViewPager);

        SearchViewPagerAdapter searchViewPagerAdapter = new SearchViewPagerAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        searchViewPager.setAdapter(searchViewPagerAdapter);
        searchTabLayout.setupWithViewPager(searchViewPager);

        return mView;
    }


}