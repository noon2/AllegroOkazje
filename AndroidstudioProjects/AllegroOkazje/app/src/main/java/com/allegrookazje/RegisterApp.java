package com.allegrookazje;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class RegisterApp extends AsyncTask<Void, Void, String> {
	Context ctx;
	GoogleCloudMessaging gcm;
	String SENDER_ID = "870637403627";
	String regid = null;
	String url = "http://allegrookazje.twomini.com/allegro/check_user.php";
	Boolean error;
	String message;

	public RegisterApp(Context ctx, GoogleCloudMessaging gcm) {
		this.ctx = ctx;
		this.gcm = gcm;
		error=false;
		message="";
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(Void... arg0) {
		String msg = "";
		try {
			if (gcm == null) {
				gcm = GoogleCloudMessaging.getInstance(ctx);
			}
			regid = gcm.register(SENDER_ID);
			msg = "Device registered, registration ID=" + regid;

			// You should send the registration ID to your server over HTTP,
			// so it can use GCM/HTTP or CCS to send messages to your app.
			// The request to your server should be authenticated if your app
			// is using accounts.
			// For this demo: we don't need to send it because the device
			// will send upstream messages to a server that echo back the
			// message using the 'from' address in the message.

			// Persist the regID - no need to register again.
			storeRegistrationId(ctx, regid);
			//search duplicate reg_id server
			List<NameValuePair> myArgs = new ArrayList<NameValuePair>();
			myArgs.add(new BasicNameValuePair("reg_id", regid));
			HttpPost post = new HttpPost(url);
			HttpClient myClient = new DefaultHttpClient();
			try {
				post.setEntity(new UrlEncodedFormEntity(myArgs));
				HttpResponse response = myClient.execute(post);
				String result = EntityUtils.toString(response.getEntity());
				JSONObject json = new JSONObject(result);
				error = json.getBoolean("success");
				if (!error) {
					message = json.getString("message");
				}
			} catch (UnsupportedEncodingException e) {
				Client.show_message(e.toString(), "Error", ctx, true);
			} catch (ClientProtocolException e) {
				Client.show_message(e.toString(), "Error", ctx, true);
			} catch (IOException e) {
				Client.show_message(e.toString(), "Error", ctx, true);
			} catch (JSONException e) {
				Client.show_message(e.toString(), "Error", ctx, true);
			}
		} catch (IOException ex) {
			msg = "Error :" + ex.getMessage();
			// If there is an error, don't just keep trying to register.
			// Require the user to click a button again, or perform
			// exponential back-off.
		}
		return msg;
	}

	private void storeRegistrationId(Context ctx, String regid) {
		final SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(ctx);
		SharedPreferences.Editor editor = prefs.edit();
		MainActivity.regid=regid;
		editor.putString("reg_id", regid);
		editor.commit();

	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
	}
}
