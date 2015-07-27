package com.playscape.publishingkit;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.facebook.*;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.util.Arrays;

public class FacebookDemoActivity extends Activity {

    private static final String TAG = FacebookDemoActivity.class.getSimpleName();

    private CallbackManager mCallbackManager;

    private static final int BASE_OFFSET = 64206;
    private static final int LOGIN_CODE = BASE_OFFSET;
    private static final int SHARE_CODE = 64207;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.fb_demo);

        configureButtons();
    }

    private void configureButtons() {
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_fb_button);
        loginButton.setReadPermissions("user_friends");

        mCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.i(TAG, "onSuccess. token: " + loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.i(TAG, "onCancel.");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.i(TAG, "onError. " + exception.toString());
                    }
                });

        Button login = (Button) findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(FacebookDemoActivity.this, Arrays.asList("public_profile", "user_friends"));
            }
        });

        Button logout = (Button) findViewById(R.id.logout_button);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
            }
        });

        Button share = (Button) findViewById(R.id.share_button);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FacebookDemoActivity.this, "on share click", Toast.LENGTH_SHORT).show();
                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse("https://developers.facebook.com"))
                        .build();
                ShareDialog.show(FacebookDemoActivity.this, content);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case LOGIN_CODE:
                trackLogin(resultCode);
                break;
            case SHARE_CODE:
                trackShare(resultCode, data);
                break;

        }
    }

    private void trackLogin(int resultCode) {
        String successful = "";
        String failed = "";
        String canceled = "";

        if (resultCode == Activity.RESULT_OK) {
            if (AccessToken.getCurrentAccessToken() != null) {
                successful = "Success!";
            } else {
                failed = "Failed!";
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            canceled = "Canceled!";
        }
        Log.i(TAG, successful + failed + canceled);
    }

    private void trackShare(int resultCode, Intent data) {
        int targetRequestCode = CallbackManagerImpl.RequestCodeOffset.Share.toRequestCode();
        Log.i(TAG, "Share is here!");
    }
}
