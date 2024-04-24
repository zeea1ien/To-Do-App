package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Controllers.ToDoListActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
//Activity to manager my user login using firebase authentication
public class Login extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    EditText emailText, passwordText;
    Button loginButton, signupButton, forgotButton;
//initilise firebase auth and UI components
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        emailText = findViewById(R.id.email);
        passwordText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginbtn);
        signupButton = findViewById(R.id.signupbtn);
        forgotButton = (Button) findViewById(R.id.forgotbutton);
        //set up for listeners
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                authenticateUser();
            }
        });
        forgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movetoforgotpassword();
            }
        });
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToSignup();
            }
        });
    }
//authenticate user with their email and password
    public void authenticateUser() {
        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();
        String validateResult = Validation.validate(email, password);
        if (validateResult.length() > 1) {
            showAlert("Input Error", validateResult, R.drawable.close);
            return;
        }


//check for registered emails and authenticate
        firebaseAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(this, firebaseTask -> {
            if (firebaseTask.isSuccessful()) {
                SignInMethodQueryResult result = firebaseTask.getResult();
                if (result == null || result.getSignInMethods() == null) {

                    showAlert("User not registered", "Please register first", R.drawable.close, true);
                } else {
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (user != null && user.isEmailVerified()) {
                                showAlert("Successful Login", "You have logged in", R.drawable.check);
                                Intent intent = new Intent(this, ToDoListActivity.class);
                                startActivity(intent);

                            } else if (!user.isEmailVerified()) {
                                showAlert("Email not verified", "Check your email and verify", R.drawable.close);
//
                            }

                        } else {
                            showAlert("Error in Login", task.getException().getLocalizedMessage(), R.drawable.close);

                        }
                    });

                }


            }
        });
    }

//navigate to signup activity
    public void moveToSignup() {
        Intent intent = new Intent(this, Signup.class);
        startActivity(intent);
    }
//show alert dialogue to user
    public void showAlert(String message, String title, int icon) {
        showAlert(message, title, icon, false);
    }

    public void showAlert(String title, String message, int icon, boolean moveToSignup) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).setIcon(icon).setTitle(title).setMessage(message).setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    if (moveToSignup) moveToSignup();
                }
            }).show();
    }
//navigate to forgot password activity
    public void movetoforgotpassword() {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }
}
