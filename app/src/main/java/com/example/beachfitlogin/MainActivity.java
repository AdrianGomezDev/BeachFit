package com.example.beachfitlogin;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.beachfitlogin.R;

public class MainActivity extends AppCompatActivity {

    // method to check if an edit text is empty
    private boolean isEmpty(EditText eText){
        if(eText.getText().toString().trim().length() > 0){
            return false;
        }
        else{
            return true;
        }
    }

    // variables
    int loginAttempts = 0;
    EditText userText;
    EditText passText;
    ImageView logoImg;
    Button loginButton;
    Button newUserButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // edit text initialization
        userText = (EditText) findViewById(R.id.usernameEditText);
        passText = (EditText) findViewById(R.id.passEditText);

        // ImageView for beachfit logo initialization
        logoImg = (ImageView) findViewById(R.id.logoView);
        logoImg.setImageResource(R.drawable.beachfitlogo);

        // Button for login
        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setKeyListener(null);

        // set a click listener to the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                // checks if the fields are empty
                if(isEmpty(userText) || isEmpty(passText)){
                    Toast.makeText(getApplicationContext(), "Please enter all required fields.",
                            Toast.LENGTH_LONG).show();
                }
                // checks for successful hardcoded admin login
                else if((userText.getText().toString().equals("admin")) && (passText.getText().toString().equals("1234"))){
                    Toast.makeText(getApplicationContext(), "Login is successful!",
                            Toast.LENGTH_LONG).show();
                    // resets login attempts on successful login
                    loginAttempts=0;
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                }
                // else will give warning and log attempts.
                else {
                    Toast.makeText(getApplicationContext(), "Username or password is incorrect.",
                            Toast.LENGTH_LONG).show();
                    // increment login attempts counter
                    loginAttempts++;
                    // check for login attempts. if third unsuccesful login then exits the program.
                    if(loginAttempts == 3){
                        finish();
                        System.exit(0);
                    }
                }

            }
        });


        // Button for new user
        newUserButton = (Button) findViewById(R.id.newUserButton);
        newUserButton.setKeyListener(null);
        // set on click listener for intent to go to new account activity.
        newUserButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, NewAccount.class));
            }

        });
    }
}