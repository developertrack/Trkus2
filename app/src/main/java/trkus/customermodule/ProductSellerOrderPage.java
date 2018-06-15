package trkus.customermodule;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import trkus.customermodule.customerorder.CustomeOrderPage;
import trkus.services.com.trkus.R;
import util.AppController;
import util.UrlConstant;
import util.UserSessionManager;
import util.Utility;
import util.VolleyMultipartRequest;
import util.VolleySingleton;

import static util.AppController.TAG;

public class ProductSellerOrderPage extends Fragment {

    Fragment fragment = null;
    String tag_json_obj = "json_obj_req";
    ProgressDialog pDialog;
    JSONObject data_jobject;
    String Tag = "Dashboard";
    String SellerUserId, Industry, FirmName, MobileNumber, Address1, PinCode, Image1, CategoryName;
    TextView seller_name_title, seller_address, seller_contact, upload_list;
    NetworkImageView image;
    Button addtofavourite, next;
    EditText et_itemlist;
    String imgFile = "";
    String str_itemlist;
    UserSessionManager session;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    ScrollView scrollView;
    int order_status = 0;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    byte[] imgbytedata;
    boolean result;

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

        SellerUserId = getArguments().getString("SellerUserId");
        CategoryName = getArguments().getString("CategoryName");
        FirmName = getArguments().getString("FirmName");
        MobileNumber = getArguments().getString("MobileNumber");
        Address1 = getArguments().getString("Address1");
        PinCode = getArguments().getString("PinCode");
        Image1 = getArguments().getString("Image1");

        getActivity().setTitle(CategoryName);
        View view = inflater.inflate(R.layout.product_seller_detail, container, false);

        seller_name_title = view.findViewById(R.id.seller_name_title);
        seller_name_title.setText(FirmName);
        seller_address = view.findViewById(R.id.seller_address);
        seller_address.setText(Address1 + " " + PinCode);
        seller_contact = view.findViewById(R.id.seller_contact);
        seller_contact.setText(MobileNumber);
        upload_list = view.findViewById(R.id.upload_list);
        image = view.findViewById(R.id.image);
        addtofavourite = view.findViewById(R.id.addtofavourite);
        et_itemlist = view.findViewById(R.id.et_itemlist);
        next = view.findViewById(R.id.next);
        session = new UserSessionManager(getActivity());
        scrollView = view.findViewById(R.id.scrollView);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                scrollView.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        String temp =Image1;

        temp = temp.replaceAll(" ", "%20");

        image.setImageUrl(temp, imageLoader);

        upload_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        result = Utility.checkPermission(getActivity());

        addtofavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSellerFavourite();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_itemlist = et_itemlist.getText().toString();
                if (str_itemlist.isEmpty()) {
                    str_itemlist = "NA";
                    order_status = 1;
                }
                if (str_itemlist.isEmpty()) {
                    str_itemlist = "NA";
                    order_status = order_status + 1;
                }

                if (order_status == 2) {
                    Toast.makeText(getActivity(), "Please write or upload product list", Toast.LENGTH_LONG).show();
                } else {
                    pDialog = new ProgressDialog(getActivity());
                    pDialog.setMessage("Loading...");
                    pDialog.show();


                        String filepath = "/sdcard/temp.png";
                        File imagefile = new File(imgFile);
                        FileInputStream fis = null;
                        try {
                            fis = new FileInputStream(imagefile);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        Bitmap bm = BitmapFactory.decodeStream(fis);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.JPEG, 100 , baos);
                        final byte[] b = baos.toByteArray();




                    VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, UrlConstant.Post_Order_Url, new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            String resultResponse = new String(response.data);
                            Log.e("resultResponse",resultResponse);

                            try {
                                final JSONObject mainObject = new JSONObject(resultResponse);
//                                Toast.makeText(getActivity(), mainObject.getString("Message"), Toast.LENGTH_LONG).show();
                                Log.e("resultResponse1",mainObject.toString());
                                String Status = mainObject.getString("Status");

                                if (Status.equals("false")) {

                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            android.support.v7.app.AlertDialog.Builder dlgAlert = new android.support.v7.app.AlertDialog.Builder(getActivity());
                                            try {
                                                dlgAlert.setMessage(mainObject.getString("Message"));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            dlgAlert.setPositiveButton("OK", null);
                                            dlgAlert.setCancelable(true);
                                            dlgAlert.create().show();
                                        }
                                    });

                                } else {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            android.support.v7.app.AlertDialog.Builder dlgAlert = new android.support.v7.app.AlertDialog.Builder(getActivity());
                                            try {
                                                dlgAlert.setMessage(mainObject.getString("Message")+"\n"+"Your Orderid:- "+mainObject.getString("OrderId"));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            dlgAlert.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    fragment = new CustomeOrderPage();
                                                    FragmentTransaction tx = getActivity().getSupportFragmentManager().beginTransaction();
                                                    tx.replace(R.id.flContent, fragment, "CustomerOrderPage");
                                                    tx.commit();
                                                    tx.addToBackStack(null);
                                                }
                                            });
                                            dlgAlert.setCancelable(true);
                                            dlgAlert.create().show();

                                        }
                                    });

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            pDialog.dismiss();
                            // parse success output
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            pDialog.dismiss();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("SellerUserId", SellerUserId);
                            params.put("CustomerUserId", session.getKeyUserid());
                            params.put("ItemName", str_itemlist);
                            return params;
                        }

                        @Override
                        protected Map<String, DataPart> getByteData() {
                            Map<String, DataPart> params = new HashMap<>();
                            // file name could found file base or direct access from real path
                            // for now just get bitmap data from ImageView
                            params.put("Image1", new DataPart(System.currentTimeMillis() + ".jpg", b, "image/jpeg"));

                            return params;
                        }
                    };

                    VolleySingleton.getInstance(getActivity()).addToRequestQueue(multipartRequest);

                }

            }
        });

        return view;

    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {


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

//        image.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                Uri selectedImageUri = data.getData();
                imgFile = getPath(getActivity().getApplicationContext(), selectedImageUri);
                Log.d("Picture Path", imgFile);


                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        image.setImageBitmap(bm);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void addSellerFavourite() {

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        data_jobject = new JSONObject();
        try {
            data_jobject.put("SellerId", SellerUserId);
            data_jobject.put("CustomerUserId", session.getKeyUserid());

        } catch (Exception e) {

        }


        Log.e("data_jobject", data_jobject.toString());

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                UrlConstant.AddFavourite_Seller, data_jobject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject response) {
                        Log.e(TAG, response.toString());
                        try {
                            String Status = response.getString("Status");

                            if (Status.equals("false")) {

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        android.support.v7.app.AlertDialog.Builder dlgAlert = new android.support.v7.app.AlertDialog.Builder(getActivity());
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
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        android.support.v7.app.AlertDialog.Builder dlgAlert = new android.support.v7.app.AlertDialog.Builder(getActivity());
                                        dlgAlert.setMessage("This seller added to your favourite.");

                                        dlgAlert.setPositiveButton("OK", null);
                                        dlgAlert.setCancelable(true);
                                        dlgAlert.create().show();
                                    }
                                });

                            }

                        } catch (Exception e) {

                        }

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
                        dlgAlert.setMessage("Error while logging in, please try again");
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

}
