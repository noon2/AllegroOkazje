package com.allegrookazje;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.allegrookazje.items.Item_Lista;
import com.example.allegrookazje.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmIntentService extends IntentService {
	public static int NOTIFICATION_ID=1;
	private static final String TAG = "GcmIntentService";
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;

	public GcmIntentService() {
		super("GcmIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		Bundle extras = intent.getExtras();
		Log.d(TAG, "wiadomosc!!");

		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) { // has effect of unparcelling Bundle
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {

			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {
				Item_Lista item = new Item_Lista();
				item.id=Long.parseLong(extras.getString("item_id"));
				item.name=extras.getString("item_name");
				item.price=Float.parseFloat(extras.getString("item_price"));
				item.buy_now_price=Float.parseFloat(extras.getString("buy_now_price"));
				item.buy_now_active=Integer.parseInt(extras.getString("buy_now_active"));
				item.thumb_url=extras.getString("thumb_url");
				new Lista_Thumb_Load(item).execute();
			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	private void sendNotification(Item_Lista item, Bitmap icon) {
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		Intent intent = new Intent(getApplicationContext(),
				MainActivity.class);
		intent.putExtra("item_id", item.id);
		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		PendingIntent contentIntent = PendingIntent.getActivity(this, NOTIFICATION_ID,
 intent, PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
		inboxStyle.setBigContentTitle("Allegro Okazje");
		inboxStyle.addLine(item.name);
		inboxStyle.addLine(" ");
		if (item.buy_now_active == 1 && item.price != 0) {// kt i licytacja
			inboxStyle.addLine("Licytacja: " + item.price + " z³");
			inboxStyle.addLine("Kup Teraz: " + item.buy_now_price + " z³");
		}
		if (item.buy_now_active == 1 && item.price == 0) {// kt
			inboxStyle.addLine("Kup Teraz: " + item.buy_now_price + " z³");
		}
		if (item.buy_now_active == 0) {// licytacja
			inboxStyle.addLine("Licytacja: " + item.price + " z³");
		}
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("Allegro Okazja").setStyle(inboxStyle)
				.setAutoCancel(true).setLargeIcon(icon);
		if (settings.getBoolean("cbp_sound", false)) {
			mBuilder.setSound(Uri.parse(settings.getString("rp_ringtone", null)));
		}
		if (settings.getBoolean("cbp_vibration", false)) {
			long[] steps = { 0, 500, 100, 200, 100, 200 };
			mBuilder.setVibrate(steps);
		}
		mBuilder.setContentIntent(contentIntent);
		Log.d(TAG, NOTIFICATION_ID+"ID!!!!!!");
		mNotificationManager.notify(NOTIFICATION_ID++, mBuilder.build());
	}

	public class Lista_Thumb_Load extends AsyncTask<Void, Void, Void> {

		private Bitmap bitmap;
		private Item_Lista item;

		public Lista_Thumb_Load(Item_Lista item) {
			this.item = item;
		}

		@Override
		protected Void doInBackground(Void... voids) {

			try {
				URL urlConnection = new URL(item.thumb_url);
				HttpURLConnection connection = (HttpURLConnection) urlConnection
						.openConnection();
				connection.setDoInput(true);
				connection.connect();
				InputStream input = connection.getInputStream();
				bitmap = BitmapFactory.decodeStream(input);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (bitmap == null) {
			}
			sendNotification(item, bitmap);
		}

	}
}