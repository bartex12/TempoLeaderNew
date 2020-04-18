package ru.barcats.tempo_leader_javanew.ui.raskladki.adapters;

import android.util.Log;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SectionsPagerAdapter  extends FragmentPagerAdapter {

    public static final String TAG = "33333";
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ArrayList<String> tabTitles = new ArrayList<>();
    private  onGetPositionListener positionListener;
    private int position;

    public int getPosition() {
        return position;
    }

    public interface onGetPositionListener{
        void onGetPosition(int position);
    }

    public void setPositionListener(onGetPositionListener positionListener) {
        this.positionListener = positionListener;
    }

    public SectionsPagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    public void addFragment(Fragment fragment, String tabTitle){
        this.fragments.add(fragment);
        this.tabTitles.add(tabTitle);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        this.position = position;
        //Log.d(TAG, "//// SectionsPagerAdapter getItem position " + position);
        positionListener.onGetPosition(position);
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
