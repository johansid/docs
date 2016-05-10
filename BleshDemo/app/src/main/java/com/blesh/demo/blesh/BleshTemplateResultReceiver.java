package com.blesh.demo.blesh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.blesh.sdk.util.BleshConstant;

public class BleshTemplateResultReceiver extends BroadcastReceiver {
    private static final String TAG = "BleshTemplate";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(BleshConstant.BLESH_TEMPLATE_RESULT_ACTION)) {
            Log.w(TAG, "received action type:" + intent.getStringExtra(BleshConstant.BLESH_ACTION_TYPE));
            Log.w(TAG, "received action value:" + intent.getStringExtra(BleshConstant.BLESH_ACTION_VALUE));

            // TODO - Your custom action here
        }
    }
}