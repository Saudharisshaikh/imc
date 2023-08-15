package sa.med.imc.myimc.medprescription.view;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sa.med.imc.myimc.Interfaces.FragmentListener;
import sa.med.imc.myimc.Profile.view.MedicinesFragment;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.Utils.NonSwipeViewPager;

/**
 *
 */
public class MedicinesPrescriptionsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    FragmentListener fragmentAdd;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.view_pager)
    NonSwipeViewPager viewPager;
    private ViewPagerAdapter mViewPagerAdapter;


    public MedicinesPrescriptionsFragment() {
        // Required empty public constructor
    }

    public static MedicinesPrescriptionsFragment newInstance(String param1, String param2) {
        MedicinesPrescriptionsFragment fragment = new MedicinesPrescriptionsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static MedicinesPrescriptionsFragment newInstance() {
        MedicinesPrescriptionsFragment fragment = new MedicinesPrescriptionsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentAdd = (FragmentListener) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_medicines_tabs, container, false);
        ButterKnife.bind(this, view);
//
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
        setupViewPager();
        setUserVisibleHint(true);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isUserVisible) {
        super.setUserVisibleHint(isUserVisible);
        // when fragment visible to user and view is not null then enter here.
        if (isUserVisible) {

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        if(fragmentAdd!=null)
            fragmentAdd.backPressed(Common.CONTAINER_FRAGMENT);
    }


    private void setupViewPager() {
        mViewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());

        mViewPagerAdapter.addFragment(PrescriptionsFragment.newInstance("", ""), getResources().getString(R.string.prescription));
        mViewPagerAdapter.addFragment(MedicinesFragment.newInstance("", ""), getResources().getString(R.string.medication));

        viewPager.setAdapter(mViewPagerAdapter);

        tabs.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(1);
        tabs.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimary));

        tabs.setTabTextColors(getResources().getColor(R.color.text_grey_color), getResources().getColor(R.color.colorPrimaryDark));

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//        if (mParam1.length() > 0) {
//            tabs.getTabAt(1).select();
//            viewPager.postDelayed(new Runnable() {
//
//                @Override
//                public void run() {
//                    viewPager.setCurrentItem(1);
//                }
//            }, 100);

//        } else {
//            tabs.getTabAt(0).select();
//            viewPager.postDelayed(new Runnable() {
//
//                @Override
//                public void run() {
//                    viewPager.setCurrentItem(0);
//                }
//            }, 100);
//        }
    }


    static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private SparseArray<Fragment> registeredFragments = new SparseArray<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @NonNull
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }
    }

}
