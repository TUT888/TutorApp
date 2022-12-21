package com.example.studentapp.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.studentapp.search.SearchPostFragment;
import com.example.studentapp.search.SearchTutorFragment;

public class SearchViewPagerAdapter extends FragmentStatePagerAdapter {

    public SearchViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SearchPostFragment();
            case 1:
                return new SearchTutorFragment();
            default:
                return new SearchPostFragment();
        }
    }


    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Bài đăng";
            case 1:
                return "Gia sư";
            default:
                return "Bài đăng";
        }
    }
}
