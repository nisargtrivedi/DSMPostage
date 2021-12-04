package com.dsmpostage.main;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;

import com.dsmpostage.R;
import com.dsmpostage.databinding.OrderDetailActivityBinding;
import com.dsmpostage.netowork.APIInterface;
import com.dsmpostage.utility.AppPreferences;
import com.dsmpostage.utility.Constants;
import com.dsmpostage.utility.Util;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.core.content.FileProvider.getUriForFile;

public class OrderDetail extends AppCompatActivity {

    OrderDetailActivityBinding binding;
    String path;
    String documentType="";
    AppPreferences appPreferences;
    int i=0;
    int urlVersion=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        appPreferences=new AppPreferences(this);
        binding= DataBindingUtil.setContentView(this, R.layout.order_detail_activity);
        String data=getIntent().getStringExtra("data");
        if(data!=null){
            String[] Orderdata=data.split(",");
            //for(int i=0;i<Orderdata.length;i++){
                //Log.d("DATA--"+i,Orderdata[i]);

//                App: DSM,Type: Manufacturing

            try {
                binding.tvInvoice.setText(Orderdata[0]);
                binding.tvCustomerName.setText(Orderdata[1]);
                binding.tvDate.setText(Orderdata[2] + Orderdata[3]);
                binding.tvDue.setText(Orderdata[4]);
                binding.tvDimond.setText(Orderdata[5]);
                if (Orderdata[6].contains("MDJ")) {
                    urlVersion=2;
                } else if (Orderdata[6].contains("DSM")) {
                    urlVersion=1;
                }
                documentType = (Orderdata[7].contains("Manufacturing")?"Manufacturing":"Sales");
            }catch (Exception ex){
                Util.showDialog(OrderDetail.this,"Please check the invoice data");
            }
            //}
            binding.imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(i==0){
                        backOne();
                        i++;
                    }else {
                        i=0;
                        finish();
                    }
                }
            });

            binding.btnUploadCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    i=0;
                    try{
                        if(binding.btnUploadCopy.getText().toString().equalsIgnoreCase("UPLOAD SCANNED COPY"))
                        {
                            i=0;
                            String[] in = binding.tvInvoice.getText().toString().trim().split(":");
                            String[] scDimanod = binding.tvDimond.getText().toString().trim().split(":");
                            RequestBody invoiceNo = RequestBody.create(in[1].trim(),MediaType.parse("text/plain"));
                            RequestBody system_type = RequestBody.create(scDimanod[1].trim(),MediaType.parse("multipart/form-data"));
                            RequestBody email = RequestBody.create(appPreferences.getString("USERNAME").toString(),MediaType.parse("multipart/form-data"));
                            RequestBody docType = RequestBody.create(documentType.toString(),MediaType.parse("multipart/form-data"));

                            File file = new File(path.toString());

                            MultipartBody.Part signature = null;

                            RequestBody requestFile = RequestBody.create(file,MediaType.parse("multipart/form-data"));
                            signature = MultipartBody.Part.createFormData("signature_document", file.getName(), requestFile);

                            Util.showDialog(OrderDetail.this);
                            DSMPostage.retrofit = null;
                            APIInterface apiInterface= (urlVersion==1 ? DSMPostage.getRetrofitClient().create(APIInterface.class) : DSMPostage.getRetrofitClient2().create(APIInterface.class));

//                        Constants.TYPE, Constants.CUSTOMER_KEY,Constants.CUSTOMER_SECRET,Constants.XKEY,

                            apiInterface.save_updateProfile(Constants.CUSTOMER_KEY,Constants.CUSTOMER_SECRET,Constants.XKEY,invoiceNo,system_type,email,docType,signature).enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    Util.hideDialog();
                                    //System.out.println("Response--->"+response.body());
                                    try {
                                        JSONObject object = new JSONObject(response.body().toString());

                                        int status = object.optInt("status");
                                        String message = object.optString("message");

                                        if (status == 200) {

                                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(OrderDetail.this);
                                            LayoutInflater inflater = getLayoutInflater();
                                            View dialogView = inflater.inflate(R.layout.dialog_success, null);
                                            dialogBuilder.setView(dialogView);
                                            dialogBuilder.setCancelable(false);

                                            com.google.android.material.button.MaterialButton btnOk = dialogView.findViewById(R.id.btnOk);
                                            AlertDialog alertDialog = dialogBuilder.create();
                                            alertDialog.show();

                                            btnOk.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    alertDialog.dismiss();
                                                    finish();

                                                }
                                            });
                                        }else if(status==500){
                                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(OrderDetail.this);
                                            LayoutInflater inflater = getLayoutInflater();
                                            View dialogView = inflater.inflate(R.layout.dialog_success, null);
                                            dialogBuilder.setView(dialogView);
                                            dialogBuilder.setCancelable(false);

                                            com.google.android.material.button.MaterialButton btnOk = dialogView.findViewById(R.id.btnOk);
                                            TextView  tvMsg= dialogView.findViewById(R.id.tvMsg);
                                            AlertDialog alertDialog = dialogBuilder.create();
                                            alertDialog.show();
                                            tvMsg.setText(message+"");

                                            btnOk.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    alertDialog.dismiss();
                                                    finish();

                                                }
                                            });
                                        }
                                        else{
                                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(OrderDetail.this);
                                            LayoutInflater inflater = getLayoutInflater();
                                            View dialogView = inflater.inflate(R.layout.dialog_success, null);
                                            dialogBuilder.setView(dialogView);
                                            dialogBuilder.setCancelable(false);

                                            com.google.android.material.button.MaterialButton btnOk = dialogView.findViewById(R.id.btnOk);
                                            TextView  tvMsg= dialogView.findViewById(R.id.tvMsg);
                                            AlertDialog alertDialog = dialogBuilder.create();
                                            alertDialog.show();
                                            tvMsg.setText(R.string.invoice_not_found);

                                            btnOk.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    alertDialog.dismiss();
                                                    finish();

                                                }
                                            });


                                        }
                                    } catch(JSONException e){
                                        e.printStackTrace();
                                    }


                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Util.hideDialog();
                                    //System.out.println("Response--->"+t.getMessage()+"---");
                                }
                            });
                        }
                        else {
                            try {
                                ImagePickerActivity.showImagePickerOptions(OrderDetail.this, new ImagePickerActivity.PickerOptionListener() {
                                    @Override
                                    public void onTakeCameraSelected() {
                                        launchCameraIntent();
                                    }

                                    @Override
                                    public void onChooseGallerySelected() {
                                        launchGalleryIntent();
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }catch (Exception ex){
                        Util.showDialog(OrderDetail.this,"Invoice Code/Consignment Code might be wrong");
                    }

                }
            });
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        //binding.imgData.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {

    }


    private void launchCameraIntent() {
        Intent intent = new Intent(OrderDetail.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, false);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 500);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 500);

        startActivityForResult(intent, 7);
    }
    private void launchGalleryIntent() {
        Intent intent = new Intent(OrderDetail.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, 8);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7) {
//            System.out.println("URI----" + data.getParcelableExtra("path") + "---");
            if (resultCode == RESULT_OK) {
                try {
                    try {

                        onCaptureImageResult(data);

                        //System.out.println("URI----" + fpath + "---");
                        // onCaptureImageResult(data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bytes);

//        File obj = new File(
//                Environment.getExternalStorageDirectory().getAbsolutePath() + "/spyker/");
        // have the object build the directory structure, if needed.


        File obj;
        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            obj = new File(
                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/DSM/");
        }else{
            obj=new File(String.valueOf(getApplicationContext().getExternalFilesDir("DSM")));
        }

        if (!obj.exists()) {
            obj.mkdirs();
        }

        try {
            File f = new File(obj, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(OrderDetail.this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private String onCaptureImageResult(Intent data) {
        //profilepic.setImageBitmap(null);

        Uri fpath = data.getParcelableExtra("path");

        binding.imgData.setImageURI(fpath);
        binding.imgData.setVisibility(View.VISIBLE);
        binding.tvTitle.setText("USER COPY");
        binding.btnUploadCopy.setText("UPLOAD SCANNED COPY");
        binding.tvInvoice.setVisibility(View.INVISIBLE);
        binding.tvCustomerName.setVisibility(View.INVISIBLE);
        binding.tvDue.setVisibility(View.INVISIBLE);
        binding.tvDate.setVisibility(View.INVISIBLE);
        binding.tvDimond.setVisibility(View.INVISIBLE);
//        String fpath = mTempCameraPhotoFile.getPath();
//        Bitmap thumbnail;
//        String path = saveImage(thumbnail);
        // Log.i("CAMERA FILE",fpath);
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(OrderDetail.this.getContentResolver(), fpath);

            path  = saveImage(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //new ImageCompression().execute(fpath.toString());
        //img.setImageURI(Uri.parse(cameraFilePath));

        return path;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
    private String onSelectFromGalleryResult(Intent data) {
        try {
            Uri fpath = data.getParcelableExtra("path");
            Uri ImageSelect = data.getData();


            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(OrderDetail.this.getContentResolver(), fpath);
                path  = saveImage(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
            //new ImageCompression().execute(fpath.toString());
            //img.setImageURI(Uri.parse(cameraFilePath));
        }catch (Exception e){
            Log.e("Error...",e.getMessage());
            Toast.makeText(OrderDetail.this,"Try Again...",Toast.LENGTH_LONG).show();
        }
        return path;
    }

    private void backOne(){
        binding.imgData.setVisibility(View.GONE);
        binding.tvTitle.setText("ORDER DETAIL");
        binding.btnUploadCopy.setText("UPLOAD CUSTOMER COPY");
        binding.tvInvoice.setVisibility(View.VISIBLE);
        binding.tvCustomerName.setVisibility(View.VISIBLE);
        binding.tvDue.setVisibility(View.VISIBLE);
        binding.tvDate.setVisibility(View.VISIBLE);
        binding.tvDimond.setVisibility(View.VISIBLE);
    }
}
