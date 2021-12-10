package com.example.picturenotes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    boolean searched;
    RecyclerView recyclerView;
    FloatingActionButton clickImage;
    List<Post> posts;
    private final int LoadDataCode = 100;
    EditText searchCategoryText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.staggeredGridView);
        searchCategoryText = findViewById(R.id.categorySearchText);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        posts = new ArrayList<Post>();
        populatePost();

        //floating action button code
        clickImage = findViewById(R.id.clickImageButton);
        clickImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                callNewImage();
            }
        });

    }

    private void callNewImage() {
        Intent intent = new Intent(this,ClickImage.class);
        startActivityForResult(intent,LoadDataCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LoadDataCode && resultCode == RESULT_OK)
        {
            populatePost();
        }
    }

    private void populatePost()
    {
        searched = false;
        postDatabase database = new postDatabase(getApplicationContext());
        posts = database.getItems();
        recyclerView.setAdapter(new staggeredAdapter(this,posts));
        searchCategoryText.setText("");
    }

    public void searchCategory(View view){
        if(searchCategoryText.getText().toString().trim().equals(""))
        {
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
        }
        else
        {
            searched = true;
            postDatabase database = new postDatabase(getApplicationContext());
            posts = database.getSpecificItems(searchCategoryText.getText().toString().trim());
            recyclerView.setAdapter(new staggeredAdapter(this,posts));
        }
    }

    @Override
    public void onBackPressed() {
        if(searched == true)
        {
            populatePost();
        }
        else
        {
            super.onBackPressed();
        }

    }
}