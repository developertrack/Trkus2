package trkus.customermodule.favouriteContacts;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import trkus.services.com.trkus.R;

public class FavouriteContactAdapter extends ArrayAdapter<FavouriteContactData> {

    Context context;
    int layoutResourceId;
    ArrayList<FavouriteContactData> dataget = null;
    FavouriteContactAdapter.ViewHolder holder;
    int pos = 0;
    FavouriteContactData getdata;

    public FavouriteContactAdapter(FragmentActivity activity, int resource, ArrayList<FavouriteContactData> data) {
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

            holder = new FavouriteContactAdapter.ViewHolder();
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
            convertView.setTag(holder);
        } else {
            holder = (FavouriteContactAdapter.ViewHolder) convertView.getTag();
        }

        getdata = dataget.get(position);
        pos = position;

        holder.name.setText(getdata.getName());
        holder.mobilenumber.setText(getdata.getMobileNumber());
        holder.address.setText(getdata.getAddress());
        holder.firmtype.setText(getdata.getFirmName());

        return convertView;

    }

    class ViewHolder {
        TextView name, mobilenumber, address, firmtype;
        TextView btn_call, btn_remove, btn_edit;
    }

}
