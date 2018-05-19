package trkus.sellermodule.sellerordermodule;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

public class SellerOrderListing extends Fragment {


    Fragment fragment = null;
    String[] SellerUserId, OrderId, ItemName, OrderDate, Name, Address, OrderImage1;

    ProgressDialog pDialog;
    String Tag = "Dashboard";
    ListView sellerlist;
    ArrayList<SellerOrderData> seller_data = null;
    SellerOrderAdapter selleradapter;
    String CategoryName, strtext;
    UserSessionManager session;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getActivity().setTitle("Customer Order");
        View view = inflater.inflate(R.layout.fragment_product_seller_listing, container, false);
        sellerlist = view.findViewById(R.id.sellerlist);
        seller_data = new ArrayList<SellerOrderData>();
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

        sellerlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getActivity(), "You Clicked at " + Name[+position], Toast.LENGTH_SHORT).show();

            }
        });

        return view;

    }

    public void getOrder(String id) {
        JsonArrayRequest req = new JsonArrayRequest(UrlConstant.GET_Seller_Order_Url + id,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(Tag, response.toString());

                        SellerUserId = new String[response.length()];
                        OrderId = new String[response.length()];
                        ItemName = new String[response.length()];
                        Name = new String[response.length()];
                        OrderDate = new String[response.length()];
                        Address = new String[response.length()];
                        OrderImage1 = new String[response.length()];

                        try {

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response
                                        .get(i);
                                SellerUserId[i] = person.getString("SellerUserId");
                                OrderId[i] = person.getString("OrderId");
                                ItemName[i] = person.getString("ItemName");
                                OrderDate[i] = person.getString("OrderDate");
                                Name[i] = person.getString("Name");
                                Address[i] = person.getString("Address");
                                OrderImage1[i] = person.getString("OrderImage1");

                                seller_data.add(new SellerOrderData(SellerUserId[i], OrderId[i], ItemName[i],
                                        OrderDate[i], Name[i], Address[i], OrderImage1[i]));

                            }
                            selleradapter = new SellerOrderAdapter(getActivity(), R.layout.seller_order_layout_adapter, seller_data);
                            sellerlist.setAdapter(selleradapter);
                            selleradapter.notifyDataSetChanged();

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
