package com.mehmetesen.encryption;

import static com.mehmetesen.encryption.Encode.encoded_image;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;


public class EncodeShare extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    ImageView homepage,share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encode_share);

        mediaPlayer = MediaPlayer.create(EncodeShare.this,R.raw.fireworks);
        mediaPlayer.start();
        homepage=findViewById(R.id.ImageviewHomePage);
        share=findViewById(R.id.ImageviewShare);


    }

    public void share(View view) {
        try{
            mediaPlayer.stop();
            String path = MediaStore.Images.Media.insertImage(getContentResolver(),encoded_image,"Image Description",null);
            Uri uri = Uri.parse(path);
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/png");
            share.putExtra(Intent.EXTRA_STREAM,uri);
            startActivity(Intent.createChooser(share, "encoded image share "));
        }catch (Exception e){
            e.printStackTrace();

        }
    }

    public void homepage(View view) {
        Intent intent = new Intent(EncodeShare.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}