package com.allegrookazje.items;

import java.io.Serializable;

public class Item_Country implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int id;
	public String name;
	public Item_Country(int id,String name) {
		this.id=id;
		this.name=name;
	}

}
