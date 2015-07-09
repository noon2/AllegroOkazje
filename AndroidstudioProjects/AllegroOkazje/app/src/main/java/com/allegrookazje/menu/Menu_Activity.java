package com.allegrookazje.menu;

import java.security.NoSuchAlgorithmException;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import com.allegrookazje.Client;
import com.allegrookazje.DbAdapter;
import com.allegrookazje.MainActivity;
import com.example.allegrookazje.R;

public class Menu_Activity extends PreferenceActivity {
	private EditTextPreference et_pass;
	private CheckBoxPreference save_pass;
	private DbAdapter dbadapter;
	Client klient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		addPreferencesFromResource(R.xml.preferences);
		save_pass = (CheckBoxPreference) findPreference("save_password");
		Preference btn_category = findPreference("btn_category");
		et_pass = (EditTextPreference) findPreference("et_password");

		klient = MainActivity.klient;
		dbadapter = new DbAdapter(getApplicationContext());
		btn_category
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {

					@Override
					public boolean onPreferenceClick(Preference preference) {
						// TODO Auto-generated method stub
						if (!DbAdapter.is_open) {
							dbadapter.open();
						}
						dbadapter.upgrade(1);

						klient.doGetCatsDataCount(Menu_Activity.this);
						klient.doGetCountries(Menu_Activity.this);
						klient.doGetShipmentData(Menu_Activity.this);
						dbadapter.close();
						return false;
					}
				});
	}

	@Override
	protected void onDestroy() {
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		Editor edit = settings.edit();
		if (!save_pass.isChecked()
				&& settings.getString("et_password", "") == "") {
			try {
				edit.putString("et_password", Client.SHA256(et_pass.getText()));
			} catch (NoSuchAlgorithmException e) {
				Client.show_message(e.toString(), "Error",
						getApplicationContext(), true);
			}
		}
		else
		{
			edit.putString("et_password", "");
		}
		edit.commit();
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		onBackPressed();
		return true;
	}

}