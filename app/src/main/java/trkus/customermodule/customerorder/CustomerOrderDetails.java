package trkus.customermodule.customerorder;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import trkus.services.com.trkus.R;
import util.AppController;
import util.UrlConstant;
import util.UserSessionManager;

import static util.AppController.TAG;

public class CustomerOrderDetails extends Fragment {


    Fragment fragment = null;
    TextView reorder, orderdetail, deliverstatue, firmname, order_date, orderid, orderdata, seller_sms, seller_call;
    NetworkImageView seller_icon,order_icon;
    ProgressDialog pDialog;
    UserSessionManager session;
    String Tag = "Dashboard";
    String order_id;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    String[] SellerUserId, OrderId, ItemName, OrderDate, FirmName, FirmImage, OrderImage1;
    JSONObject data_jobject;
    String seller_id;
    String msg_txt;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getActivity().setTitle("Order Detail");
        order_id = getArguments().getString("OrderId");
        seller_id=getArguments().getString("SellerUserId");
        session = new UserSessionManager(getActivity());
        View convertView = inflater.inflate(R.layout.customer_order_details, container, false);

        reorder = convertView
                .findViewById(R.id.reorder);
        orderdetail = convertView
                .findViewById(R.id.orderdetail);
        deliverstatue = convertView
                .findViewById(R.id.deliverstatue);
        firmname = convertView
                .findViewById(R.id.firmname);
        seller_icon = convertView
                .findViewById(R.id.seller_icon);
        order_date = convertView
                .findViewById(R.id.order_date);
        orderid = convertView
                .findViewById(R.id.orderid);
        orderdata= convertView
                .findViewById(R.id.orderdata);
        seller_sms = convertView
                .findViewById(R.id.seller_sms);
        order_icon = convertView
                .findViewById(R.id.order_icon);


        session = new UserSessionManager(getActivity());

        reorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reOrderData();
            }
        });

        order_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.image_dialog);
                NetworkImageView photo= dialog.findViewById(R.id.photo);
                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();
                photo.setImageUrl(OrderImage1[0], imageLoader);
                wlp.gravity = Gravity.CENTER;
                wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
                window.setAttributes(wlp);
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                dialog.show();
            }
        });

        getOrder(order_id);

        seller_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et_msg;
                final Button next;
                final Dialog pdialog = new Dialog(getActivity());
                // Include dialog.xml file
                pdialog.setContentView(R.layout.message_dialog_layout);

                // set values for custom dialog components - text, image and button
                et_msg = pdialog.findViewById(R.id.et_msg);
                next = pdialog.findViewById(R.id.next);

                pdialog.show();
                Window window = pdialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Close dialog
                        msg_txt = et_msg.getText().toString().trim();

                        if (msg_txt.length() == 0) {
                            Toast.makeText(getActivity(), "Write your message", Toast.LENGTH_LONG).show();
                            return;
                        }

                        pDialog = new ProgressDialog(getActivity());
                        pDialog.setMessage("Loading...");
                        pDialog.show();

                        String tag_json_obj = "json_obj_req";

                        data_jobject = new JSONObject();
                        try {
                            data_jobject.put("Message", msg_txt);
                            data_jobject.put("SellerId", session.getKeyUserid());
                            data_jobject.put("CutsomerId", seller_id);

                        } catch (Exception e) {

                        }

                        Log.e("data_jobject", data_jobject.toString());


                        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                                UrlConstant.POST_CustometosellerChat, data_jobject,
                                new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(final JSONObject response) {
                                        Log.e(TAG, response.toString());
                                        try {
                                            String Status = response.getString("Status");

                                            if (Status.equals("false")) {

                                            } else {

                                                Toast.makeText(getActivity(), "Message send successfully", Toast.LENGTH_LONG).show();

                                            }

                                        } catch (Exception e) {

                                        }

                                        pDialog.hide();
                                    }
                                }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.e(TAG, "Error: " + error.getMessage());

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
                        pdialog.dismiss();
                    }
                });

            }
        });

        return convertView;
    }

    public void getOrder(String order_id) {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonArrayRequest req = new JsonArrayRequest(UrlConstant.GET_Custor_Order_Detail + session.getKeyUserid()+"&OrderId="+order_id,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(Tag, response.toString());

                        SellerUserId = new String[response.length()];
                        OrderId = new String[response.length()];
                        ItemName = new String[response.length()];
                        FirmName = new String[response.length()];
                        OrderDate = new String[response.length()];
                        FirmName = new String[response.length()];
                        FirmImage = new String[response.length()];
                        OrderImage1 = new String[response.length()];

                        try {

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response
                                        .get(i);
                                SellerUserId[i] = person.getString("SellerUserId");
                                OrderId[i] = person.getString("OrderId");
                                Log.e("OrderId",OrderId[i]);
                                ItemName[i] = person.getString("ItemName");
                                OrderDate[i] = person.getString("OrderDate");
                                FirmName[i] = person.getString("FirmName");
                                FirmImage[i] = person.getString("FirmImage");
                                OrderImage1[i] = person.getString("OrderImage1");

                            }

                            firmname.setText(FirmName[0]);
                            orderid.setText(OrderId[0]);
                            order_date.setText(OrderDate[0]);
                            String temp =FirmImage[0];
                            temp = temp.replaceAll(" ", "%20");
                            seller_icon.setImageUrl(temp, imageLoader);
                            orderdata.setText(ItemName[0]);
                            temp =OrderImage1[0];
                            temp = temp.replaceAll(" ", "%20");
                            order_icon.setImageUrl(temp, imageLoader);

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

public void reOrderData(){
    String tag_json_obj = "json_obj_req";
    data_jobject = new JSONObject();
    try {
        data_jobject.put("OrderId", order_id);
        data_jobject.put("CustomerUserId", session.getKeyUserid());
        data_jobject.put("SellerUserId", seller_id);

    } catch (Exception e) {

    }

    Log.e("data_jobject", data_jobject.toString());

    pDialog = new ProgressDialog(getActivity());
    pDialog.setMessage("Loading...");
    pDialog.show();

    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
            UrlConstant.POST_Reorder_Item, data_jobject,
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
                                    dlgAlert.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            fragment = new CustomeOrderPage();
                                            FragmentTransaction tx = getActivity().getSupportFragmentManager().beginTransaction();
                                            tx.replace(R.id.flContent, fragment, "CustomerOrderPage");
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
