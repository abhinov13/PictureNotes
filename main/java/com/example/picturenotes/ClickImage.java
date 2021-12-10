package com.example.picturenotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClickImage extends AppCompatActivity {

    static final int CLICK_IMAGE_REQ_CODE = 1;
    ImageView imageView;
    File photoFile;
    String photoPath;
    Uri uri;
    EditText editText,category;
    Button button;
    boolean imageClicked;
    String absolutePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_image2);
        imageView = findViewById(R.id.imageView);
        photoFile = null;
        photoPath = "";
        editText = findViewById(R.id.noteInput);
        button = findViewById(R.id.submitNote);
        imageClicked = false;
        category = findViewById(R.id.category);

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if(imageClicked == false)
                {
                    Toast.makeText(getApplicationContext(),"Please click an image to save",Toast.LENGTH_SHORT).show();
                }
                else if(category.getText().toString().trim().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Category is empty", Toast.LENGTH_SHORT).show();
                }
                else if(editText.getText().toString().trim().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Note is empty",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    postDatabase database = new postDatabase(getApplicationContext());
                    database.insertData(absolutePath,editText.getText().toString(), category.getText().toString());
                    Intent intent = new Intent();
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                clickImage();
            }
        });
    }

    public  void clickImage()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
        else
        {
            Intent click = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            uri = FileProvider.getUriForFile(this,"com.example.picturenotes.FileProvider",photoFile);
            click.putExtra(MediaStore.EXTRA_OUTPUT,uri);
            startActivityForResult(click,CLICK_IMAGE_REQ_CODE);
        }
    }

    public File createImageFile() throws IOException
    {
        String name = ("IMG_" + new SimpleDateFormat("yyMMdd_HHmmss").format(new Date())) + "_";

        File directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = new File(directory, name + ".jpg");
        photoPath = file.getAbsolutePath();
        return file;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                clickImage();
            } else {
                Toast.makeText(this, "Permissions not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CLICK_IMAGE_REQ_CODE && resultCode == RESULT_OK) {
            imageClicked = true;
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            Bitmap image = BitmapFactory.decodeFile(photoFile.getAbsolutePath(),bitmapOptions);
            imageView.setImageBitmap(image);
            String name = ("IMG_" + new SimpleDateFormat("yyMMdd_HHmmss").format(new Date()));
            File directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File file = new File(directory,name+".jpg");
            try {
                FileOutputStream outFile = new FileOutputStream(file.getAbsolutePath());
                absolutePath = file.getAbsolutePath();
                image.compress(Bitmap.CompressFormat.JPEG,100,outFile);
                outFile.flush();
                outFile.close();
                photoFile.delete();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }



        } else {
            Toast.makeText(this, "Picture not clicked", Toast.LENGTH_SHORT).show();
        }
    }
}