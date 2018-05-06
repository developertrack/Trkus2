package trkus.sellermodule;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;

import trkus.services.com.trkus.R;
import util.UserSessionManager;

public class SellerDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    UserSessionManager session;
    Fragment fragment = null;
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
    FragmentManager fragmentManager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(SellerDashboard.this);

        session = new UserSessionManager(SellerDashboard.this);
        fragmentManager =getSupportFragmentManager();
//        fragment = new HomePage();
//        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
//        tx.replace(R.id.flContent, fragment, "home");
//        tx.commit();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
//
        if (id == R.id.nav_profile) {
            Fragment fragment = new ProfileEdit();
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.flContent, fragment, "home");
            tx.commit();
            getSupportActionBar().setTitle("Home fragment ");
//
        } else if (id == R.id.nav_orderhistory) {

        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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


//        FragmentManager fragmentManager =getSupportFragmentManager();
//
//        if (((HomePage) getSupportFragmentManager().findFragmentByTag("home")) == null
//                && !((HomePage) getSupportFragmentManager().findFragmentByTag("home")).isVisible()) {
//
//            Fragment fragment = new HomePage();
//            fragmentManager.beginTransaction()
//                    .replace(R.id.container, fragment)
//                    .commit();
////            getSupportActionBar().setTitle("Home fragment ");
//
//        } else {
//            super.onBackPressed();
//        }
    }
}
