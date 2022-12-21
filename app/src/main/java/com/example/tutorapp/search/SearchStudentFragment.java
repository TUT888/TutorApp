package com.example.tutorapp.search;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tutorapp.MainActivity;
import com.example.tutorapp.R;
import com.example.tutorapp.adapter.SearchStudentAdapter;
import com.example.tutorapp.api.APIService;
import com.example.tutorapp.api.ResultAPI;
import com.example.tutorapp.app_interface.IClickStudentObjectListener;
import com.example.tutorapp.model.Student;
import com.google.gson.JsonObject;

import java.util.ArrayList;


public class SearchStudentFragment extends Fragment {

    private View mView;
    private MainActivity mMainActivity;
    private RecyclerView rcvSearchStudent;
    private SearchView svSearchStudent;
    private SearchStudentAdapter searchStudentAdapter;
    private ArrayList<Student> searchStudentArrayList;

    public SearchStudentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_search_student, container, false);
        mMainActivity = (MainActivity) getActivity();
        rcvSearchStudent = mView.findViewById(R.id.rcvSearchStudent);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcvSearchStudent.setLayoutManager(linearLayoutManager);
        svSearchStudent = mView.findViewById(R.id.svSearchStudent);
        initStudent();
        searchStudentAdapter = new SearchStudentAdapter(searchStudentArrayList, new IClickStudentObjectListener() {
            @Override
            public void onClickStudentObject(Student student) {
                mMainActivity.goToStudentDetailFragment(student, SearchStudentFragment.class.getSimpleName());

            }

            @Override
            public void onClickBtnHideStudent(Student student) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Bạn có muốn ẩn người dùng này không?")
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                searchStudentAdapter.remove(student);
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
        rcvSearchStudent.setAdapter(searchStudentAdapter);

        svSearchStudent.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
        return mView;
    }

    private void initStudent() {
        filterList("");
    }

    private void filterList(String newText) {
        searchStudentArrayList = new ArrayList<>();
        Log.d("newText", "filterList: " + newText);
        APIService.apiService.getSearchStudent(newText).enqueue(new retrofit2.Callback<ResultAPI>() {
            @Override
            public void onResponse(retrofit2.Call<ResultAPI> call, retrofit2.Response<ResultAPI> response) {
                ResultAPI resultAPI = response.body();
                Log.d("resultAPI", "onResponse: " + resultAPI);
                if(response.isSuccessful() && resultAPI != null){
                    if (resultAPI.getCode() == 0){
                        for (int i = 0; i < resultAPI.getData().getAsJsonArray().size(); i++){
                            JsonObject jsonObject = resultAPI.getData().getAsJsonArray().get(i).getAsJsonObject();
                            Student student = new Student();
                            student.setPhoneNumber(jsonObject.get("id").getAsString());
                            student.setName(jsonObject.get("name").getAsString());
                            student.setGender(jsonObject.get("gender").getAsInt());
                            student.setBirthday(jsonObject.get("birthday").getAsString());
                            student.setEmail(jsonObject.get("email").getAsString());
                            student.setFields(jsonObject.get("fields").getAsString());
                            student.setAddress(jsonObject.get("area").getAsString());
                            student.setAvatar(jsonObject.get("avatar").getAsString());
                            student.setPassword(jsonObject.get("password").getAsString());

                            searchStudentArrayList.add(student);
                        }
                        searchStudentAdapter.setData(searchStudentArrayList);
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