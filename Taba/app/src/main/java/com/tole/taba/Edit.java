package com.tole.taba;


import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.api.client.googleapis.*;
import com.google.api.client.googleapis.extensions.android.accounts.GoogleAccountManager;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpRequestInitializer;
import com.google.api.services.drive.*;
import com.google.api.client.*;
import com.google.api.client.extensions.android.AndroidUtils;
import com.google.api.client.json.jackson.*;
import com.google.api.client.auth.*;
import com.google.common.*;
import org.codehaus.jackson.*;

import java.io.IOException;

import javax.annotation.*;


public class Edit extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Bundle homeData = getIntent().getExtras();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");

        AccountManager am = AccountManager.get(activity);
        am.getAuthToken(am.getAccounts())[0],
                "oauth2:" + DriveScopes.DRIVE,
                new Bundle(),
                true,
                new OnTokenAcquired(),
                null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
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

        return super.onOptionsItemSelected(item);
    }

    private class OnTokenAcquired implements AccountManagerCallback<Bundle> {
        @Override
        public void run(AccountManagerFuture<Bundle> result) {
            try {
                final String token = result.getResult().getString(AccountManager.KEY_AUTHTOKEN);
                HttpTransport httpTransport = new NetHttpTransport();
                JacksonFactory jsonFactory = new JacksonFactory();
                Drive.Builder b = new Drive.Builder(httpTransport, jsonFactory, null);
                b.setJsonHttpRequestInitializer(new JsonHttpRequestInitializer() {
                    @Override
                    public void initialize(JSonHttpRequest request) throws IOException {
                        DriveRequest driveRequest = (DriveRequest) request;
                        driveRequest.setPrettyPrint(true);
                        driveRequest.setKey(CLIENT ID YOU GOT WHEN SETTING UP THE CONSOLE BEFORE YOU STARTED CODING)
                        driveRequest.setOauthToken(token);
                    }
                });

                final Drive drive = b.build();

                final com.google.api.services.drive.model.File body = new com.google.api.services.drive.model.File();
                body.setTitle("My Test File");
                body.setDescription("A Test File");
                body.setMimeType("text/plain");

                final FileContent mediaContent = new FileContent("text/plain", an ordinary java.io.File you'd like to upload. Make it using a FileWriter or something, that's really outside the scope of this answer.)
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            com.google.api.services.drive.model.File file = drive.files().insert(body, mediaContent).execute();
                            alreadyTriedAgain = false; // Global boolean to make sure you don't repeatedly try too many times when the server is down or your code is faulty... they'll block requests until the next day if you make 10 bad requests, I found.
                        } catch (IOException e) {
                            if (!alreadyTriedAgain) {
                                alreadyTriedAgain = true;
                                AccountManager am = AccountManager.get(activity);
                                am.invalidateAuthToken(am.getAccounts()[0].type, null); // Requires the permissions MANAGE_ACCOUNTS & USE_CREDENTIALS in the Manifest
                                am.getAuthToken (same as before...)
                            } else {
                                // Give up. Crash or log an error or whatever you want.
                            }
                        }
                    }
                }).start();
                Intent launch = (Intent)result.getResult().get(AccountManager.KEY_INTENT);
                if (launch != null) {
                    startActivityForResult(launch, 3025);
                    return; // Not sure why... I wrote it here for some reason. Might not actually be necessary.
                }
            } catch (OperationCanceledException e) {
                // Handle it...
            } catch (AuthenticatorException e) {
                // Handle it...
            } catch (IOException e) {
                // Handle it...
            }
        }
    }
}
