package com.example.tutorapp.app_interface;

import com.example.tutorapp.model.Post;

public interface IClickPostObjectListener {
    void onClickPostObject(Post post);
    void onClickBtnHidePost(Post post);
    void onClickPostObjectSearch(Post post, String userName, String userRole, String userAvatar);
}
