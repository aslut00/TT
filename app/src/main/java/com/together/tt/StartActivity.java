package com.together.tt;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class StartActivity extends AppCompatActivity  {

    private long backKeyPressedTime = 0;
    private Toast toast;

    private String[] PERMISSIONS = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.READ_CONTACTS
    };
    int PERMISSION_ALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Network network = new Network();
        network.connect();

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        } else {
            StartThread startThread = new StartThread(handler);
            startThread.start();

        }

    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(msg.what == 1){
                Intent intent = new Intent(StartActivity.this, Start2Activity.class);
                startActivity(intent);
                finish();
            }
        }
    };

    public boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    private void getPermission(){
        ActivityCompat.requestPermissions(this,PERMISSIONS, 1000);
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
            toast.cancel();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (Build.VERSION.SDK_INT >= 23) {

            // requestPermission의 배열의 index가 아래 grantResults index와 매칭
            // 퍼미션이 승인되면
            if(grantResults.length > 0  && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                StartThread startThread = new StartThread(handler);
                startThread.start();
                // TODO : 퍼미션이 승인되는 경우에 대한 코드

            }
            // 퍼미션이 승인 거부되면
            else {
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);

                // TODO : 퍼미션이 거부되는 경우에 대한 코드
            }
        }
    }

}
