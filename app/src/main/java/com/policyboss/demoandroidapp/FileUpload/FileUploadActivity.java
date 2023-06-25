package com.policyboss.demoandroidapp.FileUpload;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.policyboss.demoandroidapp.R;
import com.policyboss.demoandroidapp.Utility.Utility;
import com.policyboss.demoandroidapp.databinding.ActivityFileUploadBinding;

public class FileUploadActivity extends AppCompatActivity {

    ActivityFileUploadBinding binding;
    ActivityResultLauncher<String> cropImage;
    ActivityResultLauncher<Uri> cameraLauncher;
    Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFileUploadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        cropImage = registerForActivityResult(new ActivityResultContracts.GetContent(),result ->  {

            Intent intent = new Intent(FileUploadActivity.this.getApplicationContext(),UcropperActivity.class);

             intent.putExtra("SendImageData",result.toString());

             startActivityForResult(intent, 100);
        });

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
            if (result) {
               // binding.imgProfile.setImageURI(imageUri);

                Intent intent = new Intent(FileUploadActivity.this.getApplicationContext(),UcropperActivity.class);

                intent.putExtra("SendImageData",imageUri.toString());


                startActivityForResult(intent, 102);
            } else {
                // Handle failure or cancellation
            }
        });


        binding.btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openCamera();
            }
        });

        binding.btnFileUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cropImage.launch("image/*");
            }
        });
    }

    private void   openCamera(){

        imageUri = Utility.createImageUri(this.getApplicationContext());

        cameraLauncher.launch(imageUri);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== 100 && resultCode ==101 ){

            String result = data.getStringExtra("CROP");
            Uri uri = data.getData();

            if(result!= null){
                uri = Uri.parse(result);
            }

            binding.imgProfile.setImageURI(uri);

        }
        else if(requestCode== 102 && resultCode ==101 ){

            String result = data.getStringExtra("CROP");
            Uri uri = data.getData();

            if(result!= null){
                uri = Uri.parse(result);
            }

            binding.imgProfile.setImageURI(uri);

        }
    }
}