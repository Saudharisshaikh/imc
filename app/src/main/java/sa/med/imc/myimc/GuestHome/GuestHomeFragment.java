package sa.med.imc.myimc.GuestHome;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sa.med.imc.myimc.HealthTips.view.HealthTipsActivity;
import sa.med.imc.myimc.Interfaces.FragmentListener;
import sa.med.imc.myimc.Network.WebUrl;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.RetailModule.view.RetailFacilitiesActivity;
import sa.med.imc.myimc.Settings.ContactOptionsActivity;
import sa.med.imc.myimc.Utils.Common;
import sa.med.imc.myimc.WebViewStuff.WebViewActivity;


public class GuestHomeFragment extends Fragment implements View.OnTouchListener {

    @BindView(R.id.lay_request_appointment)
    RelativeLayout layRequestAppointment;
    @BindView(R.id.lay_contact_us)
    RelativeLayout layContactUs;
    @BindView(R.id.lay_find_doctor)
    RelativeLayout layFindDoctor;
    @BindView(R.id.lay_health_byte)
    LinearLayout layHealthByte;
    @BindView(R.id.lay_institutes_spec)
    LinearLayout layInstitutesSpec;
    @BindView(R.id.lay_retail)
    LinearLayout layRetail;
    @BindView(R.id.lay_location_map)
    LinearLayout layLocationMap;
    @BindView(R.id.chatBot)
    ImageView chatBot;
//    @BindView(R.id.tv_english)
//    TextView tvEnglish;
//    @BindView(R.id.tv_arabic)
//    TextView tvArabic;

    int windowwidth; // Actually the width of the RelativeLayout.
    int windowheight; // Actually the height of the RelativeLayout.
    private RelativeLayout mRrootLayout;

    RelativeLayout.LayoutParams parms;
    LinearLayout.LayoutParams par;
    float dx = 0, dy = 0, x = 0, y = 0;
    int count = 0;


    FragmentListener fragmentAdd;

    public GuestHomeFragment() {
        // Required empty public constructor
    }

    public static GuestHomeFragment newInstance() {
        GuestHomeFragment fragment = new GuestHomeFragment();

        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentAdd = (FragmentListener) getActivity();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_guest_home, container, false);
        ButterKnife.bind(this, view);
        mRrootLayout = (RelativeLayout) view.findViewById(R.id.root);
//        Glide.with(this).asGif().load(R.raw.chatbot_img).into(chatBot);

//        chatBot.setOnTouchListener(this);

        mRrootLayout.post(new Runnable() {
            @Override
            public void run() {
                windowwidth = mRrootLayout.getWidth() - 200;
                windowheight = mRrootLayout.getHeight() - 220;
            }
        });



        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @OnClick({R.id.chatBot, R.id.lay_virtual_family, R.id.lay_request_appointment, R.id.lay_contact_us, R.id.lay_find_doctor, R.id.lay_health_byte, R.id.lay_institutes_spec, R.id.lay_retail, R.id.lay_location_map})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.chatBot:
                Common.showChatBot(getActivity());
                break;

            case R.id.lay_request_appointment:
//                RequestAppointmentActivity.startActivity(getActivity());
                break;

            case R.id.lay_contact_us:
                ContactOptionsActivity.startActivity(getActivity());
                break;

            case R.id.lay_find_doctor:
                if (fragmentAdd != null)
                    fragmentAdd.openPhysicianFragment("PhysiciansFragment", "");
                break;

            case R.id.lay_health_byte:
                HealthTipsActivity.startActivity(getActivity());
                break;

            case R.id.lay_institutes_spec:
                if (fragmentAdd != null)
                    fragmentAdd.openClinicFragment("ClinicFragmentC");
//                CalculatorsActivity.startActivity(getActivity());

                break;

            case R.id.lay_retail:
                RetailFacilitiesActivity.startActivity(getActivity());
                break;

            case R.id.lay_location_map:
                showLocation();
//                WayFinderMapActivity.startActivity(getActivity());
                break;

            case R.id.lay_virtual_family:
                WebViewActivity.startActivity(getActivity(), WebUrl.VIRTUAL_TOUR_LINK);
                break;
        }
    }


    // show IMC on map
    void showLocation() {
        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", 21.513551, 39.174131);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    @Override
    public boolean onTouch(View view, MotionEvent event) {
        x = event.getRawX();
        y = event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                count = 0;
                parms = (RelativeLayout.LayoutParams) chatBot.getLayoutParams();
                par = (LinearLayout.LayoutParams) getActivity().getWindow().findViewById(Window.ID_ANDROID_CONTENT).getLayoutParams();
                dx = event.getRawX() - chatBot.getX();//parms.leftMargin;
                dy = event.getRawY() - chatBot.getY();//parms.topMargin;

                break;

            case MotionEvent.ACTION_MOVE:
                if (count > 0) {
                    if (windowwidth >= (x - dx) && windowheight >= (y - dy)) {
                        // && (x - dx) > 0 && (y - dy) > 0
                        if ((x - dx) >= 20 && (y - dy) >= 50) {
                            parms.leftMargin = (int) (x - dx);
                            parms.topMargin = (int) (y - dy);
                            chatBot.setX(x - dx);
                            chatBot.setY(y - dy);
                        }
                    }
                }
                count = count + 1;

                break;

            case MotionEvent.ACTION_UP:
                if (count == 0)
                    Common.showChatBot(getActivity());
                break;
        }
        return true;

    }

}
