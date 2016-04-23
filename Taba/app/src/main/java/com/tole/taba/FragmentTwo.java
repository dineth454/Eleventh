package com.tole.taba;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import javax.xml.transform.Result;

public class FragmentTwo extends Fragment{

    static final int REQUEST_IMAGE_CAPTURE = 1;

    static String imagePath = "";

    //sobana process bar=============
    private ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarbHandler = new Handler();
    private long fileSize = 0;
    //===============================

    DBHandler myDb;
    EditText nameEditText;
    EditText cardEditText;
    EditText tpEditText;
    EditText emailEditText;
    EditText bdayEditText;
    EditText descEditText;
    Button captureButton;
    Button doneButton;
    ImageView photoImageView;

    public FragmentTwo() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_two, container, false);

        myDb = new DBHandler(this.getActivity());
        nameEditText = (EditText) view.findViewById(R.id.nameEditText);
        cardEditText = (EditText) view.findViewById(R.id.cardEditText);
        tpEditText = (EditText) view.findViewById(R.id.tpEditText);
        emailEditText = (EditText) view.findViewById(R.id.emailEditText);
        bdayEditText = (EditText) view.findViewById(R.id.bdayEditText);
        descEditText = (EditText) view.findViewById(R.id.descEditText);

        captureButton = (Button) view.findViewById(R.id.captureButton);
        doneButton = (Button) view.findViewById(R.id.doneButton);

        photoImageView = (ImageView) view.findViewById(R.id.photoImageView);

        captureButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //if (!hasCamera()) {
                //    captureButton.setEnabled(false);
                //}
                launchCamera(v);

            }
        });


        doneButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                addData(cardEditText.getText().toString(),
                        nameEditText.getText().toString(),
                        tpEditText.getText().toString(),
                        descEditText.getText().toString(),
                        imagePath,
                        emailEditText.getText().toString(),
                        bdayEditText.getText().toString());

                //sobana=========================================
                progressBar = new ProgressDialog(v.getContext());
                progressBar.setCancelable(true);
                progressBar.setMessage(cardEditText.getText().toString() + " is saving ...");
                progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressBar.setProgress(0);
                progressBar.setMax(100);
                progressBar.show();
                progressBarStatus = 0;

                fileSize = 0;
                new Thread(new Runnable() {
                    public void run() {
                        while (progressBarStatus < 100) {
                            progressBarStatus = 100;

                            try {
                                Thread.sleep(400);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            progressBarbHandler.post(new Runnable() {
                                public void run() {
                                    progressBar.setProgress(progressBarStatus);
                                }
                            });
                        }

                        if (progressBarStatus >= 100) {
                            try {
                                Thread.sleep(800);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            progressBar.dismiss();
                        }
                    }
                }).start();
                //========================================================

                // handler daala late karana eka vitharai wenne
                // atthatama meke thiyenne
                // 1. add()  2.clearContent() and toast eka vitharai ban :)
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        clearContent();
                        Toast.makeText(getActivity(), "හරි බන් ...!!", Toast.LENGTH_SHORT).show();
                    }
                }, 1100);



            }
        });

        return view;
    }

    public boolean hasCamera(){
        return getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    public void launchCamera(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");
            photoImageView.setImageBitmap(photo);

            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            Uri tempUri = getImageUri(getActivity().getApplicationContext(), photo);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            String path = getRealPathFromURI(tempUri);
            imagePath = path;
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public void addData(String card, String name, String tpno, String desc, String path, String email, String bday){
        try {
            FileInputStream fis = new FileInputStream(path);
            byte[] image = new byte[fis.available()];
            fis.read(image);

            myDb.insertData(card, name, tpno, desc, image, email, bday);
            fis.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public void clearContent(){
        cardEditText.setText(null);
        nameEditText.setText(null);
        tpEditText.setText(null);
        descEditText.setText(null);
        imagePath = "";
        emailEditText.setText(null);
        bdayEditText.setText(null);

        photoImageView.setImageResource(R.drawable.abc);

    }



}
