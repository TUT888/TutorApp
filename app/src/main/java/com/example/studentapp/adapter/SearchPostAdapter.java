package com.example.studentapp.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentapp.MainActivity;
import com.example.studentapp.R;
import com.example.studentapp.api.LoadImageInternet;
import com.example.studentapp.app_interface.IClickPostObjectListener;
import com.example.studentapp.model.Post;

import java.util.ArrayList;

public class SearchPostAdapter extends RecyclerView.Adapter<SearchPostAdapter.SearchPostViewHolder> {

    private ArrayList<Post> posts;
    private ArrayList<String> names;
    private ArrayList<String> avatars;

    private IClickPostObjectListener mIClickPostObjectListener;

    public void setData(ArrayList<Post> list) {
        this.posts = list;
        notifyDataSetChanged();
    }

    public void setNames(ArrayList<String> names) {
        this.names = names;
        notifyDataSetChanged();
    }

    public void setAvatars(ArrayList<String> avatars) {
        this.avatars = avatars;
        notifyDataSetChanged();
    }

    public void remove(Post post){
        posts.remove(post);
        notifyDataSetChanged();
    }

    public SearchPostAdapter(ArrayList<Post> postsList, IClickPostObjectListener iClickPostObjectListener) {
        posts = postsList;
        mIClickPostObjectListener = iClickPostObjectListener;
    }



    @NonNull
    @Override
    public SearchPostAdapter.SearchPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_object, parent, false);
        return new SearchPostViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull SearchPostAdapter.SearchPostViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Post post = posts.get(position);
        if (post == null) {
            return;
        }
        new LoadImageInternet(holder.imgAvatar).execute(MainActivity.URL_IMAGE +  avatars.get(position));

        //holder.imgAvatar.setImageResource(post.getUser().getAvatar());
        holder.tvName.setText(names.get(position));

        holder.tvRole.setText("Gia sư");
        holder.tvTitle.setText(post.getTitle());
        holder.tvMonHoc.setText(post.getSubject());
        holder.tvKhuVuc.setText(String.join(", ", post.getLearningPlaces()));

        holder.tvNgayHoc.setText(post.getDateTimesLearning());


        holder.tvHinhThuc.setText(post.getMethod());
        holder.tvHocPhi.setText(String.valueOf(post.getTuition()));
        holder.tvNgayDang.setText(post.getPostDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIClickPostObjectListener.onClickPostObjectSearch(post, names.get(position), holder.tvRole.getText().toString(), avatars.get(position));
            }
        });
        holder.btnAnBaiDang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIClickPostObjectListener.onClickBtnHidePost(post);
            }
        });
    }


    @Override
    public int getItemCount() {
        if(posts != null){
            return posts.size();
        }
        return 0;
    }

    public class SearchPostViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar;
        TextView tvName, tvRole, tvTitle,
                tvMonHoc, tvKhuVuc, tvNgayHoc,
                tvHinhThuc, tvHocPhi, tvNgayDang;
        ImageButton btnAnBaiDang;
        public SearchPostViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            tvName = itemView.findViewById(R.id.tvName);
            tvRole = itemView.findViewById(R.id.tvRole);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvMonHoc = itemView.findViewById(R.id.tvMonHoc);
            tvKhuVuc = itemView.findViewById(R.id.tvKhuVuc);
            tvNgayHoc = itemView.findViewById(R.id.tvNgayHoc);
            tvHinhThuc = itemView.findViewById(R.id.tvHinhThuc);
            tvHocPhi = itemView.findViewById(R.id.tvHocPhi);
            tvNgayDang = itemView.findViewById(R.id.tvNgayDang);
            btnAnBaiDang = itemView.findViewById(R.id.btnAnBaiDang);
        }
    }
}
