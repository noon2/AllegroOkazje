package com.allegrookazje.items;

import java.io.Serializable;

public class Item_Cel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public long id;
	public String nazwa;
	public String nazwa_wyszukiwania;
	public int max;
	public int min;
	public int kategoria_id;
	public String kategoria_nazwa;
	public int oferta_typu;
	public int count;
	public int part;
	public Item_Cel(String nazwa,String nazwa_wyszukiwania, int min,int max,int kategoria_id,String kategoria_nazwa, int oferta_typu) {
		this.nazwa = nazwa;
		this.nazwa_wyszukiwania=nazwa_wyszukiwania;
		this.min=min;
		this.max=max;
		this.kategoria_id=kategoria_id;
		this.kategoria_nazwa = kategoria_nazwa;
		this.oferta_typu=oferta_typu;
		count=0;
	}

}
