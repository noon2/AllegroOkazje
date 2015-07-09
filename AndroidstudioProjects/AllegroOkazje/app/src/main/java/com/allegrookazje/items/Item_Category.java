package com.allegrookazje.items;

import java.io.Serializable;

public class Item_Category implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String name;
	public boolean next;
	public int id;
	public Item_Category(String name,int id, boolean next) {
		this.name=name;
		this.id=id;
		this.next=next;
	}

}
