package com.tole.taba;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;


public class FragmentOne extends Fragment {

    DBHandler myDb;
    EditText cardText;
    Button findButton;

    public FragmentOne() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_one, container, false);

        myDb = new DBHandler(this.getActivity());
        cardText = (EditText) view.findViewById(R.id.cardText);
        cardText.setTypeface(Typeface.SANS_SERIF);

        findButton = (Button) view.findViewById(R.id.findButton);
        findButton.setBackgroundResource(R.drawable.hover);


        //addData("tole", "Dineth Madusara", "0717504859", "ela kollek", "/mnt/sdcard/yaka.jpg", "dineth454@gmail.com", "1992 10 10");
        //addData("pulli", "Poornima Karunarathna", "0717504859", "meki henama chora", "/mnt/sdcard/abc.jpg", "pooh454@gmail.com", "1992 05 13");
        viewData();

        return view;
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

    public void viewData(){
        findButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Cursor res = myDb.getData(cardText.getText().toString());

                        if (res.getCount() == 0) {
                            //message
                            showMessage("Error", "nothing found");
                            return;
                        }

                        StringBuffer card = new StringBuffer();
                        StringBuffer name = new StringBuffer();
                        StringBuffer desc = new StringBuffer();
                        StringBuffer tpNo = new StringBuffer();
                        if (res.moveToNext()) {
                            //buffer.append("ID : " + res.getString(0) + "\n");
                            card.append(res.getString(1));
                            name.append(res.getString(2));
                            byte[] image = res.getBlob(5);
                            desc.append(res.getString(4));
                            tpNo.append(res.getString(3));

                            passMessage(card.toString(), name.toString(), image, desc.toString(), tpNo.toString());
                        }
                        //showMessage("????? ???..", buffer.toString());
                        //passMessage(card.toString(), age.toString(), String.valueOf(image).getBytes());
                    }
                }
        );
    }

    public void passMessage(String card, String name, byte[] image, String desc, String tpNo){
        Bundle bucket = new Bundle();
        bucket.putString("card", card);
        bucket.putString("name", name);
        bucket.putByteArray("image", image);
        bucket.putString("desc", desc);
        bucket.putString("tpNo", tpNo);
        Intent i = new Intent(this.getActivity(), Results.class);
        i.putExtras(bucket);
        startActivity(i);
    }

    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNeutralButton("හරි..", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
}