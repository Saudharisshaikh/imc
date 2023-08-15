package sa.med.imc.myimc.Physicians.adapter;

import static sa.med.imc.myimc.Network.Constants.ENGLISH;
import static sa.med.imc.myimc.Utils.Common.showAlert;
import static sa.med.imc.myimc.Utils.Common.showConsentPopUpAgree;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import sa.med.imc.myimc.Adapter.PhysicianAdapter;
import sa.med.imc.myimc.Adapter.TimeSlotsAdapter;
import sa.med.imc.myimc.Appointmnet.model.DrTimeSlot;
import sa.med.imc.myimc.Appointmnet.model.TimeSlotsResponse;
import sa.med.imc.myimc.Appointmnet.presenter.AppointmentPresenter;
import sa.med.imc.myimc.Appointmnet.view.AppointmentActivity;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.Physicians.interfaces.BookingInListHelper;
import sa.med.imc.myimc.Physicians.interfaces.GetDoctorService;
import sa.med.imc.myimc.Physicians.interfaces.GetTimeSlot;
import sa.med.imc.myimc.Physicians.model.AllDoctorListModel;
import sa.med.imc.myimc.Physicians.model.DoctorFullDeatilsModel;
import sa.med.imc.myimc.Physicians.model.DrServiceResponse;
import sa.med.imc.myimc.Physicians.model.PhysicianResponse;
import sa.med.imc.myimc.Physicians.presenter.FullDoctorDetailListner;
import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.DailogAlert;
import sa.med.imc.myimc.globle.MyhttpMethod;
import sa.med.imc.myimc.globle.UrlWithURLDecoder;

public class PhysicianAdapter2 extends RecyclerView.Adapter<PhysicianAdapter2.Viewholder> {
    List<PhysicianResponse.ProviderList> list;

    Activity context;
    List<String> specialityDescription = new ArrayList<>();
    List<String> specialityCode = new ArrayList<>();
    PhysicianAdapter2.OnItemClickListener onItemClickListener;
    String lang = Constants.ENGLISH;
    DailogAlert dailogAlert;
    BookingInListHelper bookingInListHelper;

    List<TimeSlotsAdapter> parentMAdapter = new ArrayList<>();
    List<String> SelectedServiceCode = new ArrayList<>();
    List<String> SelectedServiceName = new ArrayList<>();
    List<String> apType = new ArrayList<>();
    List<String> consultationtype = new ArrayList<>();
    List<String> bookingSlotID = new ArrayList<>();
    List<String> bookingslotTime = new ArrayList<>();
    List<Boolean> isSlot = new ArrayList<>();

    List<List<String>> parentListServices = new ArrayList<>();
    List<List<String>> parentListServicesAr = new ArrayList<>();
    List<List<String>> parentListServicesCode = new ArrayList<>();
    List<List<String>> parentListServicesType = new ArrayList<>();


    String timeStamp;

    public PhysicianAdapter2(Activity context, List<PhysicianResponse.ProviderList> list1,
                             String specialityDescription, String specialityCode, DailogAlert dailogAlert1,String timeStamp) {
        this.context = context;
        this.list = list1;
        this.dailogAlert = dailogAlert1;
        this.timeStamp = timeStamp;
        lang = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);
        bookingInListHelper = new BookingInListHelper(context);


        List<String> listServices = new ArrayList<>();
        List<String> listServicesAr = new ArrayList<>();
        List<String> listServicesCode = new ArrayList<>();
        List<String> listServicesType = new ArrayList<>();

        for (int position = 0; position < list.size(); position++) {
            this.specialityDescription.add(position, specialityDescription);
            this.specialityCode.add(position, specialityCode);

            parentListServices.add(position, listServices);
            parentListServicesAr.add(position, listServicesAr);
            parentListServicesCode.add(position, listServicesCode);
            parentListServicesType.add(position, listServicesType);

            parentMAdapter.add(position, null);
            SelectedServiceCode.add(position, "");
            SelectedServiceName.add(position, "");
            apType.add(position, "");
            consultationtype.add(position, "");
            bookingSlotID.add(position, "");
            bookingslotTime.add(position, "");
            isSlot.add(position, false);

            Log.e("abcd", "position " + position);
        }

    }

    public PhysicianAdapter2(Activity context, List<PhysicianResponse.ProviderList> list1,
                             List<String> specialityDescription, List<String> specialityCode, DailogAlert dailogAlert1,String timeStamp) {
        this.context = context;
        this.list = list1;
        this.dailogAlert = dailogAlert1;
        this.timeStamp = timeStamp;
        lang = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);
        bookingInListHelper = new BookingInListHelper(context);


        List<String> listServices = new ArrayList<>();
        List<String> listServicesAr = new ArrayList<>();
        List<String> listServicesCode = new ArrayList<>();
        List<String> listServicesType = new ArrayList<>();
        this.specialityDescription.clear();
        this.specialityCode.clear();

        this.specialityDescription.addAll(specialityDescription);
        this.specialityCode.addAll(specialityCode);

        for (int position = 0; position < list.size(); position++) {

            parentListServices.add(position, listServices);
            parentListServicesAr.add(position, listServicesAr);
            parentListServicesCode.add(position, listServicesCode);
            parentListServicesType.add(position, listServicesType);

            parentMAdapter.add(position, null);
            SelectedServiceCode.add(position, "");
            SelectedServiceName.add(position, "");
            apType.add(position, "");
            consultationtype.add(position, "");
            bookingSlotID.add(position, "");
            bookingslotTime.add(position, "");
            isSlot.add(position, false);

            Log.e("abcd", "position " + position);
        }

    }


    public void refresh(String specialityDescription, String specialityCode) {

        List<String> listServices = new ArrayList<>();
        List<String> listServicesAr = new ArrayList<>();
        List<String> listServicesCode = new ArrayList<>();
        List<String> listServicesType = new ArrayList<>();
        for (int position = 0; position < list.size(); position++) {
            this.specialityDescription.add(position, specialityDescription);
            this.specialityCode.add(position, specialityCode);

            parentListServices.add(position, listServices);
            parentListServicesAr.add(position, listServicesAr);
            parentListServicesCode.add(position, listServicesCode);
            parentListServicesType.add(position, listServicesType);

            parentMAdapter.add(position, null);
            SelectedServiceCode.add(position, "");
            SelectedServiceName.add(position, "");
            apType.add(position, "");
            consultationtype.add(position, "");
            bookingSlotID.add(position, "");
            bookingslotTime.add(position, "");
            isSlot.add(position, false);

            Log.e("abcd", "position " + position);
        }
        notifyDataSetChanged();
    }

    public void refresh(List<String> specialityDescription, List<String> specialityCode) {

        List<String> listServices = new ArrayList<>();
        List<String> listServicesAr = new ArrayList<>();
        List<String> listServicesCode = new ArrayList<>();
        List<String> listServicesType = new ArrayList<>();

        this.specialityDescription.addAll(specialityDescription);
        this.specialityCode.addAll(specialityCode);

        for (int position = 0; position < list.size(); position++) {

            parentListServices.add(position, listServices);
            parentListServicesAr.add(position, listServicesAr);
            parentListServicesCode.add(position, listServicesCode);
            parentListServicesType.add(position, listServicesType);

            parentMAdapter.add(position, null);
            SelectedServiceCode.add(position, "");
            SelectedServiceName.add(position, "");
            apType.add(position, "");
            consultationtype.add(position, "");
            bookingSlotID.add(position, "");
            bookingslotTime.add(position, "");
            isSlot.add(position, false);

            Log.e("abcd", "position " + position);
        }
        notifyDataSetChanged();
    }

    public void setSpecialityDescription(String specialityDescription) {
        for (int position = 0; position < list.size(); position++) {
            this.specialityDescription.add(position, specialityDescription);
        }
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(PhysicianAdapter2.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public PhysicianAdapter2.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_physician2, parent, false);
        PhysicianAdapter2.Viewholder viewholder = new PhysicianAdapter2.Viewholder(view);

        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull PhysicianAdapter2.Viewholder holder, @SuppressLint("RecyclerView") final int position) {
        PhysicianResponse.ProviderList physician = list.get(position);
//        String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

//        unSelectSlot(position, holder);
        holder.tv_book_appointment.setText(context.getResources().getString(R.string.check_availability));

        holder.moreSlot.setVisibility(View.GONE);
        holder.slot_title.setText(context.getResources().getString(R.string.select_time_slot)+"( "+timeStamp+")");

//        hideSpinner(holder);
//        hideSlot(holder);
//        showProgress(holder);


        if (lang.equalsIgnoreCase(Constants.ENGLISH)) {
            holder.tv_physician_name.setText(context.getText(R.string.dr) + " " + physician.getProviderDescription());

        } else {
            holder.tv_physician_name.setText(context.getText(R.string.dr) + " " + physician.getArabicProviderDescription());

        }

        holder.tv_physician_speciality.setText(specialityDescription.get(position));


        Picasso.get().load(new UrlWithURLDecoder().getDoctor_profile(context, physician.getProviderCode()))
                .error(R.drawable.mdoc_placeholder)
                .placeholder(R.drawable.male_img).fit().into(holder.iv_physician_pic);

        holder.tv_view_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onViewProfile(position);
            }
        });
        holder.iv_physician_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onViewProfile(position);
            }
        });
        holder.moreSlot.setOnClickListener(v -> {
            onItemClickListener.onBookAppointment(position,holder.rdTelemed.isChecked());
        });

        holder.tv_book_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (physician.isPatientPortalDoctor()) {
                    if (isSlot.get(position)) {
                        startBooking(position, physician, holder, apType.get(position), specialityDescription.get(position), timeStamp,
                                bookingslotTime.get(position), consultationtype.get(position),
                                SelectedServiceCode.get(position), specialityCode.get(position), parentMAdapter.get(position).getSessionId(),
                                parentMAdapter.get(position).getSelectedDat(), parentMAdapter.get(position).getSelectedTime2(),
                                SelectedServiceName.get(position));
                    } else {
                        fetchServiceList(position, physician, holder);
                        holder.moreSlot.setVisibility(View.VISIBLE);
                        showSpinner(holder);
                    }
                } else {
                    dailogAlert.dailog("Physician is unavailable for online booking");
                }
            }
        });

        holder.tv_next_available_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onBookNextAvailable(position);
            }
        });

        holder.radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                fetchServiceList(position, physician, holder);
            }
        });



        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position2, long id) {
                unSelectSlot(position, holder);

                SelectedServiceCode.set(position, parentListServicesCode.get(position).get(position2));
                SelectedServiceName.set(position, parentListServices.get(position).get(position2));
//                consultationtype = listServicesType.get(position);
                apType.set(position, parentListServicesType.get(position).get(position2));
                if (apType.get(position).equals("Offline")) {
                    consultationtype.set(position, context.getString(R.string.in_person));
                } else if (apType.get(position).equals("Online")) {
                    consultationtype.set(position, context.getString(R.string.tele_med));
                }

                hideSlot(holder);
                showProgress(holder);

                Log.e("abcd","slote "+timeStamp);
                bookingInListHelper.fetchDrTimeSlot(timeStamp, timeStamp, physician.getProviderCode(), SelectedServiceCode.get(position),
                        specialityCode.get(position), new GetTimeSlot() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onGetTimeSlot(DrTimeSlot drTimeSlot) {
                                setSlot(position, physician, holder, drTimeSlot);
                                showSlot(holder);
                                hideProgress(holder);

                                try {
                                    if (drTimeSlot.getSlotList().isEmpty()) {
                                        showNoSlot(holder);
                                    }
                                } catch (Exception e) {
                                    showNoSlot(holder);
                                }

                            }

                            @Override
                            public void onFail(String msg) {
                                hideSlot(holder);
                                showNoSlot(holder);
                                hideProgress(holder);
                                holder.slot_title.setVisibility(View.GONE);

                            }
                        });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        loadDoctorData(physician.getProviderCode(), position, holder);

    }


    private void loadDoctorData(String code, int position, @NonNull PhysicianAdapter2.Viewholder holder) {
        int hospitalCode = SharedPreferencesUtils.getInstance(context).getValue(Constants.SELECTED_HOSPITAL, 0);

        new MyhttpMethod(context).getFullPhysician(String.valueOf(hospitalCode), code, false, new FullDoctorDetailListner() {
            @Override
            public void onSuccess(DoctorFullDeatilsModel doctorFullDeatilsModel) {
                if (lang.equalsIgnoreCase(Constants.ENGLISH)) {

                    try {


                        String sp = doctorFullDeatilsModel.getPhysicianData().getspecInstructEn();
                        String speclInstruct = doctorFullDeatilsModel.getPhysicianData().specInstructEn;

                        Log.e("doctorFullDeatilsModel",speclInstruct + " ++ " + doctorFullDeatilsModel.getPhysicianData().getspecInstructEn());

                        if (sp == null) {
                            holder.special_instructions.setVisibility(View.GONE);
                        } else {
                            holder.special_instructions.setText(sp.trim());
                            holder.special_instructions.setVisibility(View.VISIBLE);

                        }
                    } catch (Exception e) {
                        holder.special_instructions.setVisibility(View.GONE);
                    }

                    String specialitiesText = String.valueOf(Html.fromHtml(doctorFullDeatilsModel.getPhysicianData().getSpecialitiesTxt().trim()));

                    holder.tv_physician_speciality.setText(specialitiesText.trim() + "\n" + specialityDescription.get(position).trim());


                } else {
                    try {
                        String spAr = doctorFullDeatilsModel.getPhysicianData().getSpecInstructAr();
                        if (spAr == null) {
                            holder.special_instructions.setVisibility(View.GONE);
                        } else {
                            holder.special_instructions.setText(spAr.trim());
                            holder.special_instructions.setVisibility(View.VISIBLE);

                        }
                    } catch (Exception e) {
                        holder.special_instructions.setVisibility(View.GONE);
                    }


                    String specialitiesTextAr = String.valueOf(Html.fromHtml(doctorFullDeatilsModel.getPhysicianData().getSpecialitiesTxtAr().trim()));
                    holder.tv_physician_speciality.setText(specialitiesTextAr.trim() + "\n" + specialityDescription.get(position).trim());

                }
               // Log.e("tv_physician_speciality  ", " -- " + doctorFullDeatilsModel.getPhysicianData().getspecInstructEn());
               // holder.special_instructions.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailed() {
                holder.special_instructions.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    void startBooking(int position, PhysicianResponse.ProviderList physician,
                      @NonNull PhysicianAdapter2.Viewholder holder, String apType1,
                      String specialityDescription, String fromDate, String bookingslotTime,
                      String consultationtype, String SelectedServiceCode, String specialityCode,
                      String sessionId, String selectedDat, String selectedTime2, String serviceName) {

        if (consultationtype.equalsIgnoreCase(context.getString(R.string.tele_med))){
            showConsentPopUpAgree(context, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bookingInListHelper.showConfirmDailog(physician, apType1, specialityDescription, fromDate, bookingslotTime, consultationtype,
                            SelectedServiceCode, specialityCode, sessionId, selectedDat, selectedTime2, serviceName,timeStamp);                }
            });
        }else {
            bookingInListHelper.showConfirmDailog(physician, apType1, specialityDescription, fromDate, bookingslotTime, consultationtype,
                    SelectedServiceCode, specialityCode, sessionId, selectedDat, selectedTime2, serviceName,timeStamp);        }

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    void setSlot(int position, PhysicianResponse.ProviderList physician, @NonNull PhysicianAdapter2.Viewholder holder, DrTimeSlot response) {
        List<DrTimeSlot.SlotList> DrlstOfTimeSlot = new ArrayList<>();
        List<DrTimeSlot.SlotList> activeDrlstOfTimeSlot = new ArrayList<>();
        List<DrTimeSlot.SlotList> filetredActiveDrlstOfTimeSlot = new ArrayList<>();
        String selectedDate = "";
        int bookingTime = 0;

        try {
            DrlstOfTimeSlot.clear();

            if (response.getSlotList().size() > 0) {
                DrlstOfTimeSlot.addAll(response.getSlotList());

                if (DrlstOfTimeSlot.size() == 0) {
                    hideSlot(holder);
                    showNoSlot(holder);

                } else {
                    showSlot(holder);

                }
                holder.slot_title.setVisibility(View.VISIBLE);

            } else {
                hideSlot(holder);
                showNoSlot(holder);
                holder.slot_title.setVisibility(View.GONE);

            }
            LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(45);

            String nowTime = expirationTime.format(DateTimeFormatter.ofPattern("HH:mm",Locale.ENGLISH));
            String mNowTime = nowTime.replace(":", ".");

            for (int i = 0; i < DrlstOfTimeSlot.size(); i++) {
                DrTimeSlot.SlotList slotList = DrlstOfTimeSlot.get(i);

                String slotTime = slotList.getSlotFromTime();
                String mSlotTime = slotTime.replace(":", ".");

                Double dNowTime = Double.valueOf(mNowTime);
                Double dSlotTime = Double.valueOf(mSlotTime);

                if (dNowTime < dSlotTime) {
                    activeDrlstOfTimeSlot.add(slotList);
                }
            }

//            for (int i = 0; i < 6; i++) {
//                try {
//                    filetredActiveDrlstOfTimeSlot.add(activeDrlstOfTimeSlot.get(i));
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            }

            Collections.sort(DrlstOfTimeSlot, (o1, o2) -> o1.getSlotFromTime().compareTo(o2.getSlotFromTime()));
            TimeSlotsAdapter mAdapter = new TimeSlotsAdapter(context, activeDrlstOfTimeSlot, selectedDate,true);

            mAdapter.setSelectedDate(selectedDate, bookingTime);
            holder.rvSlots.setHasFixedSize(true);
            holder.rvSlots.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
            holder.rvSlots.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(new TimeSlotsAdapter.OnItemClickListener() {
                @Override
                public void onSlotClick(String slotID, String slotTime) {
//                Common.makeToast(AppointmentActivity.this, "Piyush");
                    selectSlot(position, holder);
                    Log.e("abcd", "Slot selected " + slotTime);
                    bookingSlotID.set(position, slotID);
                    bookingslotTime.set(position, slotTime);
                }

                @Override
                public void onUnSelect(String SlotID, String slotTime) {
                    unSelectSlot(position, holder);

                }
            });
            mAdapter.notifyDataSetChanged();
            parentMAdapter.set(position, mAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void fetchServiceList(int position, PhysicianResponse.ProviderList physician, @NonNull PhysicianAdapter2.Viewholder holder) {
        unSelectSlot(position, holder);

        List<String> listServices = parentListServices.get(position);
        List<String> listServicesAr = parentListServicesAr.get(position);
        List<String> listServicesCode = parentListServicesCode.get(position);
        List<String> listServicesType = parentListServicesType.get(position);

        bookingInListHelper.fetchServiceList(physician.getProviderCode(), specialityCode.get(position), new GetDoctorService() {
            @Override
            public void onGetServiceList(DrServiceResponse serviceResponse) {
                listServices.clear();
                listServicesAr.clear();
                listServicesCode.clear();
                listServicesType.clear();
                boolean isTele = false;


                for (int i = 0; i < serviceResponse.getServiceList().size(); i++) {
                    DrServiceResponse.ServiceList serviceList = serviceResponse.getServiceList().get(i);

                    if (holder.rdInPersoh.isChecked()) {
                        if (serviceList.getType().equalsIgnoreCase("Offline")) {
                            listServices.add(serviceResponse.getServiceList().get(i).getServiceDescription());
                            listServicesAr.add(serviceResponse.getServiceList().get(i).getArabicServiceDescription());

                            listServicesCode.add(serviceResponse.getServiceList().get(i).getServiceCode());
                            listServicesType.add(serviceResponse.getServiceList().get(i).getType());
                        }
                    }

                    if (holder.rdTelemed.isChecked()) {
                        if (serviceList.getType().equalsIgnoreCase("Online")) {
                            listServices.add(serviceResponse.getServiceList().get(i).getServiceDescription());
                            listServicesAr.add(serviceResponse.getServiceList().get(i).getArabicServiceDescription());

                            listServicesCode.add(serviceResponse.getServiceList().get(i).getServiceCode());
                            listServicesType.add(serviceResponse.getServiceList().get(i).getType());
                        }

                    }
                    if (serviceList.getType().equalsIgnoreCase("Online")) {
                        isTele = true;
                    }
                }

                if (holder.rdTelemed.isChecked()) {
                    if (listServices.size() == 0) {
                        showAlert(context, context.getString(R.string.no_telemedicine_services));
                    }
                }
                ArrayAdapter aa;
                if (lang.equalsIgnoreCase(ENGLISH)) {
                    aa = new ArrayAdapter(context, android.R.layout.simple_spinner_item, listServices);
                } else {
                    aa = new ArrayAdapter(context, android.R.layout.simple_spinner_item, listServicesAr);
                }
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //Setting the ArrayAdapter data on the Spinner
                holder.spinner.setAdapter(aa);


                if (isTele) {
                    holder.rdTelemed.setVisibility(View.VISIBLE);
                } else {
                    holder.rdTelemed.setVisibility(View.GONE);
                }

                parentListServices.set(position, listServices);
                parentListServicesAr.set(position, listServicesAr);
                parentListServicesCode.set(position, listServicesCode);
                parentListServicesType.set(position, listServicesType);

                showSpinner(holder);
                showProgress(holder);
                hideSlot(holder);

                if (serviceResponse.getServiceList().size() == 0) {
                    hideSpinner(holder);
                    hideProgress(holder);
                }
            }

            @Override
            public void onFail(String error) {
                hideSpinner(holder);
                hideProgress(holder);
            }
        });

    }

    void hideSpinner(@NonNull PhysicianAdapter2.Viewholder holder) {
        holder.lll.setVisibility(View.GONE);
    }

    void showSpinner(@NonNull PhysicianAdapter2.Viewholder holder) {
        holder.lll.setVisibility(View.VISIBLE);
    }

    void showNoSlot(@NonNull PhysicianAdapter2.Viewholder holder) {
        holder.no_slots.setVisibility(View.VISIBLE);
    }


    void hideSlot(@NonNull PhysicianAdapter2.Viewholder holder) {
        holder.rvSlots.setVisibility(View.GONE);
    }

    void showSlot(@NonNull PhysicianAdapter2.Viewholder holder) {
        holder.rvSlots.setVisibility(View.VISIBLE);
        holder.no_slots.setVisibility(View.GONE);
        holder.slot_progress_bar.setVisibility(View.GONE);
    }

    void hideProgress(@NonNull PhysicianAdapter2.Viewholder holder) {
        holder.slot_progress_bar.setVisibility(View.GONE);
    }

    void showProgress(@NonNull PhysicianAdapter2.Viewholder holder) {
        holder.slot_progress_bar.setVisibility(View.VISIBLE);
    }

    void unSelectSlot(int position, @NonNull PhysicianAdapter2.Viewholder holder) {
        isSlot.set(position, false);
        holder.tv_book_appointment.setText(context.getResources().getString(R.string.check_availability));
    }

    void selectSlot(int position, @NonNull PhysicianAdapter2.Viewholder holder) {
        isSlot.set(position, true);
        holder.tv_book_appointment.setText(context.getResources().getString(R.string.book_appointment));
    }


    class Viewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_physician_name)
        TextView tv_physician_name;
        @BindView(R.id.tv_physician_speciality)
        TextView tv_physician_speciality;
        @BindView(R.id.tv_physician_address)
        TextView tv_physician_address;
        @BindView(R.id.tv_physician_distance)
        TextView tv_physician_distance;

        @BindView(R.id.tv_view_profile)
        TextView tv_view_profile;
        @BindView(R.id.tv_book_appointment)
        TextView tv_book_appointment;

        @BindView(R.id.moreSlot)
        TextView moreSlot;
        @BindView(R.id.special_instructions)
        TextView special_instructions;
        @BindView(R.id.lay_item)
        RelativeLayout lay_item;
        @BindView(R.id.iv_physician_pic)
        ImageView iv_physician_pic;
        @BindView(R.id.tv_clinic_service)
        TextView tv_clinic_service;
        @BindView(R.id.rating)
        RatingBar rating;
        @BindView(R.id.tv_next_available_time)
        TextView tv_next_available_time;

        @BindView(R.id.progress_bar)
        ProgressBar progress_bar;

        @BindView(R.id.card_doctor)
        CardView cardDoctor;

        @BindView(R.id.rd_in_persoh)
        RadioButton rdInPersoh;

        @BindView(R.id.rd_telemed)
        RadioButton rdTelemed;
        @BindView(R.id.radio)
        RadioGroup radio;
        @BindView(R.id.spinner)
        Spinner spinner;

        @BindView(R.id.rv_slots)
        RecyclerView rvSlots;

        @BindView(R.id.slot_progress_bar)
        ProgressBar slot_progress_bar;

        @BindView(R.id.slot_title)
        TextView slot_title;

        @BindView(R.id.no_slots)
        TextView no_slots;

        @BindView(R.id.label_spinner)
        TextView label_spinner;

        @BindView(R.id.lll)
        LinearLayout lll;

        Viewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public interface OnItemClickListener {
        void onViewProfile(int position);

        void onBookAppointment(int position,boolean isTele);

        void onBookNextAvailable(int position);
    }

    public static CharSequence trim(CharSequence s, int start, int end) {
        while (start < end && Character.isWhitespace(s.charAt(start))) {
            start++;
        }

        while (end > start && Character.isWhitespace(s.charAt(end - 1))) {
            end--;
        }

        return s.subSequence(start, end);
    }
}
