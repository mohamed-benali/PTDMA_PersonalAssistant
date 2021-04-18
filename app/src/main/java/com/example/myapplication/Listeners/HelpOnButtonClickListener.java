package com.example.myapplication.Listeners;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class HelpOnButtonClickListener implements View.OnClickListener {

    private Context context;
    String title;
    String message;

    public Context getContext() {
        return context;
    }
    public void setContext(Context context) {
        this.context = context;
    }

    public HelpOnButtonClickListener(Context context, String title, String message) {
        this.setContext(context);
        this.title = title;
        this.message = message;
    }

    @Override
    public void onClick(View v) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(message)
                .create();

        alertDialog.show();
        TextView message = (TextView) alertDialog.findViewById(android.R.id.message);
        message.setTextSize(20);
        //alertDialog.dismiss();
    }


}
