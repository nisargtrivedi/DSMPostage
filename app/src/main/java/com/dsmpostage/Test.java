package com.dsmpostage;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class Test extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String cID = "fb191b5a87e994fc286b26efeef99c68ea71d461a828212df76b5515c43e8d5d";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String encode = null;
            try {
                encode = Base64.getEncoder().encodeToString(cID.getBytes("utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            System.out .println("MAIN STRING===>"+cID);
            System.out .println("ENCODE===>"+encode);

            byte[] base64decodedBytes = Base64.getDecoder().decode(encode);

            try {
                System.out.println("Original String: " + new String(base64decodedBytes, "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

    }
}
