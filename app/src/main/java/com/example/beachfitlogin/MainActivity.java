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

    int loginAttempts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // EditText for username
        final EditText userText = (EditText) findViewById(R.id.usernameEditText);
        //final String usernameContent = userText.getText().toString();
        //userText.setText(usernameContent);


        // EditText for password
        final EditText passText = (EditText) findViewById(R.id.passEditText);
        //final String passContent = passText.getText().toString();
        //passText.setText(passContent);


        // ImageView for beachfit logo
        ImageView logoImg = (ImageView) findViewById(R.id.logoView);
        logoImg.setImageResource(R.drawable.beachfitlogo);

        // Button for login
        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setKeyListener(null);

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){

                // finish();
                // System.exit(0);




                if((userText.getText().toString().equals("admin")) && (passText.getText().toString().equals("1234"))){
                    Toast.makeText(getApplicationContext(), "Login is successful!",
                            Toast.LENGTH_LONG).show();
                    // resets login attempts on successful login
                    loginAttempts=0;
                }
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
        Button newUserButton = (Button) findViewById(R.id.newUserButton);
        newUserButton.setKeyListener(null);

        newUserButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, NewAccount.class));
            }

        });
    }
}