package trkus.customermodule;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import trkus.services.com.trkus.R;
import util.AppController;
import util.UrlConstant;
import util.UserSessionManager;

public class CustomerProfileDetail extends Fragment {

    UserSessionManager session;
    TextView name, gender, dob, aadhar, bloodgroup, maritalstatus, occupation, mobile, address,
            city, state, country, pincode, landline, ename, emobile, eraltionship;
    ScrollView scrollprofile;
    String tag_json_obj = "json_obj_req";
    ProgressDialog pDialog;
    JSONObject data_jobject;
    String TAG = "LoginActivity_TAG";
    String result = "NA", response_string;
    JSONObject data;
    Button btn_save;
    Fragment fragment = null;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("My Profile");
        View view = inflater.inflate(R.layout.customer_profile_detail, container, false);

        btn_save = view.findViewById(R.id.btn_save);
        name = view.findViewById(R.id.name);
        gender = view.findViewById(R.id.gender);
        dob = view.findViewById(R.id.dob);
        aadhar = view.findViewById(R.id.aadhar);
        bloodgroup = view.findViewById(R.id.bloodgroup);
        maritalstatus = view.findViewById(R.id.maritalstatus);
        occupation = view.findViewById(R.id.occupation);
        mobile = view.findViewById(R.id.mobile);
        address = view.findViewById(R.id.address);
        city = view.findViewById(R.id.city);
        state = view.findViewById(R.id.state);
        country = view.findViewById(R.id.country);
        pincode = view.findViewById(R.id.pincode);
        landline = view.findViewById(R.id.landline);
        ename = view.findViewById(R.id.ename);
        emobile = view.findViewById(R.id.emobile);
        eraltionship = view.findViewById(R.id.eraltionship);
        scrollprofile = view.findViewById(R.id.scrollprofile);

        session = new UserSessionManager(getActivity());

        scrollprofile = view.findViewById(R.id.scrollprofile);

        scrollprofile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                scrollprofile.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new CustomerProfileEdit();
                FragmentTransaction tx = getActivity().getSupportFragmentManager().beginTransaction();
                tx.replace(R.id.flContent, fragment, "CustomerOrderPage");
                tx.commit();
                tx.addToBackStack(null);
            }
        });

        getUserDetail(session.getKeyUserid());

        return view;

    }

    public void getUserDetail(String id) {

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

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
                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
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
                        if(response.getString("AdharNumber").equals("null")){
                            fragment = new CustomerProfileEdit();
                            FragmentTransaction tx = getActivity().getSupportFragmentManager().beginTransaction();
                            tx.replace(R.id.flContent, fragment, "ProfileEdit");
                            tx.commit();
                            tx.addToBackStack(null);
                        }else {

                            name.setText(response.getString("Name"));
                            gender.setText(response.getString("Gender"));
                            dob.setText(response.getString("DOB"));
                            aadhar.setText(response.getString("AdharNumber"));
                            bloodgroup.setText(response.getString("Bloodgroup"));
                            maritalstatus.setText(response.getString("MaritalStatus"));
                            occupation.setText(response.getString("Occuption"));
                            mobile.setText(response.getString("MobileNumber"));
                            address.setText(response.getString("Address1"));
                            pincode.setText(response.getString("PinCode"));
                            landline.setText(response.getString("LandLineNumber"));
                            city.setText(response.getString("CityName"));
                            state.setText(response.getString("StateName"));
                            country.setText(response.getString("Country"));
                            ename.setText(response.getString("EmergencyName"));
                            emobile.setText(response.getString("EmergencyMobileNumber"));
                            eraltionship.setText(response.getString("EmergencyRelationShip"));
                        }

                    }
                    pDialog.dismiss();

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

}

