package com.example.studentapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.studentapp.MainActivity;
import com.example.studentapp.app_interface.IClickTimeTableObjectListener;
import com.example.studentapp.fragment.PendingClassFragment;
import com.example.studentapp.fragment.HomeFragment;
import com.example.studentapp.fragment.MyPostFragment;
import com.example.studentapp.fragment.ProfileFragment;
import com.example.studentapp.fragment.SearchFragment;
import com.example.studentapp.model.ClassObject;

public class ViewPagerAdapter extends FragmentStateAdapter {
    MainActivity mainActivity;
    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, MainActivity mainActivity) {
        super(fragmentManager, lifecycle);
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new SearchFragment();
            case 2:
                return new MyPostFragment();
            case 3:
                return new PendingClassFragment();
            case 4:
                return new ProfileFragment();
            default:
                return new HomeFragment(new IClickTimeTableObjectListener() {
                    @Override
                    public void switchToClassFragment(ClassObject classObject) {
                        mainActivity.goToClassFragment(classObject);
                    }
                });
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}