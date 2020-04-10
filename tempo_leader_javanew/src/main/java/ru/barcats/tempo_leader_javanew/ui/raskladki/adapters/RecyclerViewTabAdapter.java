package ru.barcats.tempo_leader_javanew.ui.raskladki.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.barcats.tempo_leader_javanew.R;

public class RecyclerViewTabAdapter extends
        RecyclerView.Adapter<RecyclerViewTabAdapter.ViewHolder> {

    ArrayList<String> data;
    OnClickOnLineListener onLineListener;

    public RecyclerViewTabAdapter(ArrayList<String> data) {
        if (data != null){
            this.data = data;
        }else {
            this.data = new ArrayList<>();
        }
    }

    public interface OnClickOnLineListener{
        void onClickOnLineListener(String nameItem);
    }

    public void setOnClickOnLineListener(OnClickOnLineListener onLineListener){
        this.onLineListener = onLineListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_single, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name_of_rasckadka.setText(data.get(position));
    }

    @Override
    public int getItemCount() {

        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name_of_rasckadka;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name_of_rasckadka = itemView.findViewById(R.id.name_of_rasckadka);
        }
    }
}
