package com.blesh.demo;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.blesh.demo.blesh.BleshHelperService;
import com.blesh.sdk.models.BleshTemplateModel;
import com.blesh.sdk.util.BleshConstant;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class MainActivity extends Activity implements View.OnClickListener {

    private static final int REQUEST_ENABLE_BT = 0;
    private static final int PERMISSION_REQUEST_LOCATION = 2;
    private final String TAG = "MainActivity";

    private void parseTemplateResponse(String response) {


        BleshTemplateModel[] templateModel = null;
        try {
            templateModel = new Gson().fromJson(response, BleshTemplateModel[].class);
        } catch (JsonSyntaxException | JsonIOException e) {
            e.printStackTrace();
        }

        try {
            Intent templateResultIntent = new Intent(BleshConstant.BLESH_MOCK_TEMPLATE);
            templateResultIntent.putExtra(BleshConstant.BLESH_MOCK_TEMPLATE_MODEL, templateModel);
            LocalBroadcastManager.getInstance(this).sendBroadcast(templateResultIntent);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Failed to broadcast template result. Displayed template is null or has no actions!");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button campaignTemplate = (Button) findViewById(R.id.campaign_template);
        Button fullPageTemplate = (Button) findViewById(R.id.full_page_template);
        Button pushNotificationTemplate = (Button) findViewById(R.id.push_notification_template);
        Button adTemplate = (Button) findViewById(R.id.ad_template);
        Button qaTemplate = (Button) findViewById(R.id.qa_template);

        campaignTemplate.setOnClickListener(this);
        fullPageTemplate.setOnClickListener(this);
        pushNotificationTemplate.setOnClickListener(this);
        adTemplate.setOnClickListener(this);
        qaTemplate.setOnClickListener(this);

        Log.d(TAG, "mainActivity called!" + " SDK_INT:"
                + android.os.Build.VERSION.SDK_INT + " CODENAME:"
                + android.os.Build.VERSION.CODENAME + " RELEASE:"
                + android.os.Build.VERSION.RELEASE);

        permissionCheck();
        ensureBluetoothIsEnabled();

        startService(new Intent(this, BleshHelperService.class));
    }

    private void permissionCheck() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect beacons in the background.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                    @TargetApi(23)
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                PERMISSION_REQUEST_LOCATION);
                    }

                });
                builder.show();

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {

            case PERMISSION_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted
                    Toast.makeText(this, "ACCESS_FINE_LOCATION GRANTED", Toast.LENGTH_LONG).show();

                    //startService(new Intent(this, BleshHelperService.class));
                } else {
                    // permission denied
                    Toast.makeText(this, "ACCESS_FINE_LOCATION DENIED", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * Ensures Bluetooth is available on the beacon and it is enabled. If not,
     * displays a dialog requesting user permission to enable Bluetooth.
     */
    private void ensureBluetoothIsEnabled() {
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.w(TAG, "ACTIVITY ONDESTROY");

        stopService(new Intent(this, BleshHelperService.class));
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.w(TAG, "ACTIVITY ONPAUSE");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.w(TAG, "ACTIVITY ONRESUME");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.full_page_template:
                parseTemplateResponse(getString(R.string.full_page_template_response));
                break;

            case R.id.campaign_template:
                parseTemplateResponse(getString(R.string.campaign_template_response));
                break;

            case R.id.push_notification_template:
                parseTemplateResponse(getString(R.string.push_notification_template_response));
                break;

            case R.id.ad_template:
                parseTemplateResponse(getString(R.string.ad_template_response));
                break;

            case R.id.qa_template:
                parseTemplateResponse(getString(R.string.qa_template_response));
                break;
        }
    }
}