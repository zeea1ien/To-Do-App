package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
//activity for user resignation using firebase authtentication and firestore
public class Signup extends AppCompatActivity {
    //firebase authentication and firestore database instances
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    //UI elements
    EditText nameText, emailText, passwordText;
    Button signupBtn, loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Initilise firebase auth and firestore
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        //link UI elements with layout
        nameText = findViewById(R.id.name);
        emailText = findViewById(R.id.email);
        passwordText = findViewById(R.id.password);
        signupBtn = findViewById(R.id.registerbtn);
        loginBtn = findViewById(R.id.loginbtn);
//set up listeners for buttons
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToLogin();
            }
        });
    }
//Handles the user registration process.
    public void registerUser() {
        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();
        String name = nameText.getText().toString().trim();
        String validationResult = Validation.validate(email, password, name);
        if (validationResult.length() > 1) {
            showAlert("Input Error", validationResult, R.drawable.close);
            return;
        }
//Create user account using email and password
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    sendVerificationEmail();   //send verification email
                    storeUserName(name, email); // store user in database
                } else {
                    Exception exception = task.getException();
                    if (exception instanceof FirebaseAuthUserCollisionException) {
                        showAlert("User Exists", "A user with this email already exists", R.drawable.close, true);
                    } else {
                        showAlert(exception.getLocalizedMessage(), "Error Occurred", R.drawable.close);
                    }
                }
            }
        });
    }
//sends a verification email to newly registered user
    private void sendVerificationEmail() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        showAlert("Verification email sent", "User created", R.drawable.check);
                    } else {
                        showAlert(task.getException().getLocalizedMessage(), "Error occured", R.drawable.close);

                    }
                }
            });
        }
    }
// stores users name and email in Firestore under their UID.
    private void storeUserName(String name, String email) {
        DocumentReference userRef = firestore.collection("users").document(firebaseAuth.getCurrentUser().getUid());
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", name);
        userData.put("email", email);
        userRef.set(userData);
    }
    //Navigates to login activity.
    public void moveToLogin() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);

    }
//Displays user alerts.
    public void showAlert(String message, String title, int icon) {
        showAlert(message, title, icon, false);
    }
    public void showAlert(String message, String title, int icon, boolean moveToLogin) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).setIcon(icon).setTitle(title).setMessage(message).setNeutralButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (moveToLogin) {
                    moveToLogin();
                }
                dialogInterface.dismiss();
            }
        }).show();
    }
}