package com.example.myapplication;

import android.widget.Toast;

public class Validation {

    public static String validate(String email, String password) {
        if (email.length() == 0) {
            return "Email is empty";
        } else if (password.length() == 0) {
            return "Password is empty";
        } else if (!isValidEmail(email)) {
            return "Invalid Email";
        }

        return "";
    }

    private static boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    public static String validate(String email, String password, String name) {
        if (email.length() == 0) {
            return "Email is empty";
        }
        // Validate email format
        if (password.length() == 0) {
            return "Password is empty";
        }
        if (name.length() == 0) {
            return "name is empty";
        } else if (!isValidEmail(email)) {
            return "Invalid Email";
        } else if (password.length() < 6) {
            return "Password must be greater than 6 characters";
        }
        return "";
    }
}
