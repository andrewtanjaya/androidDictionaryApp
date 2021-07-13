package edu.bluejack20_1.SOwhaDZ;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.Objects;

public class OCRActivity extends AppCompatActivity {
    String cameraPermission[];
    String storagePermission[];
    private ImageView ocrImage;
    private TextView title;
    private Button btnTranslate, btnImage, btnCamera, btnBack;
    private Context context;
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 400;
    private static final int IMAGE_PICK_GALLERY_CODE = 1000;
    private static final int IMAGE_PICK_CAMERA_CODE = 1001;
    Uri image_uri;
    public static String resultTxt="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (SplashScreen.sharedRef.loadNighModeState()) {
            setTheme(R.style.darkTheme);
        } else setTheme(R.style.lightTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_c_r);

        ocrImage = findViewById(R.id.ocr_image);
        title = findViewById(R.id.ocr_title);
        btnTranslate = findViewById(R.id.ocr_translate_button);
        btnCamera = findViewById(R.id.ocr_camera_button);
        btnImage = findViewById(R.id.ocr_image_button);

        cameraPermission = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        btnBack = findViewById(R.id.ocr_back_button);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OCRActivity.this, MainActivity.class);
                intent.putExtra("state", "translate");
                startActivity(intent);
                finish();
            }
        });


//      translated result here
        btnTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                translate button
                Intent intent = new Intent(OCRActivity.this, MainActivity.class);
                intent.putExtra("state", "translate");
                startActivity(intent);
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentTranslate()).commit();
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              camera button
                if(!checkCameraPermission()){
                    //g ada izin kamera
                    Toast.makeText(OCRActivity.this,"No permission",Toast.LENGTH_SHORT).show();
                    requestCameraPermission();
                }else{
                    //buka kamera

                    pickCamera();
                }
            }
        });

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                image button
                //gallery option
                if(!checkStoragePermission()){
                    //g ada izin storage
                    requestStoragePermission();
                }else{
//                    Toast.makeText(OCRActivity.this,"ada permisi Galeri",Toast.LENGTH_SHORT).show();
                    // buka galeri
                    pickGallery();
                }
            }
        });


        }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){

            if (requestCode == IMAGE_PICK_GALLERY_CODE){
                //dpt gambar dari galeri tinggal crop

                CropImage.activity(data.getData()).setGuidelines(CropImageView.Guidelines.ON).start(this); // biar ad gridnya

            }
            if(requestCode == IMAGE_PICK_CAMERA_CODE){
                //dapat gambar dari kamera
                CropImage.activity(image_uri).setGuidelines(CropImageView.Guidelines.ON).start(this); // biar ad gridnya
//                Toast.makeText(this,"Camera",Toast.LENGTH_SHORT).show();
            }

            if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if(resultCode == RESULT_OK){
                    Uri resultUri = result.getUri(); // dpt uri image
                    ocrImage.setImageURI(resultUri);

                    // pake drawable bitmap untuk text recog
                    BitmapDrawable bitmapDrawable = (BitmapDrawable)ocrImage.getDrawable();
                    Bitmap bitmap = bitmapDrawable.getBitmap();

                    TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();

                    if(!recognizer.isOperational()){
                        Toast.makeText(this,"Error", Toast.LENGTH_SHORT).show();
                    }else{
                        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                        SparseArray<TextBlock> items = recognizer.detect(frame);
                        StringBuilder sb = new StringBuilder();
                        for(int i=0; i<items.size(); i++){
                            TextBlock myItem = items.valueAt(i);
                            sb.append(myItem.getValue());
                        }
                        title.setText(sb.toString());
                        resultTxt = title.getText().toString();
                    }
                }
                else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                    Exception e = result.getError();
                    Toast.makeText(this,""+e,Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    public void showImageImportedDialog(){
        String[] items = {"Camera","Gallery"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(OCRActivity.this);
        dialog.setTitle("Select Image");
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    //camera option

                }
                if(which == 1){

                }
            }
        });
        dialog.create().show();
    }

    private void pickGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_GALLERY_CODE);

    }

    private void pickCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"New Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image to Text");

        image_uri = OCRActivity.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }


    private boolean checkStoragePermission() {
        boolean result1  = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result1;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(OCRActivity.this,storagePermission,STORAGE_REQUEST_CODE);
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(OCRActivity.this,cameraPermission,CAMERA_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {

        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1  = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA_REQUEST_CODE:
                if(grantResults.length > 0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && writeStorageAccepted){
                        pickCamera();
                    }
                    else{
                        Toast.makeText(this,"Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case STORAGE_REQUEST_CODE:
                if(grantResults.length > 0){

                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(writeStorageAccepted){
                        pickGallery();
                    }
                    else{
                        Toast.makeText(this,"Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}