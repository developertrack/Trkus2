package trkus.services.com.trkus;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import trkus.sellermodule.SellerDashboard;
import util.AppController;
import util.UrlConstant;
import util.UserSessionManager;

public class LoginActivity extends AppCompatActivity {
    Button btn_next;
    EditText mob_no;
    String tag_json_obj = "json_obj_req";
    ProgressDialog pDialog;
    JSONObject data_jobject;
    String UserTypeId, OTP, MobileNumber, UserId;
    String TAG = "LoginActivity_TAG";
    String result = "NA", response_string;
    JSONObject data;
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_next = findViewById(R.id.btn_next);
        mob_no = findViewById(R.id.mob_no);
        session = new UserSessionManager(LoginActivity.this);

        if (session.getKeyUserType() == null) {

        } else {

            if (session.getKeyUserType().equals("1")) {
                Intent verification = new Intent(LoginActivity.this, SellerDashboard.class);
                verification.putExtra("MobileNumber", session.getKeyNumber());
                verification.putExtra("UserTypeId", session.getKeyUserType());
                verification.putExtra("UserId", session.getKeyUserid());
                Log.e("session.getKeyUserid()", session.getKeyUserid());
                verification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(verification);
            }

            if (session.getKeyUserType().equals("2")) {
                Intent verification = new Intent(LoginActivity.this, Dashboard.class);
                verification.putExtra("MobileNumber", session.getKeyNumber());
                verification.putExtra("UserTypeId", session.getKeyUserType());
                verification.putExtra("UserId", session.getKeyUserid());
                Log.e("session.getKeyUserid()", session.getKeyUserid());
                verification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(verification);
            }
        }

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mob_no.getText().length() > 1 && mob_no.getText().length() < 10) {
                    Toast.makeText(LoginActivity.this, "Please enter correct number", Toast.LENGTH_LONG).show();
                } else {
                    confirmDialog();
                }
            }
        });
    }


    public void confirmDialog() {
        final Dialog dialog = new Dialog(LoginActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_number_verify);
        TextView txt_mobile = dialog.findViewById(R.id.txt_mobile);
        Button btn_confirm = dialog.findViewById(R.id.btn_confirm);
        Button btn_edit = dialog.findViewById(R.id.btn_edit);
        txt_mobile.setText("your mobile number is " + mob_no.getText().toString());
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
                dialog.dismiss();
            }
        });
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void userLogin() {

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        data_jobject = new JSONObject();
        try {
            data_jobject.put("MobileNumber", mob_no.getText().toString());

        } catch (Exception e) {

        }

        Log.e("data_jobject", data_jobject.toString());

        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                UrlConstant.LOGIN_URL, data_jobject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, response.toString());
                        try {
                            String Status = response.getString("Status");
                            response_string = response.toString();
                            data = response;
                            if (Status.equals("false")) {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(LoginActivity.this);
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

                            } else {
                                MobileNumber = data.getString("MobileNumber");
                                OTP = data.getString("OTP");
                                UserTypeId = data.getString("UserTypeId");
                                UserId = data.getString("UserId");
                                Intent verify = new Intent(LoginActivity.this, Phoneverification.class);
                                verify.putExtra("MobileNumber", MobileNumber);
                                verify.putExtra("OTP", OTP);
                                verify.putExtra("UserTypeId", UserTypeId);
                                verify.putExtra("UserId", UserId);
                                startActivity(verify);

                            }

                        } catch (Exception e) {

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
                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(LoginActivity.this);
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
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

}
