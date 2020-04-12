package ru.barcats.tempo_leader_javanew.ui.raskladki;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

public class FabBehaviorVisible extends FloatingActionButton.Behavior   {


    private static final String TAG = "33333";

    public FabBehaviorVisible(Context context, AttributeSet attrs) {
        super();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                               @NonNull FloatingActionButton child,
                               @NonNull View target,
                               int dxConsumed,
                               int dyConsumed,
                               int dxUnconsumed,
                               int dyUnconsumed,
                               int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);

        if ((dyConsumed > 0) && (child.getVisibility() == View.VISIBLE)) {
            Log.d(TAG,"FabBehavior onNestedScroll dyConsumed > 0");
            child.setVisibility(View.INVISIBLE);

        } else if ((dyConsumed < 0) && (child.getVisibility() != View.VISIBLE)) {
            Log.d(TAG,"FabBehavior onNestedScroll dyConsumed < 0");
            child.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull FloatingActionButton child,
                                       @NonNull View directTargetChild,
                                       @NonNull View target,
                                       int axes,
                                       int type) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
                || super.onStartNestedScroll(coordinatorLayout,child,directTargetChild,target,axes,type);
    }
}
