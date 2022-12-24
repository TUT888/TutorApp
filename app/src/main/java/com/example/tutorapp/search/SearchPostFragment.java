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
import com.example.tutorapp.adapter.SearchPostAdapter;
import com.example.tutorapp.api.APIService;
import com.example.tutorapp.api.ResultObjectAPI;
import com.example.tutorapp.api.ResultStringAPI;
import com.example.tutorapp.app_interface.IClickPostObjectListener;
import com.example.tutorapp.model.Post;
import com.example.tutorapp.model.User;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchPostFragment extends Fragment {
    private View mView;
    private MainActivity mMainActivity;

    private RecyclerView rcvSearchPost;
    private ArrayList<Post> searchPostArrayList;
    private SearchPostAdapter searchPostAdapter;
    private SearchView svSearchPost;
    private ArrayList<Post> filteredList;
    private ArrayList<String> nameList;
    private ArrayList<String> avatarList;
    private User loginUser;

    public SearchPostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_search_post, container, false);
        mMainActivity = (MainActivity) getActivity();
        loginUser = mMainActivity.getCurrentLoginUser();
        rcvSearchPost = mView.findViewById(R.id.rcvSearchPost);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcvSearchPost.setLayoutManager(linearLayoutManager);
        initPost();
        searchPostAdapter = new SearchPostAdapter(searchPostArrayList, new IClickPostObjectListener() {
            @Override
            public void onClickPostObject(Post post) {
                mMainActivity.goToPostDetailFragment(post, SearchPostFragment.class.getSimpleName());
            }

            @Override
            public void onClickBtnHidePost(Post post) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Bạn có muốn ẩn bài đăng này không?")
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (loginUser == null){
                                    searchPostAdapter.remove(post);
                                }else{
                                    Map<String,String> body = new HashMap<String, String>();
                                    body.put("id",post.getId());
                                    body.put("id_user",loginUser.getPhoneNumber());
                                    APIService.apiService.updateHideFrom(body).enqueue(new Callback<ResultStringAPI>() {
                                        @Override
                                        public void onResponse(Call<ResultStringAPI> call, Response<ResultStringAPI> response) {
                                            ResultStringAPI resultAPI = response.body();
                                            Log.d("resultAPI", "onResponse: "+ resultAPI);
                                            if(resultAPI.getCode() == 0){
                                                searchPostAdapter.remove(post);
                                                searchPostAdapter.notifyDataSetChanged();
                                                Toast.makeText(getContext(), "Đã ẩn bài đăng này", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResultStringAPI> call, Throwable t) {
                                            Toast.makeText(getContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                                            Log.d("onFailure 2", "onFailure: " + t.getMessage());
                                        }
                                    });
                                    searchPostAdapter.remove(post);
                                }
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
            public void onClickPostObjectSearch(Post post, String userName, String userRole, String userAvatar) {
                // mMainActivity.goToPostDetailFragment(post, name, avatar, SearchPostFragment.class.getSimpleName());
                mMainActivity.goToPostDetailFragment(post, SearchPostFragment.class.getSimpleName(), userName, userRole, userAvatar);
            }

        });
        rcvSearchPost.setAdapter(searchPostAdapter);
        svSearchPost = mView.findViewById(R.id.svSearchPost);
        svSearchPost.clearFocus();

        svSearchPost.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

    public void initPost() {
        filterList("");
    }

    public void filterList(String text){
        filteredList = new ArrayList<>();
        nameList = new ArrayList<>();
        avatarList = new ArrayList<>();
        String phone = "";
        if (loginUser != null) {
            phone = loginUser.getPhoneNumber();
        }
        APIService.apiService.getSearchPost(text, phone, 0).enqueue(new retrofit2.Callback<ResultObjectAPI>() {
            @Override
            public void onResponse(retrofit2.Call<ResultObjectAPI> call, retrofit2.Response<ResultObjectAPI> response) {
                ResultObjectAPI resultAPI = response.body();
                if(response.isSuccessful() && resultAPI != null){
                    if (resultAPI.getCode() == 0){
                        for (int i = 0; i < resultAPI.getData().get("post").getAsJsonArray().size(); i++){
                            JsonObject jsonObject = resultAPI.getData().get("post").getAsJsonArray().get(i).getAsJsonObject();
                            String name = resultAPI.getData().get("name").getAsJsonArray().get(i).getAsString();
                            String avatar = resultAPI.getData().get("avatar").getAsJsonArray().get(i).getAsString();
                            Post post = new Post(jsonObject.get("id").getAsString(),
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
                                    jsonObject.get("hideFrom").getAsString());
                            filteredList.add(post);
                            nameList.add(name);
                            avatarList.add(avatar);
                        }
                        searchPostAdapter.setData(filteredList);
                        searchPostAdapter.setNames(nameList);
                        searchPostAdapter.setAvatars(avatarList);
                    }
                }

            }

            @Override
            public void onFailure(retrofit2.Call<ResultObjectAPI> call, Throwable t) {
                Toast.makeText(mMainActivity, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                Log.d("onFailure", "onFailure: " + t.getMessage());
            }
        });
    }

}