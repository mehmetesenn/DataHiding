package com.mehmetesen.encryption;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }

    public void Encode(View view) {
        Intent intent = new Intent(MainActivity.this,Encode.class);
        startActivity(intent);
        finish();

    }

    public void Decode(View view) {
        Intent intent = new Intent(MainActivity.this,Decode.class);
        startActivity(intent);
        finish();
    }

    public void Restart(View view) {
        Intent intent = new  Intent(MainActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void Settings(View view) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
        finish();
    }

    public void Developer(View view) {
        //url https://tr.linkedin.com/in/mehmet-esenn
        Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://tr.linkedin.com/in/mehmet-esenn"));
        startActivity(intent);
        finish();

    }


    public void Exit(View view) {
        finish();
    }
}