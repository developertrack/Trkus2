package trkus.sellermodule.sellerorder;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import trkus.services.com.trkus.R;
import util.AppController;
import util.UrlConstant;
import util.UserSessionManager;

public class SellerOrderDetails extends Fragment {
    Fragment fragment = null;
    TextView customer_name_title, customer_address, customer_contact, customer_orderdata, order_date, orderid;
    NetworkImageView  order_icon;
    ProgressDialog pDialog;
    UserSessionManager session;
    String Tag = "Dashboard";
    String order_id;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    String[] SellerUserId, OrderId, ItemName, OrderDate, FirmName, Address, OrderImage1,MobileNumber;
    JSONObject data_jobject;
    String seller_id;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getActivity().setTitle("Order Detail");
        order_id = getArguments().getString("OrderId");
        Log.d(Tag, order_id);
        session = new UserSessionManager(getActivity());
        View convertView = inflater.inflate(R.layout.seller_order_details, container, false);

        customer_name_title = convertView
                .findViewById(R.id.customer_name_title);
        customer_address = convertView
                .findViewById(R.id.customer_address);
        customer_contact = convertView
                .findViewById(R.id.customer_contact);
        customer_orderdata = convertView
                .findViewById(R.id.customer_orderdata);
        order_date = convertView
                .findViewById(R.id.order_date);
        orderid = convertView
                .findViewById(R.id.orderid);
        order_icon = convertView
                .findViewById(R.id.order_icon);

        session = new UserSessionManager(getActivity());


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

        return convertView;
    }

//

    public void getOrder(String order_id) {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        Log.e(Tag, UrlConstant.GET_Seller_Order_Detail + session.getKeyUserid() + "&OrderId=" + order_id);
        JsonArrayRequest req = new JsonArrayRequest(UrlConstant.GET_Seller_Order_Detail + session.getKeyUserid() + "&OrderId=" + order_id,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e(Tag, response.toString());

                        SellerUserId = new String[response.length()];
                        OrderId = new String[response.length()];
                        ItemName = new String[response.length()];
                        FirmName = new String[response.length()];
                        OrderDate = new String[response.length()];
                        FirmName = new String[response.length()];
                        Address = new String[response.length()];
                        OrderImage1 = new String[response.length()];
                        MobileNumber= new String[response.length()];


//    [
//    {
//        "Status": true,
//            "Message": "OrderDetails",
//            "SellerUserId": 123,
//            "OrderId": "TRKUS180614113424153",
//            "ItemName": "data Android\n",
//            "OrderDate": "2018-06-14T00:00:00",
//            "Name": "piyush",
//            "Address": "760",
//            "OrderImage1": "http://webapi.trkus.com//Images/OrderDailyNeedItemImage/1528999463027.jpgNi8xNC8yMDE4IDExOjM0OjI0IFBN.jpg",
//            "MobileNumber": "8687210110"
//    }
//]

                        try {

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response
                                        .get(i);
                                SellerUserId[i] = person.getString("SellerUserId");
                                OrderId[i] = person.getString("OrderId");
                                Log.e("OrderId", OrderId[i]);
                                ItemName[i] = person.getString("ItemName");
                                OrderDate[i] = person.getString("OrderDate");
                                FirmName[i] = person.getString("Name");
                                Address[i] = person.getString("Address");
                                OrderImage1[i] = person.getString("OrderImage1");
                                MobileNumber[i]=person.getString("MobileNumber");

                            }

//                            customer_name_title, customer_address, customer_contact, customer_orderdata, order_date, orderid

                            orderid.setText(OrderId[0]);
                            order_date.setText(OrderDate[0]);
                            customer_name_title.setText(FirmName[0]);
                            customer_address.setText(Address[0]);
                            customer_orderdata.setText(ItemName[0]);
                            customer_contact.setText(MobileNumber[0]);

                            String temp =OrderImage1[0];
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


}