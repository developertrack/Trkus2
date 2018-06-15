package trkus.sellermodule.sellerfavouritecontacts;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import trkus.services.com.trkus.R;
import util.AppController;
import util.UrlConstant;
import util.UserSessionManager;

public class SellerFavouriteContactAdapter extends ArrayAdapter<SellerFavouriteContactData> {

    Context context;
    int layoutResourceId;
    ArrayList<SellerFavouriteContactData> dataget = null;
    ViewHolder holder;
    ProgressDialog pDialog;
    int pos = 0;
    SellerFavouriteContactData getdata;
    UserSessionManager session;
    JSONObject data_jobject;
    String Tag = "Dashboard";

    public SellerFavouriteContactAdapter(FragmentActivity activity, int resource, ArrayList<SellerFavouriteContactData> data) {
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
            holder.name = convertView
                    .findViewById(R.id.name);
            holder.mobilenumber = convertView
                    .findViewById(R.id.mobilenumber);
            holder.address = convertView
                    .findViewById(R.id.address);
            holder.firmtype = convertView
                    .findViewById(R.id.firmtype);
            holder.btn_call = convertView
                    .findViewById(R.id.btn_call);
            holder.btn_remove = convertView
                    .findViewById(R.id.btn_remove);

            holder.btn_edit = convertView
                    .findViewById(R.id.btn_edit);

            holder.btn_edit.setTag(position);
            holder.btn_remove.setTag(position);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        getdata = dataget.get(position);
        pos = position;

        holder.name.setText(getdata.getName());
        holder.mobilenumber.setText(getdata.getMobileNumber());
        holder.address.setText(getdata.getAddress());
        holder.firmtype.setText(getdata.getFirmName());

        holder.btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + getdata.getMobileNumber()));
                context.startActivity(intent);
            }
        });

        holder.btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pDialog.setMessage("Loading...");
                pDialog.show();
                pos = position;
                getdata = dataget.get(position);

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                        UrlConstant.GET_Seller_RemoveFavouriteContact_Url + getdata.getId(), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(Tag, response.toString());
                        try {
                            String Status = response.getString("Status");

                            if (Status.equals("false")) {

                                Toast.makeText(context, response.getString("Message"), Toast.LENGTH_LONG).show();


                            } else {
                                Toast.makeText(context, response.getString("Message"), Toast.LENGTH_LONG).show();
                                dataget.remove(pos);
                                notifyDataSetChanged();
                            }
                            pDialog.dismiss();
                        } catch (Exception e) {

                        }

                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e(Tag, "Error: " + error.getMessage());

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

                AppController.getInstance().addToRequestQueue(jsonObjReq, Tag);
            }
        });

        return convertView;

    }

    class ViewHolder {
        TextView name, mobilenumber, address, firmtype;
        TextView btn_call, btn_remove, btn_edit;
    }

}
