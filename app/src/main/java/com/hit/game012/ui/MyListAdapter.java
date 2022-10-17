package com.hit.game012.ui;

import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hit.game012.R;

import java.util.TreeMap;

public class MyListAdapter extends ArrayAdapter<Object> {

    private final Activity context;
    private final Integer[] imgid;
    private final TreeMap<String, String> HighScoreTM;

    public MyListAdapter(Activity context, TreeMap<String, String> HighScoreTM, Integer[] imgid) {
        super(context, R.layout.scoreboard_list, HighScoreTM.keySet().toArray());
        // TODO Auto-generated constructor stub

        this.context=context;
        this.HighScoreTM = HighScoreTM;
        this.imgid=imgid;

    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.scoreboard_list, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.subtitle);

        String userName = HighScoreTM.keySet().toArray()[position].toString();
        titleText.setText(userName);
//        imageView.setImageResource(imgid[0]); // todo actual user profile pic
        subtitleText.setText(HighScoreTM.get(userName).toString());
        // show The Image in a ImageView
        new DownloadImageTask(imageView).execute("https://robohash.org/" + userName + "?gravatar=yes");


        return rowView;

    };
}