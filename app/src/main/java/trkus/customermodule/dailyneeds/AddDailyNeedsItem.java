package trkus.customermodule.dailyneeds;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import trkus.services.com.trkus.R;
import util.AppController;
import util.MultipartRequest;
import util.MultipartRequestParams;
import util.UrlConstant;
import util.UserSessionManager;
import util.Utility;

public class AddDailyNeedsItem extends Fragment {

    EditText et_title, et_itemlist;
    RelativeLayout layout_photo1;
    Button next;
    ImageView photo1;
    UserSessionManager session;
    String imgFile = "";
    ProgressDialog pDialog;
    String str_itemlist, str_title;
    int order_status = 0;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Daily Need Item");
        View view = inflater.inflate(R.layout.add_daily_need, container, false);
        et_title = view.findViewById(R.id.et_title);
        et_itemlist = view.findViewById(R.id.et_itemlist);
        layout_photo1 = view.findViewById(R.id.layout_photo1);
        photo1 = view.findViewById(R.id.photo1);
        next = view.findViewById(R.id.next);

        session = new UserSessionManager(getActivity());

        layout_photo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_itemlist = et_itemlist.getText().toString();

                str_title = et_title.getText().toString();
                if (str_itemlist.isEmpty()) {
                    Toast.makeText(getActivity(), "Please write Title of list", Toast.LENGTH_LONG).show();

                }
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


                    MultipartRequestParams params = new MultipartRequestParams();
                    params.put("CustomerUserId", session.getKeyUserid());
                    params.put("Image1", imgFile);
                    params.put("ItemName", str_itemlist);
                    params.put("Title", str_title);

//                    CustomerUserId,Image1,ItemName,Title​​​​​​​

                    AppController.getInstance().addToRequestQueue(new MultipartRequest(Request.Method.POST, params, UrlConstant.POST_Daily_Need_Item, new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {

                            Log.e("response", response.toString());
                            try {
                                JSONObject mainObject = new JSONObject(response.toString());
                                Toast.makeText(getActivity(), mainObject.getString("Message"), Toast.LENGTH_LONG).show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                            {"CustomerUserId":45,"Message":"Order Sucessfully!","OrderId":"TRKUS180505115032389","Status":true}


                            pDialog.dismiss();
                        }


                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //TODO
                            pDialog.dismiss();
                        }
                    }));
                }


            }
        });

        return view;
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

        photo1.setImageBitmap(thumbnail);
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

        photo1.setImageBitmap(bm);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
