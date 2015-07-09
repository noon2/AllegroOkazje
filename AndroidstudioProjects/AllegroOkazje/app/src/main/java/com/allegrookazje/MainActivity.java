package com.allegrookazje;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.util.LruCache;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ViewFlipper;

import com.allegrookazje.add.Add_Cel_Activity;
import com.allegrookazje.item.Lista_Item_View;
import com.allegrookazje.items.Item_Cel;
import com.allegrookazje.items.Item_Lista;
import com.allegrookazje.lista.Lista_Adapter;
import com.example.allegrookazje.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class MainActivity extends Activity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {
	public static Client klient;
	private DbAdapter dbadapter;
	public static String TAG = "allegro";
	public static String regid;
	public static NavigationDrawerFragment mNavigationDrawerFragment;
	private Context context;
	public static ListView lv_lista;
	public static int sortowanie = 0;
	public static CharSequence mTitle;
	// wybrany cel
	public static Item_Cel cel;
	// lista przedmiotow
	public static ArrayList<Item_Lista> lista;
	public static ArrayAdapter<Item_Lista> lista_adapter;
	public LruCache<String, Bitmap> saved_images;
	// footer listview
	public static int last_total_item_count = 0;
	public boolean foot_is_enable = false;
	public static ViewFlipper vf_actual;
	public static ActionBar actionBar;
	public static int cacheSize;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("allegro", "onCreate MainActivity");
		actionBar = getActionBar();
		setContentView(R.layout.activity_main);
		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		vf_actual = (ViewFlipper) findViewById(R.id.vf_list_loading);
		lv_lista = (ListView) findViewById(R.id.list);
		// wczytywanie obrazow zapisanych
		if (saved_images == null) {
			final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
			cacheSize = maxMemory / 8;
			saved_images = new LruCache<String, Bitmap>(MainActivity.cacheSize);
		}
		// set footer listview
		final View footer = ((LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.footer, null, false);
		lv_lista.addFooterView(footer);
		lv_lista.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int lastInScreen = firstVisibleItem + visibleItemCount;
				if ((lastInScreen == totalItemCount && totalItemCount != last_total_item_count)
						&& totalItemCount - 1 < Client.count_search) {
					last_total_item_count = totalItemCount;
					footer.setVisibility(View.VISIBLE);
					foot_is_enable = true;
					klient.doSearch(MainActivity.sortowanie, vf_actual,
							MainActivity.lista_adapter, MainActivity.lista,
							Client.item_cel, getApplicationContext(),
							Client.part_search);
				}
				if (totalItemCount - 1 == Client.count_search
						&& foot_is_enable == true) {
					foot_is_enable = false;
					footer.setVisibility(View.GONE);
				}

			}
		});
		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		context = mNavigationDrawerFragment.getActivity();
		dbadapter = new DbAdapter(getApplicationContext());
		klient = NavigationDrawerFragment.klient;

		saved_images = new LruCache<String, Bitmap>(cacheSize);
		lista = new ArrayList<Item_Lista>();
		lista_adapter = new Lista_Adapter(context, lista, saved_images);
		mTitle = getTitle();
		rejestracja();
		start_aplikacji();
		lv_lista.setAdapter(lista_adapter);
		lv_lista.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(context, Lista_Item_View.class);
				klient.doGetItemsInfo(lista.get(position), context, intent);
			}
		});
	}

	@Override
	protected void onNewIntent(Intent intent) {
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			Intent intent2 = new Intent(context, Lista_Item_View.class);
			Item_Lista item = new Item_Lista();
			item.id = bundle.getLong("item_id");
			klient.doGetItemsInfo(item, this, intent2);
		}
	}

	private void rejestracja() {
		if (!checkPlayServices()) {
			finish();
		} else {
			SharedPreferences settings = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext());
			regid = getRegistrationId(settings, getApplicationContext());
			if (regid == "") {
				GoogleCloudMessaging gcm = GoogleCloudMessaging
						.getInstance(this);
				new RegisterApp(getApplicationContext(), gcm).execute();
			}
		}
	}

	public void get_session() {
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		if (settings.getLong("time_login", 0) < getUnixTime() - 3600) {
			Log.d(TAG, "if true" + " time " + settings.getLong("time_login", 0)
					+ " curr time:" + getUnixTime());
			klient.doLoginEnc(context);
		} else {
			Log.d(TAG,
					"if false" + " time " + settings.getLong("time_login", 0)
							+ " curr time:" + getUnixTime());
			if (klient.SESSION_HANDLE_PART == null) {

				klient.SESSION_HANDLE_PART = settings.getString(
						"session_handle_part", "");
			}
		}
	}

	public void select_cel(View v) {
		mNavigationDrawerFragment.mDrawerLayout.openDrawer(Gravity.LEFT);
	}

	private void start_aplikacji() {
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		sortowanie = settings.getInt("sort_order", 0);
		klient.start_aplikacji(context);
		// ilosc kategorii
		Bundle intent = getIntent().getExtras();
		if (intent != null) {
			Intent intent2 = new Intent(context, Lista_Item_View.class);
			Item_Lista item = new Item_Lista();
			item.id = intent.getLong("item_id");
			klient.doGetItemsInfo(item, this, intent2);
		}
	}

	public long getUnixTime() {
		long unixtime;
		Date currentDate = new Date();
		unixtime = currentDate.getTime() / 1000;
		return unixtime;
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						PlaceholderFragment.newInstance(position + 1)).commit();

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.setHeaderTitle("Dzia³ania");
		menu.add(0, v.getId(), 0, "Edytuj");
		menu.add(0, v.getId(), 1, "Usuñ");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		if (item.getOrder() == 0) {
			Intent dodaj = new Intent(context, Add_Cel_Activity.class);
			Item_Cel tmp = searchCel(info.position);
			dodaj.putExtra("cel", tmp);
			startActivity(dodaj);
		}
		if (item.getOrder() == 1) {
			Item_Cel tmp = searchCel(info.position);
			klient.delete_row(tmp, context);
		} else {
			return false;
		}
		return true;
	}

	public Item_Cel searchCel(int pos) {
		Item_Cel wynik = null;
		if (!DbAdapter.is_open) {
			dbadapter.open();
		}
		Cursor cursor = dbadapter.readData();
		int i = 0;
		while (true) {
			if (i == pos) {
				wynik = dbadapter.searchRowCeleId(cursor.getString(0));
				break;
			}
			i++;
			if (!cursor.moveToNext())
				break;
		}
		dbadapter.close();
		return wynik;
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// visible menu

		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			if (cel != null) {
				MenuItem cel_edit = menu.findItem(R.id.cel_edit);
				MenuItem cel_delete = menu.findItem(R.id.cel_delete);
				MenuItem cel_sort = menu.findItem(R.id.cel_sort);
				MenuItem cel_add = menu.findItem(R.id.cel_add);
				cel_add.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
				cel_sort.setVisible(true);
				cel_edit.setVisible(true);
				cel_delete.setVisible(true);
			}
			restoreActionBar();
			// SearchManager searchManager = (SearchManager)
			// getSystemService(Context.SEARCH_SERVICE);
			// SearchView searchView = (SearchView) menu.findItem(R.id.search)
			// .getActionView();
			// searchView.setSearchableInfo(searchManager
			// .getSearchableInfo(getComponentName()));
			// SearchView.OnQueryTextListener textChangeListener = new
			// SearchView.OnQueryTextListener() {
			// @Override
			// public boolean onQueryTextChange(String newText) {
			// // this is your adapter that will be filtered
			// mNavigationDrawerFragment.lista_adapter.getFilter().filter(
			// newText);
			// return true;
			// }
			//
			// @Override
			// public boolean onQueryTextSubmit(String query) {
			// // this is your adapter that will be filtered
			// mNavigationDrawerFragment.lista_adapter.getFilter().filter(
			// query);
			// return true;
			//
			// }
			// };
			// searchView.setOnQueryTextListener(textChangeListener);
			return true;
		}

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent tmp = new Intent(this,
					com.allegrookazje.menu.Menu_Activity.class);
			startActivity(tmp);
		}
		if (id == R.id.cel_sort) {
			Log.d("allegro", "sortowanie!!!");
			sortowanie();
		}
		if (id == R.id.cel_add) {
			Intent dodaj = new Intent(this, Add_Cel_Activity.class);
			startActivityForResult(dodaj, 10);
		}
		if (id == R.id.cel_edit) {
			Intent dodaj = new Intent(context, Add_Cel_Activity.class);
			dodaj.putExtra("cel", cel);
			startActivity(dodaj);
		}
		if (id == R.id.cel_delete) {
			alert_dialog_cel_delete();
		}
		return super.onOptionsItemSelected(item);
	}

	private void alert_dialog_cel_delete() {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Usuñ")
				.setMessage(
						"Czy jesteœ pewien, ¿e chcesz usun¹æ cel \""
								+ cel.nazwa + "\" ?")
				.setPositiveButton("Tak",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Item_Cel tmp = cel;
								klient.delete_row(tmp, context);
								vf_actual.setDisplayedChild(0);
								lista.clear();
								lista_adapter.notifyDataSetChanged();
								mTitle = getResources().getString(
										R.string.app_name);
								setTitle(mTitle);
								cel = null;
								invalidateOptionsMenu();
							}
						})
				.setNegativeButton("Nie",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.cancel();
							}
						});
		builder.show();
	}

	public static void sortowanie_switch(Context context, Dialog dialog,
			ArrayAdapter<Item_Lista> adapter, int checkedId) {
		if (dialog != null) {
			dialog.dismiss();
		}
		switch (checkedId) {
		case R.id.sort_radio0:
			sortowanie = 0;
			adapter.sort(new Comparator<Item_Lista>() {
				// sortowanie cena rosnaco
				public int compare(Item_Lista arg0, Item_Lista arg1) {
					Float price0;
					Float price1;
					if (cel.oferta_typu == 1) {
						price0 = arg0.buy_now_price;
						price1 = arg1.buy_now_price;
					} else {
						price0 = arg0.price;
						price1 = arg1.price;
					}
					return price0.compareTo(price1);
				}
			});
			break;
		case R.id.sort_radio1:
			sortowanie = 1;
			adapter.sort(new Comparator<Item_Lista>() {
				// sortowanie cena melejaco
				public int compare(Item_Lista arg0, Item_Lista arg1) {
					Float price0;
					Float price1;
					if (cel.oferta_typu == 1) {
						price0 = arg0.buy_now_price;
						price1 = arg1.buy_now_price;
					} else {
						price0 = arg0.price;
						price1 = arg1.price;
					}
					return price1.compareTo(price0);
				}
			});
			break;
		case R.id.sort_radio2:
			sortowanie = 2;
			adapter.sort(new Comparator<Item_Lista>() {
				// sortowanie cena z wysylka rosnaco
				public int compare(Item_Lista arg0, Item_Lista arg1) {
					Float price0;
					Float price1;
					if (cel.oferta_typu == 1) {
						price0 = arg0.buy_now_price + arg0.postage;
						price1 = arg1.buy_now_price + arg1.postage;
					} else {
						price0 = arg0.price + arg0.postage;
						price1 = arg1.price + arg1.postage;
					}
					return price0.compareTo(price1);
				}
			});
			break;
		case R.id.sort_radio3:
			sortowanie = 3;
			adapter.sort(new Comparator<Item_Lista>() {
				// sortowanie cena z wysylka malejaco
				public int compare(Item_Lista arg0, Item_Lista arg1) {
					Float price0;
					Float price1;
					if (cel.oferta_typu == 1) {
						price0 = arg0.buy_now_price + arg0.postage;
						price1 = arg1.buy_now_price + arg1.postage;
					} else {
						price0 = arg0.price + arg0.postage;
						price1 = arg1.price + arg1.postage;
					}
					return price1.compareTo(price0);
				}
			});
			break;
		case R.id.sort_radio4:
			sortowanie = 4;
			adapter.sort(new Comparator<Item_Lista>() {
				public int compare(Item_Lista arg0, Item_Lista arg1) {
					Long time0 = arg0.time_left;
					Long time1 = arg1.time_left;
					return time0.compareTo(time1);
				}
			});
			break;
		case R.id.sort_radio5:
			sortowanie = 5;
			adapter.sort(new Comparator<Item_Lista>() {
				public int compare(Item_Lista arg0, Item_Lista arg1) {
					Long time0 = arg0.time_left;
					Long time1 = arg1.time_left;
					return time1.compareTo(time0);
				}
			});
			break;
		case R.id.sort_radio6:
			sortowanie = 6;
			adapter.sort(new Comparator<Item_Lista>() {
				public int compare(Item_Lista arg0, Item_Lista arg1) {
					return arg0.name.compareTo(arg1.name);
				}
			});
			break;
		case R.id.sort_radio7:
			sortowanie = 7;
			adapter.sort(new Comparator<Item_Lista>() {
				public int compare(Item_Lista arg0, Item_Lista arg1) {
					return arg1.name.compareTo(arg0.name);
				}
			});
			break;
		}
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor edit = settings.edit();
		edit.putInt("sort_order", sortowanie);
		edit.commit();
	}

	public void sortowanie() {
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.v_sort);
		dialog.setTitle("Posortuj wed³ug:");
		RadioGroup wybor = (RadioGroup) dialog
				.findViewById(R.id.sortowanie_wybor);
		switch (sortowanie) {
		case 0:
			wybor.check(R.id.sort_radio0);
			break;
		case 1:
			wybor.check(R.id.sort_radio1);
			break;
		case 2:
			wybor.check(R.id.sort_radio2);
			break;
		case 3:
			wybor.check(R.id.sort_radio3);
			break;
		case 4:
			wybor.check(R.id.sort_radio4);
			break;
		case 5:
			wybor.check(R.id.sort_radio5);
			break;
		case 6:
			wybor.check(R.id.sort_radio6);
			break;
		case 7:
			wybor.check(R.id.sort_radio7);
			break;
		}
		wybor.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				sortowanie_switch(getApplicationContext(), dialog,
						lista_adapter, checkedId);
			}
		});
		Button anuluj = (Button) dialog.findViewById(R.id.v_sort_anuluj);
		anuluj.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	private String getRegistrationId(SharedPreferences settings, Context context) {
		String wynik;
		wynik = settings.getString("reg_id", "");
		if (wynik.isEmpty()) {
			return "";
		}
		return wynik;
	}

	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this, 9000)
						.show();
			} else {
				Log.i(TAG, "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			// ((MainActivity)
			// activity).onSectionAttached(getArguments().getInt(
			// ARG_SECTION_NUMBER));
		}
	}

}
