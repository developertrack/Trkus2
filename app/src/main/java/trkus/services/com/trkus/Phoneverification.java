package trkus.services.com.trkus;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import trkus.sellermodule.SellerDashboard;
import util.AppController;
import util.UrlConstant;
import util.UserSessionManager;

public class Phoneverification extends AppCompatActivity {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    String UserTypeId,OTP,MobileNumber,otp,UserId;
    EditText input_otp;
    TextView counter,resend,mob_text;
    String TAG = "MessageVerificationActivity_TAG";
    ProgressDialog pDialog;
    JSONObject data_jobject,data_jobject1,data_jobject2;
    Button btn_confirm,btn_previous;
    Intent intent;
    UserSessionManager session;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String txt_otp = intent.getStringExtra("message");
                input_otp.setText(txt_otp);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messageverification);

        if (checkAndRequestPermissions()) {
            // carry on the normal flow, as the case of  permissions  granted.
        }
        session=new UserSessionManager(Phoneverification.this);
        input_otp=(EditText)findViewById(R.id.input_otp);
        counter=(TextView)findViewById(R.id.counter);
        resend=(TextView)findViewById(R.id.resend);
        mob_text=(TextView)findViewById(R.id.mob_text);
        btn_previous=(Button)findViewById(R.id.btn_previous);
        btn_confirm=(Button)findViewById(R.id.btn_confirm);

        intent = getIntent();
        UserTypeId=intent.getStringExtra("UserTypeId");
        OTP=intent.getStringExtra("OTP");
        MobileNumber=intent.getStringExtra("MobileNumber");
        UserId=intent.getStringExtra("UserId");

        mob_text.setText("You will receive an one time password on your mobile number " +MobileNumber);
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResendOtp();
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp = input_otp.getText().toString().trim();

                if (otp.isEmpty() || otp.length()<6) {

                    Toast.makeText(Phoneverification.this,"Enter correct OTP",Toast.LENGTH_LONG).show();

                } else {
                    verifyUser();
                }
            }
        });
        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        new CountDownTimer(45000, 1000) {

            public void onTick(long millisUntilFinished) {
                counter.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                counter.setText("done!");
            }

        }.start();

    }



    public void verifyUser(){

        String tag_json_obj = "json_obj_req";

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        data_jobject2 = new JSONObject();
        try {
            data_jobject2.put("OTP", input_otp.getText().toString());
            data_jobject2.put("MobileNumber", MobileNumber);

        } catch (Exception e) {

        }
        Log.e("Verify OTP",data_jobject2.toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                UrlConstant.VERIFY_URL, data_jobject2,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject response) {
                        Log.e(TAG, response.toString());
                        try{
                            String Status=response.getString("Status");

                            if(Status.equals("false")){

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Phoneverification.this);
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

                            }else{
                                if(UserTypeId.equals("0")) {
                                    Intent verification = new Intent(Phoneverification.this, UserTypeSelection.class);
                                    verification.putExtra("MobileNumber", MobileNumber);
                                    verification.putExtra("UserTypeId", UserTypeId);
                                    startActivity(verification);
                                }else{
                                    if(UserTypeId.equals("1")) {
                                        Intent verification = new Intent(Phoneverification.this, SellerDashboard.class);
                                        verification.putExtra("MobileNumber", MobileNumber);
                                        verification.putExtra("UserTypeId", UserTypeId);
                                        verification.putExtra("UserId", UserId);

                                        session.createUserLoginSession(UserTypeId, UserId, MobileNumber);

                                        verification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(verification);
                                    }
                                    if(UserTypeId.equals("2")) {
                                        Intent verification = new Intent(Phoneverification.this, Dashboard.class);
                                        verification.putExtra("MobileNumber", MobileNumber);
                                        verification.putExtra("UserTypeId", UserTypeId);
                                        verification.putExtra("UserId", UserId);

                                        session.createUserLoginSession(UserTypeId, UserId, MobileNumber);

                                        verification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(verification);
                                    }
                                }
                            }

                        }catch (Exception e){

                        }

                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Phoneverification.this);
                            dlgAlert.setMessage("Error while logging in, please try again");
                            dlgAlert.setPositiveButton("OK", null);
                            dlgAlert.setCancelable(true);
                            dlgAlert.create().show();

                    }
                });
                pDialog.hide();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);


    }


    public void ResendOtp(){

        String tag_json_obj = "json_obj_req";

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        data_jobject1 = new JSONObject();
        try {
            data_jobject1.put("MobileNumber", MobileNumber);
        } catch (Exception e) {

        }

        Log.e("Resend OTP",data_jobject1.toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                UrlConstant.RESEND_OTP, data_jobject1,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject response) {
                        Log.e(TAG, response.toString());
                        try{
                            String Status=response.getString("Status");

                            if(Status.equals("false")){

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Phoneverification.this);
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

                            }else{
                                otp=response.getString("OTP");
                                input_otp.setText(otp);

                            }

                        }catch (Exception e){

                        }

                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Phoneverification.this);
                        dlgAlert.setMessage("Error, please try again");
                        dlgAlert.setPositiveButton("OK", null);
                        dlgAlert.setCancelable(true);
                        dlgAlert.create().show();
                    }
                });
                pDialog.hide();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);


    }


    private  boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);

        int receiveSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS);

        int readSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS);

        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_MMS);
        }
        if (result != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

}
