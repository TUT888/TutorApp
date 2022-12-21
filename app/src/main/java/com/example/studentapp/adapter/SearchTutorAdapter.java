package com.example.studentapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentapp.MainActivity;
import com.example.studentapp.R;
import com.example.studentapp.api.LoadImageInternet;
import com.example.studentapp.app_interface.IClickPostObjectListener;
import com.example.studentapp.app_interface.IClickTutorObjectListener;
import com.example.studentapp.model.Tutor;
import java.util.ArrayList;
public class SearchTutorAdapter extends RecyclerView.Adapter<SearchTutorAdapter.SearchTutorViewHolder> {

    private ArrayList<Tutor> tutorList;
    private IClickTutorObjectListener mListener;

    public void setData(ArrayList<Tutor> tutorList){
        this.tutorList = tutorList;
        notifyDataSetChanged();
    }

    public void remove(Tutor tutor){
        tutorList.remove(tutor);
        notifyDataSetChanged();
    }

    public SearchTutorAdapter(ArrayList<Tutor> tutorList, IClickTutorObjectListener mListener) {
        this.tutorList = tutorList;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public SearchTutorAdapter.SearchTutorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tutor_object,parent,false);
        return new SearchTutorViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull SearchTutorAdapter.SearchTutorViewHolder holder, int position) {
        Tutor tutor = tutorList.get(position);
        if (tutor == null){
            return;
        }
        holder.tvName.setText(tutor.getName());
        holder.tvRole.setText("Gia sư");
        if (tutor.getGender() == 0){
            holder.tvGioiTinh.setText("Nam");
        }else{
            holder.tvGioiTinh.setText("Nữ");
        }
        new LoadImageInternet(holder.imgAvatar).execute(MainActivity.URL_IMAGE +  tutor.getAvatar());
        holder.tvKhuVuc.setText(tutor.getAreas());
        holder.tvChuyenMon.setText(tutor.getFields());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickTutorObject(tutor);
            }
        });
        holder.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClickBtnHideTutor(tutor);
            }
        });
    }
    @Override
    public int getItemCount() {
        if (tutorList != null){
            return tutorList.size();
        }
        return 0;
    }
    public class SearchTutorViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvRole, tvGioiTinh, tvKhuVuc, tvChuyenMon;
        private ImageView imgAvatar, btnClose;
        public SearchTutorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvRole = itemView.findViewById(R.id.tvRole);
            tvGioiTinh = itemView.findViewById(R.id.tvGioiTinh);
            tvKhuVuc = itemView.findViewById(R.id.tvKhuVuc);
            tvChuyenMon = itemView.findViewById(R.id.tvChuyenMon);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            btnClose = itemView.findViewById(R.id.btnClose);
        }
    }
}