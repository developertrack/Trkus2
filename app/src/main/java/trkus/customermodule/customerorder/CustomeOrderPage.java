package trkus.customermodule.customerorder;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import trkus.services.com.trkus.R;
import util.AppController;
import util.UrlConstant;
import util.UserSessionManager;

public class CustomeOrderPage extends Fragment {


    Fragment fragment = null;
    String[] SellerUserId, OrderId, Itemn, OrderDate, FirmName, FirmImage, OrderImage1;
    ProgressDialog pDialog;
    String Tag = "Dashboard";
    ListView sellerlist;
    ArrayList<OrderData> seller_data = null;
    CustomerOrderAdapter selleradapter;
    String CategoryName, strtext;
    UserSessionManager session;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getActivity().setTitle("Order History");
        View view = inflater.inflate(R.layout.fragment_product_seller_listing, container, false);
        sellerlist = view.findViewById(R.id.sellerlist);
        seller_data = new ArrayList<OrderData>();
        session = new UserSessionManager(getActivity());

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        getOrder(session.getKeyUserid());

        sellerlist.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                sellerlist.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

//        sellerlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                Toast.makeText(getActivity(), "You Clicked at " + FirmName[+position], Toast.LENGTH_SHORT).show();
//
//            }
//        });

        return view;

    }

    public void getOrder(String id) {
        JsonArrayRequest req = new JsonArrayRequest(UrlConstant.GET_Customer_Order_Url + id,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(Tag, response.toString());

                        SellerUserId = new String[response.length()];
                        OrderId = new String[response.length()];
                        Itemn = new String[response.length()];
                        FirmName = new String[response.length()];
                        OrderDate = new String[response.length()];
                        FirmName = new String[response.length()];
                        FirmImage = new String[response.length()];
                        OrderImage1 = new String[response.length()];

                        try {

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response
                                        .get(i);
                                SellerUserId[i] = person.getString("SellerUserId");
                                OrderId[i] = person.getString("OrderId");
                                Log.e("OrderId",OrderId[i]);
                                Itemn[i] = person.getString("ItemName");
                                OrderDate[i] = person.getString("OrderDate");
                                FirmName[i] = person.getString("FirmName");
                                FirmImage[i] = person.getString("FirmImage");
                                OrderImage1[i] = person.getString("OrderImage1");

                                seller_data.add(new OrderData(SellerUserId[i], OrderId[i], Itemn[i],
                                        OrderDate[i], FirmName[i], FirmImage[i], OrderImage1[i]));
                            }
                            if (getActivity()!=null) {
//                                Collections.reverse(seller_data);
                                selleradapter = new CustomerOrderAdapter(getActivity(), R.layout.customer_order_adapter, seller_data);
                                sellerlist.setAdapter(selleradapter);
                                selleradapter.notifyDataSetChanged();
                            }

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
