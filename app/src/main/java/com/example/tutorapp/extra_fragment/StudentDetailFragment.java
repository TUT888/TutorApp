package com.example.tutorapp.extra_fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.tutorapp.MainActivity;
import com.example.tutorapp.R;
import com.example.tutorapp.api.LoadImageInternet;
import com.example.tutorapp.app_interface.IClickTutorBtnListener;
import com.example.tutorapp.model.Student;
import com.google.android.material.button.MaterialButton;

import de.hdodenhof.circleimageview.CircleImageView;


public class StudentDetailFragment extends Fragment {

    private View mView;
    private CircleImageView civAvatar;
    private TextView tvName, tvEmail, tvSDT, tvGioiTinh, tvLinhVuc, tvKhuVuc, tvDate;
    private MaterialButton mbContact;
    private ImageButton ibBack;
    private Student student;
    private String previousFragment;
    private IClickTutorBtnListener iClickStudentBtnListener;


    public StudentDetailFragment(IClickTutorBtnListener iClickStudentBtnListener) {
        this.iClickStudentBtnListener = iClickStudentBtnListener;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_student_detail, container, false);
        civAvatar = mView.findViewById(R.id.civAvatar);
        tvName = mView.findViewById(R.id.tvName);
        tvEmail = mView.findViewById(R.id.tvEmail);
        tvSDT = mView.findViewById(R.id.tvSDT);
        tvDate = mView.findViewById(R.id.tvBirthday);
        tvGioiTinh = mView.findViewById(R.id.tvGioiTinh);
        tvLinhVuc = mView.findViewById(R.id.tvLinhVuc);
        tvKhuVuc = mView.findViewById(R.id.tvKhuVuc);
        mbContact = mView.findViewById(R.id.mbContact);
        ibBack = mView.findViewById(R.id.ibBack);

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        Bundle bundle = getArguments();
        if (bundle != null){
            student = (Student) bundle.getSerializable("student");
            previousFragment = bundle.getString("previous", "");
        }

        mbContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + student.getPhoneNumber()));
                startActivity(intent);
            }
        });

        if (student != null){
            tvName.setText(student.getName());
            tvDate.setText(student.getBirthday());
            tvEmail.setText(student.getEmail());
            tvSDT.setText(student.getPhoneNumber());
            new LoadImageInternet(civAvatar).execute(MainActivity.URL_IMAGE +  student.getAvatar());
            if (student.getGender() == 0){
                tvGioiTinh.setText("Nam");
            }else {
                tvGioiTinh.setText("Nữ");
            }
            tvLinhVuc.setText(student.getFields());
            tvKhuVuc.setText(student.getAddress());
        }

        return mView;
    }


}