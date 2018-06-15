package trkus.customermodule.dailyneeds;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import trkus.services.com.trkus.R;
import util.AppController;
import util.UserSessionManager;

public class DailyNeedItemDetail extends Fragment {

    TextView et_title, et_itemlist;
    RelativeLayout layout_photo1;
    Button next;
    NetworkImageView photo1;
    UserSessionManager session;
    String DailyNeedsItemId, Itemname, Title, OrderImage1;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    Dialog dialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Itemname = getArguments().getString("Itemname");
        Title = getArguments().getString("Title");
        OrderImage1 = getArguments().getString("OrderImage1");
        getActivity().setTitle(Title);
        View view = inflater.inflate(R.layout.dailyneeditemdetail, container, false);
        et_title = view.findViewById(R.id.et_title);
        et_itemlist = view.findViewById(R.id.et_itemlist);
        photo1 = view.findViewById(R.id.photo1);

        session = new UserSessionManager(getActivity());

        et_title.setText(Title);
        et_itemlist.setText(Itemname);
        photo1.setImageUrl(OrderImage1, imageLoader);

        photo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.image_dialog);
                NetworkImageView photo= dialog.findViewById(R.id.photo);
                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();
                photo.setImageUrl(OrderImage1, imageLoader);
                wlp.gravity = Gravity.CENTER;
                wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
                window.setAttributes(wlp);
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                dialog.show();
            }
        });

        return view;
    }
}