package com.blesh.demo.blesh;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.blesh.sdk.common.BleshIntent;
import com.blesh.sdk.util.BleshConstant;

public class BleshHelperService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        isHostAppRunning(true);

        startBlesh("your_api_user", "your_api_key", "your_integration_id");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        isHostAppRunning(false);

        stopSelf();
    }

    public void startBlesh(String apiUser, String apiKey, String integrationId) {
          try {
            startService(new BleshIntent.Builder(apiUser, apiKey, integrationId).integrationType("M").optionalKey("").getIntent(this));
        } catch (IllegalArgumentException e) {
             e.printStackTrace();
        }
    }

    public void stopBlesh() {
        Intent intent = new Intent(BleshConstant.BROADCAST_CHANGE_STATE_BLESH);
        intent.putExtra(BleshConstant.BROADCAST_STOP_BLESH, true);
        intent.putExtra(BleshConstant.BLESH_CANCEL_AUTOSTART, true);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    private void isHostAppRunning(boolean status) {
        Intent intent = new Intent(BleshConstant.BLESH_HOST_APP);
        intent.putExtra(BleshConstant.BLESH_HOST_APP_RUNNING_STATUS, status);
        sendBroadcast(intent);
    }
}
