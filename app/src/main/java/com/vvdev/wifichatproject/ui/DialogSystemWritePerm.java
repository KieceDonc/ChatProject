package com.vvdev.wifichatproject.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import com.vvdev.wifichatproject.R;

public class DialogSystemWritePerm extends AlertDialog.Builder {

    private Context context;

    public DialogSystemWritePerm(final Context receive) {
        super(receive);
        context=receive;
    }

    public AlertDialog get(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.PermWriteSystemTitle));
        builder.setMessage(context.getResources().getString(R.string.PermWriteSystemDesc));
        builder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(R.string.Activate,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                context.startActivity(intent); // this intent show perm menu of write settings

            }
        });
        return builder.create();
    }
}
