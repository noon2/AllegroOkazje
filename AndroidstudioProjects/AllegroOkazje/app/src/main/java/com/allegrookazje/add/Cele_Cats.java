package com.allegrookazje.add;

import java.util.ArrayList;
import android.widget.ListView;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;


import com.allegrookazje.DbAdapter;
import com.allegrookazje.items.Item_Category;

public class Cele_Cats extends ListActivity {
	private DbAdapter dbadapter;
	Cele_Cats_Adapter adapter;
	ArrayList<Item_Category> lista;
	int kategoria;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		dbadapter = new DbAdapter(getApplicationContext());
		if(!DbAdapter.is_open)
		{
			dbadapter.open();
		}
		getActionBar().setDisplayHomeAsUpEnabled(true);
		int element = 0;
		lista = dbadapter.searchRowCatName(element);
		dbadapter.close();
		adapter = new Cele_Cats_Adapter(this,lista);
		adapter.notifyDataSetChanged();
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		setListAdapter(adapter);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		onBackPressed();
		return true;
	}
	public void select(Item_Category item)
	{
		Intent returnIntent = new Intent();
		returnIntent.putExtra("kategoria_name", item.name);
		returnIntent.putExtra("kategoria_id", item.id);
		setResult(21, returnIntent);
		finish();
	}
	public void next(int id)
	{
		if(!DbAdapter.is_open)
		{
			dbadapter.open();
		}
		lista.clear();
		lista.addAll(dbadapter.searchRowCatName(id));
		adapter.notifyDataSetChanged();
		dbadapter.close();
	}
	
}
