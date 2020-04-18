package ru.barcats.tempo_leader_javanew.ui.raskladki.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.recyclerview.widget.RecyclerView;
import ru.barcats.tempo_leader_javanew.R;
import ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags.like_frag.LikeViewModel;
import ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags.like_frag.TabBarLikeFragment;

public class RecyclerViewTabAdapter extends
        RecyclerView.Adapter<RecyclerViewTabAdapter.ViewHolder> {

    private static final String TAG = "33333";
    private ArrayList<String> data;
    private  OnClickOnLineListener onLineListener;
    private OnLongClickLikeListener onLongClickLikeListener;
    private int posItem;  //позиция выбранного элемента списка
    private String fileName;  //имя выбранного элемента списка


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

    public interface OnLongClickLikeListener{
        void onLongClickLike(String nameItem);
    }
    public void setOnLongClickLikeListenerr(OnLongClickLikeListener onLongClickLikeListener){
        this.onLongClickLikeListener = onLongClickLikeListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_single, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.name_of_rasckadka.setText(data.get(position));

        //устанавливаем слущатель щелчков на списке
        holder.name_of_rasckadka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posItem = position;
                fileName = data.get(position);
                onLineListener.onClickOnLineListener(fileName);
            }
        });
        // устанавливаем слушатель долгих нажатий на списке для вызова контекстного меню
        //запоминаем позицию в списке - нужно при удалении, например
        holder.name_of_rasckadka.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                posItem = position;
                fileName = data.get(position);
                onLongClickLikeListener.onLongClickLike(fileName);
                return false;
            }
        });
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
