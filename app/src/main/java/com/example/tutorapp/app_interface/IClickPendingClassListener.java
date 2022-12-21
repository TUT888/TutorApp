package com.example.tutorapp.app_interface;

import com.example.tutorapp.model.ClassObject;

public interface IClickPendingClassListener {
    void onClickAcceptPendingClass(ClassObject classObject);
    void onClickCancelPendingClass(ClassObject classObject);
}
