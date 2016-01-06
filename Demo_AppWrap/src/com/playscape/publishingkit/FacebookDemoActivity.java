package com.playscape.publishingkit;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.facebook.*;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.*;
import com.facebook.share.widget.AppInviteDialog;
import com.facebook.share.widget.ShareDialog;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class FacebookDemoActivity extends Activity {

    private static final String TAG = FacebookDemoActivity.class.getSimpleName();

    private CallbackManager mCallbackManager;

    private int i = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        // FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);

        setContentView(R.layout.fb_demo);

        configureButtons();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void configureButtons() {
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_fb_button);
        loginButton.setPublishPermissions("publish_actions");

        mCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.i(TAG, "onSuccess. token: " + loginResult.getAccessToken());
                        showCreateAppRequestButton();
                        showGetReuqests();
                        showSendGraphObjectRequestButton();
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
                LoginManager.getInstance().logInWithPublishPermissions(FacebookDemoActivity.this, Arrays.asList("publish_actions"));
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
                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse("https://developers.facebook.com"))
                        .build();
                ShareDialog.show(FacebookDemoActivity.this, content);
            }
        });

        share = (Button) findViewById(R.id.share_api);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareWithShareAPI();
            }
        });

        Button callRequestButton = (Button) findViewById(R.id.call_request);
        callRequestButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(i % 2 == 0) {
                   callGraphRequest();
               } else {
                   callShareGraphRequest();
               }
               i++;
           }
       });

       Button logEvent = (Button) findViewById(R.id.log_event);
       logEvent.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Bundle parameters = new Bundle();
              parameters.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "USD");
              parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "product");
              parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, "HDFU-8452");

              AppEventsLogger logger = AppEventsLogger.newLogger(FacebookDemoActivity.this);
              logger.logEvent(AppEventsConstants.EVENT_NAME_PURCHASED,
                54.23,
                parameters);
          }
      });
    }

    private void shareWithShareAPI() {
        ShareContent linkContent = new ShareLinkContent.Builder()
                .setContentTitle("share api")
                .setContentUrl(Uri.parse("https://developers.facebook.com"))
                .build();
        ShareApi.share(linkContent, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Log.i(TAG, "FacebookCallback. onSuccess");
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "FacebookCallback. onCancel");
            }

            @Override
            public void onError(FacebookException e) {
                Log.i(TAG, "FacebookCallback. onError. " + e.toString());
            }
        });
    }

    private void callGraphRequest() {
        Bundle params = new Bundle();
        params.putString("score", "3444");
        /* make the API call */
        GraphRequest graphRequest = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/score/",
                params,
                HttpMethod.POST,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        /* handle the result */
                        Log.i(TAG, "GraphRequestCallback. onCompleted");
                    }
                }
        );
        graphRequest.executeAsync();
    }

    private void callShareGraphRequest() {
        Bundle params = new Bundle();
        params.putString("message", "3444");
        /* make the API call */
        GraphRequest graphRequest = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/feed/",
                params,
                HttpMethod.POST,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        /* handle the result */
                        Log.i(TAG, "callShareGraphRequest. onCompleted");
                    }
                }
        );
        graphRequest.executeAsync();
    }

    private void showCreateAppRequestButton() {
        Button button = (Button) findViewById(R.id.create_request);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRequest(AccessToken.getCurrentAccessToken().getUserId());
            }
        });
    }

    private void showSendGraphObjectRequestButton() {
        Button button = (Button) findViewById(R.id.send_go_request);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGraphObjectIdAndSendLife();
            }
        });
    }

    private void showGetReuqests() {
        Button button = (Button) findViewById(R.id.get_requests_amount);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAppRequests(AccessToken.getCurrentAccessToken().getUserId());
            }
        });
    }

    private void createRequest(String id) {
        String appLinkUrl, previewImageUrl;
        appLinkUrl = "https://fb.me/1641943392727545";
        previewImageUrl = "https://lh3.ggpht.com/mTEVhLX7HL-tP2WApecuL4LAh4zvaBYs3RpkbhnHGqCtpcOfHnjc6xP8D3OQ7qIUrsk=w300-rw";

       if (AppInviteDialog.canShow()) {
           AppInviteContent content = new AppInviteContent.Builder()
                   .setApplinkUrl(appLinkUrl)
                   .setPreviewImageUrl(previewImageUrl)
                   .build();
           AppInviteDialog.show(FacebookDemoActivity.this, content);
       }
    }

    private void getGraphObjectIdAndSendLife() {
        final Bundle params = new Bundle();
        String fbAppId = getString(R.string.facebook_app_id);
        String type = "playscape_demo_andr:life";

        String sampleUrl = "http://samples.ogp.me/1618115238450093";
        String sampleTitle = "Sample titile";
        String imageUrl = "https://s-static.ak.fbcdn.net/images/devsite/attachment_blank.png";
        params.putString("object", "{\"fb:app_id\":\"" + fbAppId
                + "\",\"og:type\":\"" + type + "\""
                + ",\"og:url\":\"" + sampleUrl + "\""
                + ",\"og:title\":\"" + sampleTitle + "\""
                + ",\"og:image\":\"" + imageUrl + "\"}");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "me/objects/" + type,
                params,
                HttpMethod.POST,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        /* handle the result */
                        Log.i(TAG, "sendGraphObjectRequest. response.raw: " + response.getRawResponse());
                        if(response.getError() != null) {
                            Log.e(TAG, "sendGraphObjectRequest. " + response.getError().toString());
                        } else {
                            try {
                                String objectId = response.getJSONObject().getString("id");
                                sendGraphObjectRequest(AccessToken.getCurrentAccessToken().getUserId(), objectId);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
        ).executeAsync();
    }

    private void sendGraphObjectRequest(String userID, String objectId) {

        final Bundle params = new Bundle();

        String fbAppId = getString(R.string.facebook_app_id);

        params.putString("object_id", objectId);
        params.putString("app_id", fbAppId);
        params.putString("title", "Sample life");
        params.putString("message", "Sample life for friend");
        params.putString("action_type", "send");
        params.putString("action_type", "send");

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + userID + "/apprequests",
                params,
                HttpMethod.POST,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse graphResponse) {
                        Log.i(TAG, "sendGraphObjectRequest. graphResponse.raw: " + graphResponse.getRawResponse());
                        if (graphResponse.getError() != null) {
                            Log.e(TAG, "sendGraphObjectRequest. " + graphResponse.getError().toString());
                        } else {

                        }
                    }
                }
        ).executeAsync();
    }

    private void getAppRequests(String userId) {
        String path = "/" + userId + "/apprequests";
        Log.i(TAG, "getAppRequests. path: " + path);
        /* make the API call */
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                path,
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        /* handle the result */
                        Log.i(TAG, "apprequests. response.raw: " + response.getRawResponse());
                        try {
                            JSONArray data = response.getJSONObject().getJSONArray("data");
                            int dataLength = (data != null ? data.length() : -1);
                            Log.i(TAG, "apprequests. data lenght: " + dataLength);

                            if(dataLength > 0) {
                                JSONObject obj = data.getJSONObject(0);
                                String requestId = obj.getString("id");
                                getRequest(requestId);
                            } else {
                                Log.i(TAG, "data is empty!");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }

    private void getRequest(String requestId) {
        Log.i(TAG, "getRequest. requestId: " + requestId);
        /* make the API call */
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + requestId,
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        /* handle the result */
                        Log.i(TAG, "getRequest. response.raw: " + response.getRawResponse());
                    }
                }
        ).executeAsync();
    }
}
