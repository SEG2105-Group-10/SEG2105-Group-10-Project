package com.example.localloop.ui.category;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localloop.R;
import com.example.localloop.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private final List<User> users;
    private final OnUserActionListener listener;

    public interface OnUserActionListener {
        void onDeleteClicked(User user);
        void onToggleStatusClicked(User user);
    }

    public UserAdapter(List<User> users, OnUserActionListener listener) {
        this.users = users;
        this.listener = listener;
    }

    public void removeUser(User user) {
        int index = users.indexOf(user);
        if (index != -1) {
            users.remove(index);
            notifyItemRemoved(index);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_manage, parent, false);
        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        holder.username.setText(user.getUsername());
        holder.role.setText(user.getRole());
        holder.status.setText(user.isDisabled() ? "Disabled" : "Active");

        holder.toggleStatus.setText(user.isDisabled() ? "Enable" : "Disable");
        holder.toggleStatus.setOnClickListener(v -> listener.onToggleStatusClicked(user));
        holder.delete.setOnClickListener(v -> listener.onDeleteClicked(user));
    }

    @Override public int getItemCount() { return users.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView username, role, status;
        Button toggleStatus, delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.textUsername);
            role = itemView.findViewById(R.id.textRole);
            status = itemView.findViewById(R.id.textStatus);
            toggleStatus = itemView.findViewById(R.id.buttonToggleStatus);
            delete = itemView.findViewById(R.id.buttonDeleteUser);
        }
    }
}
