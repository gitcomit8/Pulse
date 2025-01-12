package com.mirza.pulse.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mirza.pulse.R;
import com.mirza.pulse.adapters.UserAdapter;
import com.mirza.pulse.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText searchEditText;
    private RecyclerView userRecyclerView;
    private TextView emptyListTextView;
    private ProgressBar progressBar;
    private UserAdapter adapter; // Declare the adapter as a class member
    private List<User> users; // Declare the user list as a class member
    private FirebaseFirestore db; // Declare Firestore

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE); // Correct constant
        userRecyclerView.setVisibility(View.GONE); // Correct constant
        emptyListTextView.setVisibility(View.GONE); // Correct constant
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE); // Correct constant
        userRecyclerView.setVisibility(View.VISIBLE); // Correct constant
    }

    private void showEmptyList() {
        emptyListTextView.setVisibility(View.VISIBLE); // Correct constant
        userRecyclerView.setVisibility(View.GONE); // Correct constant
    }

    private void hideEmptyList() {
        emptyListTextView.setVisibility(View.GONE); // Correct constant
        userRecyclerView.setVisibility(View.VISIBLE); // Correct constant
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        // Initialize UI elements
        toolbar = findViewById(R.id.toolbar);
        searchEditText = findViewById(R.id.searchEditText);
        userRecyclerView = findViewById(R.id.userRecyclerView);
        emptyListTextView = findViewById(R.id.emptyListTextView);
        progressBar = findViewById(R.id.progressBar);

        // Set up the toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Add back button

        // Set up the RecyclerView
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Create an empty list of users
        users = new ArrayList<>();

        // Create the adapter
        adapter = new UserAdapter(users);

        // Set the adapter on the RecyclerView
        userRecyclerView.setAdapter(adapter);

        // Add TextWatcher to the searchEditText
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Filter the list of users based on the text entered
                filterUsers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        showLoading();
        // Load the users from Firestore
        loadUsers();
    }

    private void loadUsers() {
        db.collection("users") // Replace "users" with your collection name
                .get()
                .addOnCompleteListener(task -> {
                    hideLoading();
                    if (task.isSuccessful()) {
                        users.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            User user = document.toObject(User.class);
                            users.add(user);
                        }
                        adapter.setUserList(users);
                        if (users.isEmpty()) {
                            showEmptyList();
                        } else {
                            hideEmptyList();
                        }
                    } else {
                        // Handle errors
                        showEmptyList();
                    }
                });
    }

    private void filterUsers(String text) {
        List<User> filteredList = new ArrayList<>();
        for (User user : users) {
            if (user.getDisplayName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(user);
            }
        }
        // Update the adapter with the filtered list
        adapter.setUserList(filteredList);
        if (filteredList.isEmpty()) {
            showEmptyList();
        } else {
            hideEmptyList();
        }
    }
}