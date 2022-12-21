package com.example.studentapp.extra_fragment;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.studentapp.MainActivity;
import com.example.studentapp.R;
import com.example.studentapp.fragment.MyPostFragment;
import com.example.studentapp.model.Post;
import com.example.studentapp.model.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountInfoFragment extends Fragment {
    private MainActivity mMainActivity;
    private View mView;
    private CircleImageView civAvatar;
    private TextView tvName, tvPhoneNumber, tvArea, tvGender, tvBirthday, tvEmail, tvStatus;
    private ImageButton ibBack;
    // Data
    User currentUser;

    public AccountInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_account_info, container, false);
        mMainActivity = (MainActivity) getActivity();

        ibBack = mView.findViewById(R.id.ibBack);
        tvPhoneNumber = mView.findViewById(R.id.tvPhoneNumber);
        tvArea = mView.findViewById(R.id.tvPhoneNumber);
        tvGender = mView.findViewById(R.id.tvGender);
        tvBirthday = mView.findViewById(R.id.tvBirthday);
        tvEmail = mView.findViewById(R.id.tvEmail);
        tvStatus = mView.findViewById(R.id.tvStatus);
        tvName = mView.findViewById(R.id.tvName);

        currentUser = mMainActivity.getCurrentLoginUser();
        if (currentUser!=null) {
            tvName.setText(currentUser.getName());
            tvPhoneNumber.setText(currentUser.getPhoneNumber());
            tvArea.setText(currentUser.getAddress());
            if (currentUser.getGender()==0) {
                tvGender.setText("Nam");
            } else {
                tvGender.setText("Nữ");
            }
            tvBirthday.setText(currentUser.getBirthday());
            tvEmail.setText(currentUser.getEmail());
            switch (currentUser.getStatus()) {
                case (User.USER_STATUS_NOT_VERIFIED):
                    tvStatus.setText("Chưa xác thực");
                    break;
                case (User.USER_STATUS_VERIFIED):
                    tvStatus.setText("Đã xác thực");
                    break;
            }
        }

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        return mView;
    }
}