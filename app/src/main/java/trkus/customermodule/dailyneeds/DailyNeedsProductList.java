package trkus.customermodule.dailyneeds;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import trkus.services.com.trkus.R;
import util.AppController;
import util.UrlConstant;
import util.UserSessionManager;

public class DailyNeedsProductList extends Fragment {

    ListView sellerlist;
    LinearLayout addcontact;
    Fragment fragment = null;
    String[] DailyNeedsItemId, Itemname, Title, OrderImage1;
    UserSessionManager session;
    ProgressDialog pDialog;
    String Tag = "Dashboard";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Daily Need Item");
        View view = inflater.inflate(R.layout.dailyneeditem_list, container, false);
        addcontact = view.findViewById(R.id.addcontact);
        sellerlist = view.findViewById(R.id.sellerlist);
        addcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new AddDailyNeedsItem();
                FragmentTransaction tx = getActivity().getSupportFragmentManager().beginTransaction();
                tx.replace(R.id.flContent, fragment, "addDailyNeedsProductList");
                tx.commit();
                tx.addToBackStack(null);
            }
        });

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        sellerlist.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                sellerlist.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        session = new UserSessionManager(getActivity());
        getDailyItemNeedList(session.getKeyUserid());

        sellerlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getActivity(), "You Clicked at " + Title[+position], Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();
                bundle.putString("Itemname", Itemname[position]);
                bundle.putString("Title", Title[position]);
                bundle.putString("OrderImage1", OrderImage1[position]);
                fragment = new DailyNeedItemDetail();
                fragment.setArguments(bundle);
                FragmentTransaction tx = getActivity().getSupportFragmentManager().beginTransaction();
                tx.replace(R.id.flContent, fragment, Title[position]);
                tx.commit();
                tx.addToBackStack(null);

            }
        });

        return view;
    }

    public void getDailyItemNeedList(String id) {
        JsonArrayRequest req = new JsonArrayRequest(UrlConstant.GET_Daily_Need_Item + id,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("response", response.toString());
                        DailyNeedsItemId = new String[response.length()];
                        Itemname = new String[response.length()];
                        Title = new String[response.length()];
                        OrderImage1 = new String[response.length()];

                        try {

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response
                                        .get(i);
                                DailyNeedsItemId[i] = person.getString("DailyNeedsItemId");
                                Itemname[i] = person.getString("Itemname");
                                Title[i] = person.getString("Title");
                                OrderImage1[i] = person.getString("OrderImage1");

                            }
                            if (getActivity()!=null) {
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                        android.R.layout.simple_list_item_1, android.R.id.text1,
                                        Title);
                                sellerlist.setAdapter(adapter);
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
