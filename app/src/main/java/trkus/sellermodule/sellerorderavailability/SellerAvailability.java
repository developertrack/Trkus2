package trkus.sellermodule.sellerorderavailability;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

import trkus.services.com.trkus.R;
import util.AppController;
import util.SetTime;
import util.UrlConstant;
import util.UserSessionManager;

public class SellerAvailability extends Fragment {

    CheckBox mon,tue,wed,thu,fri,sat,sun;
    EditText mon_from,mon_to,tue_from,tue_to,wed_from,wed_to,thu_from,thu_to,fri_from,fri_to,sat_from,sat_to,sun_from,sun_to;
    Button btn_next;
    SetTime fromTime;
    JSONObject rawdata,addtime_mon,addtime_tue,addtime_wed,addtime_thu,addtime_fri,addtime_sat,addtime_sun;
    UserSessionManager session;
    JSONArray array_rawdata;
    ProgressDialog pDialog;
    String tag_json_obj = "json_obj_req";
    String Tag = "Dashboard";
    String[] SellerId,Days,StartTime,EndTime,Id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.seller_appointment_availabilty, container, false);

        mon_from= view.findViewById(R.id.mon_from);
        mon_to= view.findViewById(R.id.mon_to);
        tue_from= view.findViewById(R.id.tue_from);
        tue_to= view.findViewById(R.id.tue_to);
        wed_from= view.findViewById(R.id.wed_from);
        wed_to= view.findViewById(R.id.wed_to);
        thu_from= view.findViewById(R.id.thu_from);
        thu_to= view.findViewById(R.id.thu_to);
        fri_from= view.findViewById(R.id.fri_from);
        fri_to= view.findViewById(R.id.fri_to);
        sat_from= view.findViewById(R.id.sat_from);
        sat_to= view.findViewById(R.id.sat_to);
        sun_from= view.findViewById(R.id.sun_from);
        sun_to= view.findViewById(R.id.sun_to);

        mon= view.findViewById(R.id.mon);
        tue= view.findViewById(R.id.tue);
        wed= view.findViewById(R.id.wed);
        thu= view.findViewById(R.id.thu);
        fri= view.findViewById(R.id.fri);
        sat= view.findViewById(R.id.sat);
        sun= view.findViewById(R.id.sun);

        btn_next= view.findViewById(R.id.btn_next);

        session=new UserSessionManager(getActivity());


        fromTime = new SetTime(mon_from, getContext());
        fromTime = new SetTime(mon_to, getActivity());
        fromTime = new SetTime(tue_from, getActivity());
        fromTime = new SetTime(tue_to, getActivity());
        fromTime = new SetTime(wed_from, getActivity());
        fromTime = new SetTime(wed_to, getActivity());
        fromTime = new SetTime(thu_from, getActivity());
        fromTime = new SetTime(thu_to, getActivity());
        fromTime = new SetTime(fri_from, getActivity());
        fromTime = new SetTime(fri_to, getActivity());
        fromTime = new SetTime(sat_from, getActivity());
        fromTime = new SetTime(sat_to, getActivity());
        fromTime = new SetTime(sun_from, getActivity());
        fromTime = new SetTime(sun_to, getActivity());
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        getAvailability(session.getKeyUserid());

        rawdata=new JSONObject();
        addtime_mon=new JSONObject();
        addtime_tue=new JSONObject();
        addtime_wed=new JSONObject();
        addtime_thu=new JSONObject();
        addtime_fri=new JSONObject();
        addtime_sat=new JSONObject();
        addtime_sun=new JSONObject();

        array_rawdata=new JSONArray();

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    rawdata.put("SellerId", session.getKeyUserid());

                    if(mon.isChecked()){


                        addtime_mon.put("Days","MONDAY");
                        addtime_mon.put("StartTime",mon_from.getText().toString());
                        addtime_mon.put("EndTime",mon_to.getText().toString());
                        array_rawdata.put(addtime_mon);
                    }
                    if(tue.isChecked()){
                        addtime_tue.put("Days","TUESDAY");
                        addtime_tue.put("StartTime",tue_from.getText().toString());
                        addtime_tue.put("EndTime",tue_to.getText().toString());
                        array_rawdata.put(addtime_tue);
                    }
                    if(wed.isChecked()){
                        addtime_wed.put("Days","WEDNESDAY");
                        addtime_wed.put("StartTime",wed_from.getText().toString());
                        addtime_wed.put("EndTime",wed_to.getText().toString());
                        array_rawdata.put(addtime_wed);
                    }
                    if(thu.isChecked()){
                        addtime_thu.put("Days","THURSDAY");
                        addtime_thu.put("StartTime",thu_from.getText().toString());
                        addtime_thu.put("EndTime",thu_to.getText().toString());
                        array_rawdata.put(addtime_thu);
                    }
                    if(fri.isChecked()){
                        addtime_fri.put("Days","FRIDAY");
                        addtime_fri.put("StartTime",fri_from.getText().toString());
                        addtime_fri.put("EndTime",fri_to.getText().toString());
                        array_rawdata.put(addtime_fri);
                    }
                    if(sat.isChecked()){
                        addtime_sat.put("Days","SATURDAY");
                        addtime_sat.put("StartTime",sat_from.getText().toString());
                        addtime_sat.put("EndTime",sat_to.getText().toString());
                        array_rawdata.put(addtime_sat);
                    }
                    if(sun.isChecked()){
                        addtime_sun.put("Days","SUNDAY");
                        addtime_sun.put("StartTime",sun_from.getText().toString());
                        addtime_sun.put("EndTime",sun_to.getText().toString());
                        array_rawdata.put(addtime_sun);
                    }

                    rawdata.put("AddTime",array_rawdata);

                    Log.e("rawdata",rawdata.toString());
                    sendAvailability();

                }catch (Exception e){

                }

            }
        });

        return view;
    }


    public void sendAvailability(){

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                UrlConstant.POST_Seller_Schedule, rawdata,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject response) {
                        Log.e(tag_json_obj, response.toString());
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
                                        dlgAlert.setMessage("Your Business availability updated successfully");

                                        dlgAlert.setPositiveButton("OK", null);
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
                VolleyLog.e(tag_json_obj, "Error: " + error.getMessage());
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
        pDialog.dismiss();
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

//                                mon,tue,wed,thu,fri,sat,sun

                                if(Days[i].equals("MONDAY")){
                                    mon_from.setText(StartTime[i]);
                                    mon_to.setText(EndTime[i]);
                                    mon.setChecked(true);
                                }

                                if(Days[i].equals("TUESDAY")){
                                    tue_from.setText(StartTime[i]);
                                    tue_to.setText(EndTime[i]);
                                    tue.setChecked(true);
                                }

                                if(Days[i].equals("WEDNESDAY")){
                                    wed_from.setText(StartTime[i]);
                                    wed_to.setText(EndTime[i]);
                                    wed.setChecked(true);
                                }

                                if(Days[i].equals("THURSDAY")){
                                    thu_from.setText(StartTime[i]);
                                    thu_to.setText(EndTime[i]);
                                    thu.setChecked(true);
                                }

                                if(Days[i].equals("FRIDAY")){
                                    fri_from.setText(StartTime[i]);
                                    fri_to.setText(EndTime[i]);
                                    fri.setChecked(true);
                                }

                                if(Days[i].equals("SATURDAY")){
                                    sat_from.setText(StartTime[i]);
                                    sat_to.setText(EndTime[i]);
                                    sat.setChecked(true);
                                }

                                if(Days[i].equals("SUNDAY")){
                                    sun_from.setText(StartTime[i]);
                                    sun_to.setText(EndTime[i]);
                                    sun.setChecked(true);
                                }


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

    }


