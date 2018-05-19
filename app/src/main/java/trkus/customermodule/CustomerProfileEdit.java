package trkus.customermodule;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import trkus.services.com.trkus.R;
import util.AppController;
import util.UrlConstant;
import util.UserSessionManager;

public class CustomerProfileEdit extends Fragment {

    EditText et_full_name, gender, et_dateofbirth, et_aadhar, et_blood_group, et_marital_status, et_occupation, et_mobile, et_address,
            et_city, et_state, et_country, et_pincode, et_landline, et_email, et_emergency_name, et_emergency_number, et_emergency_relationship;

    JSONObject data_jobject, data_post;
    Button btn_save;
    ProgressDialog pDialog;
    String[] blood_group = {"A+", "A-", "B", "B+", "AB+", "AB-", "O+", "O-"};
    String[] marital_status = {"Married", "Unmarried"};
    String[] gender_type = {"Male", "Female"};
    ArrayAdapter<String> spinner_blood_group, spinner_marital_status, spinner_state_name, spinner_city, spinner_country, spinner_gender_type;
    String Tag = "customer_Profile_Edit";
    JSONArray jsonResponse;
    String[] StateId, StateName, CategoryName, CategoryId, BCategoryId, BCategoryName, country_StateId, country_StateName, city_StateId, city_StateName;
    UserSessionManager session;
    String stateid;
    String str_full_name, str_gender, str_dateofbirth, str_aadhar, str_blood_group, str_marital_status, str_occupation, str_mobile, str_address,
            str_city, str_state, str_country, str_pincode, str_landline, str_email, str_emergency_name, str_emergency_number, str_emergency_relationship;
    int sel = 0;
    ScrollView scrollprofile;
    String TAG = "Edit_Customer_TAG";
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    String tag_json_obj = "json_obj_req";
    String result = "NA", response_string;
    JSONObject data;
    private String userChoosenTask;

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("My Profile");
        View view = inflater.inflate(R.layout.activity_customer_profile_edit, container, false);

        et_full_name = view.findViewById(R.id.et_full_name);
        gender = view.findViewById(R.id.gender);
        et_blood_group = view.findViewById(R.id.et_blood_group);
        et_dateofbirth = view.findViewById(R.id.et_dateofbirth);
        et_aadhar = view.findViewById(R.id.et_aadhar);
        et_marital_status = view.findViewById(R.id.et_marital_status);
        et_address = view.findViewById(R.id.et_address);
        et_state = view.findViewById(R.id.et_state);
        et_pincode = view.findViewById(R.id.et_pincode);
        et_landline = view.findViewById(R.id.et_landline);
        et_mobile = view.findViewById(R.id.et_mobile);
        et_email = view.findViewById(R.id.et_email);
        et_emergency_number = view.findViewById(R.id.et_emergency_number);
        et_occupation = view.findViewById(R.id.et_occupation);
        et_city = view.findViewById(R.id.et_city);
        et_country = view.findViewById(R.id.et_country);
        et_emergency_name = view.findViewById(R.id.et_emergency_name);
        et_emergency_relationship = view.findViewById(R.id.et_emergency_relationship);


        session = new UserSessionManager(getActivity());

        scrollprofile = view.findViewById(R.id.scrollprofile);

        scrollprofile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                scrollprofile.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        spinner_blood_group = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, blood_group);
        spinner_gender_type = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, gender_type);
        spinner_marital_status = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, marital_status);

        btn_save = view.findViewById(R.id.btn_save);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        myCalendar = Calendar.getInstance();

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                et_dateofbirth.setText(sdf.format(myCalendar.getTime()));
            }

        };


        et_dateofbirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_full_name = et_full_name.getText().toString();
                str_gender = gender.getText().toString();
                str_blood_group = et_blood_group.getText().toString();
                str_dateofbirth = et_dateofbirth.getText().toString();
                str_aadhar = et_aadhar.getText().toString();
                str_marital_status = et_marital_status.getText().toString();
                str_occupation = et_occupation.getText().toString();
                str_city = et_city.getText().toString();
                str_emergency_name = et_emergency_name.getText().toString();
                str_country = et_country.getText().toString();
                str_emergency_relationship = et_emergency_relationship.getText().toString();
                str_address = et_address.getText().toString();
                str_state = et_state.getText().toString();
                str_pincode = et_pincode.getText().toString();
                str_landline = et_landline.getText().toString();
                str_mobile = et_mobile.getText().toString();
                str_email = et_email.getText().toString();
                str_emergency_number = et_emergency_number.getText().toString();
                if (!validateFirstname()) {
                    return;
                }

                if (!validateGender()) {
                    return;
                }

                if (!isValidDob()) {
                    return;
                }
                if (!isValidateAadhar()) {
                    return;
                }
                if (!isValidBloodGroup()) {
                    return;
                }
                if (!isValidateMaritalStatus()) {
                    return;
                }
                if (!isValidateOccupation()) {
                    return;
                }
                if (!isValidMobile()) {
                    return;
                }
                if (!validateEmail()) {
                    return;
                }
                if (!isValidLocation()) {
                    return;
                }
                if (!isValidCity()) {
                    return;
                }
                if (!isValidState()) {
                    return;
                }
                if (!isValidCountry()) {
                    return;
                }
                if (!isValidPincode()) {
                    return;
                }

                if (!validateEmail()) {
                    return;
                }
                if (!validateEmergencyName()) {
                    return;
                }

                if (!isValidEmergencyNumber()) {
                    return;
                }
                if (!validateEmergencyRelationship()) {
                    return;
                }

                pDialog.show();

                UpdateUser();

            }
        });

        gender.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Select Gender")
                        .setAdapter(spinner_gender_type, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                gender.setText(gender_type[which].toString());
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });

        et_blood_group.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Select Blood Group")
                        .setAdapter(spinner_blood_group, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                et_blood_group.setText(blood_group[which].toString());
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });

        et_state.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Select State")
                        .setAdapter(spinner_state_name, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                et_state.setText(StateName[which].toString());
                                stateid = StateId[which].toString();
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });

        et_city.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Select City")
                        .setAdapter(spinner_city, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                et_city.setText(city_StateName[which].toString());
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });

        et_country.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Select State")
                        .setAdapter(spinner_country, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                et_country.setText(country_StateName[which].toString());
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });

        et_marital_status.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Select State")
                        .setAdapter(spinner_marital_status, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                et_marital_status.setText(marital_status[which].toString());
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });

        getStateName();
        getCityName();
        getCountryName();


        return view;

    }


    public void UpdateUser() {

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        data_jobject = new JSONObject();
        try {
            data_jobject.put("UserId", session.getKeyUserid());
            data_jobject.put("Name", str_full_name);
            data_jobject.put("Gender", str_gender);
            data_jobject.put("DOB", str_dateofbirth);
            data_jobject.put("AdharNumber", str_aadhar);
            data_jobject.put("BloodGroup", str_blood_group);
            data_jobject.put("MaritalStatus", str_marital_status);
            data_jobject.put("Occuption", str_occupation);
            data_jobject.put("MobileNumber", str_mobile);
            data_jobject.put("Address", str_address);
            data_jobject.put("City", str_city);
            data_jobject.put("State", str_state);
            data_jobject.put("Country", str_country);
            data_jobject.put("PinCode", str_pincode);
            data_jobject.put("LanLineNumber", str_landline);
            data_jobject.put("EmailId", str_email);
            data_jobject.put("EmergencyName", str_emergency_name);
            data_jobject.put("EmergencyMobileNumber", str_emergency_number);
            data_jobject.put("EmergencyRelationShip", str_emergency_relationship);
            data_jobject.put("CreatedON", "");

        } catch (Exception e) {

        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                UrlConstant.POST_Customer_Profile_Update, data_jobject,
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
                                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
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
                                pDialog.dismiss();
                                Toast.makeText(getActivity(), "Account updated Successfully", Toast.LENGTH_LONG).show();
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
                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
                        dlgAlert.setMessage("Error while signup in, please try again");
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

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
//        getActivity().setTitle("USE");
    }

    private void getCountryName() {

        JsonArrayRequest req = new JsonArrayRequest(UrlConstant.GETCountry_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(Tag, response.toString());

                        country_StateName = new String[response.length()];
                        country_StateId = new String[response.length()];

                        try {

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response
                                        .get(i);

                                country_StateName[i] = person.getString("StateName");
                                country_StateId[i] = person.getString("StateId");
                            }

                            spinner_country = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, country_StateName);

                            getUserDetail(session.getKeyUserid());


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(Tag, "Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        AppController.getInstance().addToRequestQueue(req);


    }

    private void getCityName() {

        JsonArrayRequest req = new JsonArrayRequest(UrlConstant.GETCity_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(Tag, response.toString());

                        city_StateName = new String[response.length()];
                        city_StateId = new String[response.length()];

                        try {

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response
                                        .get(i);

                                city_StateName[i] = person.getString("StateName");
                                city_StateId[i] = person.getString("StateId");
                            }

                            spinner_city = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, city_StateName);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(Tag, "Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        AppController.getInstance().addToRequestQueue(req);
    }

    private void getStateName() {

        JsonArrayRequest req = new JsonArrayRequest(UrlConstant.GETSTATE_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(Tag, response.toString());

                        StateName = new String[response.length()];
                        StateId = new String[response.length()];

                        try {

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response
                                        .get(i);

                                StateName[i] = person.getString("StateName");
                                StateId[i] = person.getString("StateId");
                            }

                            spinner_state_name = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, StateName);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(Tag, "Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        AppController.getInstance().addToRequestQueue(req);
    }

    private boolean validateEmail() {
        str_email = et_email.getText().toString().trim();

        if (str_email.isEmpty() || !isValidEmail(str_email)) {
            Toast.makeText(getActivity(), "Valid email required", Toast.LENGTH_LONG).show();

            return false;
        } else {

        }

        return true;
    }

    private boolean validateFirstname() {
        str_full_name = et_full_name.getText().toString().trim();

        if (str_full_name.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter  name", Toast.LENGTH_LONG).show();

            return false;
        } else {

        }

        return true;
    }


    private boolean isValidDob() {

        str_dateofbirth = et_dateofbirth.getText().toString().trim();

        if (str_dateofbirth.isEmpty()) {

            Toast.makeText(getActivity(), "Set date of birth", Toast.LENGTH_LONG).show();

            return false;
        } else {

        }
        return true;
    }

    private boolean isValidateAadhar() {
        str_aadhar = et_aadhar.getText().toString().trim();

        if (str_aadhar.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter aadhar number", Toast.LENGTH_LONG).show();

            return false;
        } else {

        }

        return true;
    }

    private boolean isValidateMaritalStatus() {
        str_marital_status = et_marital_status.getText().toString().trim();

        if (str_marital_status.isEmpty()) {
            Toast.makeText(getActivity(), "Please select marital status", Toast.LENGTH_LONG).show();

            return false;
        } else {

        }

        return true;
    }

    private boolean isValidateOccupation() {
        str_occupation = et_occupation.getText().toString().trim();

        if (str_occupation.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter occupation", Toast.LENGTH_LONG).show();

            return false;
        } else {

        }

        return true;
    }


    private boolean validateGender() {
        str_gender = gender.getText().toString().trim();

        if (str_gender.isEmpty()) {
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

    private boolean isValidPincode() {

        str_pincode = et_pincode.getText().toString().trim();

        if (str_pincode.isEmpty() || str_pincode.length() < 6) {
            Toast.makeText(getActivity(), "Not Valid Pincode", Toast.LENGTH_LONG).show();


            return false;
        } else {

        }
        return true;
    }

    private boolean isValidBloodGroup() {

        str_blood_group = et_blood_group.getText().toString().trim();

        if (str_blood_group.isEmpty()) {
            Toast.makeText(getActivity(), "Select Blood Group", Toast.LENGTH_LONG).show();

            return false;
        } else {

        }
        return true;
    }

    private boolean isValidLocation() {

        str_address = et_address.getText().toString().trim();

        if (str_address.isEmpty()) {
            Toast.makeText(getActivity(), "Select Location", Toast.LENGTH_LONG).show();

            return false;
        } else {

        }
        return true;
    }

    private boolean isValidLandline() {

        str_landline = et_landline.getText().toString().trim();

        if (str_landline.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter landline", Toast.LENGTH_LONG).show();

            return false;
        } else {

        }
        return true;
    }

    private boolean validateEmergencyName() {
        str_emergency_name = et_emergency_name.getText().toString().trim();

        if (str_emergency_name.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter emergency name", Toast.LENGTH_LONG).show();

            return false;
        } else {

        }

        return true;
    }

    private boolean validateEmergencyRelationship() {
        str_emergency_relationship = et_emergency_relationship.getText().toString().trim();

        if (str_emergency_relationship.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter  name", Toast.LENGTH_LONG).show();

            return false;
        } else {

        }

        return true;
    }


    private boolean isValidState() {

        str_state = et_state.getText().toString().trim();

        if (str_state.isEmpty()) {
            Toast.makeText(getActivity(), "Select state", Toast.LENGTH_LONG).show();

            return false;
        } else {

        }
        return true;
    }

    private boolean isValidCity() {

        str_city = et_city.getText().toString().trim();

        if (str_city.isEmpty()) {
            Toast.makeText(getActivity(), "Select City", Toast.LENGTH_LONG).show();

            return false;
        } else {

        }
        return true;
    }

    private boolean isValidCountry() {

        str_country = et_country.getText().toString().trim();

        if (str_country.isEmpty()) {
            Toast.makeText(getActivity(), "Select Country", Toast.LENGTH_LONG).show();

            return false;
        } else {

        }
        return true;
    }

    private boolean isValidEmergencyNumber() {

        str_emergency_number = et_emergency_number.getText().toString().trim();

        if (str_emergency_number.isEmpty() || str_emergency_number.length() < 10) {
            Toast.makeText(getActivity(), "provide valid Emergency number", Toast.LENGTH_LONG).show();

            return false;
        } else {

        }
        return true;
    }

    public void getUserDetail(String id) {


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                UrlConstant.GET_Customer_Profile_Update + id, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, response.toString());
                try {
                    String Status = response.getString("Status");
                    response_string = response.toString();
                    data = response;
                    if (Status.equals("false")) {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                android.support.v7.app.AlertDialog.Builder dlgAlert = new android.support.v7.app.AlertDialog.Builder(getActivity());
                                try {
                                    dlgAlert.setMessage(data.getString("Message"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                dlgAlert.setPositiveButton("OK", null);
                                dlgAlert.setCancelable(true);
                                dlgAlert.create().show();

                            }
                        });

                        pDialog.dismiss();

                    } else {

                        et_full_name.setText(response.getString("Name"));
                        gender.setText(response.getString("Gender"));
                        et_dateofbirth.setText(response.getString("DOB"));
                        et_email.setText(response.getString("EmailId"));
                        et_aadhar.setText(response.getString("AdharNumber"));
                        et_blood_group.setText(response.getString("Bloodgroup"));
                        et_marital_status.setText(response.getString("MaritalStatus"));
                        et_occupation.setText(response.getString("Occuption"));
                        et_mobile.setText(response.getString("MobileNumber"));
                        et_address.setText(response.getString("Address1"));
                        et_pincode.setText(response.getString("PinCode"));
                        et_landline.setText(response.getString("LandLineNumber"));
                        et_city.setText(response.getString("CityName"));
                        et_state.setText(response.getString("StateName"));
                        et_country.setText(response.getString("Country"));
                        et_emergency_name.setText(response.getString("EmergencyName"));
                        et_emergency_number.setText(response.getString("EmergencyMobileNumber"));
                        et_emergency_relationship.setText(response.getString("EmergencyRelationShip"));

                    }

                } catch (Exception e) {
                    pDialog.hide();
                }

                pDialog.dismiss();
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
            et_dateofbirth.setText(year + "-" + (month + 1) + "-" + day);

        }
    }


}
