package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
// firebase authentication object
    FirebaseAuth firebaseAuth;
    EditText emailText;  //edit text for email input
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        firebaseAuth = FirebaseAuth.getInstance();
        emailText = findViewById(R.id.email);

    }
//Activity to handle Forgot Password functionality
    public void forgotpasswordhandler(View view) {
        String email = emailText.getText().toString();
        if(email.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Email is empty", Toast.LENGTH_SHORT).show();
            return;
        }
// notify user email sent, section above is for requesting an email for password change to be made
        firebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Password reset email sent", Toast.LENGTH_SHORT).show();
                        } else {

                            Toast.makeText(getApplicationContext(), "Failed to send password reset email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
//event handler to navigate back to login screen and intent to start the login Activity
    public void movetohome(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }


}
