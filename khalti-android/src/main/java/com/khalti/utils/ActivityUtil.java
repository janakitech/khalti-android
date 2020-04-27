package com.khalti.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.HashMap;

public class ActivityUtil {

    public static void openActivity(Class className, Context context, HashMap<String, ?> data, boolean animate) {
        Intent intent = new Intent(context, className);
        if (animate) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (EmptyUtil.isNotNull(data) && EmptyUtil.isNotEmpty(data)) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("map", data);
            intent.putExtra("bundle", bundle);
        }

        context.startActivity(intent);
    }
}
