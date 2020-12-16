package com.elson.biz1;

import android.Manifest;
import android.os.Bundle;
import android.widget.Toast;

import com.tbruyelle.rxpermissions3.RxPermissions;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.rxjava3.functions.Consumer;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        Toast.makeText(this, "Elson", Toast.LENGTH_SHORT);

//        Log.d("MainActivity", "onCreate");

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