package com.allegrookazje.items;

import java.io.Serializable;
import java.util.Vector;


public class Item_User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public long id;
	public String login;
	public int country;
	public long create_date;
	public long login_date;
	public int rating;
	public int is_new_user;
	public int not_activated;
	public int closed;
	public int blocked;
	public int terminated;
	public int has_page;
	public int is_sseller;
	public int is_eco;
	public Item_Feedback positive;
	public Item_Feedback negative;
	public Item_Feedback neutral;
	public int junior_status;
	public int has_shop;
	public int company_icon;
	public int sell_rating_count;
	public Vector<Item_Rating_Average> rating_average;
	public int is_allegro_standard;
	public int is_b2c_seller;

	public Item_User() {
		rating_average = new Vector<Item_Rating_Average>();
	}
}
