package com.example.tutorapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorapp.MainActivity;
import com.example.tutorapp.R;
import com.example.tutorapp.app_interface.IClickPendingClassListener;
import com.example.tutorapp.model.ClassObject;
import com.example.tutorapp.model.User;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class PendingClassAdapter extends RecyclerView.Adapter<PendingClassAdapter.PendingClassViewHolder> {

    private ArrayList<ClassObject> classes;
    private ArrayList<Integer> roles;
    private ArrayList<String> names;
    private IClickPendingClassListener mIClickPendingClassListener;
    private MainActivity mMainActivity;
    private User currentUser;

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

    public void setRoles(ArrayList<Integer> roles) {
        this.roles = roles;
        notifyDataSetChanged();
    }

    public void setNames(ArrayList<String> names) {
        this.names = names;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PendingClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_class_object, parent, false);
        mMainActivity = (MainActivity) view.getContext();
        currentUser = mMainActivity.getCurrentLoginUser();
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
        holder.classTutor.setText(names.get(position));
        holder.classFee.setText(classObject.getFee()+ "");
        holder.classStartDate.setText(classObject.getStartDate());
        holder.classEndDate.setText(classObject.getEndDate());
        holder.classTime.setText(classObject.getDateTime());
        holder.classPlace.setText(classObject.getPlace());
        holder.classField.setText(classObject.getField());
        holder.classSubject.setText(classObject.getSubject());
        holder.classMethod.setText(classObject.getMethod());


        if(roles.get(position) == 0){
            holder.llButton.setVisibility(View.GONE);
            holder.tvMessage.setVisibility(View.VISIBLE);
        }else{
            holder.tvMessage.setVisibility(View.GONE);
            holder.llButton.setVisibility(View.VISIBLE);
        }

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
        private TextView tvMessage, className, classTutor, classStartDate, classEndDate, classSubject, classField, classPlace, classFee, classTime, classMethod;
        private MaterialButton btnAccept, btnReject;
        private LinearLayout llButton;

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
            tvMessage = itemView.findViewById(R.id.tvMessage);
            llButton = itemView.findViewById(R.id.llButton);
        }
    }
}
