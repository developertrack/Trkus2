package trkus.services.com.trkus;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;

import trkus.commonmodule.WebpageLoad;
import trkus.customermodule.CustomerProfileEdit;
import trkus.customermodule.HomePage;
import trkus.customermodule.customerorder.CustomeOrderPage;
import trkus.customermodule.favouriteContacts.FavouiteContacts;
import trkus.customermodule.myseller.MySeller;
import util.UserSessionManager;

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    UserSessionManager session;
    Fragment fragment = null;
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

        session=new UserSessionManager(Dashboard.this);
        Log.e("Trkus userid id", session.getKeyUserid());
        fragment = new HomePage();
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.flContent, fragment, "home");
        tx.commit();
    }



    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
//
        if (id == R.id.nav_profile) {
            fragment = new CustomerProfileEdit();
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.flContent, fragment, "CustomerProfile");
            tx.commit();
            tx.addToBackStack(null);
//            session.logoutUser();
        } else if (id == R.id.nav_orderhistory) {

            fragment = new CustomeOrderPage();
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.flContent, fragment, "CustomerOrderPage");
            tx.commit();
            tx.addToBackStack(null);

        }else if(id==R.id.nav_myseller){
            fragment = new MySeller();
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.flContent, fragment, "MySeller");
            tx.commit();
            tx.addToBackStack(null);
        }

        else if (id == R.id.nav_share) {
            shareAppNew();
        } else if (id == R.id.nav_aboutus) {
            Bundle bundle = new Bundle();
            bundle.putString("nav_aboutus", "http://trkus.tarule.com/Home/AboutUs");
            fragment = new WebpageLoad();
            fragment.setArguments(bundle);
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.flContent, fragment, "home");
            tx.commit();
            tx.addToBackStack(null);

        } else if (id == R.id.nav_favourite) {
            fragment = new FavouiteContacts();
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.flContent, fragment, "favourite Contacts");
            tx.commit();
            tx.addToBackStack(null);
        } else if (id == R.id.nav_about_turkus) {

        } else if (id == R.id.nav_rateapp) {
            rateApp();
        } else if (id == R.id.nav_refer) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void rateApp() {
        try {
            Intent rateIntent = rateIntentForUrl("market://https://play.google.com/");
            startActivity(rateIntent);
        } catch (ActivityNotFoundException e) {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details?id=com.medjio.medicalhealthcart&hl=en");
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
