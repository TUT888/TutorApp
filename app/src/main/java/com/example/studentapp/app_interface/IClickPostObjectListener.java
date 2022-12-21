package com.example.studentapp.app_interface;

import com.example.studentapp.model.Post;

public interface IClickPostObjectListener {
    void onClickPostObject(Post post);
    void onClickBtnHidePost(Post post);
    void onClickPostObjectSearch(Post post, String userName, String userRole, String userAvatar);
}
