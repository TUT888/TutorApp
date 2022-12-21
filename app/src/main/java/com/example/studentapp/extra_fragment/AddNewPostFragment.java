package com.example.studentapp.extra_fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.studentapp.MainActivity;
import com.example.studentapp.R;
import com.example.studentapp.api.APIService;
import com.example.studentapp.api.ResultObjectAPI;
import com.example.studentapp.api.ResultStringAPI;
import com.example.studentapp.fragment.MyPostFragment;
import com.example.studentapp.model.Post;
import com.example.studentapp.model.User;
import com.google.android.material.button.MaterialButton;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewPostFragment extends Fragment {
    // Resources
    private MainActivity mMainActivity;
    private View mView;
    private ImageButton ibBack;
    private MaterialButton btnPost;

    private EditText etTitle, etField, etSubject, etPlace, etTuition, etDescription;
    private RadioGroup rgMethod;
    private RadioButton rbOnline, rbOffline;
    private CheckBox cbMonday, cbTuesday, cbWednesday, cbThursday, cbFriday, cbSaturday, cbSunday;
    private EditText etTimeMonday, etTimeTuesday, etTimeWednesday, etTimeThursday, etTimeFriday, etTimeSaturday, etTimeSunday;

    // Data
    private String action;
    private User currentUser;
    private Post baseUpdatePost;
    private String[] choiceOfPlaces = MainActivity.PLACES_TO_CHOOSE;
    private boolean[] checkedPlaces = new boolean[choiceOfPlaces.length];
    private String[] selectedPlaces = new String[choiceOfPlaces.length];

    private String[] choiceOfFields = MainActivity.FIELDS_TO_CHOOSE;
    int checkedField = 0;

    public AddNewPostFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_add_new_post, container, false);
        mMainActivity = (MainActivity) getActivity();
        currentUser = mMainActivity.getCurrentLoginUser();
        bindView();

        Bundle bundle = getArguments();
        if (bundle != null) {
            Post basePost = (Post) bundle.getSerializable("post");
            action = bundle.getString("action", "");
            fillViewInfo(basePost);
            if (action.equals("update")) {
                btnPost.setText("Thay đổi");
                baseUpdatePost = basePost;
            }
        } else {
            action = "";
        }
        return mView;
    }

    private void fillViewInfo(Post basePost) {
        etTitle.setText(basePost.getTitle());
        etField.setText(basePost.getField());
        etSubject.setText(basePost.getSubject());
        etPlace.setText(basePost.getLearningPlaces());
        etTuition.setText(String.valueOf(basePost.getTuition()));
        etDescription.setText(basePost.getDescription());
        if (basePost.getMethod().equals("online")) {
            rbOnline.setChecked(true);
            rbOffline.setChecked(false);
        } else {
            rbOnline.setChecked(false);
            rbOffline.setChecked(true);
        }
        Map<String, CheckBox> dictionaryDay = new HashMap<String, CheckBox>();
        dictionaryDay.put("Thứ 2", cbMonday);
        dictionaryDay.put("Thứ 3", cbTuesday);
        dictionaryDay.put("Thứ 4", cbWednesday);
        dictionaryDay.put("Thứ 5", cbThursday);
        dictionaryDay.put("Thứ 6", cbFriday);
        dictionaryDay.put("Thứ 7", cbSaturday);
        dictionaryDay.put("Chủ nhật", cbSunday);
        Map<String, EditText> dictionaryTime = new HashMap<String, EditText>();
        dictionaryTime.put("Thứ 2", etTimeMonday);
        dictionaryTime.put("Thứ 3", etTimeTuesday);
        dictionaryTime.put("Thứ 4", etTimeWednesday);
        dictionaryTime.put("Thứ 5", etTimeThursday);
        dictionaryTime.put("Thứ 6", etTimeFriday);
        dictionaryTime.put("Thứ 7", etTimeSaturday);
        dictionaryTime.put("Chủ nhật", etTimeSunday);
        String dateTimeLearning = basePost.getDateTimesLearning();
        String[] dateTimes = dateTimeLearning.split(", ");
        for (String dt : dateTimes) {
            Log.d("Check dt", dt);
            String date = dt.split(":")[0];
            String time = dt.split(":")[1];
            CheckBox cb = dictionaryDay.get(date);
            EditText et = dictionaryTime.get(date);
            cb.setChecked(true);
            et.setText(time);
        }
    }

    private void bindView() {
        ibBack = mView.findViewById(R.id.ibBack);
        btnPost = mView.findViewById(R.id.btnPost);

        etTitle = mView.findViewById(R.id.etTitle);
        etField = mView.findViewById(R.id.etField);
        etSubject = mView.findViewById(R.id.etSubject);
        etPlace = mView.findViewById(R.id.etPlace);
        etTuition = mView.findViewById(R.id.etTuition);
        etDescription = mView.findViewById(R.id.etDescription);

        rgMethod = mView.findViewById(R.id.rgMethod);
        rbOnline = mView.findViewById(R.id.rbOnline);
        rbOffline = mView.findViewById(R.id.rbOffline);
        cbMonday = mView.findViewById(R.id.cbMonday);
        cbTuesday = mView.findViewById(R.id.cbTuesday);
        cbWednesday = mView.findViewById(R.id.cbWednesday);
        cbThursday = mView.findViewById(R.id.cbThursday);
        cbFriday = mView.findViewById(R.id.cbFriday);
        cbSaturday = mView.findViewById(R.id.cbSaturday);
        cbSunday = mView.findViewById(R.id.cbSunday);

        etTimeMonday = mView.findViewById(R.id.etTimeMonday);
        etTimeTuesday = mView.findViewById(R.id.etTimeTuesday);
        etTimeWednesday = mView.findViewById(R.id.etTimeWednesday);
        etTimeThursday = mView.findViewById(R.id.etTimeThursday);
        etTimeFriday = mView.findViewById(R.id.etTimeFriday);
        etTimeSaturday = mView.findViewById(R.id.etTimeSaturday);
        etTimeSunday = mView.findViewById(R.id.etTimeSunday);

        etField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChoicesOfFields();
            }
        });
        etPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChoicesOfPlaces();
            }
        });

        // Set On Click Listener
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePostData(view);
            }
        });
        setCheckboxListeners();
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        if (currentUser==null) {
            btnPost.setClickable(false);
        }
    }

    private void setCheckboxListeners() {
        cbMonday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                changeInputTimePermission(etTimeMonday, b);
            }
        });
        cbTuesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                changeInputTimePermission(etTimeTuesday, b);
            }
        });
        cbWednesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                changeInputTimePermission(etTimeWednesday, b);
            }
        });
        cbThursday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                changeInputTimePermission(etTimeThursday, b);
            }
        });
        cbFriday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                changeInputTimePermission(etTimeFriday, b);
            }
        });
        cbSaturday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                changeInputTimePermission(etTimeSaturday, b);
            }
        });
        cbSunday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                changeInputTimePermission(etTimeSunday, b);
            }
        });
    }

    private void changeInputTimePermission(EditText editTime, boolean isEnabled) {
        if (isEnabled) {
            editTime.setFocusable(true);
            editTime.setFocusableInTouchMode(true);
            editTime.setHint("15h-17h30");
            editTime.setCursorVisible(true);
            editTime.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_add_post_input));
        } else {
            editTime.setFocusable(false);
            editTime.setFocusableInTouchMode(false);
            editTime.setHint("Hãy chọn ngày học");
            editTime.setCursorVisible(false);
            editTime.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_add_post_input_faded));
        }
    }

    private void showChoicesOfPlaces() {
        hideSoftKeyboard();
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
        alertBuilder.setTitle("Chọn khu vực");

        if (etPlace.getText().toString().isEmpty()) {
            etPlace.setText(choiceOfPlaces[0]);    //default
        }

        alertBuilder.setMultiChoiceItems(choiceOfPlaces, checkedPlaces, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                if (b) {
                    selectedPlaces[i] = choiceOfPlaces[i];
                    checkedPlaces[i] = true;
                } else {
                    selectedPlaces[i] = null;
                    checkedPlaces[i] = false;
                }
            }
        });
        alertBuilder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String placeString = String.join(", ", selectedPlaces);
                placeString = placeString.replaceAll(", null", "");
                placeString = placeString.replaceAll("null, ", "");

                if (placeString.equals("null")) {
                    etPlace.setText("");
                } else {
                    etPlace.setText(placeString);
                    Toast.makeText(getContext(), placeString, Toast.LENGTH_SHORT).show();
                }

            }
        });
        alertBuilder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertBuilder.show();
    }

    private void showChoicesOfFields() {
        hideSoftKeyboard();
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
        alertBuilder.setTitle("Chọn lĩnh vực dạy");
        
        if (etField.getText().toString().isEmpty()) {
            etField.setText(choiceOfFields[checkedField]);    //default
        }

        alertBuilder.setSingleChoiceItems(choiceOfFields, checkedField, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                checkedField = i;
            }
        });
        alertBuilder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                etField.setText(choiceOfFields[checkedField]);
            }
        });
        alertBuilder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertBuilder.show();
    }

    // Option updating post's data: edit post OR add new post
    private void updatePostData(View view) {
        // Get dates
        String inputTitle = etTitle.getText().toString();
        String inputField = etField.getText().toString();
        String inputSubject = etSubject.getText().toString();
        String inputMethod = getInputMethodString();
        String inputDateTime = getInputDateTimeString();
        String inputPlace = etPlace.getText().toString();
        String inputDesc = etDescription.getText().toString();
        int inputTuition;

        if (inputTitle.equals("")) {
            Toast.makeText(getContext(), "Hãy nhập tiêu đề", Toast.LENGTH_SHORT).show();
        } else if (inputField.equals("")) {
            Toast.makeText(getContext(), "Hãy chọn lĩnh vực", Toast.LENGTH_SHORT).show();
        } else if (inputSubject.equals("")) {
            Toast.makeText(getContext(), "Hãy nhập môn học", Toast.LENGTH_SHORT).show();
        } else if (inputMethod.equals("")) {
            Toast.makeText(getContext(), "Hãy chọn hình thức học", Toast.LENGTH_SHORT).show();
        } else if (inputDateTime.equals("null")) {
            Toast.makeText(getContext(), "Hãy chọn ngày học", Toast.LENGTH_SHORT).show();
        } else if (inputDateTime.equals("")) {
            //Toast.makeText(getContext(), "Hãy nhập giờ học của ngày học đã chọn", Toast.LENGTH_SHORT).show();
            return;
        } else if (inputPlace.equals("")) {
            Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
        } else if (etTuition.getText().toString().equals("")) {
            Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
        } else {
            if (inputDesc.equals("")) {
                inputDesc = "Không có";
            }
            inputTuition = Integer.parseInt(etTuition.getText().toString());
            // Check action
            if (action.equals("update")) {
                // update action
                Post postUpdated = new Post(
                        baseUpdatePost.getId(), inputTitle, Post.POST_STATUS_EDITED, currentUser.getPhoneNumber(),
                        inputSubject, inputField, inputDateTime, inputPlace, inputMethod,
                        inputTuition, inputDesc, baseUpdatePost.getPostDate(), baseUpdatePost.getHideFrom()
                );
                Call<ResultStringAPI> apiCall =  APIService.apiService.updatePost(postUpdated);
                apiCall.enqueue(new Callback<ResultStringAPI>() {
                    @Override
                    public void onResponse(Call<ResultStringAPI> call, Response<ResultStringAPI> response) {
                        ResultStringAPI resultStringAPI = response.body();
                        if (resultStringAPI.getCode()==0) {
                            Log.d("Update post Result", "Successful");
                            Toast.makeText(mMainActivity, "Cập nhật bài đăng thành công", Toast.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager().popBackStack();
                            getActivity().getSupportFragmentManager().popBackStack();
                            mMainActivity.resetViewPagerUI(2);
                        } else {
                            Log.d("Update Post Result", "Failed: " + resultStringAPI.getMessage());
                            Toast.makeText(mMainActivity, "Cập nhật bài đăng thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultStringAPI> call, Throwable t) {
                        call.cancel();
                        Log.d("Update Result", "Failed: " + t);
                        Toast.makeText(mMainActivity, "Có lỗi xảy ra, vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                // add action
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMyyyy-hhmmss");
                String id = "P"+dtf.format(LocalDateTime.now());
                String dateCreate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now());
                Call<ResultStringAPI> apiCall =  APIService.apiService.addNewPost(new Post(
                        id, inputTitle, Post.POST_STATUS_WAITING, currentUser.getPhoneNumber(),
                        inputSubject, inputField, inputDateTime, inputPlace, inputMethod,
                        inputTuition, inputDesc, dateCreate, ""
                ));
                apiCall.enqueue(new Callback<ResultStringAPI>() {
                    @Override
                    public void onResponse(Call<ResultStringAPI> call, Response<ResultStringAPI> response) {
                        ResultStringAPI resultStringAPI = response.body();
                        if (resultStringAPI.getCode()==0) {
                            Log.d("Add Post Result", "Successful");
                            Toast.makeText(mMainActivity, "Thêm bài đăng thành công", Toast.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager().popBackStack();
                            mMainActivity.resetViewPagerUI(2);
                        } else {
                            Log.d("Add Post Result", "Failed: " + resultStringAPI.getMessage());
                            Toast.makeText(mMainActivity, "Thêm bài đăng thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultStringAPI> call, Throwable t) {
                        call.cancel();
                        Log.d("Add Result", "Failed: " + t);
                        Toast.makeText(mMainActivity, "Có lỗi xảy ra, vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private String getInputDateTimeString() {
        String[] selectedDateTimes = new String[7];
        String errorMess = "";
        if (cbMonday.isChecked()) {
            if (etTimeMonday.getText().toString().isEmpty()) {
                errorMess = "Hãy nhập giờ học của thứ 2";
                Toast.makeText(getContext(), errorMess, Toast.LENGTH_SHORT).show();
                return "";
            } else {
                selectedDateTimes[0] = "Thứ 2:" + etTimeMonday.getText().toString();
            }
        }
        if (cbTuesday.isChecked()) {
            if (etTimeTuesday.getText().toString().isEmpty()) {
                errorMess = "Hãy nhập giờ học của thứ 3";
                Toast.makeText(getContext(), errorMess, Toast.LENGTH_SHORT).show();
                return "";
            } else {
                selectedDateTimes[0] = "Thứ 3:" + etTimeTuesday.getText().toString();
            }
        }
        if (cbWednesday.isChecked()) {
            if (etTimeWednesday.getText().toString().isEmpty()) {
                errorMess = "Hãy nhập giờ học của thứ 4";
                Toast.makeText(getContext(), errorMess, Toast.LENGTH_SHORT).show();
                return "";
            } else {
                selectedDateTimes[0] = "Thứ 4:" + etTimeWednesday.getText().toString();
            }
        }
        if (cbThursday.isChecked()) {
            if (etTimeThursday.getText().toString().isEmpty()) {
                errorMess = "Hãy nhập giờ học của thứ 5";
                Toast.makeText(getContext(), errorMess, Toast.LENGTH_SHORT).show();
                return "";
            } else {
                selectedDateTimes[0] = "Thứ 5:" + etTimeThursday.getText().toString();
            }
        }
        if (cbFriday.isChecked()) {
            if (etTimeFriday.getText().toString().isEmpty()) {
                errorMess = "Hãy nhập giờ học của thứ 6";
                Toast.makeText(getContext(), errorMess, Toast.LENGTH_SHORT).show();
                return "";
            } else {
                selectedDateTimes[0] = "Thứ 6:" + etTimeFriday.getText().toString();
            }
        }
        if (cbSaturday.isChecked()) {
            if (etTimeSaturday.getText().toString().isEmpty()) {
                errorMess = "Hãy nhập giờ học của thứ 7";
                Toast.makeText(getContext(), errorMess, Toast.LENGTH_SHORT).show();
                return "";
            } else {
                selectedDateTimes[0] = "Thứ 7:" + etTimeSaturday.getText().toString();
            }
        }
        if (cbSunday.isChecked()) {
            if (etTimeSunday.getText().toString().isEmpty()) {
                errorMess = "Hãy nhập giờ học của chủ nhật";
                Toast.makeText(getContext(), errorMess, Toast.LENGTH_SHORT).show();
                return "";
            } else {
                selectedDateTimes[0] = "Chủ nhật:" + etTimeSunday.getText().toString();
            }
        }

        String dateTimeString = String.join(", ", selectedDateTimes);
        dateTimeString = dateTimeString.replaceAll(", null", "");
        dateTimeString = dateTimeString.replaceAll("null, ", "");

        return dateTimeString;
    }

    private String getInputMethodString() {
        int radioId = rgMethod.getCheckedRadioButtonId();
        switch (radioId) {
            case R.id.rbOffline:
                return "online";
            case R.id.rbOnline:
                return "offline";
            default:
                return "";
        }
    }
    

    // Ẩn bàn phím sau khi nhập dữ liệu
    public void hideSoftKeyboard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }
}