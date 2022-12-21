package com.example.studentapp.search;

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

import com.example.studentapp.MainActivity;
import com.example.studentapp.R;
import com.example.studentapp.adapter.SearchTutorAdapter;
import com.example.studentapp.api.APIService;
import com.example.studentapp.api.ResultAPI;
import com.example.studentapp.api.ResultObjectAPI;
import com.example.studentapp.app_interface.IClickTutorObjectListener;
import com.example.studentapp.fragment.MyPostFragment;
import com.example.studentapp.model.Post;
import com.example.studentapp.model.Tutor;
import com.google.gson.JsonObject;

import java.util.ArrayList;


public class SearchTutorFragment extends Fragment {

    private View mView;
    private MainActivity mMainActivity;
    private RecyclerView rcvSearchTutor;
    private SearchView svSearchTutor;
    private SearchTutorAdapter searchTutorAdapter;
    private ArrayList<Tutor> searchTutorArrayList;

    public SearchTutorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_search_tutor, container, false);
        mMainActivity = (MainActivity) getActivity();
        rcvSearchTutor = mView.findViewById(R.id.rcvSearchTutor);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcvSearchTutor.setLayoutManager(linearLayoutManager);
        svSearchTutor = mView.findViewById(R.id.svSearchTutor);
        initTutor();
        searchTutorAdapter = new SearchTutorAdapter(searchTutorArrayList, new IClickTutorObjectListener() {
            @Override
            public void onClickTutorObject(Tutor tutor) {
                mMainActivity.goToTutorDetailFragment(tutor, SearchTutorFragment.class.getSimpleName());

            }

            @Override
            public void onClickBtnHideTutor(Tutor tutor) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Bạn có muốn ẩn người dùng này không?")
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                searchTutorAdapter.remove(tutor);
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
        rcvSearchTutor.setAdapter(searchTutorAdapter);

        svSearchTutor.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

    private void initTutor() {
        filterList("");
    }

    private void filterList(String newText) {
        searchTutorArrayList = new ArrayList<>();
        Log.d("newText", "filterList: " + newText);
        APIService.apiService.getSearchTutor(newText).enqueue(new retrofit2.Callback<ResultAPI>() {
            @Override
            public void onResponse(retrofit2.Call<ResultAPI> call, retrofit2.Response<ResultAPI> response) {
                ResultAPI resultAPI = response.body();
                Log.d("resultAPI", "onResponse: " + resultAPI);
                if(response.isSuccessful() && resultAPI != null){
                    if (resultAPI.getCode() == 0){
                        for (int i = 0; i < resultAPI.getData().getAsJsonArray().size(); i++){
                            JsonObject jsonObject = resultAPI.getData().getAsJsonArray().get(i).getAsJsonObject();
                            Tutor tutor = new Tutor();
                            tutor.setPhoneNumber(jsonObject.get("id").getAsString());
                            tutor.setName(jsonObject.get("name").getAsString());
                            tutor.setAreas(jsonObject.get("areas").getAsString());
                            tutor.setGender(jsonObject.get("gender").getAsInt());
                            tutor.setBirthday(jsonObject.get("birthday").getAsString());
                            tutor.setEmail(jsonObject.get("email").getAsString());
                            tutor.setFields(jsonObject.get("fields").getAsString());
                            tutor.setSchool(jsonObject.get("school").getAsString());
                            tutor.setAcademicLevel(jsonObject.get("academicLevel").getAsString());
                            tutor.setAddress(jsonObject.get("area").getAsString());
                            tutor.setAvatar(jsonObject.get("avatar").getAsString());
                            tutor.setPassword(jsonObject.get("password").getAsString());

                            searchTutorArrayList.add(tutor);
                        }
                        searchTutorAdapter.setData(searchTutorArrayList);
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