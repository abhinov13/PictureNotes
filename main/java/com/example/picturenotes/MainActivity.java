package com.example.picturenotes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton clickImage;
    List<Post> posts;
    private final int LoadDataCode = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.staggeredGridView);
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
        postDatabase database = new postDatabase(getApplicationContext());
        posts = database.getItems();
        recyclerView.setAdapter(new staggeredAdapter(this,posts));
    }
}