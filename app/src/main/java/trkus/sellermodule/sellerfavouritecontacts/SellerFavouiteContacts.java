package trkus.sellermodule.sellerfavouritecontacts;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import trkus.customermodule.User;
import trkus.services.com.trkus.R;
import util.AppController;
import util.UrlConstant;
import util.UserSessionManager;

import static util.AppController.TAG;

public class SellerFavouiteContacts extends Fragment {

    Fragment fragment = null;
    String[] Name, Address, FirmName, MobileNumber, Id;
    ProgressDialog pDialog;
    String Tag = "Dashboard";
    ListView sellerlist;
    ArrayList<SellerFavouriteContactData> seller_data = null;
    SellerFavouriteContactAdapter selleradapter;
    UserSessionManager session;
    private static final int REQUEST_CONTACT = 200;
    String tag_json_obj = "json_obj_req";
    JSONObject data_jobject;
    EditText name, et_mobile, location, sellertype;
    Button save;
    String str_name, str_et_mobile, str_location, str_sellertype;
    TextView addcontact, synccontact;
    JSONObject contactdata;
    JSONArray arr_contactdata;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getActivity().setTitle("My Favourite Contacts");
        View view = inflater.inflate(R.layout.favourite_contact_listing, container, false);
        sellerlist = view.findViewById(R.id.sellerlist);
        addcontact = view.findViewById(R.id.addcontact);
        seller_data = new ArrayList<>();
        session = new UserSessionManager(getActivity());
        getFavouriteContact(session.getKeyUserid());
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        sellerlist.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                sellerlist.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        sellerlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getActivity(), "You Clicked at " + FirmName[+position], Toast.LENGTH_SHORT).show();

            }
        });

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CONTACT);
        } else {
            readAllContact();
        }

        synccontact = view.findViewById(R.id.synccontact);

        synccontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readAllContact();
            }
        });


        addcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                // Include dialog.xml file
                dialog.setContentView(R.layout.dialog_addcontact);

                // set values for custom dialog components - text, image and button
                name = dialog.findViewById(R.id.name);
                et_mobile = dialog.findViewById(R.id.et_mobile);
                location = dialog.findViewById(R.id.location);
                sellertype = dialog.findViewById(R.id.sellertype);
                dialog.show();

                save = dialog.findViewById(R.id.save);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Close dialog
                        str_name = name.getText().toString().trim();
                        str_sellertype = sellertype.getText().toString().trim();
                        str_et_mobile = et_mobile.getText().toString().trim();
                        str_location = location.getText().toString().trim();

                        if (!validateFirstname()) {
                            return;
                        }
                        if (!isValidMobile()) {
                            return;
                        }
                        if (!isValidLocation()) {
                            return;
                        }

                        if (!validateFirmname()) {
                            return;
                        }


                        String tag_json_obj = "json_obj_req";

                        data_jobject = new JSONObject();
                        try {
                            data_jobject.put("Name", str_name);
                            data_jobject.put("MobileNumber", str_et_mobile);
                            data_jobject.put("Location", str_location);
                            data_jobject.put("FirmName", str_sellertype);
                            data_jobject.put("SellerId", session.getKeyUserid());

                        } catch (Exception e) {

                        }

                        Log.e("data_jobject", data_jobject.toString());

                        pDialog = new ProgressDialog(getActivity());
                        pDialog.setMessage("Loading...");
                        pDialog.show();

                        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                                UrlConstant.POST_Seller_Favourite_Contact, data_jobject,
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
                                                        dlgAlert.setMessage("This contact added to your favourite.");

                                                        dlgAlert.setPositiveButton("OK", null);
                                                        dlgAlert.setCancelable(true);
                                                        dlgAlert.create().show();

                                                        seller_data = new ArrayList<SellerFavouriteContactData>();
                                                        getFavouriteContact(session.getKeyUserid());
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
                        dialog.dismiss();
                    }
                });

            }
        });

        return view;

    }


    public void readAllContact() {
        Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        contactdata = new JSONObject();
        arr_contactdata = new JSONArray();

        try {
            contactdata.put("UserId", session.getKeyUserid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        List<User> userList = new ArrayList<User>();
        while (phones.moveToNext()) {
            User user = new User();
            user.setNamee(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
            user.setPhone(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
            userList.add(user);
            Log.e(TAG, "Name : " + user.getNamee().toString());
            Log.e(TAG, "Phone : " + user.getPhone().toString());


            try {
                JSONObject student1 = new JSONObject();
                student1.put("ContactId", "3");
                student1.put("PersonName", user.getNamee().toString());
                student1.put("PersonNumber", user.getPhone().toString());
                arr_contactdata.put(student1);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        try {
            contactdata.put("dataList", arr_contactdata);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        postContact();

        phones.close();
    }

    void postContact() {
        try {

            String tag_json_obj = "json_obj_req";

            try {

                Log.e("data_jobject", contactdata.toString());

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        UrlConstant.POST_PostRefEarns, contactdata,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(final JSONObject response) {
                                Log.e(TAG, response.toString());
                            }

                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e(TAG, "Error: " + error.getMessage());

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

            } catch (Exception e) {

            }


        } catch (SecurityException ex) {
            Log.d("gps", "Location permission did not work!");
        }
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
//        getActivity().setTitle("USE");
    }

    private boolean validateFirstname() {


        if (str_name.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter  name", Toast.LENGTH_LONG).show();

            return false;
        } else {

        }

        return true;
    }

    private boolean validateFirmname() {


        if (str_sellertype.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter  name", Toast.LENGTH_LONG).show();

            return false;
        } else {

        }

        return true;
    }

    private boolean isValidMobile() {


        if (str_et_mobile.isEmpty() || str_et_mobile.length() < 10) {

            Toast.makeText(getActivity(), "Not Valid Number", Toast.LENGTH_LONG).show();

            return false;
        } else {

        }

        return true;
    }


    private boolean isValidLocation() {

        if (str_location.isEmpty()) {
            Toast.makeText(getActivity(), "Select Location", Toast.LENGTH_LONG).show();

            return false;
        } else {

        }
        return true;
    }


    public void getFavouriteContact(String id) {
        JsonArrayRequest req = new JsonArrayRequest(UrlConstant.GET_Seller_Favourite_Contact + id,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(Tag, response.toString());
                        Name = new String[response.length()];
                        Address = new String[response.length()];
                        FirmName = new String[response.length()];
                        MobileNumber = new String[response.length()];
                        Id = new String[response.length()];

//                        Name,Address,FirmName,MobileNumber

                        try {

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response
                                        .get(i);
                                Name[i] = person.getString("Name");
                                Address[i] = person.getString("Address");
                                FirmName[i] = person.getString("FirmName");
                                MobileNumber[i] = person.getString("MobileNumber");
                                Id[i] = person.getString("Id");
                                seller_data.add(new SellerFavouriteContactData(Name[i], Address[i], FirmName[i],
                                        MobileNumber[i], Id[i]));
                            }
                            selleradapter = new SellerFavouriteContactAdapter(getActivity(), R.layout.favouritecontact_adapter, seller_data);
                            sellerlist.setAdapter(selleradapter);
                            selleradapter.notifyDataSetChanged();

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


//    {
//        "Message": "Sucess!",
//            "Status": true
//    }


}
