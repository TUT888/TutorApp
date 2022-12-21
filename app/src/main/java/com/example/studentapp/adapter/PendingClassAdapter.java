package com.example.studentapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentapp.R;
import com.example.studentapp.app_interface.IClickPendingClassListener;
import com.example.studentapp.model.ClassObject;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class PendingClassAdapter extends RecyclerView.Adapter<PendingClassAdapter.PendingClassViewHolder> {

    private ArrayList<ClassObject> classes;
    private IClickPendingClassListener mIClickPendingClassListener;

    public PendingClassAdapter(IClickPendingClassListener mIClickPendingClassListener) {
        this.mIClickPendingClassListener  = mIClickPendingClassListener;
    }

    public void setData(ArrayList<ClassObject> list) {
        this.classes = list;
        notifyDataSetChanged();
    }

    public void remove(ClassObject classObject){
        classes.remove(classObject);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PendingClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_class_object, parent, false);
        return new PendingClassViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull PendingClassViewHolder holder, int position) {
        ClassObject classObject = classes.get(position);
        if (classObject == null) {
            return;
        }
        //holder.imgAvatar.setImageResource(post.getUser().getAvatar());
        holder.className.setText(classObject.getClassName());
        holder.classTutor.setText(getTutorName(classObject.getTutorPhone()));
        holder.classFee.setText(classObject.getFee() + "");
        holder.classStartDate.setText(classObject.getStartDate());
        holder.classEndDate.setText(classObject.getEndDate());
        holder.classTime.setText(classObject.getDateTime());
        holder.classPlace.setText(classObject.getPlace());
        holder.classField.setText(classObject.getField());
        holder.classSubject.setText(classObject.getSubject());
        holder.classMethod.setText(classObject.getMethod());

        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIClickPendingClassListener.onClickAcceptPendingClass(classObject);
            }
        });

        holder.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIClickPendingClassListener.onClickCancelPendingClass(classObject);
            }
        });


    }

    private String getTutorName(String tutorPhone) {
        return "Nguyễn Văn A";
    }

    @Override
    public int getItemCount() {
        if (classes != null) {
            return classes.size();
        }
        return 0;
    }

    public class PendingClassViewHolder extends RecyclerView.ViewHolder {
        private TextView className, classTutor, classStartDate, classEndDate, classSubject, classField, classPlace, classFee, classTime, classMethod;
        private MaterialButton btnAccept, btnReject;

        public PendingClassViewHolder(@NonNull View itemView) {
            super(itemView);
            className = itemView.findViewById(R.id.className);
            classTutor = itemView.findViewById(R.id.classTutor);
            classStartDate = itemView.findViewById(R.id.classStartDate);
            classEndDate = itemView.findViewById(R.id.classEndDate);
            classSubject = itemView.findViewById(R.id.classSubject);
            classField = itemView.findViewById(R.id.classField);
            classPlace = itemView.findViewById(R.id.classPlace);
            classFee = itemView.findViewById(R.id.classFee);
            classTime = itemView.findViewById(R.id.classTime);
            classMethod = itemView.findViewById(R.id.classMethod);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }
}
