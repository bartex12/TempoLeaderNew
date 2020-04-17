package ru.barcats.tempo_leader_javanew.ui.raskladki;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import ru.barcats.tempo_leader_javanew.R;
import ru.barcats.tempo_leader_javanew.ui.raskladki.adapters.RecyclerViewTabAdapter;
import ru.barcats.tempo_leader_javanew.ui.raskladki.adapters.SectionsPagerAdapter;
import ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags.AbstrTabFragment;
import ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags.like_frag.TabBarLikeFragment;
import ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags.sec_frag.TabBarSecFragment;
import ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags.temp_frag.TabBarTempFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

public class RaskladkiMainFragment extends Fragment {

    static String TAG = "33333";
    private SectionsPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private Fragment secFrag, tempFrag, likeFrag;
    int currentItem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_raskladki,container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //создаём фрагменты
        secFrag = TabBarSecFragment.newInstance(0);
        tempFrag = TabBarTempFragment.newInstance(1);
        likeFrag = TabBarLikeFragment.newInstance(2);

        //здесь используется вариант  добавления фрагментов из активити
        pagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        pagerAdapter.addFragment(secFrag, "Секундомер" );
        pagerAdapter.addFragment(tempFrag, "Темполидер" );
        pagerAdapter.addFragment(likeFrag, "Избранное" );

        viewPager = view.findViewById(R.id.container_raskladki_activity);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(0);

        TabLayout tabs = view.findViewById(R.id.tabs_raskladki_activity);
        tabs.setTabTextColors(Color.WHITE, Color.GREEN);
        tabs.setupWithViewPager(viewPager);

        //initFab(view);

    }

    private void initFab(View view) {
        FloatingActionButton fab = view.findViewById(R.id.fab_rascladki);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentItem = viewPager.getCurrentItem();
                switch (currentItem){
                    case 0:
                     //TODO
                        Snackbar.make(view, "Переделать", Snackbar.LENGTH_SHORT).show();
                        break;
                    case 1:
                        //TODO
                        Snackbar.make(view, "Переделать", Snackbar.LENGTH_SHORT).show();
                        break;
                    case 2:
                        //TODO
                        Snackbar.make(view, "Переделать", Snackbar.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

}
