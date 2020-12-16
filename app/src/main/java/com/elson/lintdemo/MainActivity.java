package com.elson.lintdemo;

import android.Manifest;
import android.os.Bundle;

import com.tbruyelle.rxpermissions3.RxPermissions;

import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.rxjava3.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    @UiThread
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();

//        Executors.newFixedThreadPool(3).execute(new Runnable() {
//            @Override
//            public void run() {
//                applyPermission();
//            }
//        });

        applyPermission();
    }

//    @UiThread
//    @MainThread
    private void applyPermission() {
        new RxPermissions(this)
                .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Throwable {
                        if (aBoolean) {

                        }
                    }
                });
    }
}