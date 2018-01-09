package com.skylix.videostatus.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.skylix.videostatus.R;


/**
 * Created by Destiny on 20-Jul-17.
 */

public class AlertDialog {

    public static NiftyDialogBuilder mDas;
    private Context context;

//    public AlertDialog(final Context context, String title, final String message, String btn1text) {
//        this.context = context;
//        mDas = NiftyDialogBuilder.getInstance(context);
//        mDas.withIcon(R.drawable.exit);
//        mDas.withDialogColor(context.getResources().getColor(R.color.colorAccent));
//        mDas.withTitle(title);
//        mDas.withTitleColor(context.getResources().getColor(R.color.title_color));
//        mDas.withMessage(message);
//        mDas.withMessageColor(context.getResources().getColor(R.color.message_color));
//        mDas.isCancelable(false);
//        mDas.withButton1Text(btn1text);
//        mDas.setButton1Click(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent i = new Intent(context, activity.getClass());
////                context.startActivity(i);
//mDas.dismiss();
//            }
//        });
//        mDas.withDuration(700);
//        mDas.withEffect(Effectstype.Fall);
//        mDas.show();
//    }

    public AlertDialog(final Context context, String title, String message, String btn1text, final Activity activity) {
        this.context = context;
        mDas = NiftyDialogBuilder.getInstance(context);
        mDas.withIcon(R.drawable.gificon);
        mDas.withDialogColor(context.getResources().getColor(R.color.colorAccent));
        mDas.withTitle(title);
        mDas.withTitleColor(context.getResources().getColor(R.color.colorPrimary));
        mDas.withMessage(message);
        mDas.withMessageColor(context.getResources().getColor(R.color.colorPrimaryDark));
        mDas.isCancelable(false);
        mDas.withButton1Text(btn1text);
        mDas.setButton1Click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, activity.getClass());
                context.startActivity(i);
                ((Activity) context).finish();
            }
        });
        mDas.withDuration(700);
        mDas.withEffect(Effectstype.Fall);
        mDas.show();
    }

}