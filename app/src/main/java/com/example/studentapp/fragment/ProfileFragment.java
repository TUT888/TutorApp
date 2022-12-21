package com.example.studentapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.studentapp.MainActivity;
import com.example.studentapp.R;
import com.example.studentapp.api.LoadImageInternet;
import com.example.studentapp.model.User;


public class ProfileFragment extends Fragment {
    // Resources
    private View mView;
    private MainActivity mMainActivity;
    private LinearLayout loginLayoutProfileHeading, logoutLayoutProfileHeading;
    private LinearLayout loginLayoutProfileContent, logoutLayoutProfileContent;
    private Button btnLogin, btnRegister;
    private ImageView imgAvatar;
    TextView tvClasses, tvAccountInfo, tvChangePassword, tvLogout, tvName, tvRole;
    // Object Class & variables
    private User currentUser;
    private boolean fromLoginFragment;

    public ProfileFragment() {
        currentUser = null;
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_profile, container, false);
        mMainActivity = (MainActivity) getActivity();
        currentUser = mMainActivity.getCurrentLoginUser();

        // Check login/logout ==> display corresponding view

        loginLayoutProfileHeading = mView.findViewById(R.id.loginLayoutProfileHeading);
        logoutLayoutProfileHeading = mView.findViewById(R.id.logoutLayoutProfileHeading);
        loginLayoutProfileContent = mView.findViewById(R.id.loginLayoutProfileContent);
        logoutLayoutProfileContent = mView.findViewById(R.id.logoutLayoutProfileContent);
        setUserInteractUI(); // Tùy theo đăng nhập hoặc chưa đăng nhập để hiện UI tương ứng

        tvClasses = mView.findViewById(R.id.tvClasses);
        tvAccountInfo = mView.findViewById(R.id.tvAccountInfo);
        tvChangePassword = mView.findViewById(R.id.tvChangePassword);
        tvLogout = mView.findViewById(R.id.tvLogout);
        tvName = mView.findViewById(R.id.tvName);
        tvRole = mView.findViewById(R.id.tvRole);
        imgAvatar = mView.findViewById(R.id.imgAvatar);

        if (currentUser!=null) {
            tvName.setText(currentUser.getName());
            tvRole.setText(MainActivity.CURRENT_LOGIN_ROLE);
            new LoadImageInternet(imgAvatar).execute(MainActivity.URL_IMAGE +  MainActivity.CURRENT_LOGIN_AVATAR);
        }

        btnLogin = mView.findViewById(R.id.btnLogin);
        btnRegister = mView.findViewById(R.id.btnRegister);

        // Set OnClickListener
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainActivity.logOut();
                setUserInteractUI();
            }
        });
        tvClasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainActivity.goToClassFragment(null);
            }
        });
        tvAccountInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainActivity.goToAccountInfoFragment();
            }
        });
        tvChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainActivity.goToChangePasswordFragment();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainActivity.goToLoginFragment();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainActivity.goToRegisterFragment();
            }
        });

        return mView;
    }

    public void setUserInteractUI() {
        currentUser = mMainActivity.getCurrentLoginUser();
        if (currentUser==null) {
            loginLayoutProfileHeading.setVisibility(View.GONE);
            loginLayoutProfileContent.setVisibility(View.GONE);

            logoutLayoutProfileHeading.setVisibility(View.VISIBLE);
            logoutLayoutProfileContent.setVisibility(View.VISIBLE);
        } else {
            // Hiện layout đã đăng nhập
            loginLayoutProfileHeading.setVisibility(View.VISIBLE);
            loginLayoutProfileContent.setVisibility(View.VISIBLE);
            // Ẩn layout chưa đăng nhập
            logoutLayoutProfileHeading.setVisibility(View.GONE);
            logoutLayoutProfileContent.setVisibility(View.GONE);
        }
    }
}