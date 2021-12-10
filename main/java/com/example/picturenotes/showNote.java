package com.example.picturenotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class showNote extends AppCompatActivity {

    ImageView showImage;
    TextView showNote;
    Button edit;
    EditText editText;
    boolean editing;
    String imagePath;
    String note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_note);

        showNote = findViewById(R.id.showNote);
        showImage = findViewById(R.id.showImage);
        edit = findViewById(R.id.editNote);
        editing = false;
        editText = findViewById(R.id.edit);

        Intent intent = getIntent();
        imagePath = intent.getStringExtra("imageSrc");
        note = intent.getStringExtra("Note");

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        Bitmap image = BitmapFactory.decodeFile(imagePath,bitmapOptions);
        showImage.setImageBitmap(image);
        showNote.setText(note);

        Intent backintent = new Intent();
        setResult(RESULT_OK,intent);
    }

    public void EditNote(View view)
    {
        if(editing == false)
        {
            editing = true;
            edit.setText("Save");
            editText.setVisibility(View.VISIBLE);
            showNote.setVisibility(View.INVISIBLE);
        }
        else
        {
            if(editText.getText().toString().trim().length() > 0)
            {
                editing = true;
                edit.setText("Edit");
                editText.setVisibility(View.INVISIBLE);
                showNote.setVisibility(View.VISIBLE);

                //handle the input from edittext
                postDatabase database = new postDatabase(this);
                String text = editText.getText().toString().trim();
                database.update(imagePath,text);
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        }
    }
}