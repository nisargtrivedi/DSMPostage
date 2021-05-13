package com.dsmpostage.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.dsmpostage.R;
import com.dsmpostage.databinding.OrderDetailActivityBinding;
import com.dsmpostage.netowork.APIInterface;
import com.dsmpostage.utility.AppPreferences;
import com.dsmpostage.utility.Constants;
import com.dsmpostage.utility.Util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetail extends AppCompatActivity {

    OrderDetailActivityBinding binding;
    String path;
    AppPreferences appPreferences;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appPreferences=new AppPreferences(this);
        binding= DataBindingUtil.setContentView(this, R.layout.order_detail_activity);
        String data=getIntent().getStringExtra("data");
        if(data!=null){
            String[] Orderdata=data.split(",");
            for(int i=0;i<Orderdata.length;i++){
                //Log.d("DATA--"+i,Orderdata[i]);
                binding.tvInvoice.setText(Orderdata[0]);
                binding.tvCustomerName.setText(Orderdata[1]);
                binding.tvDate.setText(Orderdata[2] + Orderdata[3]);
                binding.tvDue.setText(Orderdata[4]);
                binding.tvDimond.setText(Orderdata[5]);
            }

            binding.btnUploadCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(binding.btnUploadCopy.getText().toString().equalsIgnoreCase("UPLOAD SCANNED COPY")){

                        String[] in = binding.tvInvoice.getText().toString().trim().split(":");
                        RequestBody invoiceNo = RequestBody.create(MediaType.parse("text/plain"), in[1].trim());
                        RequestBody system_type = RequestBody.create(MediaType.parse("text/plain"), "diamond");
                        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), appPreferences.getString("USERNAME"));

                        File file = new File(path.toString());

                        MultipartBody.Part signature = null;

                        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                        signature = MultipartBody.Part.createFormData("signature_document", file.getName(), requestFile);

                        Util.showDialog(OrderDetail.this);
                        APIInterface apiInterface=DSMPostage.getRetrofitClient().create(APIInterface.class);


                        apiInterface.save_updateProfile(Constants.TYPE, Constants.CUSTOMER_KEY,Constants.CUSTOMER_SECRET,Constants.XKEY,invoiceNo,system_type,email,signature).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                Util.hideDialog();
                                System.out.println("Response--->"+response);
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Util.hideDialog();
                                System.out.println("Response--->"+t.getMessage());
                            }
                        });

                    }else {
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
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

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
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

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

}
