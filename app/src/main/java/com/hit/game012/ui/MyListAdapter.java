package com.hit.game012.ui;

import android.app.Activity;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hit.game012.R;
import com.hit.game012.ui.robohash.RoboHash;
import com.hit.game012.ui.robohash.handle.Handle;

import java.io.IOException;
import java.util.TreeMap;
import java.util.UUID;

public class MyListAdapter extends ArrayAdapter<Object> {

    private final Activity context;
    private final int defaultImgid;
    private final TreeMap<String, String> HighScoreTM;

    public MyListAdapter(Activity context, TreeMap<String, String> HighScoreTM, Integer defaultImgid) {
        super(context, R.layout.scoreboard_list, HighScoreTM.keySet().toArray());

        this.context=context;
        this.HighScoreTM = HighScoreTM;
        this.defaultImgid=defaultImgid;

    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.scoreboard_list, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.subtitle);

        String userName = HighScoreTM.keySet().toArray()[position].toString();
        titleText.setText(userName);
        subtitleText.setText(HighScoreTM.get(userName).toString());
        RoboHash robots = new RoboHash(getContext());
        Handle immutableHandle = robots.calculateHandleFromUUID(UUID.nameUUIDFromBytes(userName.getBytes())); // todo actually get UID instead of username
        try {
            Bitmap bitmap = robots.imageForHandle(immutableHandle);
            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
            imageView.setImageResource(defaultImgid); // default empty picture
        }

        return rowView;

    };
}