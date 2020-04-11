package ru.barcats.tempo_leader_javanew.ui.raskladki;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import ru.barcats.tempo_leader_javanew.R;
import ru.barcats.tempo_leader_javanew.ui.raskladki.adapters.SectionsPagerAdapter;
import ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags.like_frag.TabBarLikeFragment;
import ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags.sec_frag.TabBarSecFragment;
import ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags.temp_frag.TabBarTempFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.material.tabs.TabLayout;

public class RaskladkiMainFragment extends Fragment {

    static String TAG = "33333";
    private SectionsPagerAdapter pagerAdapter;
    public ViewPager viewPager;
    private Fragment secFrag, tempFrag, likeFrag;
    int currentItem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_raskladki,container, false);
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

        return view;
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_raskladki);
//
//
//        //создаём фрагменты
//        secFrag = TabBarSecFragment.newInstance(0);
//        tempFrag = TabBarTempFragment.newInstance(1);
//        likeFrag = TabBarLikeFragment.newInstance(2);
//
////                //когда прилетает интент , например из темполидера
////        Intent intent = getIntent();
////        if (getIntent()!=null){
////        if (intent.getExtras()!=null){
////            String type = intent.getStringExtra(P.TYPE_OF_FILE);
////            switch (type){
////                case P.TYPE_TIMEMETER:
////                    currentItem = 0;
////                    break;
////                case P.TYPE_TEMPOLEADER:
////                    currentItem = 1;
////                    break;
////                case P.TYPE_LIKE:
////                    currentItem = 2;
////                    break;
////                default:
////                    currentItem = 0;
////                    break;
////            }
////        }
////        }
//    }

//    @Override
//    public void onResume() {
//        super.onResume();
//
//    }

    //
//        //метод из PageAdapter, при return POSITION_NONE позволяет обновить адаптер с помощью
//        //метода notifyDataSetChanged() из любого фрагмента
//        @Override
//        public int getItemPosition(Object object) {
//            // POSITION_NONE makes it possible to reload the PagerAdapter
//            return POSITION_NONE;
//        }
//    }
}
