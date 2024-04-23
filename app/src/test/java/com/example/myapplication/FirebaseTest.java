package com.example.myapplication;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class FirebaseTest {

    @Mock
    FirebaseAuth mockFirebaseAuth;

    @Mock
    Task<AuthResult> mockAuthResultTask;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testValidLoginCreds() {
        AuthenticationManager authManager = new AuthenticationManager(mockFirebaseAuth);

        when(mockFirebaseAuth.signInWithEmailAndPassword(anyString(), anyString()))
                .thenReturn(Tasks.forResult(null));

        Task<AuthResult> result = authManager.loginUser("aman@gmail.com", "12345678");

        assertNotNull(result);
    }

    @Test
    public void testInvalidLoginCreds() {
        AuthenticationManager authManager = new AuthenticationManager(mockFirebaseAuth);

        Exception exception = new Exception("Invalid credentials");
        when(mockFirebaseAuth.signInWithEmailAndPassword(anyString(), anyString()))
                .thenReturn(Tasks.forException(exception));

        Task<AuthResult> result = authManager.loginUser("aman@gmail.com", "12");

        try {
            result.getResult();
        } catch (Exception e) {
            assertEquals("java.lang.Exception: Invalid credentials", e.getMessage());
        }
    }

    @Test
    public void testValidCredsSignup() {
        AuthenticationManager authManager = new AuthenticationManager(mockFirebaseAuth);

        when(mockFirebaseAuth.createUserWithEmailAndPassword(anyString(), anyString()))
                .thenReturn(Tasks.forResult(null));

        Task<AuthResult> result = authManager.registerUser("aman@gmail.com", "12345678");

        assertNotNull(result);
    }

    @Test
    public void testInvalidEmailSignup() {
        AuthenticationManager authManager = new AuthenticationManager(mockFirebaseAuth);

        Exception exception = new Exception("Invalid email");
        when(mockFirebaseAuth.createUserWithEmailAndPassword(anyString(), anyString()))
                .thenReturn(Tasks.forException(exception));

        Task<AuthResult> result = authManager.registerUser("invalid123", "password");

        try {

            result.getResult();
        } catch (Exception e) {
            assertEquals("java.lang.Exception: Invalid email", e.getMessage());
        }
    }
}
