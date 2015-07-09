package com.allegrookazje.item;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allegrookazje.MainActivity;
import com.allegrookazje.items.Item_Lista;
import com.allegrookazje.lista.Lista_Image_Load;
import com.example.allegrookazje.R;

public class Item_Desc extends Fragment {
	Item_Lista item;
	private LruCache<String, Bitmap> saved_images;

	public Item_Desc() {

	}

	public Item_Desc(Item_Lista item) {
		this.item = item;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// wczytywanie obrazow zapisanych
		if (saved_images == null) {
			saved_images = new LruCache<String, Bitmap>(MainActivity.cacheSize);
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.item_desc, container, false);
		Log.d("allegro", "tworzenie!!!");
		wypelnij(rootView);
		return rootView;
	}

	public void wypelnij(View v) {
		// ustawianie zdjec
		LinearLayout gallery = (LinearLayout) v.findViewById(R.id.ll_images);
		if (item.foto_count == 0) {
			ImageView imageView = new ImageView(v.getContext());
			imageView.setImageResource(R.drawable.brak_zdjecia);
			LinearLayout layout = new LinearLayout(v.getContext());
			layout.setLayoutParams(new LayoutParams(400, 300));
			layout.addView(imageView);
			gallery.addView(layout);
		} else {
			for (String url : item.image_2) {
				final ImageView imageView = new ImageView(v.getContext());
				new Lista_Image_Load(getActivity(),imageView, saved_images,
						Long.parseLong(item.image_2.indexOf(url) + ""))
						.execute(url);
				LinearLayout layout = new LinearLayout(v.getContext());
				imageView.setId(item.image_2.indexOf(url));
				
				imageView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						powieksz(arg0.getId());
					}
				});
				imageView.setOnTouchListener(new OnTouchListener() {
					
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						switch(event.getAction())
						{
						case MotionEvent.ACTION_DOWN:
							imageView.setColorFilter(R.color.orange_trans);
			
							break;
						case MotionEvent.ACTION_UP:
						case MotionEvent.ACTION_CANCEL:
							imageView.clearColorFilter();
		
							break;
						}
						return false;
					}
				});
				layout.setLayoutParams(new LayoutParams(400, 300));
				layout.addView(imageView);
				gallery.addView(layout);
			}

		}
		// ustawianie cen
		RelativeLayout rl_buy = (RelativeLayout) v.findViewById(R.id.rl_buy);
		RelativeLayout rl_bid = (RelativeLayout) v.findViewById(R.id.rl_bid);
		TextView tv_price_buy = (TextView) v.findViewById(R.id.tv_price_buy);
		TextView tv_price_bid = (TextView) v.findViewById(R.id.tv_price_bid);

		if (item.buy_now_active == 1 && item.price != 0) {// kt i licytacja
			tv_price_buy.setText(item.buy_now_price + " z³");
			tv_price_bid.setText(item.price + " z³");
		}
		if (item.buy_now_active == 1 && item.price == 0) {// kt
			rl_bid.setVisibility(View.GONE);
			tv_price_buy.setText(item.buy_now_price + " z³");
		}
		if (item.buy_now_active == 0) {// licytacja
			rl_buy.setVisibility(View.GONE);
			tv_price_bid.setText(item.price + " z³");
		}
		// dalej
		TextView tv_name = (TextView) v.findViewById(R.id.tv_name);
		TextView tv_ending_time = (TextView) v
				.findViewById(R.id.tv_ending_time);
		getActivity().getActionBar().setTitle(item.name);
		tv_name.setText(item.name);
		Date dt = new Date(item.ending_time * 1000);
		String data = new SimpleDateFormat("dd MMM, HH:mm").format(dt);
		tv_ending_time.setText(data);
		WebView wv_desc = (WebView) v.findViewById(R.id.wv_desc);
		 wv_desc.getSettings().setJavaScriptEnabled(true);
		wv_desc.loadDataWithBaseURL(null,Html.fromHtml(item.description).toString(),"text/html","UTF-8",null);

	}

	public void powieksz(int position) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse(item.image_3.get(position)), "image/*");
		startActivity(intent);
	}
}
