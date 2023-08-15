package sa.med.imc.myimc.Profile.view;

import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import sa.med.imc.myimc.Adapter.BookingsAdapter;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.RecyclerItemClickListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class ManageBookingsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.rv_bookings)
    RecyclerView rvBookings;
    @BindView(R.id.no_result)
    TextView noResult;

    BookingsAdapter adapter;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ManageBookingsFragment() {
        // Required empty public constructor
    }

    public static ManageBookingsFragment newInstance(String param1, String param2) {
        ManageBookingsFragment fragment = new ManageBookingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manage_bookings, container, false);
        ButterKnife.bind(this, view);
        setUpBookingsRecyclerView();
        return view;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    // Initialize Bookings list view and add adapter to display data,
    void setUpBookingsRecyclerView() {
        adapter = new BookingsAdapter(getContext(), new ArrayList<String>());
        rvBookings.setHasFixedSize(true);
        rvBookings.setLayoutManager(new LinearLayoutManager(getContext()));
        rvBookings.setAdapter(adapter);
        rvBookings.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        }));
    }
}
