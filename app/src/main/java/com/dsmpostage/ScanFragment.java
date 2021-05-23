package com.dsmpostage;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.dsmpostage.databinding.FragmentProfileBinding;
import com.dsmpostage.databinding.FragmentScanBinding;
import com.dsmpostage.main.ImagePickerActivity;
import com.dsmpostage.main.OrderDetail;
import com.dsmpostage.utility.AppPreferences;
import com.dsmpostage.utility.Util;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.app.Activity.RESULT_OK;
import static androidx.core.content.FileProvider.getUriForFile;

public class ScanFragment extends Fragment implements ZXingScannerView.ResultHandler {

    FragmentScanBinding binding;
    AppPreferences appPreferences;
    private ZXingScannerView mScannerView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_scan,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appPreferences=new AppPreferences(getActivity());
        mScannerView = new ZXingScannerView(getActivity());
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadView();
            }
        },100);

        binding.btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.btnStop.getText().toString().equalsIgnoreCase("START SCANNING QR")) {
                    loadView();
                    resetHandler();
                    binding.btnStop.setText("STOP SCANNING QR");
                }


            }
        });

    }
    @Override
    public void onResume() {
        super.onResume();
        resetHandler();
    }
    @UiThread
    public void resetHandler(){
        mScannerView.setResultHandler(this);
        mScannerView.startCamera(0);
    }

    @UiThread
    public void loadView(){
        binding.contentFrame.addView(mScannerView);
    }
    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
        binding.contentFrame.removeAllViews();
        binding.btnStop.setText("START SCANNING QR");
    }
    @Override
    public void handleResult(Result result) {

        try{
        Log.d("RESULT---->",result.getText().toString());
        if(result!=null && !result.getText().isEmpty()) {

            if (result.getText().toString().contains("Invoice Code:")){
                getActivity().startActivity(new Intent(getActivity(), OrderDetail.class)
                        .putExtra("data", result.getText())
                );
            }else if (result.getText().toString().contains("Consignment Code:")){
                getActivity().startActivity(new Intent(getActivity(), OrderDetail.class)
                        .putExtra("data", result.getText())
                );
            }else{
                Util.showDialog(getActivity(),getString(R.string.invoice_not_found));
            }
        }
//            try {
//                ImagePickerActivity.showImagePickerOptions(getActivity(), new ImagePickerActivity.PickerOptionListener() {
//                    @Override
//                    public void onTakeCameraSelected() {
//                        launchCameraIntent();
//                    }
//
//                    @Override
//                    public void onChooseGallerySelected() {
//                        launchGalleryIntent();
//                    }
//                });
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }catch (Exception ex){
            Util.showDialog(getActivity(),getString(R.string.invoice_not_found));
        }



//        Invoice Code: INV21353,Customer Name: G.P. ISRAEL DIAMONDS,Date: Jan 22, 2021,Due Amount: $ 144.37,System: diamond



    }


}
