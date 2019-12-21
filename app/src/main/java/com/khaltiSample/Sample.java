package com.khaltiSample;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.button.MaterialButton;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;


import com.khalti.checkOut.api.Config;
import com.khalti.checkOut.api.OnCheckOutListener;
import com.khalti.utils.Constant;
import com.khalti.widget.KhaltiButton;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Sample extends AppCompatActivity {

    @BindView(R.id.kpPay)
    KhaltiButton khaltiButton;
    @BindView(R.id.kpPay1)
    KhaltiButton khaltiButton1;
    @BindView(R.id.kpPay2)
    KhaltiButton khaltiButton2;
    @BindView(R.id.btnMore)
    MaterialButton btnMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample);
        ButterKnife.bind(this);

        HashMap<String, String> map = new HashMap<>();
        map.put("merchant_extra", "This is extra data");
        map.put("merchant_extra_2", "This is extra data 2");

        Config config = new Config(Constant.pub, "Product ID", "Product Name", "Product Url", 1100L, map, new OnCheckOutListener() {

            @Override
            public void onSuccess(HashMap<String, Object> data) {
                Log.i("Payment confirmed", data + "");
            }

            @Override
            public void onError(String action, String message) {
                Log.i(action, message);
            }
        });

        khaltiButton.setOnClickListener(view -> khaltiButton.showCheckOut(config));
        khaltiButton1.setCheckOutConfig(config);
        khaltiButton2.setCheckOutConfig(config);
    }

    @OnClick(R.id.btnMore)
    public void onBtnMoreLoadClick() {
        startActivity(new Intent(this, MoreSamples.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
