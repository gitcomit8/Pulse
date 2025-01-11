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

import com.mirza.pulse.R;
import com.mirza.pulse.adapters.UserAdapter;
import com.mirza.pulse.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText searchEditText;
    private RecyclerView userRecyclerView;
    private TextView emptyListTextView;
    private ProgressBar progressBar;
    private UserAdapter adapter; // Declare the adapter as a class member
    private List<User> users; // Declare the user list as a class member

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
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // Add back button

        // Set up the RecyclerView
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create a dummy list of users (replace this with actual data later)
        users = new ArrayList<>();
        users.add(new User("user1", "user1@example.com", "User One", ""));
        users.add(new User("user2", "user2@example.com", "User Two", ""));
        users.add(new User("user3", "user3@example.com", "User Three", ""));

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
        hideLoading();
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