package ru.barcats.tempo_leader_javanew.ui.tempoleader;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import ru.barcats.tempo_leader_javanew.R;
import ru.barcats.tempo_leader_javanew.model.DataSet;


public class RecyclerViewTempoleaderAdapter extends RecyclerView.Adapter<RecyclerViewTempoleaderAdapter.ViewHolder> {

    private static final String TAG = "33333";
    private ArrayList<DataSet> listOfSet;
    private Context context;
    private OnSetListClickListener onSetListClickListener;

    public RecyclerViewTempoleaderAdapter(ArrayList<DataSet> listOfSet){
        this.listOfSet =listOfSet;
        Log.d(TAG, "RecyclerViewTempoleaderAdapter listOfSet.size() = " + listOfSet.size());
    }

    //интерфейс слушателя щелчков на списке
    public interface OnSetListClickListener {
        void onSetListClick(int position);
    }
    //метод установки слушателя щелчков на списке
    public void setOnSetListClickListener(
            RecyclerViewTempoleaderAdapter.OnSetListClickListener onSetListClickListener) {
        this.onSetListClickListener = onSetListClickListener;
    }

    @Override
    public RecyclerViewTempoleaderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(
                R.layout.list_item_set_textview, parent, false);
        return new ViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.time_item_set_textview.setText(
                String.valueOf(listOfSet.get(position).getTimeOfRep()));
        holder.reps_item_set_textview.setText(
                String.valueOf(listOfSet.get(position).getReps()));
        holder.mark_item_set_textview.setText(
                String.valueOf(listOfSet.get(position).getNumberOfFrag()));

//        holder.card_mainmenu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onMainListClickListener.onMainListClick(position);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return listOfSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView time_item_set_textview;
        TextView reps_item_set_textview;
        TextView mark_item_set_textview;


        public ViewHolder(View itemView) {
            super(itemView);
            time_item_set_textview = itemView.findViewById(R.id.time_item_set_textview);
            reps_item_set_textview = itemView.findViewById(R.id.reps_item_set_textview);
            mark_item_set_textview = itemView.findViewById(R.id.mark_item_set_textview);
        }
    }
}
