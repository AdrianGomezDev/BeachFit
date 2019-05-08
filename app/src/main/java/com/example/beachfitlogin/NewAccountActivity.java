package com.example.beachfitlogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class NewAccountActivity extends AppCompatActivity {

    private static String TAG= "CreateAccount";

    FirebaseAuth mAuth;
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

        mAuth = FirebaseAuth.getInstance();

        usernameText = findViewById(R.id.usernameText);
        passwordText = findViewById(R.id.passwordText);
        rePasswordText = findViewById(R.id.rePasswordText);
        firstNameText = findViewById(R.id.firstNameText);
        lastNameText = findViewById(R.id.lastNameText);
        emailText = findViewById(R.id.emailText);
        ageText = findViewById(R.id.ageText);

        cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setKeyListener(null);
        cancelButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                finish();
            }
        });

        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){

                // Checks if all fields in the create account activity page are empty
                if(isEmpty(usernameText) || isEmpty(passwordText)|| isEmpty(rePasswordText)||
                        isEmpty(firstNameText)|| isEmpty(lastNameText)|| isEmpty(emailText)|| isEmpty(ageText)) {

                    if(isEmpty(usernameText)){
                        usernameText.setError(getString(R.string.required_field));
                    }
                    if(isEmpty(passwordText)){
                        passwordText.setError(getString(R.string.required_field));
                    }
                    if(isEmpty(rePasswordText)){
                        rePasswordText.setError(getString(R.string.required_field));
                    }
                    if(isEmpty(firstNameText)){
                        firstNameText.setError(getString(R.string.required_field));
                    }
                    if(isEmpty(lastNameText)){
                        lastNameText.setError(getString(R.string.required_field));
                    }
                    if(isEmpty(emailText)){
                        emailText.setError(getString(R.string.required_field));
                    }
                    if(isEmpty(ageText)){
                        ageText.setError(getString(R.string.required_field));
                    }
                    Toast.makeText(NewAccountActivity.this,
                            "Please enter all required fields.", Toast.LENGTH_LONG).show();
                }
                // Checks if the password and retype password fields match
                else if(!(passwordText.getText().toString().equals(rePasswordText.getText().toString()))){
                   passwordText.setError(getString(R.string.error_mismatched_passwords));
                   passwordText.requestFocus();
                }
                // Checks if age is too old or young
                else if(Integer.parseInt(ageText.getText().toString()) > 99 || Integer.parseInt(ageText.getText().toString()) < 1)
                {
                    ageText.setError(getString(R.string.error_invalid_age));
                    ageText.requestFocus();
                }
                else{
                    createAccount(emailText.getText().toString(), passwordText.getText().toString());
                }
            }
        });
    }

    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()) {
                            try {
                                throw task.getException();
                            } catch(FirebaseAuthWeakPasswordException e) {
                                passwordText.setError(getString(R.string.error_weak_password));
                                passwordText.requestFocus();
                            } catch(FirebaseAuthInvalidCredentialsException e) {
                                emailText.setError(getString(R.string.error_invalid_email));
                                emailText.requestFocus();
                            } catch(FirebaseAuthUserCollisionException e) {
                                emailText.setError(getString(R.string.error_user_exists));
                                emailText.requestFocus();
                            } catch(Exception e) {
                                Log.e(TAG, e.getMessage());
                                Toast.makeText(NewAccountActivity.this,
                                        e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            Intent intent = new Intent();
                            intent.putExtra("username", usernameText.getText().toString());
                            intent.putExtra("password", passwordText.getText().toString());
                            intent.putExtra("firstName", firstNameText.getText().toString());
                            intent.putExtra("lastName", lastNameText.getText().toString());
                            intent.putExtra("email", emailText.getText().toString());
                            intent.putExtra("age", ageText.getText().toString());
                            intent.putExtra("points",0);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                });
    }

    // Checks if EditText is empty
    private boolean isEmpty(EditText eText){
        return eText.getText().toString().trim().length() <= 0;
    }
}