package com.example.tutorapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorapp.MainActivity;
import com.example.tutorapp.R;
import com.example.tutorapp.api.LoadImageInternet;
import com.example.tutorapp.app_interface.IClickStudentObjectListener;
import com.example.tutorapp.model.Student;

import java.util.ArrayList;
public class SearchStudentAdapter extends RecyclerView.Adapter<SearchStudentAdapter.SearchStudentViewHolder> {

    private ArrayList<Student> studentList;
    private IClickStudentObjectListener mListener;

    public void setData(ArrayList<Student> studentList){
        this.studentList = studentList;
        notifyDataSetChanged();
    }

    public void remove(Student tutor){
        studentList.remove(tutor);
        notifyDataSetChanged();
    }

    public SearchStudentAdapter(ArrayList<Student> tutorList, IClickStudentObjectListener mListener) {
        this.studentList = tutorList;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public SearchStudentAdapter.SearchStudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_object,parent,false);
        return new SearchStudentViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull SearchStudentAdapter.SearchStudentViewHolder holder, int position) {
        Student student = studentList.get(position);
        if (student == null){
            return;
        }
        holder.tvName.setText(student.getName());
        holder.tvRole.setText("Học viên");
        if (student.getGender() == 0){
            holder.tvGioiTinh.setText("Nam");
        }else{
            holder.tvGioiTinh.setText("Nữ");
        }
        new LoadImageInternet(holder.imgAvatar).execute(MainActivity.URL_IMAGE +  student.getAvatar());
        holder.tvKhuVuc.setText(student.getAddress());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickStudentObject(student);
            }
        });
        holder.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClickBtnHideStudent(student);
            }
        });
    }
    @Override
    public int getItemCount() {
        if (studentList != null){
            return studentList.size();
        }
        return 0;
    }
    public class SearchStudentViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvRole, tvGioiTinh, tvKhuVuc;
        private ImageView imgAvatar, btnClose;
        public SearchStudentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvRole = itemView.findViewById(R.id.tvRole);
            tvGioiTinh = itemView.findViewById(R.id.tvGioiTinh);
            tvKhuVuc = itemView.findViewById(R.id.tvKhuVuc);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            btnClose = itemView.findViewById(R.id.btnClose);
        }
    }
}