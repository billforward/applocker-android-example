package com.applocker.sample;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.webkit.WebView;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApplockerLoginHandler {
    private static final Logger log = Logger.getLogger(ApplockerLoginHandler.class.getName());

    private final OkHttpClient client = new OkHttpClient();
    private final String customerApiUrl;
    private final String storefrontUrl;
    private final WebView webView;
    private final Activity activity;

    @SuppressLint("SetJavaScriptEnabled")
    public ApplockerLoginHandler(Activity activity) {
        var context = activity.getApplicationContext();
        this.customerApiUrl = context.getString(R.string.customer_api_url);
        this.storefrontUrl = context.getString(R.string.store_url);

        webView = new WebView(context);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUserAgentString("CompanyName AppLocker");

        this.activity = activity;
    }

    public void onCustomerLogin(String emailAddress) {
        //emailAddress assumed to be validated so can't contain a "
        String content = "{\"email\":\"" + emailAddress + "\"}";

        Request request = new Request.Builder()
                .url(customerApiUrl)
                .method("PUT", RequestBody.create(content, MediaType.get("application/json")))
                .build();

        client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    //TODO: Handle failure
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    var body = Objects.requireNonNull(response.body()).string();

                    String jwt;
                    try {
                        JSONObject object = new JSONObject(body);
                        jwt = object.getString("access_token");
                    } catch (JSONException e) {
                        //TODO: Handle failure
                        log.log(Level.SEVERE, "Error parsing access token", e);
                        jwt = "invalid";
                    }
                    String postData = "access_token=" + jwt;

                    activity.runOnUiThread(() -> webView.postUrl(storefrontUrl, postData.getBytes(StandardCharsets.UTF_8)));
                }
            });
    }

    public WebView getWebView() {
        return webView;
    }
}
