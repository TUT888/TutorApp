package com.example.studentapp.extra_fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentapp.MainActivity;
import com.example.studentapp.R;
import com.example.studentapp.api.APIService;
import com.example.studentapp.api.ResultObjectAPI;
import com.example.studentapp.app_interface.IClickBtnSaveRatingListener;
import com.example.studentapp.model.ClassObject;
import com.example.studentapp.model.Rate;
import com.google.android.material.button.MaterialButton;
import com.google.gson.JsonObject;

import java.time.LocalDate;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RateFragment extends Fragment {

    ClassObject classObject;
    TextView rtClassName, rtTutor;
    RatingBar ratingBar;
    EditText rtCmt;
    MaterialButton btnSaveRate;
    MainActivity mainActivity;
    IClickBtnSaveRatingListener iClickBtnSaveRatingListener;
    ImageButton ibBack;

    public RateFragment(IClickBtnSaveRatingListener iClickBtnSaveRatingListener) {
        this.iClickBtnSaveRatingListener = iClickBtnSaveRatingListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rate, container, false);
        rtClassName = view.findViewById(R.id.rtClass);
        rtTutor = view.findViewById(R.id.rtTutor);
        ratingBar = view.findViewById(R.id.ratingBar);
        rtCmt = view.findViewById(R.id.rtCmt);
        btnSaveRate = view.findViewById(R.id.btnSaveRate);
        ibBack = view.findViewById(R.id.ibBack);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        mainActivity = (MainActivity) getActivity();

        Bundle bundle = getArguments();
        if (bundle != null){
            classObject = (ClassObject) bundle.getSerializable("class_object");
        }
        rtClassName.setText(classObject.getClassName());
        getTutorName(classObject.getTutorPhone());
        btnSaveRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean check = validate();
                if (check == true) {
                    if (rtCmt.getText() == null) {
                        rtCmt.setText("");
                    }
                    iClickBtnSaveRatingListener.saveAndReturnToClassFragment(new Rate(classObject.getId(),
                            ratingBar.getRating(), rtCmt.getText().toString(), LocalDate.now().toString()));
                }
            }
        });
        return view;
    }

    void getTutorName (String tutorPhone) {
        APIService.apiService.getUser(tutorPhone).enqueue(new Callback<ResultObjectAPI>() {
            @Override
            public void onResponse(Call<ResultObjectAPI> call, Response<ResultObjectAPI> response) {
                ResultObjectAPI resultAPI = response.body();
                if(response.isSuccessful() && resultAPI != null){
                    if (resultAPI.getCode() == 0){
                        JsonObject jsonObject = resultAPI.getData().getAsJsonObject();
                        String tutorName = jsonObject.get("name").getAsString();
                        rtTutor.setText(tutorName);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultObjectAPI> call, Throwable t) {
                Toast.makeText(mainActivity, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                Log.d("onFailure", "onFailure: " + t.getMessage());
            }
        });
    }

    private boolean validate() {
        if (ratingBar.getRating() < 0.5) {
            Toast.makeText(mainActivity, "Please rate at least one star", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}