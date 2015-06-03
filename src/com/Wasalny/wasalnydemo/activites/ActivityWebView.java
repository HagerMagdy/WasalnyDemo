package com.Wasalny.wasalnydemo.activites;

import com.Wasalny.wasalnydemo.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.graphics.Bitmap;
public class ActivityWebView extends Activity {

private static final String TAG = "ActivityWebView";
/**
* Get these values after registering your oauth app at: https://foursquare.com/oauth/
*/
public static final String CALLBACK_URL = "http://x-oauthflow-foursquare://callback";
public static final String CLIENT_ID = "2BIRUXRJEIDFPTJT5YV11WHFUQMC15VJZRAV04CUFR11Y2Z3";
@Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_webview);
String url =
"https://foursquare.com/oauth2/authenticate" +
"?client_id=" + CLIENT_ID +
"&response_type=token" +
"&redirect_uri=" + CALLBACK_URL;
WebView webview = (WebView)findViewById(R.id.webview);
webview.getSettings().setJavaScriptEnabled(true);
webview.setWebViewClient(new WebViewClient() {
	
public void onPageStarted(WebView view, String url, Bitmap favicon) {
String fragment = "#access_token=";
int start = url.indexOf(fragment);
if (start > -1) {
String accessToken = url.substring(start + fragment.length(), url.length());
Log.v(TAG, "OAuth complete, token: [" + accessToken + "].");
Toast.makeText(ActivityWebView.this, "Token: " + accessToken, Toast.LENGTH_SHORT).show();
SharedPreferences prefs = getSharedPreferences("wdemo",
						Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString("Token", accessToken);
				editor.commit();
finish();
}
}
});
webview.loadUrl(url);
}

}
