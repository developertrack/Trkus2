package trkus.services.com.trkus;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import trkus.commonmodule.WebpageLoad;
import trkus.customermodule.CustomerProfileDetail;
import trkus.customermodule.HomePage;
import trkus.customermodule.appointmenthistory.AppointmentHistory;
import trkus.customermodule.customerorder.CustomeOrderPage;
import trkus.customermodule.dailyneeds.DailyNeedsProductList;
import trkus.customermodule.favouriteContacts.FavouiteContacts;
import trkus.customermodule.importantdocuments.ImportantDocumentList;
import trkus.customermodule.myseller.MySeller;
import util.AppController;
import util.UrlConstant;
import util.UserSessionManager;

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    UserSessionManager session;
    Fragment fragment = null;
    TextView navUsername;
    ProgressDialog pDialog;
    JSONObject data_jobject;
    String TAG = "LoginActivity_TAG";
    String result = "NA", response_string;
    JSONObject data;
    String tag_json_obj = "json_obj_req";
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        navUsername = headerView.findViewById(R.id.profilename);

        session = new UserSessionManager(Dashboard.this);
        getUserDetail(session.getKeyUserid());

//        Log.e("Trkus userid id", session.getKeyUserid());
        fragment = new HomePage();
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.flContent, fragment, "Dashboard");
        tx.commit();
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
//
        if (id == R.id.nav_profile) {
            fragment = new CustomerProfileDetail();
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.flContent, fragment, "CustomerProfile");
            tx.commit();
            tx.addToBackStack(null);
//            session.logoutUser();
        }  else if (id == R.id.nav_orderhistory) {

            fragment = new CustomeOrderPage();
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.flContent, fragment, "CustomerOrderPage");
            tx.commit();
            tx.addToBackStack(null);

        }else if (id == R.id.nav_appointment) {

            fragment = new AppointmentHistory();
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.flContent, fragment, "appointment");
            tx.commit();
            tx.addToBackStack(null);

        } else if (id == R.id.nav_dailyneeds) {

            fragment = new DailyNeedsProductList();
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.flContent, fragment, "navdailyneeds");
            tx.commit();
            tx.addToBackStack(null);

        } else if (id == R.id.nav_myseller) {
            fragment = new MySeller();
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.flContent, fragment, "MySeller");
            tx.commit();
            tx.addToBackStack(null);
        } else if (id == R.id.nav_impdocuments) {
            fragment = new ImportantDocumentList();
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.flContent, fragment, "ImportantDocuments");
            tx.commit();
            tx.addToBackStack(null);
        } else if (id == R.id.nav_share) {
            shareAppNew();
        } else if (id == R.id.nav_aboutus) {
            Bundle bundle = new Bundle();
            bundle.putString("nav_aboutus", "http://trkus.tarule.com/Home/AboutUs");
            fragment = new WebpageLoad();
            fragment.setArguments(bundle);
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.flContent, fragment, "aboutus");
            tx.commit();
            tx.addToBackStack(null);

        } else if (id == R.id.nav_favourite) {
            fragment = new FavouiteContacts();
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.flContent, fragment, "favouriteContacts");
            tx.commit();
            tx.addToBackStack(null);
        } else if (id == R.id.nav_about_turkus) {
            Bundle bundle = new Bundle();
            bundle.putString("nav_aboutus", "http://trkus.tarule.com/Home/AboutUs");
            fragment = new WebpageLoad();
            fragment.setArguments(bundle);
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.flContent, fragment, "abouturkus");
            tx.commit();
            tx.addToBackStack(null);

        } else if (id == R.id.nav_rateapp) {
            rateApp();
        } else if (id == R.id.nav_refer) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
//        clearBackStack();
        return true;
    }

    private void clearBackStack() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public void getUserDetail(String id) {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                UrlConstant.GET_Customer_Profile_Update + id, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, response.toString());
                try {
                    String Status = response.getString("Status");
                    response_string = response.toString();
                    data = response;
                    if (Status.equals("false")) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Dashboard.this);
                                try {
                                    dlgAlert.setMessage(data.getString("Message"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                dlgAlert.setPositiveButton("OK", null);
                                dlgAlert.setCancelable(true);
                                dlgAlert.create().show();
                            }
                        });
                        pDialog.dismiss();

                    } else {

                        navUsername.setText(response.getString("Name"));

                        pDialog.dismiss();
                    }
                    navUsername.setText(response.getString("Name"));
                } catch (Exception e) {

                }

                pDialog.hide();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Dashboard.this);
                        dlgAlert.setMessage("Error while fetching data, please try again");
                        dlgAlert.setPositiveButton("OK", null);
                        dlgAlert.setCancelable(true);
                        dlgAlert.create().show();
                    }
                });
                pDialog.hide();
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

    }

    private void rateApp() {
        try {
            Intent rateIntent = rateIntentForUrl("market://https://play.google.com/");
            startActivity(rateIntent);
        } catch (ActivityNotFoundException e) {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details?id=trkus.services.com.trkus&hl=en");
            startActivity(rateIntent);
        }
    }

    private Intent rateIntentForUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 16) {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        } else {
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }

    public void shareAppNew() {
        String url = "https://play.google.com/store/apps/details?id=com.medjio.medicalhealthcart&hl=en";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Use and like this app");

        stringBuilder.append(url);
        String shareMsg = stringBuilder.toString();

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMsg);
        shareIntent.setType("text/plain");
        startActivity(shareIntent);
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        } else {
            super.onBackPressed();
        }
    }


}
