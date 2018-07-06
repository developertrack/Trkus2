package trkus.customermodule;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import trkus.services.com.trkus.CustomGrid;
import trkus.services.com.trkus.R;
import util.AppController;
import util.SimpleLocation;
import util.UrlConstant;
import util.UserSessionManager;

import static util.AppController.TAG;


/**
 * Created by riya on 14/3/18.
 */

@SuppressLint("ValidFragment")
public class HomePage extends Fragment {


    Fragment fragment = null;
    GridView grid;
    String[] CategoryId, CategoryName, CategoryImage, CategoryType;
    ProgressDialog pDialog;
    String Tag = "Dashboard";
    UserSessionManager session;
    String latitude = "", longitude = "", area = "NA", address = "NA";
    JSONObject data_jobject;
    ImageView search_clear;
    EditText search_view;
    String[] SellerUserId, Industry, CategoryOfBusiness, FirmName, MobileNumber, Address1, PinCode, Image1;
    private SimpleLocation location;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Trkus");
        View view = inflater.inflate(R.layout.layout_home, container, false);
        grid = view.findViewById(R.id.grid);
        search_clear = view.findViewById(R.id.search_clear);
        search_view = view.findViewById(R.id.search_view);

        getCategory();
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        session = new UserSessionManager(getActivity());

        grid.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                grid.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        location = new SimpleLocation(getActivity());

        // if we can't access the location yet
        if (!location.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(getActivity());
        }

        getLocation();

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getActivity(), "You Clicked at " + CategoryName[+position], Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString("CategoryId", CategoryId[position]);
                bundle.putString("CategoryName", CategoryName[position]);

                if (CategoryType[position].equals("1")) {

                    fragment = new ProductSellerListing();
                    fragment.setArguments(bundle);
                    FragmentTransaction tx = getActivity().getSupportFragmentManager().beginTransaction();
                    tx.replace(R.id.flContent, fragment, CategoryName[position]);
                    tx.commit();
                    tx.addToBackStack(null);
                }
                if (CategoryType[position].equals("2")) {

                    fragment = new ServiceSellerListing();
                    fragment.setArguments(bundle);
                    FragmentTransaction tx = getActivity().getSupportFragmentManager().beginTransaction();
                    tx.replace(R.id.flContent, fragment, CategoryName[position]);
                    tx.commit();
                    tx.addToBackStack(null);
                }
            }
        });

        search_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (search_view.getText().toString().length() > 0) {
                    pDialog = new ProgressDialog(getActivity());
                    pDialog.setMessage("Loading...");
                    pDialog.show();
                    searchSeller();

                }

            }
        });

        return view;

    }


    void getLocation() {
        try {

            if (location != null) {
                latitude = String.valueOf(location.getLatitude());

                longitude = String.valueOf(location.getLongitude());

                address = getCompleteAddressString(location.getLatitude(), location.getLongitude());

                String tag_json_obj = "json_obj_req";

                data_jobject = new JSONObject();
                try {
                    data_jobject.put("MobileNumber", session.getKeyNumber());
                    data_jobject.put("Latitude", latitude);
                    data_jobject.put("Longitude", longitude);
                    data_jobject.put("Area", area);
                    data_jobject.put("Address", address);

                    Log.e("data_jobject", data_jobject.toString());

                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                            UrlConstant.GET_Update_Location, data_jobject,
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(final JSONObject response) {
                                    Log.e(TAG, response.toString());


                                }

                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.e(TAG, "Error: " + error.getMessage());

                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("Content-Type", "application/json; charset=utf-8");
                            return headers;
                        }
                    };

                    AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

                } catch (Exception e) {

                }

            }


        } catch (SecurityException ex) {
            Log.d("gps", "Location permission did not work!");
        }
    }


    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                address = strAdd;
                area = addresses.get(0).getSubLocality();
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }

    public void searchSeller() {

        Log.e("url", UrlConstant.GET_SearchSeller + area + "&SellerName=" + search_view.getText().toString());

        JsonArrayRequest req = new JsonArrayRequest(UrlConstant.GET_SearchSeller + area + "&SellerName=" + search_view.getText().toString(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e(Tag, response.toString());
//                        CategoryId,CategoryName,CategoryImage,


                        if (response.length() == 0) {
                            Toast.makeText(getActivity(), "No Seller Found", Toast.LENGTH_LONG).show();
                        }


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

    public void getCategory() {
        JsonArrayRequest req = new JsonArrayRequest(UrlConstant.GETBusiness_Category_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(Tag, response.toString());
//                        CategoryId,CategoryName,CategoryImage,CategoryType
                        CategoryId = new String[response.length()];
                        CategoryName = new String[response.length()];
                        CategoryImage = new String[response.length()];
                        CategoryType = new String[response.length()];

                        try {

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response
                                        .get(i);
                                CategoryId[i] = person.getString("CategoryId");
                                CategoryName[i] = person.getString("CategoryName");
                                CategoryImage[i] = person.getString("CategoryImage");
                                CategoryType[i] = person.getString("CategoryType");
                            }

                            CustomGrid adapter = new CustomGrid(getActivity(), CategoryName, CategoryImage);
                            grid.setAdapter(adapter);
                            pDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(Tag, "Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        AppController.getInstance().addToRequestQueue(req);
    }


}
