package com.example.studentapp.extra_fragment;

import android.media.Image;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.studentapp.MainActivity;
import com.example.studentapp.R;
import com.example.studentapp.api.APIService;
import com.example.studentapp.api.ResultStringAPI;
import com.example.studentapp.model.Post;
import com.example.studentapp.model.User;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordFragment extends Fragment {
    private MainActivity mMainActivity;
    private View mView;
    private ImageButton ibBack;
    private MaterialButton btnChangePass;
    private EditText etOldPassword, etNewPassword, etConfirmPassword;
    private User currentUser;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_change_password, container, false);
        mMainActivity = (MainActivity) getActivity();
        currentUser = mMainActivity.getCurrentLoginUser();

        ibBack = mView.findViewById(R.id.ibBack);
        btnChangePass = mView.findViewById(R.id.btnChangePass);
        etOldPassword = mView.findViewById(R.id.etOldPassword);
        etNewPassword = mView.findViewById(R.id.etNewPassword);
        etConfirmPassword = mView.findViewById(R.id.etConfirmPassword);

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPass = etOldPassword.getText().toString();
                String newPass = etNewPassword.getText().toString();
                String confirmPass = etConfirmPassword.getText().toString();
                if (oldPass.equals("")) {
                    Toast.makeText(mMainActivity, "Hãy nhập mật khẩu", Toast.LENGTH_SHORT).show();
                } else if (newPass.equals("")) {
                    Toast.makeText(mMainActivity, "Hãy nhập mật khẩu mới", Toast.LENGTH_SHORT).show();
                } else if (confirmPass.equals("")) {
                    Toast.makeText(mMainActivity, "Hãy xác nhận mật khẩu mới", Toast.LENGTH_SHORT).show();
                } else if (!confirmPass.equals(newPass)) {
                    Toast.makeText(mMainActivity, "Xác nhận mật khẩu mới không đúng", Toast.LENGTH_SHORT).show();
                } else {
                    changePassword(currentUser.getPhoneNumber(), newPass);
                }
            }
        });
        return mView;
    }

    private void changePassword(String phoneNumber, String newPassword) {
        //Call API
        Call<ResultStringAPI> apiCall = APIService.apiService.changePassword(phoneNumber, newPassword);
        apiCall.enqueue(new Callback<ResultStringAPI>() {
            @Override
            public void onResponse(Call<ResultStringAPI> call, Response<ResultStringAPI> response) {
                ResultStringAPI resultStringAPI = response.body();
                if (response.isSuccessful() || resultStringAPI!=null) {
                    if (resultStringAPI.getCode()==0) {
                        Toast.makeText(getContext(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().popBackStack();
                        mMainActivity.resetViewPagerUI(4);
                    } else {
                        Toast.makeText(mMainActivity, "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultStringAPI> call, Throwable t) {
                call.cancel();
                Log.d("Change Password Result", "Failed: " + t);
            }
        });
    }
}