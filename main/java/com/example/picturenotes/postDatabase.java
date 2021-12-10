package com.example.picturenotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class postDatabase extends SQLiteOpenHelper
{

    private static final int version = 1;
    private static final String dbName = "pinDB";
    private static final String tableName = "post";
    private static final String img_uri = "img_uri";
    private static final String category = "category";
    private static final String note = "note";

    public postDatabase(Context context)
    {
        super(context,dbName,null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableItem = "CREATE TABLE " + tableName + "("+ img_uri + " TEXT PRIMARY KEY, " + category + " TEXT, " + note + " TEXT)";
        Log.i("creating table",createTableItem);
        sqLiteDatabase.execSQL(createTableItem);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String drop = "DROP TABLE IF EXISTS " + tableName;
        sqLiteDatabase.execSQL(drop);
        onCreate(sqLiteDatabase);
    }

    public void insertData(String img_uri_,String note_,String category_)
    {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(img_uri,img_uri_);
        values.put(note,note_);
        values.put(category,category_);
        Log.i("category",category_);
        database.insert(tableName,null,values);
        database.close();
    }

    public List<Post> getItems()
    {
        List<Post> list= new ArrayList<Post>();
        String query = "SELECT * FROM " + tableName;
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery(query,null);
        if(cursor.moveToFirst())
        {
            do {
                //index 0 is image_uri, index 1 is category and index 2 is note attached
                list.add(new Post(cursor.getString(0),cursor.getString(2),cursor.getString(1)));
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        database.close();

        return list;
    }

    public List<Post> getSpecificItems(String category_)
    {
        List<Post> list= new ArrayList<Post>();
        String query = "SELECT * FROM " + tableName + " WHERE " + category + " = \"" + category_ + "\"";
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery(query,null);
        if(cursor.moveToFirst())
        {
            do {
                //index 0 is image_uri, index 1 is category and index 2 is note attached
                list.add(new Post(cursor.getString(0),cursor.getString(2),cursor.getString(1)));
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        database.close();

        return list;
    }
    public void update(String img_uri_,String note_)
    {
        SQLiteDatabase database = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(note,note_);
        String[] whereArgs = {String.valueOf(img_uri_)};
        database.update(tableName, values, img_uri +"=?", whereArgs );

        database.close();
    }

    public void delete(String img_uri_)
    {
        SQLiteDatabase database = getReadableDatabase();
        String[] whereArgs = {String.valueOf(img_uri_)};
        database.delete(tableName,img_uri +"=?",whereArgs);

        database.close();
    }
}