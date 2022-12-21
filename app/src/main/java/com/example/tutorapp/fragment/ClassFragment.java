package com.example.tutorapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.tutorapp.MainActivity;
import com.example.tutorapp.R;
import com.example.tutorapp.adapter.ClassAdapter;
import com.example.tutorapp.api.APIService;
import com.example.tutorapp.api.ResultAPI;
import com.example.tutorapp.app_interface.IClickBtnRatingListener;
import com.example.tutorapp.model.ClassObject;
import com.example.tutorapp.model.Rate;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class ClassFragment extends Fragment {

    RecyclerView rvClasses;
    ClassAdapter classAdapter;
    MainActivity mainActivity;
    int adapterPosition = -1;
    ImageButton ibBack;
    boolean check = false;

    public ClassFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = (View) inflater.inflate(R.layout.fragment_class, container, false);

        rvClasses = view.findViewById(R.id.rvClasses);
        ibBack = view.findViewById(R.id.ibBack);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        rvClasses.setLayoutManager(linearLayoutManager);
        mainActivity = (MainActivity) getActivity();
        classAdapter = new ClassAdapter(new IClickBtnRatingListener() {
            @Override
            public void rateClass(ClassObject classObject, int adapterPosition) {
                mainActivity.goToRateFragment(classObject, adapterPosition);
            }

            @Override
            public void seeRateDetail(Rate rate) {
                mainActivity.goToRateDetailFragment(rate);
            }
        });
        Bundle bundle = getArguments();
        getData(mainActivity.getCurrentLoginUser().getPhoneNumber(), bundle);
        rvClasses.setAdapter(classAdapter);
        return view;
    }

    private void getData(String studentPhone, Bundle bundle){
        List<ClassObject> classObjects = new ArrayList<>();
        APIService.apiService.getClasses(studentPhone).enqueue(new retrofit2.Callback<ResultAPI>() {
            @Override
            public void onResponse(retrofit2.Call<ResultAPI> call, retrofit2.Response<ResultAPI> response) {
                ResultAPI resultAPI = response.body();
                if(response.isSuccessful() && resultAPI != null){
                    if (resultAPI.getCode() == 0){
                        JsonArray jsonArray = resultAPI.getData().getAsJsonArray();
                        for (int i = 0; i < jsonArray.size(); i++){
                            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                            ClassObject classObject = new ClassObject(jsonObject.get("id").getAsString(),
                                    jsonObject.get("className").getAsString(), jsonObject.get("tutorPhone").getAsString(),
                                    jsonObject.get("studentPhone").getAsString(), jsonObject.get("place").getAsString(),
                                    jsonObject.get("status").getAsInt(), jsonObject.get("fee").getAsInt(),
                                    jsonObject.get("dateTime").getAsString(), jsonObject.get("startDate").getAsString(),
                                    jsonObject.get("endDate").getAsString(), jsonObject.get("method").getAsString(),
                                    jsonObject.get("subject").getAsString(), jsonObject.get("field").getAsString());
                            classObjects.add(classObject);
                        }
                        classAdapter.setData(classObjects);
                        if (bundle != null) {
                            ClassObject classObject = (ClassObject) bundle.getSerializable("class");
                            if (classObjects != null && classObject != null) {
                                for (ClassObject classObject1 : classObjects) {
                                    if (classObject1.equalsTo(classObject)) {
                                        adapterPosition = classObjects.indexOf(classObject1);
                                    }
                                }
                            }
                            if (adapterPosition != -1) {
                                rvClasses.scrollToPosition(adapterPosition);
                            }
                        }
                        mainActivity.getSupportFragmentManager().setFragmentResultListener("getAdapterPosition", getViewLifecycleOwner(),
                                new FragmentResultListener() {
                                    @Override
                                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                                        adapterPosition = (int) result.getInt("adapter_position");
                                        if (adapterPosition != -1) {
                                            rvClasses.scrollToPosition(adapterPosition);
                                            classAdapter.changeClassStatus(adapterPosition);
                                        }
                                    }
                                });
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResultAPI> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                Log.d("onFailure", "onFailure: " + t.getMessage());
            }
        });
    }
}