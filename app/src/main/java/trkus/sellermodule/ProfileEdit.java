package trkus.sellermodule;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import trkus.sellermodule.sellerordermodule.SellerAvailability;
import trkus.services.com.trkus.R;
import util.AppController;
import util.MultipartRequest;
import util.MultipartRequestParams;
import util.UrlConstant;
import util.UserSessionManager;
import util.Utility;

public class ProfileEdit extends Fragment {

    EditText et_full_name, gender, et_blood_group, et_industry_type, et_business_category, et_firm_name, et_address, et_state, et_pincode,
            et_landline, et_mobile, et_email, et_emergency_number;
    RelativeLayout layout_photo1, layout_photo2, layout_photo3;
    NetworkImageView photo1, photo2, photo3;
    String image_path1, image_path2, image_path3;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    Button btn_save,availability;
    ProgressDialog pDialog;
    String[] blood_group = {"A+", "A-", "B", "B+", "AB+", "AB-", "O+", "O-"};
    String[] gender_type = {"Male", "Female"};
    ArrayAdapter<String> spinner_blood_group, spinner_state_name, spinner_industry_type, spinner_business_type, spinner_gender_type;
    String Tag = "Profile_Edit";
    JSONArray jsonResponse;
    String[] StateId, StateName, CategoryName, CategoryId, BCategoryId, BCategoryName;
    UserSessionManager session;
    String stateid, categoryid, bcategoryid;
    String str_full_name, str_gender, str_blood_group, str_industry_type, str_business_category, str_firm_name, str_address, str_state, str_pincode,
            str_landline, str_mobile, str_email, str_emergency_number;
    String imgFile = "", path1 = "", path2 = "", path3 = "";
    int sel = 0;
    ScrollView scrollprofile;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    private ImageView ivImage;
    Fragment fragment = null;
    JSONObject data;
    String TAG = "LoginActivity_TAG";
    String result = "NA", response_string;
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static String getPath(Context context, Uri uri) {
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
                result = cursor.getString(column_index);
            }
            cursor.close();
        }
        if (result == null) {
            result = "Not found";
        }
        return result;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_profile_edit, container, false);
//        news= (LinearLayout) view.findViewById(R.id.news);
        boolean result = Utility.checkPermission(getActivity());
        et_full_name = view.findViewById(R.id.et_full_name);
        gender = view.findViewById(R.id.gender);
        et_blood_group = view.findViewById(R.id.et_blood_group);
        et_industry_type = view.findViewById(R.id.et_industry_type);
        et_business_category = view.findViewById(R.id.et_business_category);
        et_firm_name = view.findViewById(R.id.et_firm_name);
        et_address = view.findViewById(R.id.et_address);
        et_state = view.findViewById(R.id.et_state);
        et_pincode = view.findViewById(R.id.et_pincode);
        et_landline = view.findViewById(R.id.et_landline);
        et_mobile = view.findViewById(R.id.et_mobile);
        et_email = view.findViewById(R.id.et_email);
        et_emergency_number = view.findViewById(R.id.et_emergency_number);

        session = new UserSessionManager(getActivity());

        getUserDetail(session.getKeyUserid());

        availability= view.findViewById(R.id.availability);

        availability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new SellerAvailability();
                FragmentTransaction tx = getActivity().getSupportFragmentManager().beginTransaction();
                tx.replace(R.id.flContent, fragment, "schedule");
                tx.commit();
                tx.addToBackStack(null);
            }
        });

        layout_photo1 = view.findViewById(R.id.layout_photo1);
        layout_photo2 = view.findViewById(R.id.layout_photo2);
        layout_photo3 = view.findViewById(R.id.layout_photo3);

        photo1 = view.findViewById(R.id.photo1);
        photo2 = view.findViewById(R.id.photo2);
        photo3 = view.findViewById(R.id.photo3);
        layout_photo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivImage = photo1;
                selectImage();
                sel = 1;
            }
        });
        layout_photo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivImage = photo2;
                selectImage();
                sel = 2;
            }
        });
        layout_photo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivImage = photo3;
                selectImage();
                sel = 3;
            }
        });


        scrollprofile = view.findViewById(R.id.scrollprofile);

        scrollprofile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                scrollprofile.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        spinner_blood_group = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, blood_group);
        spinner_gender_type = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, gender_type);

        btn_save = view.findViewById(R.id.btn_save);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_full_name = et_full_name.getText().toString();
                str_gender = gender.getText().toString();
                str_blood_group = et_blood_group.getText().toString();
                str_industry_type = et_industry_type.getText().toString();
                str_business_category = et_business_category.getText().toString();
                str_firm_name = et_firm_name.getText().toString();
                str_address = et_address.getText().toString();
                str_state = et_state.getText().toString();
                str_pincode = et_pincode.getText().toString();
                str_landline = et_landline.getText().toString();
                str_mobile = et_mobile.getText().toString();
                str_email = et_email.getText().toString();
                str_emergency_number = et_emergency_number.getText().toString();

                if (!validateFirstname()) {
                    return;
                }

                if (!validateGender()) {
                    return;
                }
                if (!isValidBloodGroup()) {
                    return;
                }
                if (!isValidIndustry()) {
                    return;
                }
                if (!isValidBusiness()) {
                    return;
                }
                if (!validateEmail()) {
                    return;
                }
                if (!validateFirmname()) {
                    return;
                }
                if (!isValidLocation()) {
                    return;
                }
                if (!isValidState()) {
                    return;
                }
                if (!isValidPincode()) {
                    return;
                }
                if (!isValidMobile()) {
                    return;
                }

                if (!validateEmail()) {
                    return;
                }
                if (!isValidEmergencyNumber()) {
                    return;
                }


                pDialog.show();
                MultipartRequestParams params = new MultipartRequestParams();
                params.put("UserId", session.getKeyUserid());
                params.put("MobileNumber", str_mobile);
                params.put("Name", str_full_name);
                params.put("BloodGroup", str_blood_group);
                params.put("Gender", str_gender);
                params.put("Industry", str_industry_type);
                params.put("BusinessCategoryId", bcategoryid);
                params.put("FirmName", str_firm_name);
                params.put("Address1", str_address);
                params.put("StateName", str_state);
                params.put("PinCode", str_pincode);
                params.put("LandLineNumber", str_landline);
                params.put("EmailId", str_email);
                params.put("EmergencyNumber", str_emergency_number);
                params.put("Image1", path1);
                params.put("Image2", path2);
                params.put("Image3", path3);
                AppController.getInstance().addToRequestQueue(new MultipartRequest(Request.Method.POST, params, UrlConstant.GETADD_SEELER_PROFILE, new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {

                        Log.e("uploadresponse", response.toString());
                        pDialog.dismiss();
                    }


                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //TODO
                        Log.e("response", error.toString());
                        pDialog.dismiss();
                    }
                }));

            }
        });

        getStateName();
        getIndustryType();

        gender.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Select Gender")
                        .setAdapter(spinner_gender_type, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                gender.setText(gender_type[which].toString());
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });

        et_blood_group.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Select Blood Group")
                        .setAdapter(spinner_blood_group, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                et_blood_group.setText(blood_group[which].toString());
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });

        et_state.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Select State")
                        .setAdapter(spinner_state_name, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                et_state.setText(StateName[which].toString());
                                stateid = StateId[which].toString();
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
        et_industry_type.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Select Industry")
                        .setAdapter(spinner_industry_type, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                et_industry_type.setText(CategoryName[which].toString());
                                categoryid = CategoryId[which].toString();
                                Log.e("categoryid", categoryid);
                                et_business_category.setHint("Select Business Type");
                                getBusinessType(categoryid);
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });

        et_business_category.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Select Business Type")
                        .setAdapter(spinner_business_type, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                et_business_category.setText(BCategoryName[which].toString());
                                bcategoryid = BCategoryId[which].toString();
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });


        return view;

    }



    public void getUserDetail(String id) {


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                UrlConstant.GET_Seller_Profile_Update + id, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, response.toString());
                try {
                    String Status = response.getString("Status");
                    response_string = response.toString();
                    data = response;
                    if (Status.equals("false")) {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                android.support.v7.app.AlertDialog.Builder dlgAlert = new android.support.v7.app.AlertDialog.Builder(getActivity());
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

//                        et_full_name, gender, et_blood_group, et_industry_type, et_business_category, et_firm_name, et_address, et_state, et_pincode,
//                                et_landline, et_mobile, et_email, et_emergency_number;

                        if(response.getString("FirmName").equals("null")){

                        }else{

                        et_full_name.setText(response.getString("Name"));
                        gender.setText(response.getString("Gender"));
                        et_blood_group.setText(response.getString("Bloodgroup"));
                        et_firm_name.setText(response.getString("FirmName"));
                        et_mobile.setText(response.getString("MobileNumber"));
                        et_business_category.setText(response.getString("CategoryOfBusiness"));
                        et_address.setText(response.getString("Address1"));
                        et_email.setText(response.getString("EmailId"));
//                        et_landline.setText(response.getString("LandLineNumber"));
                        et_emergency_number.setText(response.getString("EmergencyMobileNumber"));
                        et_industry_type.setText(response.getString("Industry"));
                        et_state.setText(response.getString("StateName"));
                        et_pincode.setText(response.getString("PinCode"));

                            photo1.setImageUrl(response.getString("Image1"), imageLoader);
                            photo2.setImageUrl(response.getString("Image2"), imageLoader);
                            photo3.setImageUrl(response.getString("Image3"), imageLoader);

                        }
                    }

                } catch (Exception e) {

                }
                pDialog.dismiss();
                pDialog.hide();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        android.support.v7.app.AlertDialog.Builder dlgAlert = new android.support.v7.app.AlertDialog.Builder(getActivity());
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

        AppController.getInstance().addToRequestQueue(jsonObjReq, "profile edit");

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
//        getActivity().setTitle("USE");
    }

    private void getStateName() {

        JsonArrayRequest req = new JsonArrayRequest(UrlConstant.GETSTATE_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(Tag, response.toString());

                        StateName = new String[response.length()];
                        StateId = new String[response.length()];

                        try {

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response
                                        .get(i);

                                StateName[i] = person.getString("StateName");
                                StateId[i] = person.getString("StateId");
                            }

                            spinner_state_name = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, StateName);


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

    private void getIndustryType() {

        JsonArrayRequest req = new JsonArrayRequest(UrlConstant.GETIndustry_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(Tag, response.toString());

                        CategoryId = new String[response.length()];
                        CategoryName = new String[response.length()];

                        try {

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response
                                        .get(i);

                                CategoryId[i] = person.getString("CategoryId");
                                CategoryName[i] = person.getString("CategoryName");
                            }

                            spinner_industry_type = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, CategoryName);

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

    private void getBusinessType(String id) {

//        BCategoryId,BCategoryName

        JsonArrayRequest req = new JsonArrayRequest("http://webservicestrkus.tarule.com/api/Account/GetBusinessCategory?CategoryId=" + id,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(Tag, response.toString());

                        BCategoryId = new String[response.length()];
                        BCategoryName = new String[response.length()];

                        try {

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response
                                        .get(i);

                                BCategoryId[i] = person.getString("CategoryId");
                                BCategoryName[i] = person.getString("CategoryName");
                            }

                            spinner_business_type = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, BCategoryName);


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

    private boolean validateEmail() {
        str_email = et_email.getText().toString().trim();

        if (str_email.isEmpty() || !isValidEmail(str_email)) {
            Toast.makeText(getActivity(), "Valid email required", Toast.LENGTH_LONG).show();

            return false;
        } else {

        }

        return true;
    }

    private boolean validateFirstname() {
        str_full_name = et_full_name.getText().toString().trim();

        if (str_full_name.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter  name", Toast.LENGTH_LONG).show();

            return false;
        } else {

        }

        return true;
    }

    private boolean validateFirmname() {
        str_firm_name = et_firm_name.getText().toString().trim();

        if (str_firm_name.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter  name", Toast.LENGTH_LONG).show();

            return false;
        } else {

        }

        return true;
    }

    private boolean validateGender() {
        str_gender = gender.getText().toString().trim();

        if (str_gender.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter  name", Toast.LENGTH_LONG).show();

            return false;
        } else {

        }

        return true;
    }

    private boolean isValidMobile() {

        str_mobile = et_mobile.getText().toString().trim();

        if (str_mobile.isEmpty() || str_mobile.length() < 10) {

            Toast.makeText(getActivity(), "Not Valid Number", Toast.LENGTH_LONG).show();

            return false;
        } else {

        }

        return true;
    }

    private boolean isValidPincode() {

        str_pincode = et_pincode.getText().toString().trim();

        if (str_pincode.isEmpty() || str_pincode.length() < 6) {
            Toast.makeText(getActivity(), "Not Valid Pincode", Toast.LENGTH_LONG).show();


            return false;
        } else {

        }
        return true;
    }

    private boolean isValidBloodGroup() {

        str_blood_group = et_blood_group.getText().toString().trim();

        if (str_blood_group.isEmpty()) {
            Toast.makeText(getActivity(), "Select Blood Group", Toast.LENGTH_LONG).show();

            return false;
        } else {

        }
        return true;
    }

    private boolean isValidLocation() {

        str_address = et_address.getText().toString().trim();

        if (str_address.isEmpty()) {
            Toast.makeText(getActivity(), "Select Location", Toast.LENGTH_LONG).show();

            return false;
        } else {

        }
        return true;
    }

    private boolean isValidIndustry() {

        str_industry_type = et_industry_type.getText().toString().trim();

        if (str_industry_type.isEmpty()) {
            Toast.makeText(getActivity(), "Select Industry", Toast.LENGTH_LONG).show();

            return false;
        } else {

        }
        return true;
    }

    private boolean isValidBusiness() {

        str_business_category = et_business_category.getText().toString().trim();

        if (str_industry_type.isEmpty()) {
            Toast.makeText(getActivity(), "Select businees", Toast.LENGTH_LONG).show();

            return false;
        } else {

        }
        return true;
    }

    private boolean isValidState() {

        str_state = et_state.getText().toString().trim();

        if (str_state.isEmpty()) {
            Toast.makeText(getActivity(), "Select state", Toast.LENGTH_LONG).show();

            return false;
        } else {

        }
        return true;
    }

    private boolean isValidEmergencyNumber() {

        str_emergency_number = et_emergency_number.getText().toString().trim();

        if (str_emergency_number.isEmpty() || str_emergency_number.length() < 10) {
            Toast.makeText(getActivity(), "provide valid Emergency number", Toast.LENGTH_LONG).show();

            return false;
        } else {

        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getActivity());

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        Uri tempUri = getImageUri(getActivity().getApplicationContext(), thumbnail);

//        Uri selectedImageUri = data.getData( );
        imgFile = getPath(getActivity().getApplicationContext(), tempUri);
        Log.d("Picture Path", imgFile);

        if (sel == 1) {
            path1 = imgFile;
        } else if (sel == 2) {
            path2 = imgFile;
        } else if (sel == 3) {
            path3 = imgFile;
        }

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ivImage.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                Uri selectedImageUri = data.getData();
                imgFile = getPath(getActivity().getApplicationContext(), selectedImageUri);
                Log.d("Picture Path", imgFile);

                if (sel == 1) {
                    path1 = imgFile;
                } else if (sel == 2) {
                    path2 = imgFile;
                } else if (sel == 3) {
                    path3 = imgFile;
                }

                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ivImage.setImageBitmap(bm);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
