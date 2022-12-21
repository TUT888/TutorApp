package com.example.studentapp.fragment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentapp.MainActivity;
import com.example.studentapp.R;
import com.example.studentapp.adapter.PendingClassAdapter;
import com.example.studentapp.adapter.SearchViewPagerAdapter;
import com.example.studentapp.api.APIService;
import com.example.studentapp.api.ResultAPI;
import com.example.studentapp.api.ResultStringAPI;
import com.example.studentapp.app_interface.IClickPendingClassListener;
import com.example.studentapp.extra_fragment.LoginFragment;
import com.example.studentapp.model.ClassObject;
import com.example.studentapp.model.Tutor;
import com.example.studentapp.model.User;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class PendingClassFragment extends Fragment {

    private RecyclerView rcvPendingClass;
    private ArrayList<ClassObject> pendingClassArrayList;
    private PendingClassAdapter pendingClassAdapter;
    private TextView tvLoginRequest;


    private MainActivity mMainActivity;
    private User currentUser;

    public PendingClassFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainActivity = (MainActivity) getActivity();
        currentUser = mMainActivity.getCurrentLoginUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View mView = inflater.inflate(R.layout.fragment_pending_class, container, false);
        rcvPendingClass = mView.findViewById(R.id.rcvPendingClass);
        tvLoginRequest = mView.findViewById(R.id.tvLoginRequest);
        if (currentUser == null){
            tvLoginRequest.setVisibility(View.VISIBLE);
            rcvPendingClass.setVisibility(View.GONE);
        }
        else{
            rcvPendingClass.setVisibility(View.VISIBLE);
            tvLoginRequest.setVisibility(View.GONE);

            initClass();
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            rcvPendingClass.setLayoutManager(linearLayoutManager);

            pendingClassAdapter = new PendingClassAdapter(new IClickPendingClassListener() {
                @Override
                public void onClickAcceptPendingClass(ClassObject classObject) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Bạn chắc chắn muốn tham gia lớp học này không?")
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Map<String,String> body = new HashMap<String, String>();
                                    body.put("id",classObject.getId());
                                    body.put("status",String.valueOf(ClassObject.CLASS_STATUS_AVAILABLE));
                                    APIService.apiService.updateStatus(body).enqueue(new retrofit2.Callback<ResultStringAPI>() {
                                        @Override
                                        public void onResponse(retrofit2.Call<ResultStringAPI> call, retrofit2.Response<ResultStringAPI> response) {
                                            ResultStringAPI resultAPI = response.body();
                                            if(resultAPI.getCode() == 0){
                                                pendingClassAdapter.remove(classObject);
                                                pendingClassAdapter.notifyDataSetChanged();
                                                Toast.makeText(getContext(), "Đã tham gia lớp học", Toast.LENGTH_SHORT).show();

                                            }
                                        }

                                        @Override
                                        public void onFailure(retrofit2.Call<ResultStringAPI> call, Throwable t) {
                                            Toast.makeText(getContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                                            Log.d("onFailure 2", "onFailure: " + t.getMessage());
                                        }
                                    });
                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    builder.show();
                }


                @Override
                public void onClickCancelPendingClass(ClassObject classObject) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Bạn chắc chắn muốn từ chối tham gia lớp học này không?")
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Map<String,String> body = new HashMap<String, String>();
                                    body.put("id",classObject.getId());
                                    body.put("status",String.valueOf(ClassObject.CLASS_STATUS_REJECTED));
                                    APIService.apiService.updateStatus(body).enqueue(new retrofit2.Callback<ResultStringAPI>() {
                                        @Override
                                        public void onResponse(retrofit2.Call<ResultStringAPI> call, retrofit2.Response<ResultStringAPI> response) {
                                            ResultStringAPI resultAPI = response.body();
                                            if(resultAPI.getCode() == 0){
                                                Toast.makeText(getContext(), "Đã từ chối tham gia lớp học", Toast.LENGTH_SHORT).show();
                                                pendingClassAdapter.remove(classObject);
                                                pendingClassAdapter.notifyDataSetChanged();
                                            }
                                        }

                                        @Override
                                        public void onFailure(retrofit2.Call<ResultStringAPI> call, Throwable t) {
                                            Toast.makeText(getContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                                            Log.d("onFailure 1", "onFailure: " + t.getMessage());
                                        }
                                    });
                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    builder.show();
                }
            });
            pendingClassAdapter.setData(pendingClassArrayList);
            rcvPendingClass.setAdapter(pendingClassAdapter);
        }


        return mView;

    }

    private void initClass() {
        pendingClassArrayList = new ArrayList<>();
        Log.d("currentUser", "initClass: " + currentUser.getPhoneNumber());
        APIService.apiService.getPendingClass(currentUser.getPhoneNumber()).enqueue(new retrofit2.Callback<ResultAPI>() {
            @Override
            public void onResponse(retrofit2.Call<ResultAPI> call, retrofit2.Response<ResultAPI> response) {
                ResultAPI resultAPI = response.body();
                Log.d("resultAPI", "onResponse: " + resultAPI);
                if(response.isSuccessful() && resultAPI != null){
                    if (resultAPI.getCode() == 0){
                        for (int i = 0; i < resultAPI.getData().getAsJsonArray().size(); i++){
                            JsonObject jsonObject = resultAPI.getData().getAsJsonArray().get(i).getAsJsonObject();
                            ClassObject classObject = new ClassObject();
                            classObject.setId(jsonObject.get("id").getAsString());
                            classObject.setClassName(jsonObject.get("className").getAsString());
                            classObject.setSubject(jsonObject.get("subject").getAsString());
                            classObject.setTutorPhone(jsonObject.get("tutorPhone").getAsString());
                            classObject.setStudentPhone(jsonObject.get("studentPhone").getAsString());
                            classObject.setPlace(jsonObject.get("place").getAsString());
                            classObject.setStatus(jsonObject.get("status").getAsInt());
                            classObject.setStartDate(jsonObject.get("startDate").getAsString());
                            classObject.setEndDate(jsonObject.get("endDate").getAsString());
                            classObject.setFee(jsonObject.get("fee").getAsInt());
                            classObject.setDateTime(jsonObject.get("dateTime").getAsString());
                            classObject.setMethod(jsonObject.get("method").getAsString());
                            classObject.setField(jsonObject.get("field").getAsString());
                            pendingClassArrayList.add(classObject);
                        }
                        pendingClassAdapter.setData(pendingClassArrayList);
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

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Pending Class Fragment", "On Resume: Refresh & Get Data Again");
        initClass();
        pendingClassAdapter.notifyDataSetChanged();
    }
}