package com.hit.game012.gameplay;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.hit.game012.R;

public class ShowPopUp implements Runnable {

    private View view;

    public ShowPopUp(View view) {
        this.view = view;
    }

    @Override
    public void run() {
        popupMessage();
    }

    public void popupMessage(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        final View layout=LayoutInflater.from(view.getContext()).inflate(R.layout.pop_up_win,null);
        alertDialogBuilder.setView(layout);
//        alertDialogBuilder.setMessage("No Internet Connection. Check Your Wifi Or enter code hereMobile Data.");
//        alertDialogBuilder.setTitle("Connection Failed");
//        alertDialogBuilder.setNegativeButton("ok", new DialogInterface.OnClickListener(){

//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
        // add these two lines, if you wish to close the app:
//                finishAffinity();
//                System.exit(0);
//            }
//        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}