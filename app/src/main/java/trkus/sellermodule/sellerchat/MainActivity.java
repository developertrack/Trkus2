package trkus.sellermodule.sellerchat;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import trkus.services.com.trkus.R;
import util.AppController;
import util.UrlConstant;
import util.UserSessionManager;

import static util.AppController.TAG;

public class MainActivity extends Fragment {

    boolean myMessage = true;
    ProgressDialog pDialog;
    UserSessionManager session;
    String[] SellerMessage, CustomerMessage;
    String CustomerId, CustomerName = null, chatlist;
    JSONArray data_chat;
    JSONObject data_jobject;
    private ListView listView;
    private View btnSend;
    private EditText editText;
    private List<ChatBubble> ChatBubbles;
    private ArrayAdapter<ChatBubble> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getActivity().setTitle("Chat");
        chatlist = getArguments().getString("chatlist");
        CustomerId = getArguments().getString("CustomerId");
        CustomerName = getArguments().getString("CustomerName");
        View view = inflater.inflate(R.layout.chatscreen_listing, container, false);

        listView = view.findViewById(R.id.list_msg);
        btnSend = view.findViewById(R.id.btn_chat_send);
        editText = view.findViewById(R.id.msg_type);
        session = new UserSessionManager(getActivity());
        //set ListView adapter first
        adapter = new MessageAdapter(getActivity(), R.layout.left_chat_bubble, ChatBubbles);
        listView.setAdapter(adapter);

        try {

            data_chat = new JSONArray(chatlist);

            SellerMessage = new String[data_chat.length()];
            CustomerMessage = new String[data_chat.length()];

            for (int i = 0; i < data_chat.length(); i++) {

                JSONObject person = (JSONObject) data_chat
                        .get(i);
                SellerMessage[i] = person.getString("SellerMessage");

                if (!SellerMessage[i].equals("NA")) {
                    ChatBubble ChatBubble = new ChatBubble(SellerMessage[i], false);
                    ChatBubbles.add(ChatBubble);
                    adapter.notifyDataSetChanged();
                }

                CustomerMessage[i] = person.getString("CustomerMessage");
                if (!CustomerMessage[i].equals("NA")) {
                    ChatBubble ChatBubble = new ChatBubble(CustomerMessage[i], myMessage);
                    ChatBubbles.add(ChatBubble);
                    adapter.notifyDataSetChanged();
                }

            }

        } catch (Exception e) {

        }


        //event for button SEND
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity(), "Please input some text...", Toast.LENGTH_SHORT).show();
                } else {
                    //add message to list
                    ChatBubble ChatBubble = new ChatBubble(editText.getText().toString(), false);
                    ChatBubbles.add(ChatBubble);
                    adapter.notifyDataSetChanged();

                    String tag_json_obj = "json_obj_req";

                    data_jobject = new JSONObject();
                    try {
                        data_jobject.put("Message", editText.getText().toString());
                        data_jobject.put("SellerId", session.getKeyUserid());
                        data_jobject.put("CutsomerId", CustomerId);

                    } catch (Exception e) {

                    }

                    Log.e("data_jobject", data_jobject.toString());

                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                            UrlConstant.POST_SellerToCustomerChat, data_jobject,
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(final JSONObject response) {
                                    Log.e(TAG, response.toString());
                                    try {
                                        String Status = response.getString("Status");

                                        if (Status.equals("false")) {

                                        } else {
                                            Toast.makeText(getActivity(), "Message send successfully", Toast.LENGTH_LONG).show();

                                        }

                                    } catch (Exception e) {

                                    }


                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.e(TAG, "Error: " + error.getMessage());


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


                    editText.setText("");

                    myMessage = !myMessage;
                }
            }
        });

        session = new UserSessionManager(getActivity());


        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                listView.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        return view;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ChatBubbles = new ArrayList<>();


    }
}
