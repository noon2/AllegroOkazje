package com.allegrookazje.add;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.allegrookazje.items.Item_Category;
import com.example.allegrookazje.R;

public class Cele_Cats_Adapter extends ArrayAdapter<Item_Category> {

	private final Context context;
	private final ArrayList<Item_Category> all_items;
	ArrayList<Item_Category> lista;
	Cele_Cats cele_cats;

	public Cele_Cats_Adapter(Context context,
			ArrayList<Item_Category> itemsArrayList) {

		super(context, R.layout.cats_item, itemsArrayList);
		this.context = context;
		cele_cats = (Cele_Cats) context;
		this.all_items = itemsArrayList;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View rowView = convertView;
		ViewHolderAdd holder = null;
		Item_Category item = all_items.get(position);
		if (rowView == null) {
			holder = new ViewHolderAdd();
			// 1. Create inflater
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			// 2. Get rowView from inflater
			rowView = inflater.inflate(R.layout.cats_item, parent, false);

			// 3. Get the two text view from the rowView
			holder.name = (TextView) rowView.findViewById(R.id.cat_name);
			holder.next = (TextView) rowView.findViewById(R.id.cat_next);
			rowView.setTag(holder);
		} else {
			holder = (ViewHolderAdd) rowView.getTag();
		}

		// 4. Set the text for textView
		holder.name.setText(all_items.get(position).name);
		if (!item.next) {
			holder.next.setVisibility(View.INVISIBLE);
		} else {
			holder.next.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					cele_cats.next(all_items.get(position).id);

				}
			});
		}
		holder.name.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cele_cats.select(all_items.get(position));

			}
		});

		return rowView;
	}
}
