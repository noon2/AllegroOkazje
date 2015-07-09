package com.allegrookazje.lista;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.allegrookazje.items.Item_Lista;
import com.example.allegrookazje.R;

public class Lista_Adapter extends ArrayAdapter<Item_Lista> {

	private final Context context;
	private ArrayList<Item_Lista> all_items;
	private LruCache<String, Bitmap> saved_images;

	public Lista_Adapter(Context context, ArrayList<Item_Lista> itemsArrayList,
			LruCache<String, Bitmap> saved_images) {
		super(context, R.layout.lista_item, itemsArrayList);
		this.saved_images = saved_images;
		this.context = context;
		this.all_items = itemsArrayList;
	}

	public void setItemList(ArrayList<Item_Lista> itemsArrayList) {
		all_items = itemsArrayList;
		notifyDataSetChanged();
	}

	public ArrayList<Item_Lista> getItemList() {
		return all_items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		ViewHolderLista holder = null;
		Item_Lista item = all_items.get(position);
		if (rowView == null) {
			holder = new ViewHolderLista();
			// 1. Create inflater
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			// 2. Get rowView from inflater
			rowView = inflater.inflate(R.layout.lista_item, parent, false);
			// 3. Get the two text view from the rowView
			holder.item_name = (TextView) rowView
					.findViewById(R.id.tv_item_name);
			holder.item_when = (TextView) rowView
					.findViewById(R.id.tv_item_when);
			holder.item_price = (TextView) rowView
					.findViewById(R.id.tv_item_price);
			holder.item_price_bid = (TextView) rowView
					.findViewById(R.id.tv_item_price_bid);
			holder.item_price_full = (TextView) rowView
					.findViewById(R.id.tv_item_price_full);
			holder.item_image = (ImageView) rowView
					.findViewById(R.id.iv_item_image);
			rowView.setTag(holder);
		} else {
			holder = (ViewHolderLista) rowView.getTag();
			if (holder.task != null)
				holder.task.cancel(true);
		}

		// 4. Set the text for textView
		float cena_z_dostawa = 0;
		if (item.postage != -1) {
			cena_z_dostawa = item.postage;
		}
		if (item.buy_now_active == 1 && item.price != 0) {// kt i licytacja
			holder.item_price.setText(rowView.getResources().getString(
					R.string.item_cena_kupteraz)
					+ " " + item.buy_now_price);
			holder.item_price_bid.setText(item.price + "");
			cena_z_dostawa += item.price;
			holder.item_price_bid.setVisibility(View.VISIBLE);
			holder.item_price.setVisibility(View.VISIBLE);
		} else if (item.buy_now_active == 1 && item.price == 0) {// kt
			holder.item_price.setText(rowView.getResources().getString(
					R.string.item_cena_kupteraz)
					+ " " + item.buy_now_price);
			cena_z_dostawa += item.buy_now_price;
			holder.item_price_bid.setVisibility(View.GONE);
			holder.item_price.setVisibility(View.VISIBLE);
		} else if (item.buy_now_active == 0) {// licytacja
			holder.item_price_bid.setText(item.price + "");
			cena_z_dostawa += item.price;
			holder.item_price.setVisibility(View.GONE);
			holder.item_price_bid.setVisibility(View.VISIBLE);
		}
		holder.item_price_full.setText(rowView.getResources().getString(
				R.string.item_cena_z_dostawa)
				+ " " + cena_z_dostawa);
		holder.item_name.setText(item.name);
		Date dt = new Date(item.ending_time * 1000);
		String data = new SimpleDateFormat("dd MMM, HH:mm").format(dt);
		holder.item_when.setText(data);
		holder.task = new Lista_Image_Load(context, holder.item_image,
				saved_images, item.id).execute(item.thumb_url);
		// 5. retrn rowView
		return rowView;
	}

	@Override
	public int getCount() {
		return all_items.size();
	}

}