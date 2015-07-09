package com.allegrookazje.item;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.allegrookazje.Client;
import com.allegrookazje.MainActivity;
import com.allegrookazje.items.Item_Lista;
import com.allegrookazje.items.Item_User;
import com.allegrookazje.soap.MyWebView;
import com.example.allegrookazje.R;

public class Lista_Item_View extends FragmentActivity  {
	Item_Lista item;
	public Client klient;
	private ViewPager viewPager;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.item_swipe_view);
		viewPager = (ViewPager) findViewById(R.id.pager);
		Bundle extras = getIntent().getExtras();
		item = (Item_Lista) extras.get("item");
		
		Item_User user = new Item_User();
		klient=MainActivity.klient;
		klient.doShowUser(getApplicationContext(), item.seller_login, user);
		
		String[] tabs = {"OPIS","DOSTAWA","PARAMETRY","SPRZEDAWCA"};
		TabsPagerAdapter mAdapter = new TabsPagerAdapter(getSupportFragmentManager(), item, user);
		viewPager.setAdapter(mAdapter);
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		//adding tabs
		for (String tab_name : tabs) {
			getActionBar().addTab(getActionBar().newTab().setText(tab_name).setTabListener(new TabListener() {
				
				@Override
				public void onTabUnselected(Tab tab, FragmentTransaction ft) {
					
				}
				
				@Override
				public void onTabSelected(Tab tab, FragmentTransaction ft) {
					viewPager.setCurrentItem(tab.getPosition());
					
				}
				
				@Override
				public void onTabReselected(Tab tab, FragmentTransaction ft) {
					
					
				}
			}));
		}
		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
		 
		    @Override
		    public void onPageSelected(int position) {
		        // on changing the page
		        // make respected tab selected
		        getActionBar().setSelectedNavigationItem(position);
		    }
		 
		    @Override
		    public void onPageScrolled(int arg0, float arg1, int arg2) {
		    }
		 
		    @Override
		    public void onPageScrollStateChanged(int arg0) {
		    }
		});
	}

	public void full_desc(View v) {
		Intent intent = new Intent(this, MyWebView.class);
		intent.putExtra("desc", item.description);
		intent.putExtra("title", item.name);
		startActivity(intent);
	}
	public void kup_teraz(View v) {
		MainActivity.klient.doBidItem(v.getContext(), 1, item.buy_now_price,item.id);
	}

	public void licytuj(final View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
		LayoutInflater inflater = MainActivity.mNavigationDrawerFragment
				.getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_price, null);
		final EditText kwota = (EditText) view.findViewById(R.id.et_dialog_price);
		builder.setMessage("Podaj kwotê:").setTitle("Licytacja").setView(view)
				.setPositiveButton("Ok", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Float price = Float.parseFloat(kwota.getText().toString());
						MainActivity.klient.doBidItem(v.getContext(), 0, price,item.id);
					}
				}).setNegativeButton("Anuluj", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.show();
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		onBackPressed();
		return true;
	}

	@Override
	public void onBackPressed() {
		finish();
		getSupportFragmentManager().popBackStack();
		super.onBackPressed();
	}

}
