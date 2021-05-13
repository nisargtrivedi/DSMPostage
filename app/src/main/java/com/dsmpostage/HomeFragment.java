package com.dsmpostage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.dsmpostage.databinding.FragmentHomeBinding;
import com.dsmpostage.main.BaseActivity;
import com.dsmpostage.utility.Util;

public class HomeFragment  extends Fragment {

    FragmentHomeBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.rlLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity)getActivity()).replaceFragment(new LogFragment());
                ((BaseActivity)getActivity()).binding.tvTitle.setText("ACTIVITY LOGS");
                Util.setBg(4,((BaseActivity)getActivity()).binding.img1,((BaseActivity)getActivity()).binding.img2,((BaseActivity)getActivity()).binding.img3,((BaseActivity)getActivity()).binding.img4,((BaseActivity)getActivity()).binding.img5);
            }
        });

        binding.rlScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity)getActivity()).replaceFragment(new ScanFragment());
                ((BaseActivity)getActivity()).binding.tvTitle.setText("SCAN QR CODE");
                Util.setBg(3,((BaseActivity)getActivity()).binding.img1,((BaseActivity)getActivity()).binding.img2,((BaseActivity)getActivity()).binding.img3,((BaseActivity)getActivity()).binding.img4,((BaseActivity)getActivity()).binding.img5);
            }
        });
    }
}
