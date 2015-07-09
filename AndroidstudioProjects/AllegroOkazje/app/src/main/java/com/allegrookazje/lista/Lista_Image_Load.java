package com.allegrookazje.lista;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.allegrookazje.R;

public class Lista_Image_Load extends AsyncTask<String, Void, Bitmap> {
	LruCache<String, Bitmap> saved_images;
	ImageView imageView;
	String id;
	Context context;

	public Lista_Image_Load(Context context, ImageView imageView,
			LruCache<String, Bitmap> saved_images, long id) {
		this.imageView = imageView;
		this.context = context;
		this.saved_images = saved_images;
		this.id = id + "";
		imageView.setImageResource(R.drawable.ic_loading);
		Animation a = AnimationUtils.loadAnimation(imageView.getContext(),
				R.anim.progress_anim);
		a.setDuration(1000);
		a.setRepeatMode(Animation.INFINITE);
		a.setRepeatCount(Animation.INFINITE);
		imageView.startAnimation(a);
	}

	@Override
	protected void onPreExecute() {
	}

	@Override
	protected Bitmap doInBackground(String... url) {
		Bitmap bitmap = saved_images.get(id);
		if (url[0] == "") {
			bitmap = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.brak_zdjecia);
			return bitmap;
		}
		if (bitmap != null) {
			return bitmap;
		}
		try {
			URL urlConnection = new URL(url[0]);
			HttpURLConnection connection = (HttpURLConnection) urlConnection
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			bitmap = BitmapFactory.decodeStream(input);
			saved_images.put(id, bitmap);
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	protected void onPostExecute(Bitmap bitmap) {
		super.onPostExecute(bitmap);
		if (imageView != null && bitmap != null) {
			imageView.clearAnimation();
			imageView.setImageBitmap(bitmap);
		}

	}

}