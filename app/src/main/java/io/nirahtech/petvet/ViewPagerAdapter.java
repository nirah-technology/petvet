package io.nirahtech.petvet;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private static final int TOTAL_TABS = 2;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragmentToDisplay;
        switch (position) {
            case 0: fragmentToDisplay = new PassedEventsFragment();
            case 1: fragmentToDisplay = new NextEventsFragment();
            default: fragmentToDisplay = new PassedEventsFragment();
        }
        return fragmentToDisplay;
    }

    @Override
    public int getItemCount() {
        return TOTAL_TABS;
    }
}
