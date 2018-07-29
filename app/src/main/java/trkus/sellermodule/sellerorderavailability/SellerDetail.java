package trkus.sellermodule.sellerorderavailability;

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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import trkus.sellermodule.ProfileEdit;
import trkus.services.com.trkus.R;
import util.AppController;
import util.UrlConstant;
import util.UserSessionManager;

public class SellerDetail extends Fragment {

    UserSessionManager session;
    TextView name, gender, firmname,categoryofbusiness, bloodgroup,mobile,email, address,
           landline,  emobile;
    NetworkImageView Image1,Image2,Image3;
    ScrollView scrollprofile;
    String tag_json_obj = "json_obj_req";
    ProgressDialog pDialog;
    JSONObject data_jobject;
    String TAG = "LoginActivity_TAG";
    String result = "NA", response_string;
    JSONObject data;
    Button btn_save;
    Fragment fragment = null;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("My Profile");
         View view = inflater.inflate(R.layout.seller_profile_detail, container, false);

        btn_save = view.findViewById(R.id.btn_save);
        name = view.findViewById(R.id.name);
        gender = view.findViewById(R.id.gender);
        firmname = view.findViewById(R.id.firmname);
        categoryofbusiness = view.findViewById(R.id.categoryofbusiness);
        bloodgroup = view.findViewById(R.id.bloodgroup);
        email = view.findViewById(R.id.email);
        mobile = view.findViewById(R.id.mobile);
        address = view.findViewById(R.id.address);
        landline = view.findViewById(R.id.landline);
        emobile = view.findViewById(R.id.emobile);
        scrollprofile = view.findViewById(R.id.scrollprofile);
        Image1= view.findViewById(R.id.image1);
        Image2= view.findViewById(R.id.image2);
        Image3= view.findViewById(R.id.image3);
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
                fragment = new ProfileEdit();
                FragmentTransaction tx = getActivity().getSupportFragmentManager().beginTransaction();
                tx.replace(R.id.flContent, fragment, "ProfileEdit");
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
                UrlConstant.GET_Seller_Profile_Update + id, new Response.Listener<JSONObject>() {

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


                    } else {

                        if(response.getString("FirmName").equals("null")){
                            fragment = new ProfileEdit();
                            FragmentTransaction tx = getActivity().getSupportFragmentManager().beginTransaction();
                            tx.replace(R.id.flContent, fragment, "ProfileEdit");
                            tx.commit();
                            tx.addToBackStack(null);
                        }else {

                            name.setText(response.getString("Name"));
                            gender.setText(response.getString("Gender") + "");
                            bloodgroup.setText(response.getString("Bloodgroup"));
                            firmname.setText(response.getString("FirmName"));
                            mobile.setText(response.getString("MobileNumber"));
                            categoryofbusiness.setText(response.getString("CategoryOfBusiness"));
                            address.setText(response.getString("Address1"));
                            email.setText(response.getString("EmailId"));
//                            landline.setText(response.getString("LandLineNumber"));
                            emobile.setText(response.getString("EmergencyMobileNumber"));

                            Image1.setImageUrl(response.getString("Image1"), imageLoader);
                            Image2.setImageUrl(response.getString("Image2"), imageLoader);
                            Image3.setImageUrl(response.getString("Image3"), imageLoader);
                        }

                    }

                } catch (Exception e) {

                }
                pDialog.dismiss();
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

