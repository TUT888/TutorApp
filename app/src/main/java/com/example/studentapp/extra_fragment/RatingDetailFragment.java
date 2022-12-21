package com.example.studentapp.extra_fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.studentapp.MainActivity;
import com.example.studentapp.R;
import com.example.studentapp.api.APIService;
import com.example.studentapp.api.ResultAPI;
import com.example.studentapp.api.ResultObjectAPI;
import com.example.studentapp.fragment.MyPostFragment;
import com.example.studentapp.model.ClassObject;
import com.example.studentapp.model.Post;
import com.example.studentapp.model.Rate;
import com.google.android.material.button.MaterialButton;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingDetailFragment extends Fragment {
    // Resources
    private ImageButton ibBack;
    private TextView ratedClassName, ratedTutorName, ratedCmt, ratedDate;
    private RatingBar ratedBar;
    // Class
    private Rate rate;
    private MainActivity mainActivity;

    public RatingDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rating_detail, container, false);

        // Bind View
        ratedClassName = view.findViewById(R.id.ratedClassName);
        ratedTutorName = view.findViewById(R.id.ratedClassTutor);
        ratedBar = view.findViewById(R.id.ratedBar);
        ratedCmt = view.findViewById(R.id.ratedCmt);
        ratedDate = view.findViewById(R.id.ratedDate);
        ibBack = view.findViewById(R.id.ibBack);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        // Get Bundle
        Bundle bundle = getArguments();
        if (bundle != null){
            rate = (Rate) bundle.getSerializable("rate");
        }
        mainActivity = (MainActivity) getActivity();
        getClassInfo(rate.getClassId());
        ratedBar.setRating(rate.getRate());
        ratedCmt.setText(rate.getComment());
        ratedDate.setText(rate.getDate());
        return view;
    }

    private void getClassInfo (String classId) {
        APIService.apiService.getClassById(classId).enqueue(new Callback<ResultObjectAPI>() {
            @Override
            public void onResponse(Call<ResultObjectAPI> call, Response<ResultObjectAPI> response) {
                ResultObjectAPI resultAPI = response.body();
                if(response.isSuccessful() && resultAPI != null){
                    if (resultAPI.getCode() == 0){
                        JsonObject jsonObject = resultAPI.getData().getAsJsonObject();
                        String tutorPhone = jsonObject.get("tutorPhone").getAsString();
                        String className = jsonObject.get("className").getAsString();
                        ratedClassName.setText(className);
                        getTutorName (tutorPhone);
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

    private void getTutorName (String tutorPhone) {
        APIService.apiService.getUser(tutorPhone).enqueue(new Callback<ResultObjectAPI>() {
            @Override
            public void onResponse(Call<ResultObjectAPI> call, Response<ResultObjectAPI> response) {
                ResultObjectAPI resultAPI = response.body();
                if(response.isSuccessful() && resultAPI != null){
                    if (resultAPI.getCode() == 0){
                        JsonObject jsonObject = resultAPI.getData().getAsJsonObject();
                        String tutorName = jsonObject.get("name").getAsString();
                        ratedTutorName.setText(tutorName);
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
}
