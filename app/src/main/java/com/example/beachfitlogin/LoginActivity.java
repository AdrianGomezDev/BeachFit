package com.example.beachfitlogin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int NEW_ACCOUNT_REQUEST = 6312;
    private static String TAG= "EmailPassword";

    private FirebaseAuth mAuth;
    private TextView mStatusTextView;
    private EditText mEmailField;
    private EditText mPasswordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_firebase);

        mAuth = FirebaseAuth.getInstance();

        // Views
        mStatusTextView = findViewById(R.id.status);
        mEmailField = findViewById(R.id.fieldEmail);
        mPasswordField = findViewById(R.id.fieldPassword);

        // Buttons
        findViewById(R.id.emailSignInButton).setOnClickListener(this);
        findViewById(R.id.emailCreateAccountButton).setOnClickListener(this);
        findViewById(R.id.signOutButton).setOnClickListener(this);
        findViewById(R.id.verifyEmailButton).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {

            if(user.isEmailVerified()) {
                Intent myIntent = new Intent(LoginActivity.this, NavigationActivity.class);
                myIntent.putExtra("key", "Test Value from LoginActivity"); //Optional parameters
                LoginActivity.this.startActivity(myIntent);
            }
            else{
                Toast.makeText(LoginActivity.this, "Please verify email",
                        Toast.LENGTH_SHORT).show();
            }

            mStatusTextView.setText(user.getEmail() + " is signed in\nNot your account? Press Sign Out below");

            findViewById(R.id.emailSignInButton).setVisibility(View.GONE);
            findViewById(R.id.emailCreateAccountButton).setVisibility(View.GONE);
            mEmailField.setVisibility(View.GONE);
            mPasswordField.setVisibility(View.GONE);

            findViewById(R.id.signOutButton).setVisibility(View.VISIBLE);
            findViewById(R.id.verifyEmailButton).setVisibility(View.VISIBLE);

            findViewById(R.id.verifyEmailButton).setEnabled(!user.isEmailVerified());

        } else {
            mStatusTextView.setText(R.string.signed_out);

            findViewById(R.id.emailSignInButton).setVisibility(View.VISIBLE);
            findViewById(R.id.emailCreateAccountButton).setVisibility(View.VISIBLE);
            mEmailField.setVisibility(View.VISIBLE);
            mPasswordField.setVisibility(View.VISIBLE);

            findViewById(R.id.signOutButton).setVisibility(View.GONE);
            findViewById(R.id.verifyEmailButton).setVisibility(View.GONE);
        }
    }

    private void createAccount(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                            if(user.isEmailVerified()) {
                                Intent myIntent = new Intent(LoginActivity.this, NavigationActivity.class);
                                myIntent.putExtra("key", "Test Value from LoginActivity"); //Optional parameters
                                LoginActivity.this.startActivity(myIntent);
                            }
                            else{
                                Toast.makeText(LoginActivity.this, "Please verify email",
                                        Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                            if(user.isEmailVerified()) {
                                Intent myIntent = new Intent(LoginActivity.this, NavigationActivity.class);
                                myIntent.putExtra("key", "Test Value from LoginActivity"); //Optional parameters
                                LoginActivity.this.startActivity(myIntent);
                            }
                            else{
                                Toast.makeText(LoginActivity.this, "Please verify email",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        if (!task.isSuccessful()) {
                            mStatusTextView.setText(R.string.auth_failed);
                        }
                    }
                });
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    private void sendEmailVerification() {
        // Disable button
        findViewById(R.id.verifyEmailButton).setEnabled(false);

        // Send verification email
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Re-enable button
                        findViewById(R.id.verifyEmailButton).setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(LoginActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.emailCreateAccountButton) {
            Intent myIntent = new Intent(this, NewAccountActivity.class);
            startActivityForResult(myIntent, NEW_ACCOUNT_REQUEST);
        } else if (i == R.id.emailSignInButton) {
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if (i == R.id.signOutButton) {
            signOut();
        } else if (i == R.id.verifyEmailButton) {
            sendEmailVerification();
            updateUI(null);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_ACCOUNT_REQUEST) {
            if(resultCode == RESULT_OK) {
                createAccount(data.getStringExtra("email"), data.getStringExtra("password"));
            }
        }
    }

    @Override
    public void onBackPressed(){
        // Do nothing if user tries to go back
    }
}