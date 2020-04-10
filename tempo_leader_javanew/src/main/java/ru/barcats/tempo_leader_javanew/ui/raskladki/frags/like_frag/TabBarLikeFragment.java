package ru.barcats.tempo_leader_javanew.ui.raskladki.frags.like_frag;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import ru.barcats.tempo_leader_javanew.model.P;
import ru.barcats.tempo_leader_javanew.ui.raskladki.frags.sec_frag.SecViewModel;
import ru.barcats.tempo_leader_javanew.ui.raskladki.adapters.RecyclerViewTabAdapter;
import ru.barcats.tempo_leader_javanew.ui.raskladki.frags.AbstrTabFragment;

public class TabBarLikeFragment extends AbstrTabFragment {

    private LikeViewModel likeViewModel;

    public static TabBarLikeFragment newInstance(int numberItem){
        Bundle args = new Bundle();
        args.putInt(P.ARG_NUMBER_ITEM,numberItem);
        TabBarLikeFragment fragment = new TabBarLikeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "// AbstrTabFragment onViewCreated // " );

        //TODO у каждого фрагмента свой ViewModel
        likeViewModel =
                ViewModelProviders.of(this).get(LikeViewModel.class);
        likeViewModel.getRascladki()
                .observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
                    @Override
                    public void onChanged(ArrayList<String> strings) {
                        initRecycler(view, strings);
                    }
                });

        //объявляем о регистрации контекстного меню
        registerForContextMenu(recyclerView);
    }

    @Override
    public RecyclerViewTabAdapter.OnClickOnLineListener getOnClickOnLineListener() {
        return new RecyclerViewTabAdapter.OnClickOnLineListener() {
            @Override
            public void onClickOnLineListener(String nameItem) {
                //TODO
            }
        };
    }


}
