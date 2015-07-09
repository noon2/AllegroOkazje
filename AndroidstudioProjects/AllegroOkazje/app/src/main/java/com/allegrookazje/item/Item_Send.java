package com.allegrookazje.item;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.allegrookazje.items.Item_Lista;
import com.allegrookazje.items.Item_Shipment;
import com.example.allegrookazje.R;

public class Item_Send extends Fragment {
	Item_Lista item;

	public Item_Send() {

	}

	public Item_Send(Item_Lista item) {
		this.item = item;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.item_send, container, false);
		wypelnij(rootView);

		return rootView;
	}



	private void wypelnij(View v) {
		TextView tv_location = (TextView) v.findViewById(R.id.tv_location);
		tv_location.setText(item.location);
		// koszty dostawy
		TableLayout tl_koszty_dostawy = (TableLayout) v
				.findViewById(R.id.tl_koszty_dostawy);
		for (Item_Shipment a : item.postage_options) {
			TableRow row = new TableRow(getActivity());
			TextView nazwa = new TextView(getActivity());
			nazwa.setTextAppearance(getActivity(),
					android.R.style.TextAppearance_Small);
			nazwa.setGravity(Gravity.LEFT);
			nazwa.setSingleLine(false);
			nazwa.setText(a.name);
			row.addView(nazwa);

			TextView amount = new TextView(getActivity());
			amount.setTextAppearance(getActivity(),
					android.R.style.TextAppearance_Small);
			amount.setText(a.amount + "");
			amount.setGravity(Gravity.RIGHT);
			row.addView(amount);

			TextView amount_add = new TextView(getActivity());
			amount_add.setTextAppearance(getActivity(),
					android.R.style.TextAppearance_Small);
			amount_add.setText(a.amount_add + "");
			amount_add.setGravity(Gravity.RIGHT);
			row.addView(amount_add);

			TextView pack_size = new TextView(getActivity());
			pack_size.setTextAppearance(getActivity(),
					android.R.style.TextAppearance_Small);
			pack_size.setText(a.pack_size + "");
			pack_size.setGravity(Gravity.RIGHT);
			row.addView(pack_size);

			tl_koszty_dostawy.addView(row);
		}

		// formy platnosci
		LinearLayout ll_formy_platnosci = (LinearLayout) v
				.findViewById(R.id.ll_formy_platnosci);
		ll_formy_platnosci.removeAllViews();
		TextView option = null;
		if (item.payment.option_transfer == 1) {
			option = new TextView(getActivity());
			option.setTextAppearance(getActivity(),
					android.R.style.TextAppearance_Small);
			option.setText("Zwyk³y przelew");
			ll_formy_platnosci.addView(option);
		}
		if (item.payment.option_on_delivery == 1) {
			option = new TextView(getActivity());
			option.setTextAppearance(getActivity(),
					android.R.style.TextAppearance_Small);
			option.setText("P³atnoœæ przy odbiorze");
			ll_formy_platnosci.addView(option);
		}
		if (item.payment.option_allegro_pay == 1) {
			option = new TextView(getActivity());
			option.setTextAppearance(getActivity(),
					android.R.style.TextAppearance_Small);
			option.setText("PayU");
			ll_formy_platnosci.addView(option);
		}
		if (item.payment.option_see_desc == 1) {

		}
		// dodatkowe informacje
		LinearLayout ll_dodatkowe_informacje = (LinearLayout) v
				.findViewById(R.id.ll_formy_platnosci);
		ll_dodatkowe_informacje.removeAllViews();
	}

}
