package io.nirahtech.petvet.ui.fragments.planner;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import io.nirahtech.petvet.ui.adapters.ViewPagerAdapter;
import io.nirahtech.petvet.databinding.FragmentPlannerBinding;

public class PlannerFragment extends Fragment {

    private FragmentPlannerBinding binding;

    private PlannerViewModel mViewModel;

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    public static PlannerFragment newInstance() {
        return new PlannerFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentPlannerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        this.tabLayout = binding.tabLayout;
        this.viewPager2 = binding.viewPager;
        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this.getActivity());
        this.viewPager2.setAdapter(viewPagerAdapter);
        this.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        this.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });




        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PlannerViewModel.class);
        // TODO: Use the ViewModel
    }

}