package com.example.tutorapp.extra_fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tutorapp.MainActivity;
import com.example.tutorapp.R;
import com.example.tutorapp.api.APIService;
import com.example.tutorapp.api.LoadImageInternet;
import com.example.tutorapp.api.ResultStringAPI;
import com.example.tutorapp.fragment.MyPostFragment;
import com.example.tutorapp.model.ClassObject;
import com.example.tutorapp.model.Post;
import com.example.tutorapp.model.User;
import com.example.tutorapp.search.SearchPostFragment;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostDetailFragment extends Fragment {
    // Resources
    private MainActivity mMainActivity;
    private LinearLayout layoutPostOption;
    private View mView;
    private ImageButton ibBack, ibPostOption;
    private MaterialButton mbContact;
    private TextView tvStatus, tvTitle, tvName, tvRole, tvField, tvDateTime, tvTuition, tvMethod, tvArea, tvDesc, tvSubject;
    private ImageView imgAvatar;
    // Object Class
    private User currentUser;
    private Post post;
    private String previousFragment; //MyPostFragment, SearchPostFragment,...

    public PostDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_post_detail, container, false);
        mMainActivity = (MainActivity) getActivity();
        currentUser = mMainActivity.getCurrentLoginUser();

        // Bind View
        layoutPostOption = mView.findViewById(R.id.layoutPostOption);
        mbContact = mView.findViewById(R.id.mbContact);
        ibBack = mView.findViewById(R.id.ibBack);
        ibPostOption = mView.findViewById(R.id.ibPostOption);
        tvStatus = mView.findViewById(R.id.tvStatus);

        mbContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + post.getIdUser()));
                startActivity(intent);
            }
        });

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // Get Bundle
        // Ki???m tra xem chuy???n t??? Fragment n??o ?????n ????y
        // N???u l?? t??? Fragment MyPost: ???n n??t "???n b??i ????ng", hi???n n??t option menu cho ph??p s???a/x??a/????ng l???i
        // N???u l?? t??? Fragment kh??c: M???C ?????NH ng?????c l???i v???i c??i ??? tr??n
        Bundle bundle = getArguments();
        if (bundle != null){
            post = (Post) bundle.getSerializable("post");
            previousFragment = bundle.getString("previous", "");
            bindAndFillDetail();
            // T??? MyPostFragment
            if (previousFragment.equals(MyPostFragment.class.getSimpleName())) {
                bindUserData(currentUser.getName(), MainActivity.CURRENT_LOGIN_ROLE, currentUser.getAvatar());
                mbContact.setVisibility(View.GONE);
                layoutPostOption.setVisibility(View.VISIBLE);

                switch (post.getStatus()) {
                    case ( Post.POST_STATUS_CREATED_CLASS ):
                        tvStatus.setText("???? t???o l???p");
                        tvStatus.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.post_created_class));
                        break;
                    case ( Post.POST_STATUS_EDITED ):
                        tvStatus.setText("???? ch???nh s???a");
                        tvStatus.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.post_edited));
                        break;
                    case ( Post.POST_STATUS_CANCELLED ):
                        tvStatus.setText("???? h???y");
                        tvStatus.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.post_cancelled));
                        break;
                    default:
                        tvStatus.setText("??ang ?????i");
                        tvStatus.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.post_waiting));
                        break;
                }
            }
            if (previousFragment.equals(SearchPostFragment.class.getSimpleName())) {
                String name = bundle.getString("name", "");
                String role = bundle.getString("role", "");
                String avatar = bundle.getString("avatar", "");
                bindUserData(name, role, avatar);
            }
        }

        ibPostOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu pm = new PopupMenu(view.getContext(), view);
                pm.getMenuInflater().inflate(R.menu.mypost_popup_menu, pm.getMenu());
                if (post.getStatus()==Post.POST_STATUS_CREATED_CLASS) {
                    pm.getMenu().removeItem(R.id.delete_post);
                    pm.getMenu().removeItem(R.id.edit_post);
                    pm.getMenu().removeItem(R.id.create_class);
                }
                if (post.getStatus()==Post.POST_STATUS_CANCELLED) {
                    pm.getMenu().removeItem(R.id.delete_post);
                    pm.getMenu().removeItem(R.id.edit_post);
                    pm.getMenu().removeItem(R.id.create_class);
                }
                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.create_class:
                                confirmCreateClassFromPost();
                                return true;
                            case R.id.edit_post:
                                if (post.getStatus()!=Post.POST_STATUS_CREATED_CLASS) {
                                    mMainActivity.goToAddNewPostFragment(post, "update");
                                } else {
                                    Toast.makeText(view.getContext(), "B??i vi???t ???? t???o l???p, kh??ng th??? s???a", Toast.LENGTH_SHORT).show();
                                }
                                return true;
                            case R.id.repost:
                                mMainActivity.goToAddNewPostFragment(post, "add");
                                return true;
                            case R.id.delete_post:
                                if (post.getStatus()!=Post.POST_STATUS_CREATED_CLASS) {
                                    confirmDeletePost();
                                } else {
                                    Toast.makeText(view.getContext(), "B??i vi???t ???? t???o l???p, kh??ng th??? x??a", Toast.LENGTH_SHORT).show();
                                }
                                return true;
                            default:
                                return true;
                        }
                    }
                });
                pm.show();
            }
        });

        return mView;
    }

    private void bindAndFillDetail() {
        if (post==null) {
            return;
        }
        tvTitle = mView.findViewById(R.id.tvTitle);
        tvField = mView.findViewById(R.id.tvField);
        tvDateTime = mView.findViewById(R.id.tvDateTime);
        tvTuition = mView.findViewById(R.id.tvTuition);
        tvMethod = mView.findViewById(R.id.tvRole);
        tvArea = mView.findViewById(R.id.tvArea);
        tvDesc = mView.findViewById(R.id.tvDesc);
        tvSubject = mView.findViewById(R.id.tvSubject);

        tvTitle.setText(post.getTitle());
        tvSubject.setText(post.getSubject());
        tvField.setText(post.getField());
        tvDateTime.setText(post.getDateTimesLearning());
        tvTuition.setText(String.valueOf(post.getTuition()));
        tvMethod.setText(post.getMethod());
        tvArea.setText(post.getLearningPlaces());
        tvDesc.setText(post.getDescription());
    }

    private void bindUserData(String userName, String userRole, String avatar) {
        tvName = mView.findViewById(R.id.tvName);
        tvRole = mView.findViewById(R.id.tvRole);
        imgAvatar = mView.findViewById(R.id.imgAvatar);

        tvName.setText(userName);
        tvRole.setText(userRole);
        new LoadImageInternet(imgAvatar).execute(MainActivity.URL_IMAGE +  avatar);
    }

    private void confirmCreateClassFromPost() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
        alertBuilder.setTitle("X??c nh???n t???o l???p h???c");
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.layout_create_class_diaglog, null);
        alertBuilder.setView(dialogView);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText etUserPhone = dialogView.findViewById(R.id.etUserPhone);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText etStartDate = dialogView.findViewById(R.id.etStartDate);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText etEndDate = dialogView.findViewById(R.id.etEndDate);

        alertBuilder.setPositiveButton("X??c nh???n", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // T???o l???p
                String studentPhone = etUserPhone.getText().toString();
                String startDate = etStartDate.getText().toString();
                String endDate = etEndDate.getText().toString();
                if (studentPhone.equals("") || startDate.equals("") || endDate.equals("")) {
                    Toast.makeText(getContext(), "Th??ng tin kh??ng h???p l???", Toast.LENGTH_SHORT).show();
                } else {
                    /* Call API:
                    - Select * from where exist this user
                    - If exists, change the status of post & class
                     */
                    createClassFromPost(studentPhone, startDate, endDate);
                }
            }
        });
        alertBuilder.setNegativeButton("H???y", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    public void createClassFromPost(String studentPhone, String startDate, String endDate) {
        ClassObject classObject = new ClassObject(
                post.getId(), post.getSubject(), currentUser.getPhoneNumber(),
                studentPhone, post.getLearningPlaces(),
                ClassObject.CLASS_STATUS_PENDING, post.getTuition(),
                post.getDateTimesLearning(), startDate, endDate,
                post.getMethod(), post.getSubject(), post.getField()
        );

        Call<ResultStringAPI> apiCall =  APIService.apiService.addNewClass(classObject);
        apiCall.enqueue(new Callback<ResultStringAPI>() {
            @Override
            public void onResponse(Call<ResultStringAPI> call, Response<ResultStringAPI> response) {
                ResultStringAPI resultStringAPI = response.body();
                if (response.isSuccessful() || resultStringAPI!=null) {
                    if (resultStringAPI.getCode()==0) {
                        post.setStatus(Post.POST_STATUS_CREATED_CLASS);
                        tvStatus.setText("???? t???o l???p");
                        tvStatus.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.post_created_class));

                        Toast.makeText(getContext(), "???? t???o l???p, ?????i ?????i ph????ng ch???p nh???n", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().popBackStack();
                        mMainActivity.resetViewPagerUI(2);
                    } else {
                        Log.d("Add Class Result", "Failed: " + resultStringAPI.getMessage());
                        Toast.makeText(mMainActivity, "T???o l???p th???t b???i", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultStringAPI> call, Throwable t) {
                call.cancel();
                Log.d("Add Result", "Failed: " + t);
                Toast.makeText(mMainActivity, "C?? l???i x???y ra, vui l??ng th??? l???i sau!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void confirmDeletePost() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
        alertBuilder.setTitle("X??c nh???n h???y b??i vi???t");
        alertBuilder.setMessage("B???n c?? ch???c mu???n h???y b??i vi???t n??y?");

        alertBuilder.setPositiveButton("C??", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deletePost();
            }
        });
        alertBuilder.setNegativeButton("Kh??ng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    private void deletePost() {
        Call<ResultStringAPI> apiCall = APIService.apiService.removeMyPost(post.getId());
        apiCall.enqueue(new Callback<ResultStringAPI>() {
            @Override
            public void onResponse(Call<ResultStringAPI> call, Response<ResultStringAPI> response) {
                ResultStringAPI resultStringAPI = response.body();
                if (response.isSuccessful() || resultStringAPI!=null) {
                    if (resultStringAPI.getCode()==0) {
                        post.setStatus(Post.POST_STATUS_CANCELLED);
                        tvStatus.setText("???? h???y");
                        tvStatus.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.post_cancelled));

                        Toast.makeText(getContext(), "H???y th??nh c??ng", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().popBackStack();
                        mMainActivity.resetViewPagerUI(2);
                    } else {
                        Toast.makeText(mMainActivity, "H???y th???t b???i", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultStringAPI> call, Throwable t) {
                call.cancel();
                Log.d("Remove Post Result", "Failed: " + t);
            }
        });
    }
}