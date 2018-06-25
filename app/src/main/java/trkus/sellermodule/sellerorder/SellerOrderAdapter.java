package trkus.sellermodule.sellerorder;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import trkus.services.com.trkus.R;
import util.AppController;
import util.UrlConstant;

import static util.AppController.TAG;

public class SellerOrderAdapter extends ArrayAdapter<SellerOrderData> {

    Context context;
    int layoutResourceId;
    ArrayList<SellerOrderData> dataget = null;
    ViewHolder holder;
    int pos = 0;
    SellerOrderData getdata;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    Fragment fragment = null;
    JSONObject data_jobject;
    String msg_txt;
    ProgressDialog pDialog;
    public SellerOrderAdapter(FragmentActivity activity, int resource, ArrayList<SellerOrderData> data) {
        super(activity, resource, data);

        context = activity;
        layoutResourceId = resource;
        dataget = data;
    }

    @Override
    public int getCount() {
        return dataget.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        holder = null;
        LayoutInflater inflater = LayoutInflater.from(context);

        if (convertView == null) {

            convertView = inflater.inflate(layoutResourceId, parent, false);

            holder = new ViewHolder();
            holder.seller_call = convertView
                    .findViewById(R.id.seller_call);
            holder.seller_sms = convertView
                    .findViewById(R.id.seller_sms);
            holder.customer_address = convertView
                    .findViewById(R.id.customer_address);
            holder.customer_name_title = convertView
                    .findViewById(R.id.customer_name_title);
            holder.customer_icon = convertView
                    .findViewById(R.id.customer_icon);
            holder.order_date = convertView
                    .findViewById(R.id.order_date);
            holder.orderid = convertView
                    .findViewById(R.id.orderid);
            holder.sellerdetail= convertView.findViewById(R.id.sellerdetail);

            holder.customer_name_title.setTag(position);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        getdata = dataget.get(position);
        pos = position;
        holder.customer_name_title.setText(getdata.getName());
        holder.customer_address.setText(getdata.getAddress());
        holder.orderid.setText(getdata.getOrderId());
        holder.order_date.setText(getdata.getOrderDate());
        holder.customer_icon.setImageUrl(getdata.getOrderImage1(), imageLoader);

        holder.seller_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag_json_obj = "json_obj_req";
                getdata = dataget.get(position);
                context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", getdata.getMobileNumber(), null)));
            }
        });

        holder.seller_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getdata = dataget.get(position);
                final EditText et_msg;
                final Button next;
                final Dialog pdialog = new Dialog(context);
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
                            Toast.makeText(context, "Write your message", Toast.LENGTH_LONG).show();
                            return;
                        }

                        pDialog = new ProgressDialog(context);
                        pDialog.setMessage("Loading...");
                        pDialog.show();

                        String tag_json_obj = "json_obj_req";

                        data_jobject = new JSONObject();
                        try {
                            data_jobject.put("Message", msg_txt);
                            data_jobject.put("SellerId", getdata.getSellerUserId());
                            data_jobject.put("CutsomerId", getdata.getCustomerUserId());

                        } catch (Exception e) {

                        }

                        Log.e("data_jobject", data_jobject.toString());


                        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                                UrlConstant.POST_SellerToCustomerChat, data_jobject,
                                new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(final JSONObject response) {
                                        Log.e(TAG, response.toString());
                                        try {
                                            String Status = response.getString("Status");

                                            if (Status.equals("false")) {


                                            } else {

                                                Toast.makeText(context, "Message send successfully", Toast.LENGTH_LONG).show();


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

        holder.sellerdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getdata = dataget.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("OrderId",getdata.getOrderId() );
                fragment = new SellerOrderDetails();
                fragment.setArguments(bundle);
                FragmentTransaction tx = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
                tx.replace(R.id.flContent, fragment, getdata.getOrderId() );
                tx.commit();
                tx.addToBackStack(null);
            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView seller_call, seller_sms, customer_address, customer_name_title, order_date, orderid;
        NetworkImageView customer_icon;
        LinearLayout sellerdetail;
    }

}
