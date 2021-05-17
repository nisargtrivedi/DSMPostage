package com.dsmpostage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.dsmpostage.databinding.FragmentGeneralSettingsBinding;
import com.dsmpostage.databinding.FragmentProfileBinding;
import com.dsmpostage.main.BaseActivity;
import com.dsmpostage.main.DSMPostage;
import com.dsmpostage.utility.AppPreferences;
import com.dsmpostage.utility.Cognito;
import com.dsmpostage.utility.Util;

public class GeneralSettingsFragment extends Fragment {

    FragmentGeneralSettingsBinding binding;
    AppPreferences appPreferences;

    Cognito cognito;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_general_settings,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appPreferences=new AppPreferences(getActivity());

        cognito=new Cognito(getActivity());
        binding.rlClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity)getActivity()).replaceFragment(new ProfileFragment());
                ((BaseActivity)getActivity()).binding.tvTitle.setText("PROFILE SETTINGS");
                Util.setBg(1,((BaseActivity)getActivity()).binding.img1,((BaseActivity)getActivity()).binding.img2,((BaseActivity)getActivity()).binding.img3,((BaseActivity)getActivity()).binding.img4,((BaseActivity)getActivity()).binding.img5);
            }
        });

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.dialog_confirm, null);
                    dialogBuilder.setView(dialogView);
                    dialogBuilder.setCancelable(false);

                    com.google.android.material.button.MaterialButton btnCancel = dialogView.findViewById(R.id.btnCancel);
                    com.google.android.material.button.MaterialButton btnOk = dialogView.findViewById(R.id.btnOk);
                    AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();

                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            appPreferences.set("USERNAME", "");
                            cognito.userLogout(getActivity(), appPreferences.getString("USERNAME"));
                            //getActivity().finish();
                        }
                    });
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                }catch (Exception ex){

                }


            }
        });
    }
}
