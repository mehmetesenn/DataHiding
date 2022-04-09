package com.mehmetesen.encryption;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
}