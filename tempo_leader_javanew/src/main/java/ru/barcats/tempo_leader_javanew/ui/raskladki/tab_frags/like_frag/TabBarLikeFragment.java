package ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags.like_frag;


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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import ru.barcats.tempo_leader_javanew.R;
import ru.barcats.tempo_leader_javanew.model.P;
import ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags.AbstrTabFragment;

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
        Log.d(TAG, "// TabBarLikeFragment onViewCreated // " );

        likeViewModel =
                ViewModelProviders.of(this).get(LikeViewModel.class);
        likeViewModel.getRascladki()
                .observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
                    @Override
                    public void onChanged(ArrayList<String> strings) {
                        initRecyclerAdapter(strings);

                    }
                });
    }

    //создаём контекстное меню для списка - не из hml файла, так как разные пункты на вкладках
    @Override
    public void onCreateContextMenu(
            @NotNull ContextMenu menu, @NotNull View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, P.MOVE_SHOW_GRAF_LIKE, 5, "Показать на графике");
        menu.add(0, P.DELETE_ACTION_LIKE, 10, "Удалить запись");
        menu.add(0, P.CHANGE_ACTION_LIKE, 20, "Изменить запись");
        menu.add(0, P.MOVE_SEC_ACTION_LIKE, 30, "Переместить в секундомер");
        menu.add(0, P.MOVE_TEMP_ACTION_LIKE, 40, "Переместить в темполидер");
        menu.add(0, P.CANCEL_ACTION_LIKE, 50, "Отмена");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        handleMenuItemClick(item);
        return super.onContextItemSelected(item);
    }

    private void handleMenuItemClick(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case P.MOVE_SHOW_GRAF_LIKE:
                NavController controller = Navigation.findNavController(getView());
                Bundle bundle = new Bundle();
                bundle.putString(P.NAME_OF_FILE, getFileName());
                controller.navigate(R.id.action_nav_rascladki_to_nav_grafic, bundle);
                break;

            case P.DELETE_ACTION_LIKE:
                showDeleteDialog();
                getAdapter().notifyDataSetChanged();
                break;

            case P.CHANGE_ACTION_LIKE:
                showChangeDialog();
                getAdapter().notifyDataSetChanged();
                break;

            case  P.MOVE_SEC_ACTION_LIKE:
                //поручаем перемещение файла ViewModel
                likeViewModel.moveItemInSec(getFileName());
                //обновляем список вкладки после перемещения файла
                getAdapter().notifyDataSetChanged();
                // обновляем вкладки после перемещения файла
                getViewPager().getAdapter().notifyDataSetChanged(); //работает !
                break;

            case P.MOVE_TEMP_ACTION_LIKE:
                //поручаем перемещение файла ViewModel
                likeViewModel.moveItemInTemp(getFileName());
                //обновляем список вкладки после перемещения файла
                getAdapter().notifyDataSetChanged();
                // обновляем вкладки после перемещения файла
                getViewPager().getAdapter().notifyDataSetChanged(); //работает !
                break;

            case  P.CANCEL_ACTION_LIKE:
                break;
        }
    }

    @Override
    protected void doDeleteAction(String fileName) {
        //поручаем удаление файла ViewModel
        likeViewModel.loadDataDeleteItem(fileName);
    }

        @Override
    protected String getDateAndTime(String fileName) {
        return likeViewModel.getDateAndTime(fileName);
    }

    @Override
    protected void doChangeAction(String fileNameOld, String fileNameNew) {
        //поручаем удаление файла ViewModel
        likeViewModel.doChangeAction(fileNameOld, fileNameNew);
    }

    @Override
    protected long getIdFromFileName(String fileName) {
        return likeViewModel.getIdFromFileName(fileName);
    }

}
