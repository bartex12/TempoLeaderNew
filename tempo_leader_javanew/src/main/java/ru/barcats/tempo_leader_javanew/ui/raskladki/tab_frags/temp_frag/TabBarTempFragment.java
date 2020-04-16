package ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags.temp_frag;

import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import ru.barcats.tempo_leader_javanew.R;
import ru.barcats.tempo_leader_javanew.model.P;
import ru.barcats.tempo_leader_javanew.ui.raskladki.adapters.RecyclerViewTabAdapter;
import ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags.AbstrTabFragment;

public class TabBarTempFragment extends AbstrTabFragment {

    private  TempViewModel tempViewModel;

    public static TabBarTempFragment newInstance(int numberItem){
        Bundle args = new Bundle();
        args.putInt(P.ARG_NUMBER_ITEM,numberItem);
        TabBarTempFragment fragment = new TabBarTempFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "// AbstrTabFragment onViewCreated // " );

        tempViewModel =
                ViewModelProviders.of(this).get(TempViewModel.class);
        tempViewModel.getRascladki()
                .observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
                    @Override
                    public void onChanged(ArrayList<String> strings) {
                        initRecycler(strings);
                    }
                });
    }

    //создаём контекстное меню для списка
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, P.DELETE_ACTION_TEMP, 10, "Удалить запись");
        menu.add(0, P.CHANGE_ACTION_TEMP, 20, "Изменить запись");
        menu.add(0, P.MOVE_SEC_ACTION_TEMP, 30, "Переместить в секундомер");
        menu.add(0, P.MOVE_LIKE_ACTION_TEMP, 40, "Переместить в избранное");
        menu.add(0, P.CANCEL_ACTION_TEMP, 50, "Отмена");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        handleMenuItemClick(item);
        return super.onContextItemSelected(item);
    }

    private void handleMenuItemClick(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case P.DELETE_ACTION_TEMP: {
                showDeleteDialog();
                getAdapter().notifyDataSetChanged();
                break;
            }
            case P.CHANGE_ACTION_TEMP: {

                break;
            }
            case P.MOVE_SEC_ACTION_TEMP: {

                break;
            }
            case P.MOVE_LIKE_ACTION_TEMP: {
                //TODO
                break;
            }
            case P.CANCEL_ACTION_TEMP: {
                //TODO
                break;
            }
        }
    }

    @Override
    protected void doDeleteAction(String fileName) {
        tempViewModel.loadDataDeleteItem(fileName);
    }

}
