package com.example.kunal.imagesqlitemachinetest;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private int SELECT_FILE = 1;
    private DBHelper dbHelper;
    private AppCompatImageView imgView;
    private Button btn_show;
    //R.id.list
    private RecyclerView recyclerView;
    private CustomAdaptor customAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the Database helper object
        dbHelper = new DBHelper(this);
        setRecyclerView();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
      //  imgView = (AppCompatImageView) findViewById(R.id.imgView);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean result = Utility.checkPermission(MainActivity.this);
                if (result)
                    galleryIntent();
            }
        });



    }

    private void setRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.list);
        customAdaptor = new CustomAdaptor(loadImageFromDB());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(customAdaptor);
    }

    private void setUpdatedRecyclerView() {
        customAdaptor = new CustomAdaptor(loadImageFromDB());
//        customAdaptor.notifyDataSetChanged();
        recyclerView.setAdapter(customAdaptor);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    galleryIntent();
                }
        }
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(intent, SELECT_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {

                    // Saving to Database...
                    if (saveImageInDB(selectedImageUri)) {
                        setUpdatedRecyclerView();
                        Toast.makeText(this, "Image Saved to DB", Toast.LENGTH_SHORT).show();
                    }

//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (loadImageFromDB()) {
//                                Toast.makeText(MainActivity.this, "Image Loaded from DB", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }, 3000);
                }
            }
        }
    }

    Boolean saveImageInDB(Uri selectedImageUri) {

        try {
            dbHelper.open();
            InputStream iStream = getContentResolver().openInputStream(selectedImageUri);
            Log.d("Image name" ,  selectedImageUri.toString());
            byte[] inputData = Utility.getBytes(iStream);
            dbHelper.insertImage(inputData);
            dbHelper.close();
            return true;
        } catch (IOException ioe) {
            Log.e("Main Activity", "<saveImageInDB> Error : " + ioe.getLocalizedMessage());
            dbHelper.close();
            return false;
        }
    }

    public List<byte[]> loadImageFromDB () {
            dbHelper.open();
            List<byte[]> images = dbHelper.retreiveImagesFromDB();
            dbHelper.close();
            // Show Image from DB in ImageView
            //imgView.setImageBitmap(Utility.getImage(bytes));
        return images;
    }

        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);

            return true;
        }
    }
