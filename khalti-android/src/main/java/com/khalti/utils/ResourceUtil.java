package com.khalti.utils;

import android.content.Context;
import androidx.core.content.ContextCompat;

public class ResourceUtil {
    public static String getString(Context context, int id) {
        return context.getResources().getString(id);
    }

    public static int getColor(Context context, int id) {
        return ContextCompat.getColor(context, id);
    }
}
