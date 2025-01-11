package com.mirza.pulse;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

/**
 *  RegisterActivity allows a new user to register an account with an email and password.
 *  It utilizes Firebase Authentication to handle the registration process.
 */
public class RegisterActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button registerButton;
    private FirebaseAuth mAuth;

    /**
     * Called when the activity is first created. This is where you should do all of your normal
     * static set up: create views, bind data to lists, etc. This method also provides you with a
     * Bundle containing the activity's previously frozen state, if there was one.
     *
     * This implementation initializes the activity, sets the layout, configures the toolbar,
     * retrieves Firebase authentication instance, and initializes UI elements.
     * It also sets an OnClickListener for the register button to initiate user registration.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut
     *     down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *     Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(v -> registerUser());
    }

    /**
     * Registers a new user with the provided email and password using Firebase Authentication.
     *
     * This method retrieves the email and password from the respective EditText fields.
     * It then performs the following actions:
     * 1. **Input Validation:** Checks if the email or password fields are empty. If either is empty,
     *    a Toast message is displayed to the user, and the registration process is terminated.
     * 2. **Firebase Registration:** If both fields are populated, it attempts to create a new user
     *    account using Firebase Authentication's `createUserWithEmailAndPassword` method.
     * 3. **Success Handling:** If the user creation is successful, a success Toast message is displayed,
     *    and the current Activity is finished, returning the user to the previous Activity (likely the Login Activity).
     * 4. **Failure Handling:** If the user creation fails, a failure Toast message is displayed to the user.
     *
     * This method utilizes the `mAuth` Firebase Authentication instance to interact with the Firebase authentication service.
     *
     * @see FirebaseAuth#createUserWithEmailAndPassword(String, String)
     */
    private void registerUser() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Registration success
                        Toast.makeText(this, "Registration Successful.", Toast.LENGTH_SHORT).show();
                        finish(); // Go back to Login Activity
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this, "Registration failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}