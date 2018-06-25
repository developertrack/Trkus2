package trkus.customermodule.importantdocuments;

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

public class ImportantDocumentList extends Fragment {

    ListView sellerlist;
    LinearLayout adddocuments;
    Fragment fragment = null;
    String[] Name, Subject = null, Remarks, File;
    UserSessionManager session;
    ProgressDialog pDialog;
    String Tag = "Dashboard";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Important Documents");
        View view = inflater.inflate(R.layout.fragment_importantdocument_list, container, false);
        adddocuments = view.findViewById(R.id.adddocuments);
        sellerlist = view.findViewById(R.id.sellerlist);
        adddocuments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new AddDocumentPage();
                FragmentTransaction tx = getActivity().getSupportFragmentManager().beginTransaction();
                tx.replace(R.id.flContent, fragment, "AddDocumentPage");
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
                Toast.makeText(getActivity(), "You Clicked at " + Subject[+position], Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString("Subject", Subject[position]);
                bundle.putString("Remarks", Remarks[position]);
                bundle.putString("File", File[position]);
                fragment = new ImportantDocumentDetail();
                fragment.setArguments(bundle);
                FragmentTransaction tx = getActivity().getSupportFragmentManager().beginTransaction();
                tx.replace(R.id.flContent, fragment, Subject[position]);
                tx.commit();
                tx.addToBackStack(null);
            }
        });

        return view;
    }

    public void getDailyItemNeedList(String id) {
        JsonArrayRequest req = new JsonArrayRequest(UrlConstant.GET_Important_Document + id,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("response", response.toString());
                        Name = new String[response.length()];
                        Subject = new String[response.length()];
                        File = new String[response.length()];
                        Remarks = new String[response.length()];
//
//                        "CustomerId": 45,


                        try {

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response
                                        .get(i);
                                Name[i] = person.getString("Name");
                                Subject[i] = person.getString("Subject");
                                File[i] = person.getString("File");
                                Remarks[i] = person.getString("Remarks");

                            }
                            if (getActivity()!=null) {
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                        android.R.layout.simple_list_item_1, android.R.id.text1,
                                        Subject);
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
