package ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags.sec_frag;

import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import ru.barcats.tempo_leader_javanew.R;
import ru.barcats.tempo_leader_javanew.model.P;
import ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags.AbstrTabFragment;

public class TabBarSecFragment extends AbstrTabFragment {

    public static final String TAG = "33333";
    private SecViewModel secViewModel;
    private View view;

    public static TabBarSecFragment newInstance(int numberItem) {
        Bundle args = new Bundle();
        args.putInt(P.ARG_NUMBER_ITEM, numberItem);
        TabBarSecFragment fragment = new TabBarSecFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "// TabBarSecFragment onViewCreated // " );
        this.view = view;
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
        menu.add(0, P.MOVE_SHOW_GRAF_SEC, 5, "Показать на графике");
        menu.add(0, P.DELETE_ACTION_SEC, 10, "Удалить запись");
        menu.add(0, P.CHANGE_ACTION_SEC, 20, "Изменить запись");
        menu.add(0, P.MOVE_TEMP_ACTION_SEC, 30, "Переместить в темполидер");
        menu.add(0, P.MOVE_LIKE_ACTION_SEC, 40, "Переместить в избранное");
        menu.add(0, P.CANCEL_ACTION_SEC, 50, "Отмена");

        if (getFileName().equals(P.FILENAME_OTSECHKI_SEC)) {
            menu.findItem(P.DELETE_ACTION_SEC).setEnabled(false);
            menu.findItem(P.CHANGE_ACTION_SEC).setEnabled(false);
            menu.findItem(P.MOVE_TEMP_ACTION_SEC).setEnabled(false);
            menu.findItem(P.MOVE_LIKE_ACTION_SEC).setEnabled(false);
            //выводим сообщение Системный файл. Действия ограничены.
            Toast.makeText(getActivity(), getResources()
                    .getString(R.string.system_file_limited),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        handleMenuItemClick(item);
        return super.onContextItemSelected(item);
    }

    private void handleMenuItemClick(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case P.MOVE_SHOW_GRAF_SEC:
                NavController controller = Navigation.findNavController(getView());
                Bundle bundle = new Bundle();
                bundle.putString(P.NAME_OF_FILE, getFileName());
                controller.navigate(R.id.action_nav_rascladki_to_nav_grafic, bundle);
                break;

            case P.DELETE_ACTION_SEC:
                showDeleteDialog();
                getAdapter().notifyDataSetChanged();
                break;

            case P.CHANGE_ACTION_SEC:
                Log.d(TAG, "// TabBarSecFragment CHANGE_ACTION_SEC getFileName() = " + getFileName());
                showChangeDialog();
                getAdapter().notifyDataSetChanged();
                break;

            case P.MOVE_TEMP_ACTION_SEC:
                Log.d(TAG, "// TabBarSecFragment  getFileName() = " + getFileName());
                    //поручаем перемещение файла ViewModel
                    secViewModel.moveItemInTemp(getFileName());
                    //обновляем список вкладки после перемещения файла
                    getAdapter().notifyDataSetChanged();
                    // обновляем вкладки после перемещения файла
                    getViewPager().getAdapter().notifyDataSetChanged(); //работает !
                break;

            case P.MOVE_LIKE_ACTION_SEC:
                Log.d(TAG, "// TabBarSecFragment  getFileName() = " + getFileName());
                    //поручаем перемещение файла ViewModel
                    secViewModel.moveItemInLike(getFileName());
                    //обновляем список вкладки после перемещения файла
                    getAdapter().notifyDataSetChanged();
                    // обновляем вкладки после перемещения файла
                    getViewPager().getAdapter().notifyDataSetChanged(); //работает !
                break;

            case P.CANCEL_ACTION_SEC:
                break;

        }
    }

    @Override
    protected void doDeleteAction(String fileName) {
        //поручаем удаление файла ViewModel
        secViewModel.loadDataDeleteItem(fileName);
    }

    @Override
    protected String getDateAndTime(String fileName) {
        return secViewModel.getDateAndTime(fileName);
    }

    @Override
    protected void doChangeAction(String fileNameOld, String fileNameNew) {
        //поручаем удаление файла ViewModel
        secViewModel.doChangeAction(fileNameOld, fileNameNew);
    }

    @Override
    protected long getIdFromFileName(String fileName) {
        return secViewModel.getIdFromFileName(fileName);
    }

}
