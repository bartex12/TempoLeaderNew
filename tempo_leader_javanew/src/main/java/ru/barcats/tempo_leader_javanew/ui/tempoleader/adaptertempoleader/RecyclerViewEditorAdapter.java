package ru.barcats.tempo_leader_javanew.ui.tempoleader.adaptertempoleader;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import androidx.recyclerview.widget.RecyclerView;
import ru.barcats.tempo_leader_javanew.R;
import ru.barcats.tempo_leader_javanew.model.DataSet;


public class RecyclerViewEditorAdapter extends RecyclerView.Adapter<RecyclerViewEditorAdapter.ViewHolder> {

    private static final String TAG = "33333";
    private ArrayList<DataSet> listOfSet;
    private Context context;
    private OnSetListClickListener onSetListClickListener;
    private int accurancy; //точность отсечек - количество знаков после запятой
    private int item = 0;
    private int posItem = -1;
    private boolean isEnd = false;

    public void setEnd(boolean end) {
        //
        isEnd = end;
    }

    public void setPosItem(int posItem) {
        this.posItem = posItem;
    }

    public void setItem(int item) {
        this.item = item;
        Log.d(TAG, " +++ RecyclerViewTempoleaderAdapter item = " + item);
    }

    public RecyclerViewEditorAdapter(ArrayList<DataSet> listOfSet, int accurancy){
        this.listOfSet =listOfSet;
        this.accurancy = accurancy;
        Log.d(TAG, "RecyclerViewTempoleaderAdapter listOfSet.size() = "
                + listOfSet.size()+ " accurancy = " + accurancy);
    }

    //интерфейс слушателя щелчков на списке
    public interface OnSetListClickListener {
        void onSetListClick(int position);
    }
    //метод установки слушателя щелчков на списке
    public void setOnSetListClickListener(
            RecyclerViewEditorAdapter.OnSetListClickListener onSetListClickListener) {
        this.onSetListClickListener = onSetListClickListener;
    }

    @Override
    public RecyclerViewEditorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view = LayoutInflater.from(context).inflate(
                R.layout.list_item_set_textview, parent, false);

        return new ViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.time_item_set_textview.setText(
                getTimeAccur(listOfSet.get(position).getTimeOfRep()));
        holder.reps_item_set_textview.setText(
                String.valueOf(listOfSet.get(position).getReps()));
        holder.mark_item_set_textview.setText(
                String.valueOf(listOfSet.get(position).getNumberOfFrag()));
            if (position < item){
                holder.mark_item_set_textview.setBackgroundColor(Color.YELLOW);
            }else if (posItem == position){
                holder.mark_item_set_textview.setBackgroundColor(Color.YELLOW);
            }else {
                holder.mark_item_set_textview
                        .setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
            }

        holder.all_item_set_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSetListClickListener.onSetListClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        //
        return listOfSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout all_item_set_textview ;
        TextView time_item_set_textview;
        TextView reps_item_set_textview;
        TextView mark_item_set_textview;


        public ViewHolder(View itemView) {
            super(itemView);
            all_item_set_textview =  itemView.findViewById(R.id.all_item_set_textview);
            time_item_set_textview = itemView.findViewById(R.id.time_item_set_textview);
            reps_item_set_textview = itemView.findViewById(R.id.reps_item_set_textview);
            mark_item_set_textview = itemView.findViewById(R.id.mark_item_set_textview);
        }
    }

    private String getTimeAccur(float time_now){
        String s_delta = "";
        switch (accurancy){
            case 1:
                s_delta = String.format(Locale.ENGLISH,"%.01f",time_now);
                break;
            case 2:
                s_delta = String.format(Locale.ENGLISH,"%.02f",time_now);
                break;
            case 3:
                s_delta = String.format(Locale.ENGLISH,"%.03f",time_now);
                break;
            default:
                s_delta =String.format(Locale.ENGLISH,"%.01f",time_now);
        }
        return s_delta;
    }

}
