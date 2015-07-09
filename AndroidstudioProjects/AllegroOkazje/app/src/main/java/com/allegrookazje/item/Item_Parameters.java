package com.allegrookazje.item;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allegrookazje.items.Item_Lista;
import com.example.allegrookazje.R;

public class Item_Parameters extends Fragment {
	Item_Lista item;

	public Item_Parameters() {

	}

	public Item_Parameters(Item_Lista item) {
		this.item = item;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.item_parameters, container,
				false);
		wypelnij(rootView);

		return rootView;
	}

	private void wypelnij(View v) {
		LinearLayout ll_parameters = (LinearLayout) v
				.findViewById(R.id.ll_parameters);
		for (int i = 0; i != item.params_name.size(); i++) {
			TextView tv = new TextView(getActivity());
			tv.setTextAppearance(getActivity(),
					android.R.style.TextAppearance_Medium);
			tv.setText(Html.fromHtml("<b>" + item.params_name.get(i) + ":</b> "
					+ item.params_value1.get(i)));
			ll_parameters.addView(tv);
		}

	}

}
