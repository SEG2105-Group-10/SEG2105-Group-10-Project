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

public class EventJoinAdapter extends RecyclerView.Adapter<EventJoinAdapter.ViewHolder> {

    private final List<Event> eventList;
    private final OnJoinClickListener listener;

    public interface OnJoinClickListener {
        void onJoinClick(Event event);
    }

    public EventJoinAdapter(List<Event> eventList, OnJoinClickListener listener) {
        this.eventList = eventList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_join, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.textViewName.setText(event.getName());
        holder.textViewDateTime.setText(event.getDate() + " " + event.getTime());
        holder.buttonJoin.setText("Join");
        holder.buttonJoin.setOnClickListener(v -> listener.onJoinClick(event));
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewDateTime;
        Button buttonJoin;

        ViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewDateTime = itemView.findViewById(R.id.textViewDateTime);
            buttonJoin = itemView.findViewById(R.id.buttonJoin);  // ✅ FIXED: was incorrectly using buttonEdit
        }
    }
}
