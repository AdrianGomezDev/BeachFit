package com.example.beachfitlogin;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Calendar;

public final class Util {

    // Checks if EditText is empty
    public static Boolean isEmpty(EditText eText){
        return eText.getText().toString().trim().length() <= 0;
    }

    // Sets the giver calender instance's hours, minutes, seconds, and millisecs to zero
    public static void setTimeToZero(Calendar calendar){
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    // Capitalize first letters of all words in a string
    public static String capitalizeString(String str){
        String words[]=str.split("\\s");
        StringBuilder capitalizedString= new StringBuilder();
        for(String w:words){
            String first = w.substring(0,1);
            String afterFirst = w.substring(1);
            capitalizedString.append(first.toUpperCase()).append(afterFirst).append(" ");
        }
        return capitalizedString.toString().trim();
    }

    // Replaces null strings with 0.0 and returns as double
    public static Double myDoubleParser(String str){
        if(str.equals("null")){
            return 0.0;
        }
        return Double.parseDouble(str);
    }

    // hides soft keyboard in fragments
    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    // hides soft keyboard in activities
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
