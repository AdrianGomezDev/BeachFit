package com.example.beachfitlogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;

public class NewAccountActivity extends AppCompatActivity {

    EditText usernameText;
    EditText passwordText;
    EditText rePasswordText;
    EditText firstNameText;
    EditText lastNameText;
    EditText emailText;
    EditText ageText;
    Button submitButton;
    Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        // Do not allow user to exist dialog activity by tapping outside dialog
        this.setFinishOnTouchOutside(false);

        // Assign EditText fields
        usernameText = findViewById(R.id.usernameText);
        passwordText = findViewById(R.id.passwordText);
        rePasswordText = findViewById(R.id.rePasswordText);
        firstNameText = findViewById(R.id.firstNameText);
        lastNameText = findViewById(R.id.lastNameText);
        emailText = findViewById(R.id.emailText);
        ageText = findViewById(R.id.ageText);

        // Button for submitting
        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){

                // Checks if all fields in the create account activity page are empty
                if(isEmpty(usernameText) || isEmpty(passwordText)|| isEmpty(rePasswordText)|| isEmpty(firstNameText)|| isEmpty(lastNameText)|| isEmpty(emailText)|| isEmpty(ageText)) {
                    Toast.makeText(getApplicationContext(), "Please enter all required fields.",
                            Toast.LENGTH_LONG).show();
                }
                // Checks if the password and retype password fields match
                else if(!(passwordText.getText().toString().equals(rePasswordText.getText().toString()))){
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
                    intent.putExtra("username", usernameText.getText().toString());
                    intent.putExtra("password", passwordText.getText().toString());
                    intent.putExtra("firstName", firstNameText.getText().toString());
                    intent.putExtra("lastName", lastNameText.getText().toString());
                    intent.putExtra("email", emailText.getText().toString());
                    intent.putExtra("age", ageText.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        // Close NewAccountActivity
        cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setKeyListener(null);
        cancelButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                finish();
            }
        });
    }

    // Checks if EditText is empty
    private boolean isEmpty(EditText eText){
        return eText.getText().toString().trim().length() <= 0;
    }
}