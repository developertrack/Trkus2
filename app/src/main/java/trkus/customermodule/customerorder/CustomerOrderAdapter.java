package trkus.customermodule.customerorder;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import util.UserSessionManager;

import static util.AppController.TAG;

public class CustomerOrderAdapter extends ArrayAdapter<OrderData> {

    Context context;
    int layoutResourceId;
    ArrayList<OrderData> dataget = null;
    ViewHolder holder;
    ProgressDialog pDialog;
    int pos = 0;
    OrderData getdata;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    UserSessionManager session;
    JSONObject data_jobject;
    String Tag = "Dashboard";

    public CustomerOrderAdapter(FragmentActivity activity, int resource, ArrayList<OrderData> data) {
        super(activity, resource, data);

        context = activity;
        layoutResourceId = resource;
        dataget = data;
        session = new UserSessionManager(context);
        pDialog = new ProgressDialog(context);
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
            holder.reorder = convertView
                    .findViewById(R.id.reorder);
            holder.orderdetail = convertView
                    .findViewById(R.id.orderdetail);
            holder.deliverstatue = convertView
                    .findViewById(R.id.deliverstatue);
            holder.firmname = convertView
                    .findViewById(R.id.firmname);
            holder.seller_icon = convertView
                    .findViewById(R.id.seller_icon);
            holder.order_date = convertView
                    .findViewById(R.id.order_date);
            holder.orderid = convertView
                    .findViewById(R.id.orderid);

            holder.orderdetail.setTag(position);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        getdata = dataget.get(position);
        pos = position;
        holder.firmname.setText(getdata.getFirmName());
        holder.orderid.setText(getdata.getOrderId());
        holder.order_date.setText(getdata.getOrderDate());
        holder.seller_icon.setImageUrl(getdata.getFirmImage(), imageLoader);

        holder.reorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pDialog.setMessage("Loading...");
                pDialog.show();

                String tag_json_obj = "json_obj_req";

                data_jobject = new JSONObject();
                try {
                    data_jobject.put("OrderId", getdata.getOrderId());
                    data_jobject.put("CustomerUserId", session.getKeyUserid());
                    data_jobject.put("SellerUserId", getdata.getSellerUserId());

                } catch (Exception e) {

                }

                Log.e("data_jobject", data_jobject.toString());

                pDialog = new ProgressDialog(context);
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

                                        Toast.makeText(context, response.getString("Message"), Toast.LENGTH_LONG).show();

                                    } else {
                                        Toast.makeText(context, response.getString("Message"), Toast.LENGTH_LONG).show();
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
        });

        return convertView;
    }

    class ViewHolder {
        TextView reorder, orderdetail, deliverstatue, firmname, order_date, orderid;
        NetworkImageView seller_icon;
    }


}
