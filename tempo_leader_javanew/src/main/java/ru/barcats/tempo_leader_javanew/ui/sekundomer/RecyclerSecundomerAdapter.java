package ru.barcats.tempo_leader_javanew.ui.sekundomer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.barcats.tempo_leader_javanew.R;
import ru.barcats.tempo_leader_javanew.model.DataSecundomer;

public class RecyclerSecundomerAdapter
        extends RecyclerView.Adapter<RecyclerSecundomerAdapter.ViewHolder> {

    private static final String TAG = "33333";
    private ArrayList<DataSecundomer> dataSec;
    private Context context;

    public RecyclerSecundomerAdapter(ArrayList<DataSecundomer> dataSec) {

        this.dataSec = dataSec;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
     View view = LayoutInflater.from(parent.getContext())
             .inflate(R.layout.list_rep, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.item_listSec.setText(dataSec.get(position).getNumber());
        holder.time_listSec.setText(dataSec.get(position).getTime());
        holder.delta_listSec.setText(dataSec.get(position).getDelta());
    }

    @Override
    public int getItemCount() {
        return dataSec.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView item_listSec;
        TextView time_listSec;
        TextView delta_listSec;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_listSec = itemView.findViewById(R.id.item_listSec);
            time_listSec = itemView.findViewById(R.id.time_listSec);
            delta_listSec = itemView.findViewById(R.id.delta_listSec);
        }
    }
}
