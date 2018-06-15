package trkus.sellermodule.sellerorder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import trkus.services.com.trkus.R;
import util.AppController;

public class SellerOrderAdapter extends ArrayAdapter<SellerOrderData> {

    Context context;
    int layoutResourceId;
    ArrayList<SellerOrderData> dataget = null;
    ViewHolder holder;
    int pos = 0;
    SellerOrderData getdata;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    Fragment fragment = null;
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
                String tag_json_obj = "json_obj_req";
                getdata = dataget.get(position);
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", getdata.getMobileNumber(), null)));
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
