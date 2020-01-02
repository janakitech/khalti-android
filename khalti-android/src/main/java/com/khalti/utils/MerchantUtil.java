package com.khalti.utils;

import androidx.fragment.app.Fragment;

import java.util.HashMap;

import com.khalti.R;
import com.khalti.checkOut.banking.Banking;
import com.khalti.checkOut.helper.PaymentPreference;
import com.khalti.checkOut.wallet.Wallet;

public class MerchantUtil {

    private static final HashMap<String, Fragment> CHECKOUT_FRAGMENTS = new HashMap<String, Fragment>() {{
        put(PaymentPreference.EBANKING.getValue(), new Banking());
        put(PaymentPreference.WALLET.getValue(), new Wallet());
    }};

    private static final HashMap<String, String> CHECKOUT_TITLES = new HashMap<String, String>() {{
        put(PaymentPreference.EBANKING.getValue(), "E-Banking");
        put(PaymentPreference.WALLET.getValue(), "Wallet");
    }};

    private static final HashMap<String, Integer> CHECKOUT_ICONS = new HashMap<String, Integer>() {{
        put(PaymentPreference.EBANKING.getValue(), R.drawable.ic_ebanking);
        put(PaymentPreference.WALLET.getValue(), R.drawable.ic_wallet);
    }};

    private static final HashMap<String, Integer> CHECKOUT_ICONS_ACTIVE = new HashMap<String, Integer>() {{
        put(PaymentPreference.EBANKING.getValue(), R.drawable.ic_ebanking_active);
        put(PaymentPreference.WALLET.getValue(), R.drawable.ic_wallet_active);
    }};

    public static HashMap<String, Object> getTab(String key) {
        return CHECKOUT_FRAGMENTS.containsKey(key) && CHECKOUT_TITLES.containsKey(key) && CHECKOUT_ICONS.containsKey(key) ?
                new HashMap<String, Object>() {{
                    put("fragment", CHECKOUT_FRAGMENTS.get(key));
                    put("title", CHECKOUT_TITLES.get(key));
                    put("icon", CHECKOUT_ICONS.get(key));
                    put("icon_active", CHECKOUT_ICONS_ACTIVE.get(key));
                }} : null;
    }
}