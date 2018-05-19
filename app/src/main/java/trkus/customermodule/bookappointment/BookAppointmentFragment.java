package trkus.customermodule.bookappointment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import trkus.services.com.trkus.R;

public class BookAppointmentFragment extends Fragment {


    Fragment fragment = null;
    String SellerUserId, Industry, CategoryOfBusiness, FirmName, MobileNumber, Address1, PinCode, Image1;
    ProgressDialog pDialog;
    String Tag = "Dashboard";
    TextView et_firm_name, et_location, et_date;
    Button btn_calendar, btn_confirm;
    EditText et_name, et_mobile;
    String CategoryName;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        SellerUserId = getArguments().getString("SellerUserId");
        FirmName = getArguments().getString("FirmName");
        MobileNumber = getArguments().getString("MobileNumber");
        Address1 = getArguments().getString("Address1");
        PinCode = getArguments().getString("PinCode");
        Image1 = getArguments().getString("Image1");
        CategoryName = getArguments().getString("CategoryName");

        getActivity().setTitle(FirmName);
        View view = inflater.inflate(R.layout.layout_book_appointment, container, false);
        et_firm_name = view.findViewById(R.id.et_firm_name);
        et_location = view.findViewById(R.id.et_location);
        et_date = view.findViewById(R.id.et_date);
        btn_calendar = view.findViewById(R.id.btn_calendar);
        btn_confirm = view.findViewById(R.id.btn_confirm);
        et_name = view.findViewById(R.id.et_name);
        et_mobile = view.findViewById(R.id.et_mobile);

        et_firm_name.setText(FirmName);
        et_location.setText(Address1 + " " + PinCode);

        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

        et_date.setText(currentDateTimeString);

        myCalendar = Calendar.getInstance();

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM-dd-yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                et_date.setText(sdf.format(myCalendar.getTime()));
            }

        };
        btn_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        return view;
    }

    @SuppressLint("ValidFragment")
    public class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
            dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
            return dialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            et_date.setText(year + "-" + (month + 1) + "-" + day);

        }
    }

}
