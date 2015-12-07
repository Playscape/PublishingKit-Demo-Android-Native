package com.playscape.publishingkit;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

/**
 * Created by mac on 9/24/15.
 */
public class BaseActivity extends FragmentActivity {

    public static final String TAG = BaseActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate ends");
    }
}
