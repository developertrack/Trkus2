package trkus.customermodule;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
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


public class ServiceSellerListing extends Fragment {


    Fragment fragment = null;
    String[] SellerUserId,Industry,CategoryOfBusiness,FirmName,MobileNumber,Address1,PinCode,Image1;
    ProgressDialog pDialog;
    String Tag = "Dashboard";
    ListView sellerlist;
    ArrayList<ServiceSeller> seller_data = null;
    ServiceSellerAdapter selleradapter;
    String CategoryName,strtext;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         strtext = getArguments().getString("CategoryId");
        CategoryName=getArguments().getString("CategoryName");
        getActivity().setTitle(CategoryName);
        View view = inflater.inflate(R.layout.fragment_product_seller_listing, container, false);
        sellerlist=(ListView)view.findViewById(R.id.sellerlist);
        seller_data = new ArrayList<ServiceSeller>();
        getStore(strtext);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        sellerlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getActivity(), "You Clicked at " + FirmName[+position], Toast.LENGTH_SHORT).show();

            }
        });

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
//        getActivity().setTitle("USE");
    }


    public void getStore(String id) {
        JsonArrayRequest req = new JsonArrayRequest(UrlConstant.GET_STORE_byID+id,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(Tag, response.toString());
                        SellerUserId = new String[response.length()];
                        Industry = new String[response.length()];
                        CategoryOfBusiness = new String[response.length()];
                        FirmName = new String[response.length()];
                        MobileNumber = new String[response.length()];
                        Address1 = new String[response.length()];
                        PinCode = new String[response.length()];
                        Image1 = new String[response.length()];

                        try {

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response
                                        .get(i);
                                SellerUserId[i] = person.getString("SellerUserId");
                                Industry[i] = person.getString("Industry");
                                CategoryOfBusiness[i] = person.getString("CategoryOfBusiness");
                                FirmName[i] = person.getString("FirmName");
                                MobileNumber[i] = person.getString("MobileNumber");
                                Address1[i] = person.getString("Address1");
                                PinCode[i] = person.getString("PinCode");
                                Image1[i] = person.getString("Image1");

                                seller_data.add(new ServiceSeller(SellerUserId[i],Industry[i],CategoryOfBusiness[i],FirmName[i],
                                        MobileNumber[i],Address1[i],PinCode[i],Image1[i]));
                            }
                            selleradapter=new ServiceSellerAdapter(getActivity(),R.layout.service_seller_layout_listing_adapter,seller_data);
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