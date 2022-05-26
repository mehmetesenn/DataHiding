package com.mehmetesen.encryption;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ayush.imagesteganographylibrary.Text.AsyncTaskCallback.TextEncodingCallback;
import com.ayush.imagesteganographylibrary.Text.ImageSteganography;
import com.ayush.imagesteganographylibrary.Text.TextEncoding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class Encode extends AppCompatActivity implements TextEncodingCallback {

    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "Encode Class";
    //Created variables for UI
    private ImageView imageView;
     private EditText message;
     private EditText secret_key;
    //Objects needed for encoding
    private TextEncoding textEncoding;
    private ImageSteganography imageSteganography;
    private Uri filepath;
    //Bitmaps
    private Bitmap original_image;
    public static Bitmap encoded_image;
    private String messageToEncode;
    private String secretkeyEncode;
    private Button buttonencode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encode);
        //initialized the UI components
        imageView = findViewById(R.id.imageVieworiginal);
        message = findViewById(R.id.message);
        secret_key = findViewById(R.id.secretkey);

        Button choose_image_button = findViewById(R.id.button3);
        buttonencode = findViewById(R.id.buttonEncode);
        checkAndRequestPermissions();
        buttonencode.setClickable(false);

    }

    private void ImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Image set to imageView
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filepath = data.getData();
            try {
                original_image = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);

                imageView.setImageBitmap(original_image);
              buttonencode.setClickable(true);
            } catch (IOException e) {
                Log.d(TAG, "Error : " + e);
            }
        }

    }

    // Override method of TextEncodingCallback

    @Override
    public void onStartTextEncoding() {
        //Whatever you want to do at the start of text encoding

    }

    @Override
    public void onCompleteTextEncoding(ImageSteganography result) {

        //By the end of textEncoding

        if (result != null && result.isEncoded()) {
            encoded_image = result.getEncoded_image();
            imageView.setImageBitmap(encoded_image);
            //alertdialog
            AlertDialog.Builder builder = new AlertDialog.Builder(Encode.this);
            builder.setTitle("Resme mesaj gizleme");
            builder.setIcon(R.drawable.encryptionicon);
            builder.setCancelable(false);
            builder.setMessage("Mesajınız resmin içerisine başarıyla gizlendi");
            builder.setPositiveButton("Resmi Kaydet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    saveToInternalStorage(encoded_image);
                    Intent intent = new Intent(Encode.this,EncodeShare.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(Encode.this, "başarıyla indirilenler klasörüne kaydedildi", Toast.LENGTH_LONG).show();

                }
            });
            builder.show();
        }
    }

    private void saveToInternalStorage(Bitmap bitmapImage) {
        OutputStream outputStream = null;
         // the File to save
        File galerypath;
        galerypath=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File dir;
        dir = new File(galerypath.getAbsolutePath()+"/Steganografi/");
        dir.mkdir();
        File dest;
        dest = new File(dir,System.currentTimeMillis()+".PNG");
        try {
            outputStream = new FileOutputStream(dest);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmapImage.compress(Bitmap.CompressFormat.PNG,100,outputStream);
        try {
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void checkAndRequestPermissions() {
        int permissionWriteStorage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int ReadPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (ReadPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissionWriteStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[0]), 1);
        }
    }


    public void choose(View view) {
        ImageChooser();
    }

    public void encode(View view) {
        messageToEncode=message.getText().toString();
        secretkeyEncode = secret_key.getText().toString();
        if(messageToEncode != null && secretkeyEncode !=null){
            imageSteganography = new ImageSteganography(message.getText().toString(),
                    secret_key.getText().toString(),
                    original_image);
            //TextEncoding object Instantiation
            textEncoding = new TextEncoding(Encode.this, Encode.this);
            //Executing the encoding
            textEncoding.execute(imageSteganography);


        }else{
            Toast.makeText(this, "an unknown error occurred", Toast.LENGTH_SHORT).show();


        }



    }
}


