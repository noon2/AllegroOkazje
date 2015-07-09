package com.allegrookazje.add;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.allegrookazje.MainActivity;
import com.allegrookazje.items.Item_Cel;
import com.example.allegrookazje.R;

public class Add_Cel_Activity extends Activity {
	Item_Cel cel;
	EditText nazwa;
	EditText nazwa_wyszukiwania;
	EditText max;
	EditText min;
	TextView nazwa_kategori;
	Spinner sp_oferta_typu;
	int kategoria;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.cel_dodaj);
		nazwa = (EditText) findViewById(R.id.et_nazwa);
		nazwa_wyszukiwania = (EditText) findViewById(R.id.et_nazwa_wyszukiwania);
		max = (EditText) findViewById(R.id.et_max);
		min = (EditText) findViewById(R.id.et_min);
		nazwa_kategori = (TextView) findViewById(R.id.tv_nazwa_kategorii);
		kategoria = 0;
		sp_oferta_typu = (Spinner) findViewById(R.id.sp_wybor_oferty);
		List<String> list = new ArrayList<String>();
		list.add(getResources().getString(R.string.wszystkie));
		list.add(getResources().getString(R.string.kup_teraz));
		list.add(getResources().getString(R.string.licytacje));
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				R.layout.custom_spinner_layout, list);
		sp_oferta_typu.setAdapter(dataAdapter);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			cel = (Item_Cel) extras.getSerializable("cel");
			nazwa.setText(cel.nazwa);
			nazwa_wyszukiwania.setText(cel.nazwa_wyszukiwania);
			max.setText(cel.max + "");
			min.setText(cel.min + "");
			sp_oferta_typu.setSelection(cel.oferta_typu);
			kategoria = cel.kategoria_id;
			nazwa_kategori.setText(cel.kategoria_nazwa);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		onBackPressed();
		return true;
	}

	public void zapisz(View v) {
		String name = nazwa.getText().toString();
		String name_search = nazwa_wyszukiwania.getText().toString();
		int s_min = Integer.parseInt(min.getText().toString());
		int s_max = Integer.parseInt(max.getText().toString());
		String cat_name = nazwa_kategori.getText().toString();
		if (!name.equals("") && !nazwa_wyszukiwania.equals("")
				&& kategoria != 0 && !max.equals("MAX") && !min.equals("MIN")) {

			if (cel == null) {
				// tworzenie nowego celu
				Item_Cel row = new Item_Cel(name, name_search, s_min, s_max,
						kategoria, cat_name,
						sp_oferta_typu.getSelectedItemPosition());
				MainActivity.klient.create_row(row,
						MainActivity.mNavigationDrawerFragment.getActivity());
			} else {
				// aktualizacja celu
				long tmp = cel.id;
				cel = new Item_Cel(name, name_search, s_min, s_max, kategoria,
						cat_name, sp_oferta_typu.getSelectedItemPosition());
				cel.id = tmp;
				MainActivity.klient.update_row(cel,
						MainActivity.mNavigationDrawerFragment.getActivity());
			}
			
			finish();
		} else {
			Toast.makeText(getApplicationContext(),
					"Wprowad� poprawnie dane!", Toast.LENGTH_LONG).show();
		}

	}

	public void pobierz_kategorie(View v) {
		Intent intent = new Intent(this, Cele_Cats.class);
		startActivityForResult(intent, 20);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 20) {
			if (resultCode == 21) {
				kategoria = data.getExtras().getInt("kategoria_id");
				nazwa_kategori.setText(data.getExtras().getString(
						"kategoria_name"));
			}
		}
	}

}
