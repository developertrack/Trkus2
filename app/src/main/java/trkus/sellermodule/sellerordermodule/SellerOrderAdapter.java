package trkus.sellermodule.sellerordermodule;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

        return convertView;
    }

    class ViewHolder {
        TextView seller_call, seller_sms, customer_address, customer_name_title, order_date, orderid;
        NetworkImageView customer_icon;
    }

}
