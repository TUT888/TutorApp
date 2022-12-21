package com.example.studentapp.fragment;

import android.os.Bundle;

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
import com.example.studentapp.adapter.MyPostAdapter;
import com.example.studentapp.api.APIService;
import com.example.studentapp.api.ResultAPI;
import com.example.studentapp.api.ResultObjectAPI;
import com.example.studentapp.app_interface.IClickPostObjectListener;
import com.example.studentapp.model.Field;
import com.example.studentapp.model.Post;
import com.example.studentapp.model.User;
import com.google.android.material.button.MaterialButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPostFragment extends Fragment {
    private View mView;
    private MainActivity mMainActivity;

    private RecyclerView rcvMyPosts;
    private ArrayList<Post> myPostArrayList;
    private MyPostAdapter myPostAdapter;

    private MaterialButton btnAddNewPost;
    private User currentUser;

    public MyPostFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_post, container, false);
        mMainActivity = (MainActivity) getActivity();
        currentUser = mMainActivity.getCurrentLoginUser();

        rcvMyPosts = mView.findViewById(R.id.rcvMyPosts);
        rcvMyPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        //Create empty ArrayList & setup adapter
        myPostArrayList = initPost();
        myPostAdapter = new MyPostAdapter(myPostArrayList, new IClickPostObjectListener() {
            @Override
            public void onClickPostObject(Post post) {
                Log.d("Fragment", "set on clicked");
                mMainActivity.goToPostDetailFragment(post, MyPostFragment.class.getSimpleName());
            }

            @Override
            public void onClickBtnHidePost(Post post) {

            }

            @Override
            public void onClickPostObjectSearch(Post post, String userName, String userRole, String userAvatar) {

            }
        });
        rcvMyPosts.setAdapter(myPostAdapter);
        if (currentUser!=null) {
            getPosts(currentUser.getPhoneNumber());
        }

        //Get data from database
        //getBooksFromDatabase(getBooksUrl);

        btnAddNewPost = mView.findViewById(R.id.btnAddNewPost);
        btnAddNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainActivity.goToAddNewPostFragment();
            }
        });

        return mView;
    }

    public void getPosts(String phone) {
        myPostArrayList = new ArrayList<>();

        Call<ResultAPI> apiCall = APIService.apiService.getMyPosts(currentUser.getPhoneNumber());
        apiCall.enqueue(new Callback<ResultAPI>() {
            @Override
            public void onResponse(Call<ResultAPI> call, Response<ResultAPI> response) {
                ResultAPI resultAPI = response.body();
                if (response.isSuccessful() && resultAPI != null) {
                    if (resultAPI.getCode()==0) {
                        JsonArray jsonArray = resultAPI.getData();
                        for (int i= 0; i < jsonArray.size(); i++) {
                            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                            String hideFormOpt = "";
                            if (jsonObject.get("hideForm")!=null) {
                                hideFormOpt = jsonObject.get("hideForm").getAsString();
                            }
                            myPostArrayList.add(new Post(
                                    jsonObject.get("id").getAsString(),
                                    jsonObject.get("title").getAsString(),
                                    jsonObject.get("status").getAsInt(),
                                    jsonObject.get("idUser").getAsString(),
                                    jsonObject.get("subject").getAsString(),
                                    jsonObject.get("field").getAsString(),
                                    jsonObject.get("dateTimesLearning").getAsString(),
                                    jsonObject.get("learningPlaces").getAsString(),
                                    jsonObject.get("method").getAsString(),
                                    jsonObject.get("tuition").getAsInt(),
                                    jsonObject.get("description").getAsString(),
                                    jsonObject.get("postDate").getAsString(),
                                    hideFormOpt
                            ));
                        }
                        myPostAdapter.notifyDataSetChanged();
                    } else {
                        Log.d("Load Post Result", "Failed!" + resultAPI.getMessage());
                    }
                    myPostAdapter.setData(myPostArrayList);
                    myPostAdapter.notifyDataSetChanged();
                } else {
                    Log.d("Response Error", response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<ResultAPI> call, Throwable t) {
                call.cancel();
                Log.d("Load Post Result", "Failed: " + t);
            }
        });
    }

    public ArrayList<Post> initPost() {
        ArrayList<Post> arrayList = new ArrayList<>();

        return arrayList;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("My Post Fragment", "On Resume: Refresh & Get Data Again");
        myPostArrayList = initPost();
        getPosts(currentUser.getPhoneNumber());
    }
}