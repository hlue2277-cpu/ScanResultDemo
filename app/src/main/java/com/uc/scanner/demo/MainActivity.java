package com.uc.scanner.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    IntentFilter mScanResultIntentFilter;
    private EditText edit_scan_result_broadcast_mode_bytes;
    private EditText edit_scan_result_broadcast_mode_characters;
    private Button btn_start_scan;
    private Button btn_stop_scan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerScanResultReceiver();

        edit_scan_result_broadcast_mode_bytes = (EditText)findViewById(R.id.edit_scan_result_broadcast_mode_bytes);
        edit_scan_result_broadcast_mode_characters = (EditText)findViewById(R.id.edit_scan_result_broadcast_mode_characters);
        btn_start_scan = (Button)findViewById(R.id.btn_start_scan);
        btn_stop_scan = (Button)findViewById(R.id.btn_stop_scan);

        initListener();
    }

    private void registerScanResultReceiver() {
        //注册接收扫码结果的广播
        mScanResultIntentFilter = new IntentFilter("com.uc.scanner.result"); //"com.uc.scanner.result"这个action值 对应 "扫描助手App"广播模式的广播action
        registerReceiver(scanResultReceiver, mScanResultIntentFilter);
    }


    //接收扫码结果的广播监听器
    private BroadcastReceiver scanResultReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            edit_scan_result_broadcast_mode_bytes.setText("");
            edit_scan_result_broadcast_mode_characters.setText("");

            byte[] byteArrays = intent.getByteArrayExtra("byteArray"); //"byteArray"这个key值 对应 "扫描助手App"广播模式的字节数组数据key
            String resultString = intent.getStringExtra("string");   //"string"这个key值 对应 "扫描助手App"广播模式的字符串数据key

            edit_scan_result_broadcast_mode_bytes.setText(Arrays.toString(byteArrays));
            edit_scan_result_broadcast_mode_characters.setText(resultString);

            Log.d("TAG","===Demo接收到扫码结果byteArray:" + Arrays.asList(byteArrays));
            Log.d("TAG","===Demo接收到扫码结果string:" + resultString);
        }
    };


    private void initListener() {
        btn_start_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.uc.scanner.trigger.START");//"com.uc.scanner.trigger.START"这个action值 对应 "扫描助手App"设置启动广播的action
                sendBroadcast(intent);
            }
        });

        btn_stop_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.uc.scanner.trigger.STOP");//"com.uc.scanner.trigger.STOP"这个action值 对应 "扫描助手App"设置启动广播的action
                sendBroadcast(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(scanResultReceiver != null) {
            unregisterReceiver(scanResultReceiver);
        }

    }
}