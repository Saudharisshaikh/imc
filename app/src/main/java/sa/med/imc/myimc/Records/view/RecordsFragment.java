package sa.med.imc.myimc.Records.view;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import sa.med.imc.myimc.Interfaces.FragmentListener;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Notifications.view.NotificationsActivity;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Records.view.activity.Lab.LabReportNewFragment;
import sa.med.imc.myimc.Records.view.activity.radilogy.RadiologyReportsNewFragment;
import sa.med.imc.myimc.Records.view.activity.sick.SickReportNewFragment;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.Utils.NonSwipeViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/*
Records fragments have three tabs with swiping pages
lab reports,
radiology reports and
sick leave reports.
 */
public class RecordsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.iv_notifications)
    ImageView ivNotifications;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.main_toolbar)
    RelativeLayout mainToolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.ed_search)
    EditText edSearch;

    FragmentListener fragmentAdd;
    boolean edit = false;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.view_pager)
    NonSwipeViewPager viewPager;
    String mParam1 = "", mParam2 = "";

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentAdd = (FragmentListener) getActivity();
    }


    public RecordsFragment() {
        // Required empty public constructor
    }


    public static RecordsFragment newInstance() {
        RecordsFragment fragment = new RecordsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_records, container, false);
        ButterKnife.bind(this, view);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

            ivLogo.setVisibility(View.GONE);
            ivBack.setVisibility(View.GONE);
        }

        setupViewPager();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.iv_notifications, R.id.iv_search, R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.iv_notifications:
                NotificationsActivity.startActivity(getActivity());
                break;


            case R.id.iv_back:
                if (mParam2.equalsIgnoreCase("yes")) {
                    getActivity().finish();
                } else {
                    if (fragmentAdd != null)
                        fragmentAdd.backPressed(Common.CONTAINER_FRAGMENT);
                }
                break;

            case R.id.iv_search:
                if (edit) {
                    ivSearch.setImageResource(R.drawable.ic_search_blue);
                    edSearch.setVisibility(View.GONE);
                    edSearch.setText("");
                    edit = false;
                } else {
                    ivSearch.setImageResource(R.drawable.ic_close_blue);
                    edSearch.setVisibility(View.VISIBLE);
                    edit = true;
                }
                break;
        }
    }

    private void setupViewPager() {

        if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH).equalsIgnoreCase(Constants.ARABIC)) {
            viewPager.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            viewPager.setRotationY(180);

        } else {
            viewPager.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());

//        SingleFragmentActivity.startActivity(getActivity(), Constants.TYPE.RECORD_LAB, "");


        mViewPagerAdapter.addFragment(new LabReportNewFragment(), getResources().getString(R.string.lab));
        if (SharedPreferencesUtils.getInstance(getActivity()).getValue(Constants.SELECTED_HOSPITAL, 0) == 1){
            mViewPagerAdapter.addFragment(new RadiologyReportsNewFragment(), getResources().getString(R.string.radiology));
        }
        mViewPagerAdapter.addFragment(new SickReportNewFragment(), getResources().getString(R.string.sick_levae));
        viewPager.setAdapter(mViewPagerAdapter);

        tabs.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);
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
