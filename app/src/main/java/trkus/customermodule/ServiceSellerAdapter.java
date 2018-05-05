package trkus.customermodule;

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

public class ServiceSellerAdapter extends ArrayAdapter<ServiceSeller> {

    Context context;
    int layoutResourceId;
    ArrayList<ServiceSeller> dataget = null;
    ViewHolder holder;
    int pos = 0;
    ServiceSeller getdata;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    class ViewHolder {
        TextView seller_name_title, seller_address,seller_contact,seller_call,seller_order;
        NetworkImageView seller_icon;
    }

    public ServiceSellerAdapter(FragmentActivity activity, int resource, ArrayList<ServiceSeller> data) {
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
            holder.seller_order = (TextView) convertView
                    .findViewById(R.id.seller_order);
            holder.seller_call = (TextView) convertView
                    .findViewById(R.id.seller_call);
            holder.seller_contact = (TextView) convertView
                    .findViewById(R.id.seller_contact);
            holder.seller_address = (TextView) convertView
                    .findViewById(R.id.seller_address);
            holder.seller_icon = (NetworkImageView) convertView
                    .findViewById(R.id.seller_icon);
            holder.seller_name_title = (TextView) convertView
                    .findViewById(R.id.seller_name_title);

            holder. seller_name_title .setTag(position);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        getdata = dataget.get(position);
        pos = position;

        holder.seller_name_title.setText(getdata.getFirmName());
        holder.seller_address.setText(getdata.getAddress1()+" "+getdata.getPinCode());
        holder.seller_contact.setText(getdata.getMobileNumber());
        holder.seller_icon.setImageUrl("http://webservicestrkus.tarule.com"+getdata.getImage1(), imageLoader);

        return convertView;

    }

}
