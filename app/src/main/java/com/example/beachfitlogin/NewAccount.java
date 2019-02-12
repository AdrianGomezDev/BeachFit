package com.example.beachfitlogin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beachfitlogin.R;

public class NewAccount extends AppCompatActivity {

    private boolean isEmpty(EditText eText){
        if(eText.getText().toString().trim().length() > 0){
            return false;
        }
        else{
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        final EditText userText = (EditText) findViewById(R.id.userText);
        final EditText passText = (EditText) findViewById(R.id.passText);
        final EditText repassText = (EditText) findViewById(R.id.repassText);
        final EditText firstText = (EditText) findViewById(R.id.firstText);
        final EditText lastText = (EditText) findViewById(R.id.lastText);
        final EditText emailText = (EditText) findViewById(R.id.emailText);
        final EditText ageText = (EditText) findViewById(R.id.ageText);

        //button for saving
        // TEMPORARILY FOR CHECKING NEW ACCOUNT FIELDS
        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setKeyListener(null);

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                // checks if all fields in the new user activity page are empty
                if(isEmpty(userText) || isEmpty(passText)|| isEmpty(repassText)|| isEmpty(firstText)|| isEmpty(lastText)|| isEmpty(emailText)|| isEmpty(ageText)){
                    Toast.makeText(getApplicationContext(), "Please enter all required fields.",
                            Toast.LENGTH_LONG).show();
                }


                // checks if the password and retype password fields match
                if(!(passText.getText().toString().equals(repassText.getText().toString()))){
                    Toast.makeText(getApplicationContext(), "Password fields must match.",
                            Toast.LENGTH_LONG).show();
                }

                int ageInt = Integer.parseInt(ageText.getText().toString());

                // checks that age is between 1 and 99.
                if(ageInt > 99 || ageInt < 1){
                    Toast.makeText(getApplicationContext(), "Age must be between 1-99.",
                            Toast.LENGTH_LONG).show();
                }


            }
        });

        // button for canceling new user creation and return to login page
        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setKeyListener(null);

        cancelButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                finish();
            }

        });
    }
}