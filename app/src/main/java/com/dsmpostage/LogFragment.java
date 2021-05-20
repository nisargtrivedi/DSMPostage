package com.dsmpostage;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dsmpostage.adapter.LogAdapter;
import com.dsmpostage.databinding.FragmentLogBinding;
import com.dsmpostage.databinding.FragmentProfileBinding;
import com.dsmpostage.main.DSMPostage;
import com.dsmpostage.model.LogResponse;
import com.dsmpostage.netowork.APIInterface;
import com.dsmpostage.utility.AppPreferences;
import com.dsmpostage.utility.Constants;
import com.dsmpostage.utility.Util;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogFragment extends Fragment {

    FragmentLogBinding binding;
    AppPreferences appPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_log,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appPreferences=new AppPreferences(getActivity());
        LoadData();
    }

    @UiThread
    private void LoadData(){
        Util.showDialog(getActivity());
       APIInterface apiInterface=DSMPostage.getRetrofitClient().create(APIInterface.class);

       apiInterface.getData(Constants.TYPE, Constants.CUSTOMER_KEY,Constants.CUSTOMER_SECRET,Constants.XKEY,appPreferences.getString("USERNAME")).enqueue(new Callback<LogResponse>() {
           @Override
           public void onResponse(Call<LogResponse> call, Response<LogResponse> response) {
                Util.hideDialog();
               //System.out.println("Response--->"+response);
               if(response.isSuccessful()) {
                   LogAdapter adapter = new LogAdapter(getActivity(), response.body().logList);

                   binding.rvLogs.setLayoutManager(new LinearLayoutManager(getActivity()));
                   binding.rvLogs.setAdapter(adapter);
                   if(response.body().logList!=null && response.body().logList.size()>0) {
                       binding.tvMsg.setVisibility(View.GONE);
                       binding.rvLogs.setVisibility(View.VISIBLE);
                   }else{
                       binding.tvMsg.setVisibility(View.VISIBLE);
                       binding.rvLogs.setVisibility(View.GONE);
                   }
               }else{
                   binding.tvMsg.setVisibility(View.VISIBLE);
                   binding.rvLogs.setVisibility(View.GONE);
               }
           }

           @Override
           public void onFailure(Call<LogResponse> call, Throwable t) {
               Util.hideDialog();
              // System.out.println("Response--->"+t.getMessage());
           }
       });

    }
}
