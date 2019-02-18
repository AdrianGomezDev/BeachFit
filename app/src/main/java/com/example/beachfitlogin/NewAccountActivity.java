package com.example.beachfitlogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;

public class NewAccountActivity extends AppCompatActivity {

    // method to check if edit text is empty
    private boolean isEmpty(EditText eText){
        if(eText.getText().toString().trim().length() > 0){
            return false;
        }
        else{
            return true;
        }
    }

    // variables
    EditText userText;
    EditText passText;
    EditText repassText;
    EditText firstText;
    EditText lastText;
    EditText emailText;
    EditText ageText;
    Button submitButton;
    Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);
        this.setFinishOnTouchOutside(false);

        // initialize text fields
        userText = (EditText) findViewById(R.id.userText);
        passText = (EditText) findViewById(R.id.passText);
        repassText = (EditText) findViewById(R.id.repassText);
        firstText = (EditText) findViewById(R.id.firstText);
        lastText = (EditText) findViewById(R.id.lastText);
        emailText = (EditText) findViewById(R.id.emailText);
        ageText = (EditText) findViewById(R.id.ageText);

        //button for saving
        // TEMPORARILY FOR CHECKING NEW ACCOUNT FIELDS
        submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setKeyListener(null);

        // on click listener to check edit text field requirements
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){

                // checks if all fields in the new user activity page are empty
                if(isEmpty(userText) || isEmpty(passText)|| isEmpty(repassText)|| isEmpty(firstText)|| isEmpty(lastText)|| isEmpty(emailText)|| isEmpty(ageText)) {
                    Toast.makeText(getApplicationContext(), "Please enter all required fields.",
                            Toast.LENGTH_LONG).show();
                }
                // checks if the password and retype password fields match
                else if(!(passText.getText().toString().equals(repassText.getText().toString()))){
                    Toast.makeText(getApplicationContext(), "Password fields must match.",
                            Toast.LENGTH_LONG).show();
                }
                // Checks if age is too old or young
                else if(Integer.parseInt(ageText.getText().toString()) > 99 || Integer.parseInt(ageText.getText().toString()) < 1)
                {
                    Toast.makeText(getApplicationContext(), "Age must be between 1-99.",
                            Toast.LENGTH_LONG).show();
                }
                else{
                    // TODO: Need to save the rest of the user data in the database
                    Intent intent = new Intent();
                    intent.putExtra("email", emailText.getText().toString());
                    intent.putExtra("password", passText.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        // button for canceling new user creation and return to login page
        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setKeyListener(null);
        // on click listener finishes the activity
        cancelButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                finish();
            }
        });
    }
}