package trkus.customermodule;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import trkus.services.com.trkus.CustomGrid;
import trkus.services.com.trkus.R;
import util.AppController;
import util.UrlConstant;


/**
 * Created by riya on 14/3/18.
 */

@SuppressLint("ValidFragment")
public class HomePage extends Fragment {

    LinearLayout usefull_link, news;
    Fragment fragment = null;
    GridView grid;
    String[] CategoryId, CategoryName, CategoryImage, CategoryType;
    ProgressDialog pDialog;
    String Tag = "Dashboard";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Trkus");
        View view = inflater.inflate(R.layout.layout_home, container, false);
        grid = view.findViewById(R.id.grid);
        getCategory();
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();


        grid.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                grid.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

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

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
//        getActivity().setTitle("USE");
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
