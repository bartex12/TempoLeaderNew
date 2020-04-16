package ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags.sec_frag;

import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import ru.barcats.tempo_leader_javanew.model.P;
import ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags.AbstrTabFragment;

public class TabBarSecFragment extends AbstrTabFragment {

    private SecViewModel secViewModel;

    public static TabBarSecFragment newInstance(int numberItem) {
        Bundle args = new Bundle();
        args.putInt(P.ARG_NUMBER_ITEM, numberItem);
        TabBarSecFragment fragment = new TabBarSecFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "// TabBarSecFragment onViewCreated // " );

        secViewModel =
                ViewModelProviders.of(this).get(SecViewModel.class);
        secViewModel.getRascladki()
                .observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
                    @Override
                    public void onChanged(ArrayList<String> strings) {
                        initRecyclerAdapter(strings);
                    }
                });

}

    //создаём контекстное меню для списка onCreateContextMenu вызывается один раз
    @Override
    public void onCreateContextMenu(
            @NotNull ContextMenu menu, @NotNull View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, P.DELETE_ACTION_SEC, 10, "Удалить запись");
        menu.add(0, P.CHANGE_ACTION_SEC, 20, "Изменить запись");
        menu.add(0, P.MOVE_TEMP_ACTION_SEC, 30, "Переместить в темполидер");
        menu.add(0, P.MOVE_LIKE_ACTION_SEC, 40, "Переместить в избранное");
        menu.add(0, P.CANCEL_ACTION_SEC, 50, "Отмена");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        handleMenuItemClick(item);
        return super.onContextItemSelected(item);
    }

    private void handleMenuItemClick(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case P.DELETE_ACTION_SEC: {
                showDeleteDialog();
                getAdapter().notifyDataSetChanged();
                break;
            }
            case P.CHANGE_ACTION_SEC: {

                break;
            }
            case P.MOVE_TEMP_ACTION_SEC: {

                break;
            }
            case P.MOVE_LIKE_ACTION_SEC: {
                //TODO
                break;
            }
            case P.CANCEL_ACTION_SEC: {
                //TODO
                break;
            }
        }
    }

    @Override
    protected void doDeleteAction(String fileName) {
        //поручаем удаление файла ViewModel
        secViewModel.loadDataDeleteItem(fileName);
    }

}
