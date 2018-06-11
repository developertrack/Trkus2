package trkus.customermodule.bookappointment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import trkus.customermodule.appointmenthistory.AppointmentHistory;
import trkus.services.com.trkus.R;
import util.AppController;
import util.UrlConstant;
import util.UserSessionManager;

import static util.AppController.TAG;

public class BookAppointmentFragment extends Fragment {


    Fragment fragment = null;
    String SellerUserId, Industry, CategoryOfBusiness, FirmName, MobileNumber, Address1, PinCode, Image1;
    String[] SellerId,Days,StartTime,EndTime,Id;
    UserSessionManager session;
    ProgressDialog pDialog;
    String Tag = "Dashboard";
    TextView et_firm_name, et_location, et_date;
    Button btn_calendar, btn_confirm;
    EditText et_name, et_mobile;
    String CategoryName;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    HorizontalScrollView hscroll;
    LinearLayout availabilty;
    String str_name,str_mobile;
    JSONObject data_jobject;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        SellerUserId = getArguments().getString("SellerUserId");
        FirmName = getArguments().getString("FirmName");
        MobileNumber = getArguments().getString("MobileNumber");
        Address1 = getArguments().getString("Address1");
        PinCode = getArguments().getString("PinCode");
        Image1 = getArguments().getString("Image1");
        CategoryName = getArguments().getString("CategoryName");
        session = new UserSessionManager(getActivity());
        getActivity().setTitle(FirmName);
        View view = inflater.inflate(R.layout.layout_book_appointment, container, false);
        et_firm_name = view.findViewById(R.id.et_firm_name);
        et_location = view.findViewById(R.id.et_location);
        et_date = view.findViewById(R.id.et_date);
        btn_calendar = view.findViewById(R.id.btn_calendar);
        btn_confirm = view.findViewById(R.id.btn_confirm);
        et_name = view.findViewById(R.id.et_name);
        et_mobile = view.findViewById(R.id.et_mobile);
        availabilty= view.findViewById(R.id.availabilty);
        hscroll= view.findViewById(R.id.hscroll);

        hscroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                hscroll.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        et_firm_name.setText(FirmName);
        et_location.setText(Address1 + " " + PinCode);

        Log.e("SellerUserId",SellerUserId);

        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

        et_date.setText(currentDateTimeString);

        myCalendar = Calendar.getInstance();


        getAvailability(SellerUserId);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validatename()) {
                    return;
                }

                if (!isValidMobile()) {
                    return;
                }

                bookAppointment();

            }
        });

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd"; //In which you need put here
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

    public void bookAppointment(){
        String tag_json_obj = "json_obj_req";



        data_jobject = new JSONObject();
        try {
            data_jobject.put("SellerId", SellerUserId);
            data_jobject.put("CustomerId", session.getKeyUserid());
            data_jobject.put("ApponmentDate", et_date.getText().toString());
            data_jobject.put("Time","" );
            data_jobject.put("ParentName", str_name);
            data_jobject.put("ParentMobileNumber", str_mobile);
            data_jobject.put("Comments","");

        } catch (Exception e) {

        }

        Log.e("data_jobject", data_jobject.toString());

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                UrlConstant.POST_Seller_Appointment, data_jobject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject response) {
                        Log.e(TAG, response.toString());
                        try {
                            String Status = response.getString("Status");

                            if (Status.equals("false")) {

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        android.support.v7.app.AlertDialog.Builder dlgAlert = new android.support.v7.app.AlertDialog.Builder(getActivity());
                                        try {
                                            dlgAlert.setMessage(response.getString("Message"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        dlgAlert.setPositiveButton("OK", null);
                                        dlgAlert.setCancelable(true);
                                        dlgAlert.create().show();
                                    }
                                });

                            } else {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        android.support.v7.app.AlertDialog.Builder dlgAlert = new android.support.v7.app.AlertDialog.Builder(getActivity());
                                        try {
                                        dlgAlert.setMessage(response.getString("Message"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                fragment = new AppointmentHistory();
                                                FragmentTransaction tx = getActivity().getSupportFragmentManager().beginTransaction();
                                                tx.replace(R.id.flContent, fragment, "appointment");
                                                tx.commit();
                                                tx.addToBackStack(null);
                                            }
                                        });
                                        dlgAlert.setCancelable(true);
                                        dlgAlert.create().show();

                                    }
                                });

                            }

                        } catch (Exception e) {

                        }

                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        android.support.v7.app.AlertDialog.Builder dlgAlert = new android.support.v7.app.AlertDialog.Builder(getActivity());
                        dlgAlert.setMessage("Error while fetching data, please try again");
                        dlgAlert.setPositiveButton("OK", null);
                        dlgAlert.setCancelable(true);
                        dlgAlert.create().show();
                    }
                });
                pDialog.hide();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }




    public void getAvailability(String id) {
        JsonArrayRequest req = new JsonArrayRequest(UrlConstant.GET_Seller_Availability + id,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(Tag, response.toString());
                        SellerId = new String[response.length()];
                        Days = new String[response.length()];
                        StartTime = new String[response.length()];
                        EndTime = new String[response.length()];
                        Id = new String[response.length()];

//                        SellerId,Days,StartTime,EndTime,Id

                        try {

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response
                                        .get(i);
                                SellerId[i] = person.getString("SellerId");
                                Days[i] = person.getString("Days");
                                StartTime[i] = person.getString("StartTime");
                                EndTime[i] = person.getString("EndTime");
                                Id[i] = person.getString("Id");

                                TextView textView= new TextView(getActivity());              //dynamically create textview
                                textView.setLayoutParams(new LinearLayout.LayoutParams(             //select linearlayoutparam- set the width & height
                                        ViewGroup.LayoutParams.WRAP_CONTENT, 48));
                                textView.setPadding(10,10,10,10);
                                LinearLayout.LayoutParams lp = new
                                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                lp.setMargins(10, 0, 10, 0);
                                textView.setLayoutParams(lp);
                                textView.setGravity(Gravity.CENTER_VERTICAL);

                                ShapeDrawable sd = new ShapeDrawable();

                                // Specify the shape of ShapeDrawable
                                sd.setShape(new RectShape());

                                // Specify the border color of shape
                                sd.getPaint().setColor(Color.BLUE);

                                // Set the border width
                                sd.getPaint().setStrokeWidth(6f);

                                // Specify the style is a Stroke
                                sd.getPaint().setStyle(Paint.Style.STROKE);

                                // Finally, add the drawable background to TextView
                                textView.setBackground(sd);

                                //set the gravity too
                                String text = "<font color='black'>"+Days[i]+"</font><br /><font color='red'>"+StartTime[i]+"-"+EndTime[i]+"</font>";
                                textView.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
                                availabilty.addView(textView);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                        pDialog.dismiss();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(Tag, "Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        });

        AppController.getInstance().addToRequestQueue(req);
    }

    private boolean validatename() {
        str_name = et_firm_name.getText().toString().trim();

        if (str_name.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter  name", Toast.LENGTH_LONG).show();

            return false;
        } else {

        }



        return true;
    }

    private boolean isValidMobile() {

        str_mobile = et_mobile.getText().toString().trim();

        if (str_mobile.isEmpty() || str_mobile.length() < 10) {

            Toast.makeText(getActivity(), "Not Valid Number", Toast.LENGTH_LONG).show();

            return false;
        } else {

        }

        return true;
    }
}
