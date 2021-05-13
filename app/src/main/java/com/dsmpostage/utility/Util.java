package com.dsmpostage.utility;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;

import com.dsmpostage.R;

public class Util {

    public static ProgressDialog dialog;
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static void showDialog(Context context,String msg){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(msg);
        builder1.setTitle("Alert");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        dialog.dismiss();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
    public static void setBg(int no,ImageView im1,ImageView im2,ImageView im3,ImageView im4,ImageView im5){
       if(no==2){
           im1.setImageResource(R.drawable.ic_baseline_person_outline_24);
           im1.setBackgroundResource(0);

           im2.setImageResource(R.drawable.home_black);
           im2.setBackgroundResource(R.drawable.round);

           im3.setImageResource(R.drawable.ic_baseline_qr_code_scanner_24);
           im3.setBackgroundResource(0);

           im4.setImageResource(R.drawable.ic_setting);
           im4.setBackgroundResource(0);

           im5.setImageResource(R.drawable.ic_baseline_settings_24);
           im5.setBackgroundResource(0);
       } else if(no==1){
            im1.setImageResource(R.drawable.person_black);
            im1.setBackgroundResource(R.drawable.round);

            im2.setImageResource(R.drawable.ic_baseline_home_24);
            im2.setBackgroundResource(0);

            im3.setImageResource(R.drawable.ic_baseline_qr_code_scanner_24);
            im3.setBackgroundResource(0);

            im4.setImageResource(R.drawable.ic_setting);
            im4.setBackgroundResource(0);

            im5.setImageResource(R.drawable.ic_baseline_settings_24);
            im5.setBackgroundResource(0);
        }else if(no==3){
           im1.setImageResource(R.drawable.ic_baseline_person_outline_24);
           im1.setBackgroundResource(0);

           im2.setImageResource(R.drawable.ic_baseline_home_24);
           im2.setBackgroundResource(0);

           im3.setImageResource(R.drawable.scan_black);
           im3.setBackgroundResource(R.drawable.round);

           im4.setImageResource(R.drawable.ic_setting);
           im4.setBackgroundResource(0);

           im5.setImageResource(R.drawable.ic_baseline_settings_24);
           im5.setBackgroundResource(0);
       }else if(no==4){
           im1.setImageResource(R.drawable.ic_baseline_person_outline_24);
           im1.setBackgroundResource(0);

           im2.setImageResource(R.drawable.ic_baseline_home_24);
           im2.setBackgroundResource(0);

           im3.setImageResource(R.drawable.ic_baseline_qr_code_scanner_24);
           im3.setBackgroundResource(0);

           im4.setImageResource(R.drawable.black_setting);
           im4.setBackgroundResource(R.drawable.round);

           im5.setImageResource(R.drawable.ic_baseline_settings_24);
           im5.setBackgroundResource(0);
       }else if(no==5){
           im1.setImageResource(R.drawable.ic_baseline_person_outline_24);
           im1.setBackgroundResource(0);

           im2.setImageResource(R.drawable.ic_baseline_home_24);
           im2.setBackgroundResource(0);

           im3.setImageResource(R.drawable.ic_baseline_qr_code_scanner_24);
           im3.setBackgroundResource(0);

           im4.setImageResource(R.drawable.ic_setting);
           im4.setBackgroundResource(0);
           im5.setImageResource(R.drawable.base_setting_black);
           im5.setBackgroundResource(R.drawable.round);
       }
    }

    public static void showDialog(Context context){
        if(dialog==null){
            dialog=new ProgressDialog(context);
            dialog.setMessage("Loading..");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.show();
        }
    }
    public static void hideDialog(){
        if(dialog!=null){
            dialog.cancel();
            dialog.dismiss();
            dialog=null;
        }
    }

}
