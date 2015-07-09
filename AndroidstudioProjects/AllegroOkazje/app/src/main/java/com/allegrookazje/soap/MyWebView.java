package com.allegrookazje.soap;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.allegrookazje.R;

public class MyWebView extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.full_desc);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		WebView webview = (WebView) findViewById(R.id.wv_full);

		WebSettings webSettings = webview.getSettings();
		webSettings.setBuiltInZoomControls(true);
		webSettings.setSupportZoom(true);
		Bundle extra = getIntent().getExtras();
		if (extra != null) {
			String url = extra.getString("desc");
			String title = extra.getString("title");
			getActionBar().setTitle(title); 
			webview.loadDataWithBaseURL(null, url, "text/html", "UTF-8", null);
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		onBackPressed();
		return true;
	}

	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}

}
