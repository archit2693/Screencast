package com.example.gemswin.screancasttest;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by this pc on 14-02-17.
 */

public class CustomGridAdapter extends BaseAdapter {
    private Context mContext;
    private final String name[];
    private final int Imageid[];


    public CustomGridAdapter(Context mContext, String[] name, int[] Imageid)
    {
        this.mContext = mContext;
        this.name = name;
        this.Imageid = Imageid;
    }

    @Override
    public int getCount() {
        return name.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            grid = inflater.inflate(R.layout.card_filetype, null);
            ImageView image = (ImageView)grid.findViewById(R.id.imagetype);
            image.setImageResource(Imageid[i]);
            TextView textView1 = (TextView) grid.findViewById(R.id.cname);
            textView1.setText(name[i]);
        } else {
            grid = view;
        }
        Log.d("Adapeter","card ban diya");
        return grid;
    }
}
