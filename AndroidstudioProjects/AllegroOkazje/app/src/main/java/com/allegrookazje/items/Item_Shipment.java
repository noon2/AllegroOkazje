package com.allegrookazje.items;

import java.io.Serializable;

public class Item_Shipment implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int id;
	public String name;
	public int type;
	public int time_from;
	public int time_to;
	public float amount;
	public float amount_add;
	public int pack_size;
	public int free_shipping;

}
