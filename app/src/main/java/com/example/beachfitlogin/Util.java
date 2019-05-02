package com.example.beachfitlogin;

import android.widget.EditText;

public class Util {

    // Checks if EditText is empty
    public static  boolean isEmpty(EditText eText){
        return eText.getText().toString().trim().length() <= 0;
    }
}
