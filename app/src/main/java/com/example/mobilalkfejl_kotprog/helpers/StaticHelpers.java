package com.example.mobilalkfejl_kotprog.helpers;

import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.snackbar.Snackbar;

public class StaticHelpers {
    public static void showTopSnackbar(View view, String message) {
        Snackbar snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View view2 = snack.getView();
        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)view2.getLayoutParams();
        params.gravity = Gravity.TOP;
        params.topMargin = 100;
        view2.setLayoutParams(params);
        snack.show();
    }
}
