package sa.med.imc.myimc.Chat.Inbox;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import sa.med.imc.myimc.Adapter.InboxAdapter;
import sa.med.imc.myimc.Chat.ChatActivity;
import sa.med.imc.myimc.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class InboxFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.recycler_inbox)
    RecyclerView recyclerInbox;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.txt_no_msg)
    TextView txtNoMsg;

    InboxAdapter adapter;

    private String mParam1;
    private String mParam2;


    public InboxFragment() {
        // Required empty public constructor
    }


    public static InboxFragment newInstance(String param1, String param2) {
        InboxFragment fragment = new InboxFragment();
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
        View view = inflater.inflate(R.layout.fragment_inbox, container, false);
        ButterKnife.bind(this, view);
        setupRecycler();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    void setupRecycler() {
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity());
        linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerInbox.setLayoutManager(linearLayoutManager1);

        adapter = new InboxAdapter(getActivity(), new ArrayList<>());
        recyclerInbox.setAdapter(adapter);
        adapter.setOnItemClickListener(new InboxAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ChatActivity.startActivity(getActivity(), "");
            }
        });
    }
}
