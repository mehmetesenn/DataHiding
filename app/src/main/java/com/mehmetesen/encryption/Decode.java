package com.mehmetesen.encryption;





import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.ayush.imagesteganographylibrary.Text.AsyncTaskCallback.TextDecodingCallback;
import com.ayush.imagesteganographylibrary.Text.ImageSteganography;
import com.ayush.imagesteganographylibrary.Text.TextDecoding;

import java.io.IOException;

public class Decode extends AppCompatActivity implements TextDecodingCallback {

    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "Decode Class";
    //Initializing the UI components
    private ImageView imageView;
    private EditText message;
    private EditText secret_key;
    private Uri filepath;
    //Bitmap
    private Bitmap original_image;
    Button choose_image_button;
    Button decode_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decode);

        //Instantiation of UI components

        imageView = findViewById(R.id.decodeimageview);

        message = findViewById(R.id.messagedecode);
        secret_key = findViewById(R.id.secretkeydecode);

         choose_image_button = findViewById(R.id.button15);
         decode_button = findViewById(R.id.button6);


        //Choose Image Button
        choose_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageChooser();
            }
        });

        //Decode Button
        decode_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (filepath != null) {

                        //Making the ImageSteganography object
                        ImageSteganography imageSteganography = new ImageSteganography(secret_key.getText().toString(),
                                original_image);

                        //Making the TextDecoding object
                        TextDecoding textDecoding = new TextDecoding(Decode.this, Decode.this);

                        //Execute Task
                        textDecoding.execute(imageSteganography);
                    }

            }
        });


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

            } catch (IOException e) {
                Log.d(TAG, "Error : " + e);
            }
        }

    }

    @Override
    public void onStartTextEncoding() {
        //Whatever you want to do by the start of textDecoding
    }

    @Override
    public void onCompleteTextEncoding(ImageSteganography result) {

        //By the end of textDecoding

        if (result != null) {
            if (!result.isDecoded()){
                message.setText("No message found");
            }else {
                if (!result.isSecretKeyWrong()) {
                    AlertDialog.Builder decodebuilder = new AlertDialog.Builder(Decode.this);
                    decodebuilder.setTitle("Message inside the picture");
                    decodebuilder.setIcon(R.drawable.encryptionicon);
                    decodebuilder.setMessage(result.getMessage());
                    decodebuilder.setCancelable(false);
                    decodebuilder.setPositiveButton("Copy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ClipboardManager manager1 = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                            ClipData data1 = ClipData.newPlainText("result",result.getMessage());
                            manager1.setPrimaryClip(data1);
                            Toast.makeText(Decode.this, "Copied", Toast.LENGTH_LONG).show();
                            Intent intent =new Intent(Decode.this,MainActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    });
                    decodebuilder.show();



                } else {
                    message.setText("Wrong secret key");
                }
            }
        } else {
            message.setText("Select Image First");
        }


    }
}
