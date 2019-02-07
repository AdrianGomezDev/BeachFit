package com.example.beachfitlogin;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.beachfitlogin.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView userText = (TextView) findViewById(R.id.usernameEditText);
        //EditText userEdit = (EditText) findViewById(R.id.editText2);
        String content = userText.getText().toString();
        userText.setText(content);

        userText.setTextColor(Color.BLACK);

        TextView passText = (TextView) findViewById(R.id.passEditText);



        ImageView imgtest = (ImageView) findViewById(R.id.imageView);
        imgtest.setImageResource(R.drawable.beachfitlogo);

        EditText newUserText = (EditText) findViewById(R.id.editText);
        newUserText.setKeyListener(null);

        newUserText.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, NewAccount.class));
            }
        });
    }
}