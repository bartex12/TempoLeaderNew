package ru.barcats.tempo_leader_javanew.ui.raskladki.adapters;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SectionsPagerAdapter  extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ArrayList<String> tabTitles = new ArrayList<>();

    public SectionsPagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    public void addFragment(Fragment fragment, String tabTitle){
        this.fragments.add(fragment);
        this.tabTitles.add(tabTitle);
    }

    public void remove(int position){
        //получаем фрагмент из списка фрагментов в зависимости от позиции вкладки
        Fragment fragment = getItem(position);
        switch (position){
            case 0:
               // ((TabBarSecFragment) fragment).getAdapter().removeElement(position);
                break;
            case 1:
               // ((TabBarTempFragment) fragment).getAdapter().removeElement(position);
                break;
            case 2:
                //((TabBarLikeFragment) fragment).getAdapter().removeElement(position);
                break;
        }
    }

    @Override
        public int getItemPosition(Object object) {
            // POSITION_NONE makes it possible to reload the PagerAdapter
            return POSITION_NONE;
        }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        return fragments.get(position);
    }

    @Override
    public int getCount() {

        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        return tabTitles.get(position);
    }
}
