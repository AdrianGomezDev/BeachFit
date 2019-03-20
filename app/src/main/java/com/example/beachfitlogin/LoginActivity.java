package com.example.beachfitlogin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int NEW_ACCOUNT_REQUEST = 6312;
    private static String TAG= "EmailPassword";

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private TextView mStatusTextView;
    private EditText mEmailField;
    private EditText mPasswordField;

    private Button signInButton;
    private Button createAccountButton;
    private Button signOutButton;
    private Button verifyEmailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_firebase);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        mStatusTextView = findViewById(R.id.status);
        mEmailField = findViewById(R.id.fieldEmail);
        mPasswordField = findViewById(R.id.fieldPassword);

        signInButton = findViewById(R.id.emailSignInButton);
        createAccountButton = findViewById(R.id.emailCreateAccountButton);
        signOutButton = findViewById(R.id.signOutButton);
        verifyEmailButton = findViewById(R.id.verifyEmailButton);

        signInButton.setOnClickListener(this);
        createAccountButton.setOnClickListener(this);
        signOutButton.setOnClickListener(this);
        verifyEmailButton.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {

            startNavActivityIfVerified(user);

            mStatusTextView.setText(user.getEmail() + " is signed in\nNot your account? Press Sign Out below");
            verifyEmailButton.setEnabled(!user.isEmailVerified());

            signInButton.setVisibility(View.GONE);
            createAccountButton.setVisibility(View.GONE);
            mEmailField.setVisibility(View.GONE);
            mPasswordField.setVisibility(View.GONE);

            signOutButton.setVisibility(View.VISIBLE);
            verifyEmailButton.setVisibility(View.VISIBLE);

        } else {
            mStatusTextView.setText(R.string.signed_out);

            signInButton.setVisibility(View.VISIBLE);
            createAccountButton.setVisibility(View.VISIBLE);
            mEmailField.setVisibility(View.VISIBLE);
            mPasswordField.setVisibility(View.VISIBLE);

            signOutButton.setVisibility(View.GONE);
            verifyEmailButton.setVisibility(View.GONE);
        }
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
                            startNavActivityIfVerified(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            displayToast("Authentication failed.");
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
                            displayToast("Verification email sent to " + user.getEmail());
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            displayToast("Failed to send verification email.");
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

    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_ACCOUNT_REQUEST) {
            if(resultCode == RESULT_OK) {
                mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        if(mAuth.getCurrentUser() != null){
                            if(!mAuth.getCurrentUser().isEmailVerified()){
                                sendEmailVerification();
                            }
                            Map<String, Object> user = new HashMap<>();
                            user.put( "username", data.getStringExtra("username"));
                            user.put(    "email", data.getStringExtra("email"));
                            user.put("firstName", data.getStringExtra("firstName"));
                            user.put( "lastName", data.getStringExtra("lastName"));
                            user.put(      "age", data.getStringExtra("age"));

                            // Add a new document with a generated ID
                            db.collection("users")
                                    .document(mAuth.getCurrentUser().getUid())
                                    .set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.w(TAG, "Added user to firestore.");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error adding document", e);
                                        }
                                    });
                            mAuth.removeAuthStateListener(this);
                        }
                    }
                });
            }
        }
    }

    private void startNavActivityIfVerified(FirebaseUser user){
        if(user.isEmailVerified()) {
            Intent myIntent = new Intent(LoginActivity.this, NavigationActivity.class);
            LoginActivity.this.startActivity(myIntent);
            finish();
        }
        else{
            displayToast("Please verify email");
        }
    }

    private void displayToast(String message){
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
    }
}