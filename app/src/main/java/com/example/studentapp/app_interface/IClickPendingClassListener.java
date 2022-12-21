package com.example.studentapp.app_interface;

import com.example.studentapp.model.ClassObject;

public interface IClickPendingClassListener {
    void onClickAcceptPendingClass(ClassObject classObject);
    void onClickCancelPendingClass(ClassObject classObject);
}
