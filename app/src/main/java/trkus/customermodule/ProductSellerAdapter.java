package trkus.customermodule;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

public class ProductSellerAdapter extends ArrayAdapter<ProductSeller> {

    Context context;
    int layoutResourceId;
    ArrayList<ProductSeller> dataget = null;
    ViewHolder holder;
    int pos = 0;
    ProductSeller getdata;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public ProductSellerAdapter(FragmentActivity activity, int resource, ArrayList<ProductSeller> data) {
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
            holder.seller_order = convertView
                    .findViewById(R.id.seller_order);
            holder.seller_call = convertView
                    .findViewById(R.id.seller_call);
            holder.seller_contact = convertView
                    .findViewById(R.id.seller_contact);
            holder.seller_address = convertView
                    .findViewById(R.id.seller_address);
            holder.seller_icon = convertView
                    .findViewById(R.id.seller_icon);
            holder.seller_name_title = convertView
                    .findViewById(R.id.seller_name_title);

            holder.seller_name_title.setTag(position);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        getdata = dataget.get(position);
        pos = position;

        holder.seller_name_title.setText(getdata.getFirmName());
        holder.seller_address.setText(getdata.getAddress1() + " " + getdata.getPinCode());
        holder.seller_contact.setText(getdata.getMobileNumber());
        String temp = getdata.getImage1();

        temp = temp.replaceAll(" ", "%20");
        holder.seller_icon.setImageUrl(temp, imageLoader);

        holder.seller_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + getdata.getMobileNumber()));
                context.startActivity(intent);
            }
        });

        return convertView;

    }

    class ViewHolder {
        TextView seller_name_title, seller_address, seller_contact, seller_call, seller_order;
        NetworkImageView seller_icon;
    }

}
