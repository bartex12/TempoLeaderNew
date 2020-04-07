package ru.barcats.tempo_leader_javanew.ui.main;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import ru.barcats.tempo_leader_javanew.R;
import ru.barcats.tempo_leader_javanew.model.DataHome;

public class RecyclerViewMainAdapter extends RecyclerView.Adapter<RecyclerViewMainAdapter.ViewHolder> {

    private static final String TAG = "33333";
    private ArrayList<DataHome> listOfMain;
    private Context context;
    private OnMainListClickListener onMainListClickListener;

    public RecyclerViewMainAdapter(ArrayList<DataHome> listOfMain){
        this.listOfMain =listOfMain;
        Log.d(TAG, "RecyclerViewMainAdapter listOfMain.size() = " + listOfMain.size());
    }

    //интерфейс слушателя щелчков на списке
    public interface OnMainListClickListener {
        void onMainListClick(int position);
    }
    //метод установки слушателя щелчков на списке
    public void setOnMainListClickListener(OnMainListClickListener onMainListClickListener) {
        this.onMainListClickListener = onMainListClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(
                R.layout.list_item_main, parent, false);
        return new ViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.picture.setImageDrawable(listOfMain.get(position).getPicture());
        holder.base_text.setText(listOfMain.get(position).getHead());
        holder.sub_text.setText(listOfMain.get(position).getSubHead());

        holder.card_mainmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMainListClickListener.onMainListClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOfMain.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView picture;
        TextView base_text;
        TextView sub_text;
        CardView card_mainmenu;

        public ViewHolder(View itemView) {
            super(itemView);
            card_mainmenu = itemView.findViewById(R.id.card_mainmenu);
            picture = itemView.findViewById(R.id.picture);
            base_text = itemView.findViewById(R.id.base_text);
            sub_text = itemView.findViewById(R.id.sub_text);
        }
    }


}