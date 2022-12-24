package com.example.tutorapp.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tutorapp.MainActivity;
import com.example.tutorapp.R;
import com.example.tutorapp.api.APIService;
import com.example.tutorapp.api.ResultAPI;
import com.example.tutorapp.app_interface.IClickTimeTableObjectListener;
import com.example.tutorapp.model.ClassObject;
import com.example.tutorapp.model.User;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private List<ClassObject> classes = new ArrayList<>();
    MainActivity mainActivity;
    TextView txtViewHome;
    LinearLayout linearLayoutHome;
    TableLayout thu2, thu3, thu4, thu5, thu6, thu7, cn;
    IClickTimeTableObjectListener iClickTimeTableObjectListener;
    User u;

    public HomeFragment(IClickTimeTableObjectListener iClickTimeTableObjectListener) {
        this.iClickTimeTableObjectListener = iClickTimeTableObjectListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        u = mainActivity.getCurrentLoginUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        txtViewHome = view.findViewById(R.id.txtViewHome);
        linearLayoutHome = view.findViewById(R.id.linearLayoutHome);
        thu2 = view.findViewById(R.id.thu2);
        thu3 = view.findViewById(R.id.thu3);
        thu4 = view.findViewById(R.id.thu4);
        thu5 = view.findViewById(R.id.thu5);
        thu6 = view.findViewById(R.id.thu6);
        thu7 = view.findViewById(R.id.thu7);
        cn = view.findViewById(R.id.cn);
        if (u != null) {
            String userId = u.getPhoneNumber();
            getData(userId);
        }
        else {
            txtViewHome.setText("Bạn cần đăng nhập để sử dụng chức năng");
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Home Fragment", "On Resume: Refresh & Get Data Again");
        if (u != null) {
            Log.d("viewCount", thu2.getChildCount()+"");
            if (thu2.getChildCount() > 1) {
                Log.d("viewCount", thu2.getChildCount()+"");
                thu2.removeViews(1, thu2.getChildCount() - 1);
            }
            if (thu3.getChildCount() > 1) {
                thu3.removeViews(1, thu3.getChildCount() - 1);
            }
            if (thu4.getChildCount() > 1) {
                thu4.removeViews(1, thu4.getChildCount() - 1);
            }
            if (thu5.getChildCount() > 1) {
                thu5.removeViews(1, thu5.getChildCount() - 1);
            }
            if (thu6.getChildCount() > 1) {
                thu6.removeViews(1, thu6.getChildCount() - 1);
            }
            if (thu7.getChildCount() > 1) {
                thu7.removeViews(1, thu7.getChildCount() - 1);
            }
            if (cn.getChildCount() > 1) {
                cn.removeViews(1, cn.getChildCount() - 1);
            }
            String userId = u.getPhoneNumber();
            getData(userId);
        }
        else {
            txtViewHome.setText("Bạn cần đăng nhập để sử dụng chức năng");
        }
    }

    void getData (String userId) {
        APIService.apiService.getActiveClassFromTutor(userId).enqueue(new Callback<ResultAPI>() {
            @Override
            public void onResponse(Call<ResultAPI> call, Response<ResultAPI> response) {
                ResultAPI resultAPI = response.body();
                if(response.isSuccessful() && resultAPI != null){
                    if (resultAPI.getCode() == 0){
                        JsonArray jsonArray = resultAPI.getData().getAsJsonArray();
                        for (int i = 0; i < jsonArray.size(); i++){
                            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                            ClassObject classOb = new ClassObject();
                            classOb.setId(jsonObject.get("id").getAsString());
                            classOb.setClassName(jsonObject.get("className").getAsString());
                            classOb.setDateTime(jsonObject.get("dateTime").getAsString());
                            classes.add(classOb);
                        }
                        if (classes.size() != 0) {
                            linearLayoutHome.setVisibility(View.VISIBLE);
                            txtViewHome.setVisibility(View.GONE);
                            for (ClassObject classObject : classes) {
                                String classTime = classObject.getDateTime();
                                if (classTime.contains (", ")) {
                                    String[] classDate = classTime.split(", ");
                                    for (String dateTime : classDate) {
                                        String date = dateTime.split(": ")[0];
                                        String time = dateTime.split(": ")[1];
                                        if (date.contains("Thứ 2")) {
                                            checkDateTime(time, classObject, thu2);
                                        }
                                        if (date.contains("Thứ 3")) {
                                            checkDateTime(time, classObject, thu3);
                                        }
                                        if (date.contains("Thứ 4")) {
                                            checkDateTime(time, classObject, thu4);
                                        }
                                        if (date.contains("Thứ 5")) {
                                            checkDateTime(time, classObject, thu5);
                                        }
                                        if (date.contains("Thứ 6")) {
                                            checkDateTime(time, classObject, thu6);
                                        }
                                        if (date.contains("Thứ 7")) {
                                            checkDateTime(time, classObject, thu7);
                                        }
                                        if (date.contains("Chủ nhật")) {
                                            checkDateTime(time, classObject, cn);
                                        }
                                    }
                                }
                                else {
                                    String date = classTime.split(": ")[0];
                                    String time = classTime.split(": ")[1];
                                    if (date.contains("Thứ 2")) {
                                        checkDateTime(time, classObject, thu2);
                                    }
                                    if (date.contains("Thứ 3")) {
                                        checkDateTime(time, classObject, thu3);
                                    }
                                    if (date.contains("Thứ 4")) {
                                        checkDateTime(time, classObject, thu4);
                                    }
                                    if (date.contains("Thứ 5")) {
                                        checkDateTime(time, classObject, thu5);
                                    }
                                    if (date.contains("Thứ 6")) {
                                        checkDateTime(time, classObject, thu6);
                                    }
                                    if (date.contains("Thứ 7")) {
                                        checkDateTime(time, classObject, thu7);
                                    }
                                    if (date.contains("Chủ nhật")) {
                                        checkDateTime(time, classObject, cn);
                                    }
                                }
                            }
                        }
                        else {
                            linearLayoutHome.setVisibility(View.GONE);
                            txtViewHome.setVisibility(View.VISIBLE);
                            thu2.setVisibility(View.GONE);
                            thu3.setVisibility(View.GONE);
                            thu4.setVisibility(View.GONE);
                            thu5.setVisibility(View.GONE);
                            thu6.setVisibility(View.GONE);
                            thu7.setVisibility(View.GONE);
                            cn.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultAPI> call, Throwable t) {
                Toast.makeText(mainActivity, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                Log.d("onFailure", "onFailure: " + t.getMessage());
            }
        });
    }

    void checkDateTime (String time, ClassObject classObject, TableLayout tableLayout) {
        tableLayout.setVisibility(View.VISIBLE);
        int childNum = tableLayout.getChildCount();
        if (childNum == 1) {
            addSchedule(childNum, time, classObject, tableLayout);
        } else {
            boolean check = false;
            for (int i = 1; i < childNum; i++) {
                Log.d("childNum: ", ""+i);
                TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
                TextView textView = (TextView) tableRow.getChildAt(0);
                String txt = textView.getText().toString();
                Log.d("txtView: ", txt);
                if (txt.contains(time)) {
                    check = true;
                }
            }
            if (check == false) {
                addSchedule(childNum, time, classObject, tableLayout);
            }
        }
    }

    public void addSchedule (int childNum, String time, ClassObject classObject, TableLayout tableLayout) {
        TableRow tbr = new TableRow(mainActivity);
        TextView tg = new TextView(mainActivity);
        tg.setLayoutParams(new TableRow.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 4.0f));
        tg.setText(time);
        tg.setTextSize(20);
        tg.setGravity(Gravity.CENTER);
        tg.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tg.setTextColor(ContextCompat.getColor(mainActivity, R.color.text_color));
        tg.setBackgroundDrawable(ContextCompat.getDrawable(mainActivity, R.drawable.bg_time_table));
        tbr.addView(tg);
        TextView tg2 = new TextView(mainActivity);
        tg2.setLayoutParams(new TableRow.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 8.0f));
        tg2.setTextSize(20);
        tg2.setGravity(Gravity.CENTER);
        tg2.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
        tg2.setText(classObject.getClassName());
        tg2.setTextColor(ContextCompat.getColor(mainActivity, R.color.text_color));
        tg2.setBackgroundDrawable(ContextCompat.getDrawable(mainActivity, R.drawable.bg_time_table));
        tbr.addView(tg2);
        tbr.setVisibility(View.VISIBLE);
        tbr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickTimeTableObjectListener.switchToClassFragment(classObject);
            }
        });
        tableLayout.addView(tbr, childNum);
    }
}