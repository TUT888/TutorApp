package com.example.studentapp.extra_fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentapp.MainActivity;
import com.example.studentapp.R;
import com.example.studentapp.api.APIService;
import com.example.studentapp.api.ResultObjectAPI;
import com.example.studentapp.api.ResultStringAPI;
import com.example.studentapp.model.User;
import com.google.android.material.button.MaterialButton;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginFragment extends Fragment {
    // Resources
    private View mView;
    private MainActivity mMainActivity;
    private ImageButton ibBack;
    private EditText etPhoneNumber, etPassword;
    private MaterialButton btnLogin;
    private TextView tvRegister, tvErrorMess;
    // Data
    private User userData;

    public LoginFragment() {
        userData = null;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_login, container, false);
        mMainActivity = (MainActivity) getActivity();

        etPhoneNumber = mView.findViewById(R.id.etPhoneNumber);
        etPassword = mView.findViewById(R.id.etPassword);
        btnLogin = mView.findViewById(R.id.btnLogin);
        ibBack = mView.findViewById(R.id.ibBack);
        tvRegister = mView.findViewById(R.id.tvRegister);
        tvErrorMess = mView.findViewById(R.id.tvErrorMess);

        // Set OnClickListener
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginAccount();
            }
        });
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
                mMainActivity.goToRegisterFragment();
            }
        });

        return mView;
    }

    private void loginAccount() {
        String phone = etPhoneNumber.getText().toString();
        String pass = etPassword.getText().toString();

        if (phone.equals("")) {
            Toast.makeText(mMainActivity, "Hãy nhập số điện thoại", Toast.LENGTH_SHORT).show();
        } else if (pass.equals("")) {
            Toast.makeText(mMainActivity, "Hãy nhận mật khẩu", Toast.LENGTH_SHORT).show();
        } else {
            // Call API get account has same username & pass
            requestLogin(phone, pass);
            if (userData!=null) {
                // Save SharedPreference
                mMainActivity.savedLoginUser(userData);
                // Back stack & reset UI
                getActivity().getSupportFragmentManager().popBackStack();
                mMainActivity.resetViewPagerUI(4); // Restart view pager, viewing Profile page
            }
        }
    }

    // Call API
    private void requestLogin(String phone, String pass) {
        // Giả sử gọi API, query ra user có phone & pass trùng khớp
        Call<ResultObjectAPI> apiCall = APIService.apiService.userLogin(phone, pass);
        apiCall.enqueue(new Callback<ResultObjectAPI>() {
            @Override
            public void onResponse(Call<ResultObjectAPI> call, Response<ResultObjectAPI> response) {
                ResultObjectAPI resultObj = response.body();
                if (resultObj.getCode()==0) {
                    hideError();
                    JsonObject jsonUserObject = resultObj.getData().getAsJsonObject();
                    User user = new User(
                            jsonUserObject.get("phoneNumber").getAsString(),
                            jsonUserObject.get("name").getAsString(),
                            jsonUserObject.get("status").getAsInt(),
                            jsonUserObject.get("area").getAsString(),
                            jsonUserObject.get("gender").getAsInt(),
                            jsonUserObject.get("birthday").getAsString(),
                            jsonUserObject.get("email").getAsString(),
                            jsonUserObject.get("avatar").getAsString(),
                            "");
                    userData = user;
                    //
                } else {
                    Log.d("Login Result", "Failed!");
                    showError("Đăng nhập thất bại:\n" + resultObj.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultObjectAPI> call, Throwable t) {
                call.cancel();
                Log.d("Login Result", "Failed: " + t);
                showError("Có lỗi xảy ra, vui lòng thử lại sau");
            }
        });
    }

    private void showError(String errorMess) {
        tvErrorMess.setText(errorMess);
        tvErrorMess.setVisibility(View.VISIBLE);
    }
    private void hideError() {
        tvErrorMess.setText("");
        tvErrorMess.setVisibility(View.GONE);
    }
}