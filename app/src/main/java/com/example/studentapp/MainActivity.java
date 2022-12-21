package com.example.studentapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.studentapp.adapter.ViewPagerAdapter;
import com.example.studentapp.api.APIService;
import com.example.studentapp.api.ResultStringAPI;
import com.example.studentapp.app_interface.IClickBtnSaveRatingListener;
import com.example.studentapp.app_interface.IClickTutorBtnListener;
import com.example.studentapp.extra_fragment.AccountInfoFragment;
import com.example.studentapp.extra_fragment.AddNewPostFragment;
import com.example.studentapp.extra_fragment.AllRatingsFragment;
import com.example.studentapp.extra_fragment.ChangePasswordFragment;
import com.example.studentapp.extra_fragment.LoginFragment;
import com.example.studentapp.extra_fragment.PostDetailFragment;
import com.example.studentapp.extra_fragment.RegisterFragment;
import com.example.studentapp.extra_fragment.RateFragment;
import com.example.studentapp.extra_fragment.RatingDetailFragment;
import com.example.studentapp.extra_fragment.TutorDetailFragment;
import com.example.studentapp.fragment.ClassFragment;
import com.example.studentapp.model.ClassObject;
import com.example.studentapp.model.Post;
import com.example.studentapp.model.Rate;
import com.example.studentapp.model.Tutor;
import com.example.studentapp.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.Serializable;

import javax.xml.transform.Result;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public final static String URL = "http://192.168.1.8:8282"; // Tam url
//    public final static String URL = "http://10.35.48.79"; ///Tien url
//    public final static String URL = "http://172.16.12.110"; ///Tien url
//    public final static String URL = "http://192.168.1.9:8080"; /// San url

    public final static String URL_IMAGE = URL +  "/image/";
    public static String CURRENT_LOGIN_AVATAR = "";
    public static String CURRENT_LOGIN_NAME = "";
    public static final String CURRENT_LOGIN_ROLE = "Học viên";
    public static final String PROFILE_FRAGMENT_TAG = "PROFILE_FRAGMENT_TAG";
    public static final String LOGIN_FRAGMENT_TAG = "LOGIN_FRAGMENT_TAG";

    public static final String KEY_USER_LOGIN_HISTORY = "KEY_USER_LOGIN_HISTORY";
    public static final String[] PLACES_TO_CHOOSE = {
            "Quận 1", "Quận 2", "Quận 3", "Quận 4", "Quận 5", "Quận 6",
            "Quận 7", "Quận 8", "Quận 9", "Quận 10", "Quận 11", "Quận 12",
            "Quận Bình Tân", "Quận Bình Thạnh", "Quận Gò Vấp", "Quận Phú Nhuận", "Quận Tân Bình", "Quận Tân Phú",
            "Quận Thủ Đức", "Huyện Bình Chánh", "Huyện Cần Giờ", "Huyện Củ Chi", "Huyện Hóc Môn", "Huyện Nhà Bè"
    };
    public static final String[] FIELDS_TO_CHOOSE = {
            "Toán Học", "Vật Lý", "Hóa Học", "Ngữ Văn", "Lịch Sử", "Địa Lý", "Sinh Học", "Tiếng Anh",
            "Âm Nhạc", "Hội Họa", "Kỹ Năng", "Kinh Tế", "Kỹ thuật", "Công nghệ thông tin", "Y Học", "Khác"
    };
    BottomNavigationView mBottomNavigationView;
    ViewPager2 mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNavigationView = findViewById(R.id.bottomNav);
        mViewPager = findViewById(R.id.viewPager);

        setUpViewPager();
        setUpBottomNavigationView();

        User u = getCurrentLoginUser();
        if (u!=null) {
            setCurrentUserData(u.getName(), u.getAvatar());
        }
    }

    private static void setCurrentUserData(String name, String avatar) {
        CURRENT_LOGIN_NAME = name;
        CURRENT_LOGIN_AVATAR = avatar;
    }

    //ViewPager settings
    private void setUpViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle(), this);
        mViewPager.setAdapter(viewPagerAdapter);

        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        mBottomNavigationView.getMenu().findItem(R.id.bottomNav_home).setChecked(true);
                        break;
                    case 1:
                        mBottomNavigationView.getMenu().findItem(R.id.bottomNav_search).setChecked(true);
                        break;
                    case 2:
                        mBottomNavigationView.getMenu().findItem(R.id.bottomNav_post).setChecked(true);
                        break;
                    case 3:
                        mBottomNavigationView.getMenu().findItem(R.id.bottomNav_pending_class).setChecked(true);
                        break;
                    case 4:
                        mBottomNavigationView.getMenu().findItem(R.id.bottomNav_profile).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    private void setUpBottomNavigationView() {
        mBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottomNav_home:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.bottomNav_search:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.bottomNav_post:
                        mViewPager.setCurrentItem(2);
                        break;
                    case R.id.bottomNav_pending_class:
                        mViewPager.setCurrentItem(3);
                        break;
                    case R.id.bottomNav_profile:
                        mViewPager.setCurrentItem(4);
                        break;
                }
                return true;
            }
        });
    }

    public void goToPostDetailFragment(Post post, String previousFragment) {
        //Example: previousFragment = MyPostFragment.class.getSimpleName() = "MyPostFragment"
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        PostDetailFragment detailFragment = new PostDetailFragment(); //Child fragment
        Bundle bundle = new Bundle();
        bundle.putSerializable("post", (Serializable) post);
        bundle.putString("previous", previousFragment);

        detailFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.main_activity_content, detailFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void goToPostDetailFragment(Post post, String previousFragment, String name, String role, String avatar) {
        //Example: previousFragment = MyPostFragment.class.getSimpleName() = "MyPostFragment"
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        PostDetailFragment detailFragment = new PostDetailFragment(); //Child fragment
        Bundle bundle = new Bundle();
        bundle.putSerializable("post", (Serializable) post);
        bundle.putString("previous", previousFragment);
        bundle.putString("name", name);
        bundle.putString("role", role);
        bundle.putString("avatar", avatar);

        detailFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.main_activity_content, detailFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void goToAddNewPostFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        AddNewPostFragment detailFragment = new AddNewPostFragment(); //Child fragment
        //Bundle bundle = new Bundle();
        //detailFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.main_activity_content, detailFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void goToAddNewPostFragment(Post newPost, String action) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        AddNewPostFragment detailFragment = new AddNewPostFragment(); //Child fragment
        Bundle bundle = new Bundle();
        bundle.putSerializable("post", (Serializable) newPost);
        bundle.putString("action", action);
        detailFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.main_activity_content, detailFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void goToRateDetailFragment(Rate rate) {
        //Example: previousFragment = MyPostFragment.class.getSimpleName() = "MyPostFragment"
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        RatingDetailFragment ratingDetailFragment = new RatingDetailFragment(); //Child fragment
        Bundle bundle = new Bundle();
        bundle.putSerializable("rate", (Serializable) rate);

        ratingDetailFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.main_activity_content, ratingDetailFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void returnToClassFragment(int adapterPosition) {
        Bundle bundle = new Bundle();
        bundle.putInt("adapter_position", adapterPosition);
        getSupportFragmentManager().setFragmentResult("getAdapterPosition", bundle);
        getSupportFragmentManager().popBackStack();
    }

    public void goToRateFragment(ClassObject classObject, int adapterPosition) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        RateFragment rateFragment = new RateFragment(new IClickBtnSaveRatingListener() {
            @Override
            public void saveAndReturnToClassFragment(Rate rate) {
                callAPIToAddRating(rate);
                returnToClassFragment(adapterPosition);
            }
        }); //Child fragment
        Bundle bundle = new Bundle();
        bundle.putSerializable("class_object", (Serializable) classObject);

        rateFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.main_activity_content, rateFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void goToLoginFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        LoginFragment loginFragment = new LoginFragment(); //Child fragment
        Bundle bundle = new Bundle();
        loginFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.main_activity_content, loginFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void goToRegisterFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        RegisterFragment registerFragment = new RegisterFragment(); //Child fragment
        Bundle bundle = new Bundle();
        registerFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.main_activity_content, registerFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void goToClassFragment(ClassObject classObject) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        ClassFragment classFragment = new ClassFragment(); //Child fragment
        Bundle bundle = new Bundle();
        if (classObject != null) {
            bundle.putSerializable("class", classObject);
        }
        classFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.main_activity_content, classFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void goToAllRatingsFragment(String tutorPhone) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        AllRatingsFragment allRatingsFragment = new AllRatingsFragment(); //Child fragment
        Bundle bundle = new Bundle();
        bundle.putString("tutorPhone", tutorPhone);
        allRatingsFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.main_activity_content, allRatingsFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void goToAccountInfoFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        AccountInfoFragment accountInfoFragment = new AccountInfoFragment(); //Child fragment
        Bundle bundle = new Bundle();
        accountInfoFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.main_activity_content, accountInfoFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void goToChangePasswordFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment(); //Child fragment
        Bundle bundle = new Bundle();
        changePasswordFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.main_activity_content, changePasswordFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public User getCurrentLoginUser() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String jsonString = sharedPref.getString(KEY_USER_LOGIN_HISTORY, null);
        User currentUser = gson.fromJson(jsonString, User.class);

        return currentUser;
    }

    public void savedLoginUser(User user) {
        SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString(KEY_USER_LOGIN_HISTORY, json);
        editor.apply();
    }

    public void logOut() {
        SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(null);
        editor.putString(KEY_USER_LOGIN_HISTORY, json);
        editor.apply();
    }

    public void goToTutorDetailFragment(Tutor tutor, String previousFragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        TutorDetailFragment detailFragment = new TutorDetailFragment(new IClickTutorBtnListener() {
            @Override
            public void openAllRatingsFragment(String tutorPhone) {
                goToAllRatingsFragment(tutorPhone);
            }
        }); //Child fragment
        Bundle bundle = new Bundle();
        bundle.putSerializable("tutor", (Serializable) tutor);
        bundle.putString("previous", previousFragment);

        detailFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.main_activity_content, detailFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void resetViewPagerUI(int pagePosition) {
        setUpViewPager();
        mViewPager.setCurrentItem(pagePosition);
    }

    private void callAPIToAddRating (Rate rate) {
        APIService.apiService.addNewRating(rate.getClassId(), rate.getRate(), rate.getComment(), rate.getDate())
                .enqueue(new Callback<ResultStringAPI>() {
                    @Override
                    public void onResponse(Call<ResultStringAPI> call, Response<ResultStringAPI> response) {
                        ResultStringAPI resultAPI = response.body();
                        if (response.isSuccessful() && resultAPI != null) {
                            if (resultAPI.getCode() == 0) {
                                Log.d("onResponse:", "Successfully added new rating");
                            }
                            else {
                                Log.d("onFailure:", "Rating: " + resultAPI.getMessage());
                                Toast.makeText(MainActivity.this, "Thêm bài đăng thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultStringAPI> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                        Log.d("onFailure", "onFailure: " + t.getMessage());
                    }
                });
    }
}