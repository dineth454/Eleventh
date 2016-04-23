package com.tole.taba;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.gsm.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class Results extends AppCompatActivity {

    Button callButton;
    Button smsButton;
    Bundle homeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");

        homeData = getIntent().getExtras();
        if(homeData==null){
            return;
        }

        String card = homeData.getString("card");
        String name = homeData.getString("name");
        byte[] image = homeData.getByteArray("image");
        String desc = homeData.getString("desc");
        String tpNo = homeData.getString("tpNo");

        TextView resultText = (TextView) findViewById(R.id.resultText);
        TextView resultText2 = (TextView) findViewById(R.id.resultText2);
        TextView resultText3 = (TextView) findViewById(R.id.resultText3);
        TextView resultText5 = (TextView) findViewById(R.id.resultText5);
        ImageView toleImageView = (ImageView)findViewById(R.id.toleImageView);

        resultText.setText("This is " +card);
        resultText5.setText(name);
        resultText3.setText(desc);
        resultText2.setText(tpNo);

        Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
        toleImageView.setImageBitmap(bmp);
        Toast.makeText(this, card + " is found", Toast.LENGTH_SHORT).show();

        callButton = (Button) findViewById(R.id.callButton);
        makeCall(tpNo);

        smsButton = (Button) findViewById(R.id.smsButton);
        sendSms(tpNo);
    }


    public void sendSms(final String num){
        smsButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        smsIntent(num);
                    }
                }
        );
    }

    public void makeCall(final String num){
        callButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        callIntent(num);
                    }
                }
        );
    }

    public void callIntent(String number){
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + number));
        startActivity(callIntent);
    }

    public void smsIntent(String num){
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setType("vnd.android-dir/mms-sms");
        sendIntent.putExtra("sms_body", "");
        sendIntent.setData(Uri.parse("sms:"+num));
        startActivity(sendIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_results, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.editContact) {
            Intent i = new Intent(this, Edit.class);
            startActivity(i);
            return true;
        }

        if (id == R.id.email) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
