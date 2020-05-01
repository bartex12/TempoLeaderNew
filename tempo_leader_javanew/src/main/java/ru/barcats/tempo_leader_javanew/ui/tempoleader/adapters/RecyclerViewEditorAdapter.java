package ru.barcats.tempo_leader_javanew.ui.tempoleader.adapters;

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

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import ru.barcats.tempo_leader_javanew.R;
import ru.barcats.tempo_leader_javanew.model.DataSet;


public class RecyclerViewEditorAdapter extends RecyclerView.Adapter<RecyclerViewEditorAdapter.ViewHolder> {

    private static final String TAG = "33333";
    private ArrayList<DataSet> listOfSet;
    private Context context;
    private OnSetListClickListener onSetListClickListener;
    private OnLongClickLikeListener onLongClickLikeListener;
    private int accurancy; //точность отсечек - количество знаков после запятой
    private int posItem = -1;
    private boolean isForAll;
    private boolean isEditTime;
    private boolean isLongClick;

    public void setLongClick(boolean longClick) {
        isLongClick = longClick;
        Log.d(TAG, " +++ RecyclerViewEditorAdapter isLongClick = " + isLongClick);
    }

    public void setEditTime(boolean isEditTime) {
        this.isEditTime = isEditTime;
        Log.d(TAG, " +++ RecyclerViewEditorAdapter isEditTime = " + isEditTime);
    }

    public void setForAll(boolean isForAll) {
        this.isForAll = isForAll;
        Log.d(TAG, " +++ RecyclerViewEditorAdapter isForAll = " + isForAll);
    }

    public void setPosItem(int posItem) {
        this.posItem = posItem;
        Log.d(TAG, " +++ RecyclerViewEditorAdapter posItem = " + posItem);
    }

    public RecyclerViewEditorAdapter(ArrayList<DataSet> listOfSet, int accurancy){
        this.listOfSet =listOfSet;
        this.accurancy = accurancy;
        Log.d(TAG, "RecyclerViewEditorAdapter listOfSet.size() = "
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

    public interface OnLongClickLikeListener{
        void onLongClickLike(int position);
    }
    public void setOnLongClickLikeListenerr(OnLongClickLikeListener onLongClickLikeListener){
        this.onLongClickLikeListener = onLongClickLikeListener;
    }

    @Override
    public RecyclerViewEditorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view = LayoutInflater.from(context).inflate(
                R.layout.list_item_set_textview, parent, false);

        return new ViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.time_item_set_textview.setText(
                getTimeAccur(listOfSet.get(position).getTimeOfRep()));
        holder.reps_item_set_textview.setText(
                String.valueOf(listOfSet.get(position).getReps()));
        holder.mark_item_set_textview.setText(
                String.valueOf(listOfSet.get(position).getNumberOfFrag()));

        if (isLongClick){
            if (posItem == position){
                //holder.all_item_set_textview.setBackgroundColor(Color.YELLOW);

                holder.view_time.setBackgroundColor(Color.YELLOW);
                holder.view_reps.setBackgroundColor(Color.YELLOW);
               // holder.view_mark.setBackgroundColor(Color.YELLOW);
            }else {
//                holder.all_item_set_textview.setBackgroundColor(context.getResources()
//                        .getColor(android.R.color.transparent));

                holder.view_time.setBackgroundColor(context.getResources()
                        .getColor(android.R.color.transparent));
                holder.view_reps.setBackgroundColor(context.getResources()
                        .getColor(android.R.color.transparent));
//                holder.view_mark.setBackgroundColor(context.getResources()
//                        .getColor(android.R.color.transparent));
            }
        }else {
            if (isForAll){
                if (isEditTime){
                    holder.view_time.setBackgroundColor(Color.YELLOW);
                    holder.view_reps.setBackgroundColor(context.getResources()
                            .getColor(android.R.color.transparent));
                }else {
                    holder.view_reps.setBackgroundColor(Color.YELLOW);
                    holder.view_time.setBackgroundColor(context.getResources()
                            .getColor(android.R.color.transparent));
                }
            }else {
                if (isEditTime){
                    if (posItem == position){
                        holder.view_time.setBackgroundColor(Color.YELLOW);
                        holder.view_reps.setBackgroundColor(context.getResources()
                                .getColor(android.R.color.transparent));
                    }else {
                        holder.view_time.setBackgroundColor(context.getResources()
                                .getColor(android.R.color.transparent));
                        holder.view_reps.setBackgroundColor(context.getResources()
                                .getColor(android.R.color.transparent));
                    }
                }else {
                    if (posItem == position){
                        holder.view_reps.setBackgroundColor(Color.YELLOW);
                        holder.view_time.setBackgroundColor(context.getResources()
                                .getColor(android.R.color.transparent));
                    }else {
                        holder.view_reps.setBackgroundColor(context.getResources()
                                .getColor(android.R.color.transparent));
                        holder.view_time.setBackgroundColor(context.getResources()
                                .getColor(android.R.color.transparent));
                    }
                }
            }
        }


        //передаём позицию списка при щелчке на строке списка
        holder.all_item_set_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posItem = position;
                onSetListClickListener.onSetListClick(position);
            }
        });

        // устанавливаем слушатель долгих нажатий на списке для вызова контекстного меню
        holder.all_item_set_textview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.view_reps.setBackgroundColor(context.getResources()
                        .getColor(android.R.color.transparent));
                holder.view_time.setBackgroundColor(context.getResources()
                        .getColor(android.R.color.transparent));
                posItem = position;
                onLongClickLikeListener.onLongClickLike(position);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        //
        return listOfSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout all_item_set_textview ;
        TextView time_item_set_textview;
        TextView reps_item_set_textview;
        TextView mark_item_set_textview;

        TextView view_time;
        TextView view_reps;
        TextView view_mark;


        public ViewHolder(View itemView) {
            super(itemView);
            all_item_set_textview =  itemView.findViewById(R.id.all_item_set_textview);
            time_item_set_textview = itemView.findViewById(R.id.time_item_set_textview);
            reps_item_set_textview = itemView.findViewById(R.id.reps_item_set_textview);
            mark_item_set_textview = itemView.findViewById(R.id.mark_item_set_textview);

            view_time = itemView.findViewById(R.id.view_time);
            view_reps = itemView.findViewById(R.id.view_reps);
            view_mark = itemView.findViewById(R.id.view_mark);
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
