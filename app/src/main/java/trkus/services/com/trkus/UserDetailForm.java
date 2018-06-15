package trkus.services.com.trkus;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import trkus.sellermodule.SellerDashboard;
import util.AppController;
import util.UrlConstant;
import util.UserSessionManager;

public class UserDetailForm extends AppCompatActivity {

    public final static int TAG_PERMISSION_CODE = 1;
    EditText name, email, address;
    LocationManager locationManager;
    double latitude, longitude;
    LinearLayout detect;
    String TAG = "UserDetailForm_TAG";
    LocationManager mlocManager;
    ProgressDialog pDialog;
    JSONObject data_jobject;
    Intent intent;
    Location location = null;
    JSONObject data;
    String UserTypeId, OTP, MobileNumber, UserId, response_string;
    Button next;
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userdetailform);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        address = findViewById(R.id.address);
        detect = findViewById(R.id.detect);
        next = findViewById(R.id.next);

        session = new UserSessionManager(UserDetailForm.this);
        intent = getIntent();
        UserTypeId = intent.getStringExtra("UserTypeId");
        MobileNumber = intent.getStringExtra("MobileNumber");

        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            // Build the alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Location Services Not Active");
            builder.setMessage("Please enable Location Services and GPS");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Show location settings when the user acknowledges the alert dialog
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            Dialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }

        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener mlocListener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener);

        detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Text = "location: " +
                        "Latitude = " + location.getLatitude() +
                        "Longitude = " + location.getLongitude();
                getCompleteAddressString(location.getLatitude(), location.getLongitude());
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (name.getText().toString().length() > 0 && isValidEmaillId(email.getText().toString()) && email.getText().toString().length() > 0 && address.getText().toString().length() > 0) {
                    RegisterUser();
                } else if (name.getText().toString().length() == 0) {
                    Toast.makeText(UserDetailForm.this, "Please enter name", Toast.LENGTH_LONG).show();
                } else if (email.getText().toString().length() == 0) {
                    Toast.makeText(UserDetailForm.this, "Please enter valid email", Toast.LENGTH_LONG).show();
                } else if (isValidEmaillId(email.getText().toString())) {
                    Toast.makeText(UserDetailForm.this, "Please enter email", Toast.LENGTH_LONG).show();
                } else if (address.getText().toString().length() == 0) {
                    Toast.makeText(UserDetailForm.this, "Please enter Address", Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();

                address.setText(strAdd);

            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return strAdd;
    }

    private boolean isValidEmaillId(String email) {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    public void RegisterUser() {

        String tag_json_obj = "json_obj_req";

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        data_jobject = new JSONObject();

        try {
            data_jobject.put("MobileNumber", MobileNumber);
            data_jobject.put("Name", name.getText().toString());
            data_jobject.put("EmailId", email.getText().toString());
            data_jobject.put("Location", address.getText().toString());
            data_jobject.put("UserTypeId", UserTypeId);

        } catch (Exception e) {

        }
        Log.e("Verify OTP", data_jobject.toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                UrlConstant.AddUserInformation_URL, data_jobject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject response) {
                        Log.e(TAG, response.toString());
                        try {
                            String Status = response.getString("Status");

                            if (Status.equals("false")) {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        android.support.v7.app.AlertDialog.Builder dlgAlert = new android.support.v7.app.AlertDialog.Builder(UserDetailForm.this);
                                        try {
                                            dlgAlert.setMessage(response.getString("Message"));

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        dlgAlert.setPositiveButton("OK", null);
                                        dlgAlert.setCancelable(true);
                                        dlgAlert.create().show();
                                    }
                                });

                            } else {
                                response_string = response.toString();
                                data = response;
                                MobileNumber = data.getString("MobileNumber");
                                OTP = data.getString("OTP");
                                UserTypeId = data.getString("UserTypeId");
                                UserId = data.getString("UserId");
                                if (UserTypeId.equals("1")) {
                                    Intent verification = new Intent(UserDetailForm.this, SellerDashboard.class);
                                    verification.putExtra("MobileNumber", MobileNumber);
                                    verification.putExtra("UserTypeId", UserTypeId);
                                    verification.putExtra("UserId", UserId);

                                    session.createUserLoginSession(UserTypeId, UserId, MobileNumber);

                                    verification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(verification);
                                }
                                if (UserTypeId.equals("2")) {
                                    Intent verification = new Intent(UserDetailForm.this, Dashboard.class);
                                    verification.putExtra("MobileNumber", MobileNumber);
                                    verification.putExtra("UserTypeId", UserTypeId);
                                    verification.putExtra("UserId", UserId);

                                    session.createUserLoginSession(UserTypeId, UserId, MobileNumber);

                                    verification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(verification);
                                }                            }

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
                        android.support.v7.app.AlertDialog.Builder dlgAlert = new android.support.v7.app.AlertDialog.Builder(UserDetailForm.this);
                        dlgAlert.setMessage("Error while saving data, please try again");
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

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);


    }

    public class MyLocationListener implements LocationListener {
        @Override

        public void onLocationChanged(Location loc) {

            location = loc;


        }

        public void onProviderDisabled(String provider) {

            //nothin
        }


        public void onProviderEnabled(String provider) {

            //nothin
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            //nothin
        }
    }/* End of Class MyLocationListener */
}
