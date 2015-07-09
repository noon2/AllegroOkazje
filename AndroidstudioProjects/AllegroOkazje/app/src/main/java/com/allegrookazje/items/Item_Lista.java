package com.allegrookazje.items;

import java.io.Serializable;
import java.util.Vector;

import android.graphics.Bitmap;

public class Item_Lista implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public boolean is_loaded;
	public boolean is_downloading;
	public long id;
	public String name;
	public float price;
	public float starting_price;
	public float buy_now_price;
	public int buy_now_active;
	public int bid_count;
	public int foto_count;
	public long starting_time;
	public long ending_time;
	public long time_left;
	public String city;
	public int state;
	public int country;
	public int category_id;
	public int featured;
	public String thumb_url;
	public int allergo_standard;
	public int has_free_shipping;
	public int installments_available;
	public int order_fulfillment_time;
	public long seller_id;
	public String seller_login;
	public int seller_rating;
	public int seller_info;
	public int quantity;
	public String location;
	public Vector<String> image_1;
	public Vector<String> image_2;
	public Vector<String> image_3;
	public Bitmap image_mini;
	public String description;
	public Long hit_count;
	public int is_new_used;
	public float postage;
	public Vector<String> params_name;
	public Vector<String> params_value1;
	public Vector<Item_Shipment> postage_options;
	public Item_Payment payment;
	public Item_Lista()
	{
		is_loaded=false;
		is_downloading=false;
		postage_options = new Vector<Item_Shipment>();
		image_1=new Vector<String>();
		image_2=new Vector<String>();
		image_3=new Vector<String>();
		params_name=new Vector<String>();
		params_value1=new Vector<String>();
	}
	

}
