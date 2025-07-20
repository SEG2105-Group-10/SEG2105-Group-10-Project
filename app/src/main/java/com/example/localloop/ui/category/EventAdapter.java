package com.example.localloop.ui.category;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localloop.R;
import com.example.localloop.model.Event;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private final List<Event> eventList;
    private final OnEventActionListener listener;
    private final String role;

    public interface OnEventActionListener {
        void onEditClick(Event event);
        void onDeleteClick(Event event);
        void onJoinClick(Event event);
    }

    public EventAdapter(List<Event> eventList, String role, OnEventActionListener listener) {
        this.eventList = eventList;
        this.role = role;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.textViewName.setText(event.getName());
        holder.textViewDateTime.setText(event.getDate() + " " + event.getTime());

        if ("admin".equalsIgnoreCase(role)) {
            holder.buttonEdit.setVisibility(View.VISIBLE);
            holder.buttonDelete.setVisibility(View.VISIBLE);
            holder.buttonJoin.setVisibility(View.GONE);
        } else {
            holder.buttonEdit.setVisibility(View.GONE);
            holder.buttonDelete.setVisibility(View.GONE);
            holder.buttonJoin.setVisibility(View.VISIBLE);
        }

        holder.buttonEdit.setOnClickListener(v -> listener.onEditClick(event));
        holder.buttonDelete.setOnClickListener(v -> listener.onDeleteClick(event));
        holder.buttonJoin.setOnClickListener(v -> listener.onJoinClick(event));
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewDateTime;
        Button buttonEdit, buttonDelete, buttonJoin;

        ViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewDateTime = itemView.findViewById(R.id.textViewDateTime);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            buttonJoin = itemView.findViewById(R.id.buttonJoin);
        }
    }
}
