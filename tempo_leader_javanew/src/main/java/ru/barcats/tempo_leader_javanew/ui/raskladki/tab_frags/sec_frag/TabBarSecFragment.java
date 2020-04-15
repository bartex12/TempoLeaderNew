package ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags.sec_frag;

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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import ru.barcats.tempo_leader_javanew.R;
import ru.barcats.tempo_leader_javanew.model.P;
import ru.barcats.tempo_leader_javanew.ui.raskladki.adapters.RecyclerViewTabAdapter;
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
        Log.d(TAG, "// AbstrTabFragment onViewCreated // " );

        secViewModel =
                ViewModelProviders.of(this).get(SecViewModel.class);
        secViewModel.getRascladki()
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
        getActivity().getMenuInflater().inflate(R.menu.tab_temp_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        handleMenuItemClick(item);
        return super.onContextItemSelected(item);
    }

    private boolean handleMenuItemClick(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_delete_sec: {
                adapter.deleteLine();
                break;
            }
            case R.id.menu_change_sec: {
                item.setVisible(false);
                item.setEnabled(false);
                Log.d(TAG, "//TabBarSecFragment handleMenuItemClick menu_change// " );
                return true;
            }
            case R.id.menu_move_temp_sec: {

                return true;
            }
            case R.id.menu_move_like_sec: {
                //TODO
                return true;
            }
            case R.id.menu_cancel_sec: {
                //TODO
               return true;
            }
            default:
                return super.onContextItemSelected(item);
        }
        return super.onContextItemSelected(item);
    }

}
