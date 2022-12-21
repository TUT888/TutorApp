package com.example.studentapp.extra_fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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


public class RegisterFragment extends Fragment {
    // Resources
    private View mView;
    private MainActivity mMainActivity;
    private ImageButton ibBack;
    private EditText etName, etPhoneNumber, etPassword, etEmail, etBirthday, etArea;
    private RadioGroup rgGender;
    private MaterialButton btnRegister;
    // Input Data
    private String[] choiceOfAreas = MainActivity.PLACES_TO_CHOOSE;
    int checkedArea = 0;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_register, container, false);
        mMainActivity = (MainActivity) getActivity();

        etName = mView.findViewById(R.id.etName);
        etPhoneNumber = mView.findViewById(R.id.etPhoneNumber);
        etPassword = mView.findViewById(R.id.etPassword);
        etEmail = mView.findViewById(R.id.etEmail);
        etBirthday = mView.findViewById(R.id.etBirthday);
        etArea = mView.findViewById(R.id.etArea);
        rgGender = mView.findViewById(R.id.rgGender);
        btnRegister = mView.findViewById(R.id.btnRegister);
        ibBack = mView.findViewById(R.id.ibBack);

        etArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChoicesOfPlaces();
            }
        });
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerAccount();
            }
        });

        return mView;
    }

    private void showChoicesOfPlaces() {
        hideSoftKeyboard();
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
        alertBuilder.setTitle("Chọn khu vực nơi ở");

        if (etArea.getText().toString().isEmpty()) {
            etArea.setText(choiceOfAreas[0]);    //default
        }

        alertBuilder.setSingleChoiceItems(choiceOfAreas, checkedArea, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                checkedArea = i;
            }
        });
        alertBuilder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                etArea.setText(choiceOfAreas[checkedArea]);
            }
        });
        alertBuilder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertBuilder.show();
    }

    private void registerAccount() {
        String name = etName.getText().toString();
        String phoneNumber = etPhoneNumber.getText().toString();
        String pass = etPassword.getText().toString();
        String email = etEmail.getText().toString();
        String birthday = etBirthday.getText().toString();
        String area = etArea.getText().toString();
        int gender = 0;
        switch (rgGender.getCheckedRadioButtonId()) {
            case R.id.rbMale:
                gender = 0;
            case R.id.rbFemale:
                gender = 1;
            default:
                gender = 0;
        }

        if (name.equals("")) {
            Toast.makeText(mMainActivity, "Hãy nhập họ và tên", Toast.LENGTH_SHORT).show();
        } else if (birthday.equals("")) {
            Toast.makeText(mMainActivity, "Hãy nhập ngày sinh", Toast.LENGTH_SHORT).show();
        } else if (phoneNumber.equals("")) {
            Toast.makeText(mMainActivity, "Hãy nhập số điện thoại", Toast.LENGTH_SHORT).show();
        } else if (pass.equals("")) {
            Toast.makeText(mMainActivity, "Hãy nhập mật khẩu", Toast.LENGTH_SHORT).show();
        } else if (email.equals("")) {
            Toast.makeText(mMainActivity, "Hãy nhập email", Toast.LENGTH_SHORT).show();
        } else if (area.equals("")) {
            Toast.makeText(mMainActivity, "Hãy nhập nơi ở", Toast.LENGTH_SHORT).show();
        } else {
            //User newUser = new User(phoneNumber, name, 0, area,
            //        gender, birthday, email, R.drawable.ic_default_user, pass);
            //Call Register API
            User newUser = new User(phoneNumber, name, User.USER_STATUS_NOT_VERIFIED, area, gender, birthday, email, pass);
            Call<ResultStringAPI> apiCall =  APIService.apiService.register(newUser);
            apiCall.enqueue(new Callback<ResultStringAPI>() {
                @Override
                public void onResponse(Call<ResultStringAPI> call, Response<ResultStringAPI> response) {
                    ResultStringAPI resultStringAPI = response.body();
                    if (resultStringAPI.getCode()==0) {
                        Log.d("Register Result", "Successful");
                        Toast.makeText(mMainActivity, "Đăng ký tài khoản thành công", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().popBackStack();
                        mMainActivity.resetViewPagerUI(4);
                    } else {
                        Log.d("Register Result", "Failed: " + resultStringAPI.getMessage());
                        Toast.makeText(mMainActivity, "Đăng ký tài khoản thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResultStringAPI> call, Throwable t) {
                    call.cancel();
                    Log.d("Register Result", "Failed: " + t);
                    Toast.makeText(mMainActivity, "Có lỗi xảy ra, vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Ẩn bàn phím sau khi nhập dữ liệu
    public void hideSoftKeyboard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }
}