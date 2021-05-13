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
import com.dsmpostage.databinding.FragmentProfileBinding;
import com.dsmpostage.utility.AppPreferences;
import com.dsmpostage.utility.Util;

public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;
    AppPreferences appPreferences;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_profile,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appPreferences=new AppPreferences(getActivity());
        binding.tvTitle.setText("Employee Email : "+appPreferences.getString("USERNAME"));
    }
}
