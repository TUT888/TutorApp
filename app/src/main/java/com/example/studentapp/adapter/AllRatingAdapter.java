package com.example.studentapp.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentapp.MainActivity;
import com.example.studentapp.R;
import com.example.studentapp.api.APIService;
import com.example.studentapp.api.ResultObjectAPI;
import com.example.studentapp.model.Rate;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllRatingAdapter extends RecyclerView.Adapter<AllRatingAdapter.RatingViewHolder>{

    private List<Rate> ratings;
    MainActivity mainActivity;

    public AllRatingAdapter (List<Rate> ratings) {
        this.ratings = ratings;
        notifyDataSetChanged();
    }

    public void addData (Rate rate) {
        this.ratings.add(rate);
        notifyItemInserted(ratings.size());
    }

    @NonNull
    @Override
    public RatingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.classes_rating_object, parent, false);
        return new RatingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingViewHolder holder, int position) {
        Rate rating = ratings.get(position);
        if (ratings == null) {
            return;
        }
        this.mainActivity = (MainActivity) holder.rateClassName.getContext();
        getClassInfo(rating.getClassId(), holder);
        holder.rateCmt.setText(rating.getComment());
        holder.rateDate.setText(rating.getDate());
        holder.rateStarNum.setText("" + rating.getRate());
        holder.ratedBar.setRating(rating.getRate());
    }

    @Override
    public int getItemCount() {
        if (ratings != null) {
            return ratings.size();
        }
        return 0;
    }

    public class RatingViewHolder extends RecyclerView.ViewHolder {
        private TextView rateClassName, rateTutor, rateStarNum, rateCmt, rateDate;
        private RatingBar ratedBar;

        public RatingViewHolder(@NonNull View itemView) {
            super(itemView);

            rateClassName = itemView.findViewById(R.id.rateClassName);
            rateTutor = itemView.findViewById(R.id.rateTutor);
            rateStarNum = itemView.findViewById(R.id.rateStarNum);
            rateCmt = itemView.findViewById(R.id.rateCmt);
            rateDate = itemView.findViewById(R.id.rateDate);
            ratedBar = itemView.findViewById(R.id.ratedBar);
        }
    }

    void getClassInfo (String classId, RatingViewHolder holder) {
        APIService.apiService.getClassById(classId).enqueue(new Callback<ResultObjectAPI>() {
            @Override
            public void onResponse(Call<ResultObjectAPI> call, Response<ResultObjectAPI> response) {
                ResultObjectAPI resultAPI = response.body();
                if(response.isSuccessful() && resultAPI != null){
                    if (resultAPI.getCode() == 0){
                        JsonObject jsonObject = resultAPI.getData().getAsJsonObject();
                        String className = jsonObject.get("className").getAsString();
                        String tutorPhone = jsonObject.get("tutorPhone").getAsString();
                        holder.rateClassName.setText(className);
                        getTutorName (tutorPhone, holder);
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

    void getTutorName (String tutorPhone, RatingViewHolder holder) {
        APIService.apiService.getUser(tutorPhone).enqueue(new Callback<ResultObjectAPI>() {
            @Override
            public void onResponse(Call<ResultObjectAPI> call, Response<ResultObjectAPI> response) {
                ResultObjectAPI resultAPI = response.body();
                if (response.isSuccessful() && resultAPI != null) {
                    if (resultAPI.getCode() == 0) {
                        JsonObject jsonObject = resultAPI.getData().getAsJsonObject();
                        String tutorName = jsonObject.get("name").getAsString();
                        holder.rateTutor.setText(tutorName);
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