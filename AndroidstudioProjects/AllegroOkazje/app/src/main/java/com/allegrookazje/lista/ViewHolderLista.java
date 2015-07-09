package com.allegrookazje.lista;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolderLista {
	TextView item_name;
	TextView item_when;
	TextView item_price;
	TextView item_price_bid;
	TextView item_price_full;
	ImageView item_image;
	boolean is_downloading;
	AsyncTask<String, Void, Bitmap> task;
}
