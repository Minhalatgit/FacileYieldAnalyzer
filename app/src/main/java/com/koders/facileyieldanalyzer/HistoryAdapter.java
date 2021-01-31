package com.koders.facileyieldanalyzer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryHolder> {

    private List<FirebaseModel> list;

    public HistoryAdapter(List<FirebaseModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HistoryHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.crop_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryHolder holder, int position) {
        FirebaseModel firebaseModel = list.get(position);
        holder.moisture.setText(firebaseModel.Moisture);
        holder.ph.setText(firebaseModel.PH);
        holder.date.setText(getDateTime(firebaseModel.createdAt));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class HistoryHolder extends RecyclerView.ViewHolder {

        private TextView moisture, ph, date;

        public HistoryHolder(@NonNull View itemView) {
            super(itemView);
            moisture = itemView.findViewById(R.id.moisture);
            ph = itemView.findViewById(R.id.ph);
            date = itemView.findViewById(R.id.date);
        }

    }

    private String getDateTime(long timeInMillis) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        return formatter.format(calendar.getTime());
    }

}
