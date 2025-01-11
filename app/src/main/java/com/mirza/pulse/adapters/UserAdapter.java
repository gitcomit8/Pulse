package com.mirza.pulse.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mirza.pulse.R;
import com.mirza.pulse.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;
    private List<User> originalUserList; // Store the original list

    public UserAdapter(List<User> userList) {
        this.userList = userList;
        this.originalUserList = new ArrayList<>(userList); // Initialize the original list
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.userNameTextView.setText(user.getDisplayName());
        // Load the user's profile image using Glide
        Glide.with(holder.userImageView.getContext())
                .load(user.getProfileImageUrl())
                .placeholder(R.drawable.ic_default_user) // Optional: Placeholder image
                .into(holder.userImageView);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setUserList(List<User> newList) {
        userList = newList;
        if (newList.isEmpty()) {
            userList = new ArrayList<>(originalUserList);
        }
        notifyDataSetChanged();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public ImageView userImageView;
        public TextView userNameTextView;

        public UserViewHolder(View itemView) {
            super(itemView);
            userImageView = itemView.findViewById(R.id.userImageView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
        }
    }
}