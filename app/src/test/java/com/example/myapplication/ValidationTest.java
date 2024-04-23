package com.example.myapplication;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ValidationTest {

    @Test
    public void testEmptyEmail() {
        String emptyEmail = "";
        String password = "password123";
        String expectedErrorMessage = "Email is empty";
        String actualErrorMessage = Validation.validate(emptyEmail, password);
        assertEquals(expectedErrorMessage, actualErrorMessage);
    }

    @Test
    public void testEmptyPassword() {
        String email = "test@example.com";
        String emptyPassword = "";
        String expectedErrorMessage = "Password is empty";
        String actualErrorMessage = Validation.validate(email, emptyPassword);
        assertEquals(expectedErrorMessage, actualErrorMessage);
    }

    @Test
    public void testPasswordLessThan6() {
        String email = "test@example.com";
        String name = "aman";
        String password = "123";
        String expectedErrorMessage = "Password must be greater than 6 characters";
        String actualErrorMessage = Validation.validate(email, password, name);
        assertEquals(expectedErrorMessage, actualErrorMessage);
    }

    @Test
    public void testInvalidEmailFormat() {
        String invalidEmail = "invalid_email";
        String password = "password123";
        String expectedErrorMessage = "Invalid Email";
        String actualErrorMessage = Validation.validate(invalidEmail, password);
        assertEquals(expectedErrorMessage, actualErrorMessage);
    }
}
