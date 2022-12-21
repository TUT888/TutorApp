package com.example.tutorapp.app_interface;

import com.example.tutorapp.model.ClassObject;
import com.example.tutorapp.model.Rate;

public interface IClickBtnRatingListener {
    void rateClass(ClassObject classObject, int adapterPosition);
    void seeRateDetail (Rate rate);
}
