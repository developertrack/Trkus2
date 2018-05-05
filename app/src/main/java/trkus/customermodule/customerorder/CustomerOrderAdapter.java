package trkus.customermodule.customerorder;

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

public class CustomerOrderAdapter extends ArrayAdapter<OrderData> {

    Context context;
    int layoutResourceId;
    ArrayList<OrderData> dataget = null;
    ViewHolder holder;
    int pos = 0;
    OrderData getdata;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    class ViewHolder {
        TextView reorder, orderdetail,deliverstatue,firmname,order_date,orderid;
        NetworkImageView seller_icon;
    }

    public CustomerOrderAdapter(FragmentActivity activity, int resource, ArrayList<OrderData> data) {
        super(activity, resource, data);

        context=activity;
        layoutResourceId=resource;
        dataget=data;
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
            holder.reorder = (TextView) convertView
                    .findViewById(R.id.reorder);
            holder.orderdetail = (TextView) convertView
                    .findViewById(R.id.orderdetail);
            holder.deliverstatue = (TextView) convertView
                    .findViewById(R.id.deliverstatue);
            holder.firmname = (TextView) convertView
                    .findViewById(R.id.firmname);
            holder.seller_icon = (NetworkImageView) convertView
                    .findViewById(R.id.seller_icon);
            holder.order_date = (TextView) convertView
                    .findViewById(R.id.order_date);
            holder.orderid=(TextView) convertView
                    .findViewById(R.id.orderid);

            holder. orderdetail .setTag(position);
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

        return convertView;
    }

}
