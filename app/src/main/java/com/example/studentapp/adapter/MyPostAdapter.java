package com.example.studentapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentapp.MainActivity;
import com.example.studentapp.R;
import com.example.studentapp.api.LoadImageInternet;
import com.example.studentapp.app_interface.IClickPostObjectListener;
import com.example.studentapp.model.Post;

import java.util.ArrayList;
import java.util.List;

public class MyPostAdapter extends RecyclerView.Adapter<MyPostAdapter.PostViewHolder> {
    private List<Post> mPostList;
    private IClickPostObjectListener mIClickPostObjectListener;

    public MyPostAdapter(List<Post> listPosts, IClickPostObjectListener listener) {
        mPostList = listPosts;
        mIClickPostObjectListener = listener;
    }

    public void setData(List<Post> listPosts) {
        this.mPostList = listPosts;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View bookView = inflater.inflate(R.layout.post_object, parent, false);
        PostViewHolder viewHolder = new PostViewHolder(bookView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = (Post) mPostList.get(position);
        if (post == null) {
            return;
        }
        holder.btnAnBaiDang.setVisibility(View.GONE);
        holder.layoutPostOption.setVisibility(View.VISIBLE);
        new LoadImageInternet(holder.imgAvatar).execute(MainActivity.URL_IMAGE +  MainActivity.CURRENT_LOGIN_AVATAR);

        holder.tvName.setText(MainActivity.CURRENT_LOGIN_NAME);

        holder.tvRole.setText(MainActivity.CURRENT_LOGIN_ROLE);
        holder.tvTitle.setText(post.getTitle());
        holder.tvMonHoc.setText(post.getSubject());
        holder.tvKhuVuc.setText(String.join(", ", post.getLearningPlaces()));
        holder.tvNgayHoc.setText(String.join(", ", post.getDateTimesLearning()));
        holder.tvHinhThuc.setText(post.getMethod());
        holder.tvHocPhi.setText(String.valueOf(post.getTuition()));
        holder.tvNgayDang.setText(post.getPostDate());
        switch (post.getStatus()) {
            case ( Post.POST_STATUS_CREATED_CLASS ):
                holder.tvStatus.setText("Đã tạo lớp");
                holder.tvStatus.setBackgroundColor(ContextCompat.getColor(holder.tvStatus.getContext(), R.color.post_created_class));
                break;
            case ( Post.POST_STATUS_EDITED ):
                holder.tvStatus.setText("Đã chỉnh sửa");
                holder.tvStatus.setBackgroundColor(ContextCompat.getColor(holder.tvStatus.getContext(), R.color.post_edited));
                break;
            case ( Post.POST_STATUS_CANCELLED ):
                holder.tvStatus.setText("Đã hủy");
                holder.tvStatus.setBackgroundColor(ContextCompat.getColor(holder.tvStatus.getContext(), R.color.post_cancelled));
                break;
            default:
                holder.tvStatus.setText("Đang đợi");
                holder.tvStatus.setBackgroundColor(ContextCompat.getColor(holder.tvStatus.getContext(), R.color.post_waiting));
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Adapter", "clicked");
                mIClickPostObjectListener.onClickPostObject(post);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mPostList==null) {
            return 0;
        }
        return mPostList.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutPostOption;
        ImageView imgAvatar;
        TextView tvName, tvRole, tvTitle,
                tvMonHoc, tvKhuVuc, tvNgayHoc,
                tvHinhThuc, tvHocPhi, tvNgayDang,
                tvStatus;
        ImageView btnAnBaiDang;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            layoutPostOption = itemView.findViewById(R.id.layoutPostOption);

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
            tvStatus = itemView.findViewById(R.id.tvStatus);

            btnAnBaiDang = itemView.findViewById(R.id.btnAnBaiDang);
        }
    }
}
