package com.group_1.usege.utilities.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ActivityUtilities {

    public  static final String TRAN_ACT_BUNDLE = "TRAN_ACT_BUNDLE";
    public static <S> void TransitActivity(Activity currentAct, Class<S> newAct, Bundle bundle, int flags)
    {
        Intent transIntent = new Intent(currentAct, newAct);
        transIntent.putExtra(TRAN_ACT_BUNDLE, bundle);
        if (flags != 0)
            transIntent.setFlags(flags);
        currentAct.startActivity(transIntent);
    }

    public static <S> void TransitActivity(Activity currentAct, Class<S> newAct, Bundle bundle)
    {
        TransitActivity(currentAct, newAct, bundle, 0);
    }

    public static <S> void TransitActivity(Activity currentAct, Class<S> newAct)
    {
        TransitActivity(currentAct, newAct, null, 0);
    }

    public static <S> void TransitActivityAndFinish(Activity currentAct, Class<S> newAct, Bundle bundle)
    {
        TransitActivity(currentAct, newAct, bundle, Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }

    public static <S> void TransitActivityAndFinish(Activity currentAct, Class<S> newAct)
    {
        TransitActivityAndFinish(currentAct, newAct, null);
    }
}
