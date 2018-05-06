package trkus.services.com.trkus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import util.AppController;

public class CustomGrid extends BaseAdapter {
    private Context mContext;
    private final String[] web;
    private final String[] Imageid;
    ImageLoader img_loader;

    public CustomGrid(Context c,String[] web,String[] Imageid ) {
        mContext = c;
        this.Imageid = Imageid;
        this.web = web;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return web.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_single, null);
            TextView textView = grid.findViewById(R.id.grid_text);
            NetworkImageView imageView = grid.findViewById(R.id.grid_image);
            textView.setText(web[position]);
            ImageLoader imageLoader = AppController.getInstance().getImageLoader();
            imageView.setImageUrl(Imageid[position], imageLoader);

        } else {
            grid = convertView;
        }

        return grid;
    }
}