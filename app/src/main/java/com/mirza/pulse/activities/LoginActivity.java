package com.mirza.pulse.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.mirza.pulse.MainActivity;
import com.mirza.pulse.R;

/**
 *  LoginActivity is responsible for handling user authentication.
 *  It provides functionality for users to log in using their email and password,
 *  or through Google Sign-In. It also provides a link to the registration activity for new users.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private ActivityResultLauncher<Intent> googleSignInLauncher;

    /**
     * Called when the activity is first created. This is where most initialization
     * should go: calling setContentView(int) to inflate the activity's UI,
     * using findViewById(int) to retrieve widgets in the UI that will be interacted with,
     * setting up listeners for those widgets, and initializing any other required components.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in onSaveInstanceState(Bundle). Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        Button loginButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.registerButton);
        Button googleSignInButton = findViewById(R.id.googleSignInButton);

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleSignInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        try {
                            // Google Sign In was successful, authenticate with Firebase
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            firebaseAuthWithGoogle(account);
                        } catch (ApiException e) {
                            // Google Sign In failed, update UI appropriately
                            Toast.makeText(this, "Google sign in failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        loginButton.setOnClickListener(v -> loginUser());

        registerButton.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

        googleSignInButton.setOnClickListener(v -> signInWithGoogle());
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        googleSignInLauncher.launch(signInIntent);
    }

    /**
     * Authenticates the user with Firebase using their Google account credentials.
     * <p>
     * This method takes a GoogleSignInAccount object, extracts the ID token, and uses it to
     * create Firebase credentials. It then attempts to sign in the user with these credentials.
     * Upon successful sign-in, it navigates to the MainActivity and finishes the current activity.
     * If sign-in fails, it displays a toast message indicating the failure.
     *
     * @param acct The GoogleSignInAccount object containing the user's Google account information,
     *             specifically the ID token required for authentication.
     */
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(this, "Google Sign In Successful.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this, "Firebase Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Attempts to log in a user with the provided email and password.
     * <p>
     * This method retrieves the email and password from the respective EditText fields.
     * It performs basic input validation to ensure that both fields are not empty.
     * If the input is valid, it uses Firebase Authentication to attempt to sign in the user.
     * On successful login, it displays a success message and navigates to the MainActivity.
     * On failed login, it displays an authentication failure message.
     */
    private void loginUser() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Login success
                        Toast.makeText(this, "Login Successful.", Toast.LENGTH_SHORT).show();
                        // Start the main activity or other relevant activity
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();//prevents the user from going back to the login screen
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}