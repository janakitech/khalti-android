package com.khalti.checkOut.helper;

import android.content.Context;
import androidx.annotation.Keep;

import com.khalti.checkOut.CheckOutActivity;
import com.khalti.checkOut.api.Config;
import com.khalti.rxBus.RxBus;
import com.khalti.utils.ActivityUtil;
import com.khalti.utils.EmptyUtil;
import com.khalti.utils.Store;

@Keep
public class KhaltiCheckOut implements KhaltiCheckOutInterface {
    private Context context;

    public KhaltiCheckOut(Context context) {
        this.context = context;
    }

    public KhaltiCheckOut(Context context, Config config) {
        Store.setConfig(config);
        this.context = context;
    }

    @Override
    public void show() {
        if (EmptyUtil.isNull(Store.getConfig())) {
            throw new IllegalArgumentException("Config not set");
        }
        String message = checkConfig(Store.getConfig());
        if (EmptyUtil.isNotNull(message)) {
            throw new IllegalArgumentException(message);
        }
        ActivityUtil.openActivity(CheckOutActivity.class, context, null, true);
    }

    @Override
    public void show(Config config) {
        Store.setConfig(config);
        if (EmptyUtil.isNull(Store.getConfig())) {
            throw new IllegalArgumentException("Config not set");
        }
        String message = checkConfig(Store.getConfig());
        if (EmptyUtil.isNotNull(message)) {
            throw new IllegalArgumentException(message);
        }
        ActivityUtil.openActivity(CheckOutActivity.class, context, null, true);
    }

    @Override
    public void destroy() {
        RxBus.getInstance().post("close_check_out", null);
    }

    private String checkConfig(Config config) {
        if (EmptyUtil.isNull(config.getPublicKey())) {
            return "Public key cannot be null";
        }
        if (EmptyUtil.isEmpty(config.getPublicKey())) {
            return "Public key cannot be empty";
        }
        if (EmptyUtil.isNull(config.getProductId())) {
            return "Product identity cannot be null";
        }
        if (EmptyUtil.isEmpty(config.getProductId())) {
            return "Product identity cannot be empty";
        }
        if (EmptyUtil.isNull(config.getProductName())) {
            return "Product name cannot be null";
        }
        if (EmptyUtil.isEmpty(config.getProductName())) {
            return "Product name cannot be empty";
        }
        if (EmptyUtil.isNull(config.getAmount())) {
            return "Product url cannot be null";
        }
        if (EmptyUtil.isEmpty(config.getAmount())) {
            return "Product url cannot be 0";
        }
        if (EmptyUtil.isNull(config.getOnCheckOutListener())) {
            return "Listener cannot be null";
        }
        return null;
    }
}
