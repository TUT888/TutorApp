package com.example.studentapp.extra_fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.studentapp.MainActivity;
import com.example.studentapp.R;
import com.example.studentapp.api.LoadImageInternet;
import com.example.studentapp.app_interface.IClickTutorBtnListener;
import com.example.studentapp.model.Tutor;
import com.google.android.material.button.MaterialButton;

import de.hdodenhof.circleimageview.CircleImageView;


public class TutorDetailFragment extends Fragment {

    private View mView;
    private CircleImageView civAvatar;
    private TextView tvName, tvRole, tvEmail, tvSDT, tvGioiTinh, tvLinhVuc, tvKhuVuc, tvHocVan, tvTruong, tvDate;
    private MaterialButton mbContact, mbRating;
    private ImageButton ibBack;
    private Tutor tutor;
    private String previousFragment;
    private IClickTutorBtnListener iClickTutorBtnListener;


    public TutorDetailFragment(IClickTutorBtnListener iClickTutorBtnListener) {
        this.iClickTutorBtnListener = iClickTutorBtnListener;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_tutor_detail, container, false);
        civAvatar = mView.findViewById(R.id.civAvatar);
        tvName = mView.findViewById(R.id.tvName);
        tvRole = mView.findViewById(R.id.tvRole);
        tvEmail = mView.findViewById(R.id.tvEmail);
        tvSDT = mView.findViewById(R.id.tvSDT);
        tvDate = mView.findViewById(R.id.tvBirthday);
        tvGioiTinh = mView.findViewById(R.id.tvGioiTinh);
        tvLinhVuc = mView.findViewById(R.id.tvLinhVuc);
        tvKhuVuc = mView.findViewById(R.id.tvKhuVuc);
        tvHocVan = mView.findViewById(R.id.tvHocVan);
        tvTruong = mView.findViewById(R.id.tvTruong);
        mbContact = mView.findViewById(R.id.mbContact);
        ibBack = mView.findViewById(R.id.ibBack);
        mbRating = mView.findViewById(R.id.mbRating);

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        Bundle bundle = getArguments();
        if (bundle != null){
            tutor = (Tutor) bundle.getSerializable("tutor");
            previousFragment = bundle.getString("previous", "");
        }

        mbContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + tutor.getPhoneNumber()));
                startActivity(intent);
            }
        });

        if (tutor != null){
            tvName.setText(tutor.getName());
            tvDate.setText(tutor.getBirthday());
            tvEmail.setText(tutor.getEmail());
            tvSDT.setText(tutor.getPhoneNumber());
            new LoadImageInternet(civAvatar).execute(MainActivity.URL_IMAGE +  tutor.getAvatar());
            if (tutor.getGender() == 0){
                tvGioiTinh.setText("Nam");
            }else {
                tvGioiTinh.setText("Nữ");
            }
            tvLinhVuc.setText(tutor.getFields());
            tvKhuVuc.setText(tutor.getAreas());
            tvHocVan.setText(tutor.getAcademicLevel());
            tvTruong.setText(tutor.getSchool());
            mbRating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iClickTutorBtnListener.openAllRatingsFragment(tutor.getPhoneNumber());
                }
            });
        }

        return mView;
    }


}