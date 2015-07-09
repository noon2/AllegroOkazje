package com.allegrookazje;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.allegrookazje.items.Item_Category;
import com.allegrookazje.items.Item_Cel;
import com.allegrookazje.items.Item_Country;
import com.allegrookazje.items.Item_Shipment;
import com.allegrookazje.menu.Kate_Record;

public class DbAdapter {
	public static final String KEY_ID = "_id";
	public static final String KEY_NAME = "key_name";
	public static final String KEY_NAME_SEARCH = "key_name_search";
	public static final String KEY_MAX = "key_max";
	public static final String KEY_MIN = "key_min";
	public static final String KEY_CAT = "key_cat_id";
	public static final String KEY_CAT_NAME = "key_cat_name";
	public static final String KEY_CAT_COUNT = "key_cat_count";
	public static final String KEY_TYP_OFERTY = "key_typ_oferty";
	public static final String CAT_AI_ID = "_id";
	public static final String CAT_ID = "cat_id";
	public static final String CAT_NAME = "cat_name";
	public static final String CAT_PARENT = "cat_parent";
	public static final String CAT_POSITION = "cat_position";
	public static final String DATABASE_NAME = "baza.db";
	public static final String SQLITE_TABLE_CELE = "Cele";
	public static final String SQLITE_TABLE_CELE_DATE = "Cele_date";
	public static final String SQLITE_TABLE_CAT = "Kategorie";
	public static final String SQLITE_TABLE_LISTA = "Lista";
	public static final String SQLITE_TABLE_SHIP = "Wysylka";
	public static final String SQLITE_TABLE_COUNTRIES = "Kraje";
	public static final String CAT_ID_DATE = "id";
	public static final String CAT_END_DATE = "end_date";
	public static final String SH_ID = "sh_id";
	public static final String SH_NAME = "sh_name";
	public static final String SH_TYPE = "sh_type";
	public static final String SH_TIME_FROM = "sh_time_from";
	public static final String SH_TIME_TO = "sh_time_to";
	public static final String CT_ID = "ct_id";
	public static final String CT_NAME = "ct_name";

	public static boolean is_open = false;
	public static int DATABASE_VERSION = 155555;
	public int count_cele = 0;
	public int count_kate = 0;

	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	private final Context dbContex;

	private static final String DATABASE_CELE_CREATE = "CREATE TABLE if not exists "
			+ SQLITE_TABLE_CELE
			+ " ("
			+ KEY_ID
			+ " integer PRIMARY KEY autoincrement,"
			+ KEY_NAME
			+ " TEXT,"
			+ KEY_NAME_SEARCH
			+ " TEXT,"
			+ KEY_MIN
			+ " INTEGER,"
			+ KEY_MAX
			+ " INTEGER,"
			+ KEY_CAT
			+ " INTEGER,"
			+ KEY_CAT_NAME
			+ " TEXT,"
			+ KEY_TYP_OFERTY + " INTEGER," + KEY_CAT_COUNT + " INTEGER);";
	private static final String DATABASE_CELE_DATE = "CREATE TABLE if not exists "
			+ SQLITE_TABLE_CELE_DATE
			+ " ("
			+ KEY_ID
			+ " integer PRIMARY KEY autoincrement,"
			+ CAT_ID_DATE
			+ " INTEGER,"
			+ CAT_END_DATE
			+ " long);";
	private static final String DATABASE_CAT_CREATE = "CREATE TABLE if not exists "
			+ SQLITE_TABLE_CAT
			+ " ("
			+ CAT_AI_ID
			+ " integer PRIMARY KEY autoincrement,"
			+ CAT_ID
			+ " INTEGER,"
			+ CAT_NAME
			+ " TEXT,"
			+ CAT_PARENT
			+ " INTEGER,"
			+ CAT_POSITION
			+ " INTEGER);";
	private static final String DATABASE_SHIP_CREATE = "CREATE TABLE if not exists "
			+ SQLITE_TABLE_SHIP
			+ " ("
			+ KEY_ID
			+ " integer PRIMARY KEY autoincrement,"
			+ SH_ID
			+ " INTEGER,"
			+ SH_NAME
			+ " STRING,"
			+ SH_TYPE
			+ " INTEGER,"
			+ SH_TIME_FROM
			+ " INTEGER," + SH_TIME_TO + " INTEGER);";
	private static final String DATABASE_COUNTRIES_CREATE = "CREATE TABLE if not exists "
			+ SQLITE_TABLE_COUNTRIES
			+ " ("
			+ KEY_ID
			+ " integer PRIMARY KEY autoincrement,"
			+ CT_ID
			+ " INTEGER,"
			+ CT_NAME + " STRING);";

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CELE_CREATE);
			db.execSQL(DATABASE_CAT_CREATE);
			db.execSQL(DATABASE_SHIP_CREATE);
			db.execSQL(DATABASE_COUNTRIES_CREATE);
			db.execSQL(DATABASE_CELE_DATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE_CAT);
			db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE_COUNTRIES);
			db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE_SHIP);
			db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE_CELE_DATE);
			onCreate(db);
		}
	}

	public DbAdapter(Context ctx) {
		this.dbContex = ctx;
	}

	public DbAdapter open() throws SQLException {
		dbHelper = new DatabaseHelper(dbContex);
		db = dbHelper.getWritableDatabase();
		count_cele = getCountFromDb(SQLITE_TABLE_CELE);
		count_kate = getCountFromDb(SQLITE_TABLE_CAT);
		is_open = true;
		return this;
	}

	public void close() {
		if (dbHelper != null) {
			is_open = false;
			dbHelper.close();
		}
	}

	public int getCount() {
		return count_cele;
	}

	public void upgrade(int version) {
		dbHelper.onUpgrade(db, DATABASE_VERSION, version);
	}

	// zadania do tabeli cele
	public long createRowCele(Item_Cel t) {
		count_cele++;
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NAME, t.nazwa);
		initialValues.put(KEY_NAME_SEARCH, t.nazwa_wyszukiwania);
		initialValues.put(KEY_MIN, t.min);
		initialValues.put(KEY_MAX, t.max);
		initialValues.put(KEY_CAT, t.kategoria_id);
		initialValues.put(KEY_CAT_NAME, t.kategoria_nazwa);
		initialValues.put(KEY_TYP_OFERTY, t.oferta_typu);
		initialValues.put(KEY_CAT_COUNT, t.count);
		return db.insert(SQLITE_TABLE_CELE, null, initialValues);
	}

	public long deleteRowCele(Item_Cel t) {
		count_cele--;
		return db.delete(SQLITE_TABLE_CELE, CAT_AI_ID + " =?",
				new String[] { t.id + "" });
	}

	public long updateRowCele(Item_Cel t) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NAME, t.nazwa);
		initialValues.put(KEY_NAME_SEARCH, t.nazwa_wyszukiwania);
		initialValues.put(KEY_MIN, t.min);
		initialValues.put(KEY_MAX, t.max);
		initialValues.put(KEY_CAT, t.kategoria_id);
		initialValues.put(KEY_CAT_NAME, t.kategoria_nazwa);
		initialValues.put(KEY_TYP_OFERTY, t.oferta_typu);
		initialValues.put(KEY_CAT_COUNT, t.count);
		return db.update(SQLITE_TABLE_CELE, initialValues, CAT_AI_ID + " =?",
				new String[] { t.id + "" });
	}
	public long updateRowCeleCount(String nazwa,int count) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_CAT_COUNT, count);
		return db.update(SQLITE_TABLE_CELE, initialValues, CAT_NAME + " =?",
				new String[] { nazwa + "" });
	}
	public Item_Cel searchRowCeleId(String id) {
		Item_Cel wynik = null;
		String[] allColumns = new String[] { KEY_ID, KEY_NAME, KEY_NAME_SEARCH,
				KEY_MIN, KEY_MAX, KEY_CAT, KEY_CAT_NAME, KEY_TYP_OFERTY };
		Cursor c = db.query(SQLITE_TABLE_CELE, allColumns, KEY_ID + "= ?",
				new String[] { id }, null, null, null);
		if (c != null && c.moveToFirst()) {
			wynik = new Item_Cel(c.getString(1), c.getString(2), c.getInt(3),
					c.getInt(4), c.getInt(5), c.getString(6), c.getInt(7));
			wynik.id = c.getLong(0);
			c.close();
		}

		return wynik;
	}

	public Cursor readData() {
		String[] allColumns = new String[] { KEY_ID, KEY_NAME, KEY_CAT_COUNT };
		Cursor c = db.query(SQLITE_TABLE_CELE, allColumns, null, null, null,
				null, KEY_NAME);
		if (c != null) {
			c.moveToFirst();
		} else {
		}
		return c;
	}
	// zadania do tabeli cele data
	public long createRowCeleDate(int id,long date)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(CAT_ID_DATE, id);
		initialValues.put(CAT_END_DATE, date);
		return db.insert(SQLITE_TABLE_CELE_DATE, null, initialValues);
	}
	public long deleteRowCeleDate(int id)
	{
		return db.delete(SQLITE_TABLE_CELE_DATE, CAT_ID_DATE + " =?", new String[]{id+""});
	}
	public void refreshRowCeleDate()
	{

	}
	// zadania do tabeli kategorie
	public long createRowKate(Kate_Record t) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(CAT_ID, t.id);
		initialValues.put(CAT_NAME, t.name);
		initialValues.put(CAT_PARENT, t.parent);
		initialValues.put(CAT_POSITION, t.position);
		return db.insert(SQLITE_TABLE_CAT, null, initialValues);
	}

	public int getCountFromDb(String table) {
		int tmp = 0;
		Cursor kursor = db.rawQuery("SELECT COUNT(*) FROM " + table, null);
		if (kursor != null && kursor.moveToFirst()) {
			tmp = kursor.getInt(0);
		}
		kursor.close();
		return tmp;
	}

	public int searchRowKateId(String name) {
		int wynik = 0;
		Cursor c = db.query(SQLITE_TABLE_CAT, new String[] { CAT_ID }, CAT_NAME
				+ "= ?", new String[] { String.valueOf(name) }, null, null,
				null);
		if (c != null) {
			c.moveToFirst();
			wynik = c.getInt(0);
			c.close();
		}
		return wynik;
	}

	// zadania do tabeli kategorie
	public ArrayList<Item_Category> searchRowCatName(int pos) {
		ArrayList<Item_Category> lista = new ArrayList<Item_Category>();
		Cursor c = db.query(SQLITE_TABLE_CAT,
				new String[] { CAT_NAME, CAT_ID }, CAT_PARENT + "= ?",
				new String[] { String.valueOf(pos) }, null, null, CAT_POSITION);
		if (c != null && c.moveToFirst()) {
			Item_Category item = new Item_Category(c.getString(0), c.getInt(1),
					searchRowCatNext(c.getInt(1)));
			lista.add(item);
			while (c.moveToNext()) {
				item = new Item_Category(c.getString(0), c.getInt(1),
						searchRowCatNext(c.getInt(1)));
				lista.add(item);
			}
		}
		return lista;
	}

	public boolean searchRowCatNext(int pos) {
		boolean wynik = false;
		Cursor c = db.query(SQLITE_TABLE_CAT, new String[] { CAT_NAME },
				CAT_PARENT + "= ?", new String[] { String.valueOf(pos) }, null,
				null, CAT_POSITION);
		if (c != null && c.moveToFirst()) {
			wynik = true;
		}
		return wynik;
	}

	// zadania do tabeli kraje
	public long createRowCountry(Item_Country item) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(CT_ID, item.id);
		initialValues.put(CT_NAME, item.name);
		return db.insert(SQLITE_TABLE_COUNTRIES, null, initialValues);
	}

	public Cursor getRowCountry() {
		String[] allColumns = new String[] { CT_ID, CT_NAME };
		Cursor c = db.query(SQLITE_TABLE_COUNTRIES, allColumns, null, null,
				null, null, CT_ID);
		if (c != null) {
			c.moveToFirst();
		} else {
		}
		return c;
	}

	// zadania do tabeli wysylka
	public long createRowShipment(Item_Shipment item) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(SH_ID, item.id);
		initialValues.put(SH_NAME, item.name);
		initialValues.put(SH_TYPE, item.type);
		initialValues.put(SH_TIME_FROM, item.time_from);
		initialValues.put(SH_TIME_TO, item.time_to);
		return db.insert(SQLITE_TABLE_SHIP, null, initialValues);
	}

	public Item_Shipment getRowShipment(int id) {
		Item_Shipment wynik = new Item_Shipment();
		wynik.id = id;
		String[] allColumns = new String[] { SH_NAME, SH_TYPE };
		Cursor c = db.query(SQLITE_TABLE_SHIP, allColumns, SH_ID + "= ?",
				new String[] { id + "" }, null, null, null);
		if (c != null && c.moveToFirst()) {
			wynik.name = c.getString(0);
			wynik.type = c.getInt(1);
			c.close();
		}
		return wynik;
	}

}
