package com.allegrookazje.item;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allegrookazje.items.Item_Feedback;
import com.allegrookazje.items.Item_User;
import com.example.allegrookazje.R;

public class Item_Seller extends Fragment {
	Item_User user;
	View v;
	boolean loading;
	public Item_Seller()
	{
		
	}
	public Item_Seller(Item_User user) {
		this.user=user;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		loading = true;
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater
				.inflate(R.layout.item_seller, container, false);
		v = rootView;
		wypelnij();
		return rootView;
	}

	public void wypelnij() {
		loading = false;
		TextView tv_poss_comment = (TextView) v
				.findViewById(R.id.tv_poss_comment);
		String ocena = "100";
		if (user.neutral.all != 0 || user.negative.all != 0) {
			float tmp = (user.rating - (user.negative.all + user.neutral.all))
					/ (float) user.rating;
			tmp *= 100;
			ocena = String.format("%.1f", tmp);
		}

		tv_poss_comment.setText(ocena + "% pozytywnych komenatrzy");

		// oceny rating bar
		TextView tv_average = (TextView) v.findViewById(R.id.tv_avarage);
		TextView tv_sell_rating_info = (TextView) v
				.findViewById(R.id.tv_sell_rating_info);

		if (user.sell_rating_count < 10) {
			tv_average.setText("Œrednia z ocen sprzeda¿y");
			tv_sell_rating_info
					.setText("Bêdzei widoczna, gdy sprzedawca otrzyma co najmniej 10 ocen");
			RelativeLayout rl_ocena1 = (RelativeLayout) v
					.findViewById(R.id.rl_ocena1);
			RelativeLayout rl_ocena2 = (RelativeLayout) v
					.findViewById(R.id.rl_ocena2);
			RelativeLayout rl_ocena3 = (RelativeLayout) v
					.findViewById(R.id.rl_ocena3);
			RelativeLayout rl_ocena4 = (RelativeLayout) v
					.findViewById(R.id.rl_ocena4);
			rl_ocena1.setVisibility(View.GONE);
			rl_ocena2.setVisibility(View.GONE);
			rl_ocena3.setVisibility(View.GONE);
			rl_ocena4.setVisibility(View.GONE);

		} else {
			RatingBar rb_ocena1 = (RatingBar) v.findViewById(R.id.rb_ocena1);
			RatingBar rb_ocena2 = (RatingBar) v.findViewById(R.id.rb_ocena2);
			RatingBar rb_ocena3 = (RatingBar) v.findViewById(R.id.rb_ocena3);
			RatingBar rb_ocena4 = (RatingBar) v.findViewById(R.id.rb_ocena4);
			TextView tv_ocena1 = (TextView) v.findViewById(R.id.tv_ocena1);
			TextView tv_ocena2 = (TextView) v.findViewById(R.id.tv_ocena2);
			TextView tv_ocena3 = (TextView) v.findViewById(R.id.tv_ocena3);
			TextView tv_ocena4 = (TextView) v.findViewById(R.id.tv_ocena4);
			tv_sell_rating_info.setVisibility(View.GONE);
			tv_average.setText("Œrednia z " + user.sell_rating_count
					+ " ocen sprzeda¿y");
			Float rating = user.rating_average.get(0).rating_average;
			rb_ocena1.setRating(rating);
			tv_ocena1.setText(rating + "");
			rating = user.rating_average.get(1).rating_average;
			rb_ocena2.setRating(rating);
			tv_ocena2.setText(rating + "");
			rating = user.rating_average.get(2).rating_average;
			rb_ocena3.setRating(rating);
			tv_ocena3.setText(rating + "");
			rating = user.rating_average.get(3).rating_average;
			rb_ocena4.setRating(rating);
			tv_ocena4.setText(rating + "");
		}

		// komentarze pozytywne
		Item_Feedback feedback = user.positive;
		TextView tv_poss_7 = (TextView) v.findViewById(R.id.tv_poss_7);
		TextView tv_poss_30 = (TextView) v.findViewById(R.id.tv_poss_30);
		TextView tv_poss_all = (TextView) v.findViewById(R.id.tv_poss_all);
		TextView tv_poss_buy_sell = (TextView) v
				.findViewById(R.id.tv_poss_buy_sell);

		tv_poss_7.setText(feedback.last_week + "");
		tv_poss_30.setText(feedback.last_month + "");
		tv_poss_all.setText(feedback.all + "");
		tv_poss_buy_sell
				.setText(feedback.buy_items + "/" + feedback.sold_items);
		// komentarze neutralne
		feedback = user.neutral;
		TextView tv_neutral_7 = (TextView) v.findViewById(R.id.tv_neutral_7);
		TextView tv_neutral_30 = (TextView) v.findViewById(R.id.tv_neutral_30);
		TextView tv_neutral_all = (TextView) v
				.findViewById(R.id.tv_neutral_all);
		TextView tv_neutral_buy_sell = (TextView) v
				.findViewById(R.id.tv_neutral_buy_sell);

		tv_neutral_7.setText(feedback.last_week + "");
		tv_neutral_30.setText(feedback.last_month + "");
		tv_neutral_all.setText(feedback.all + "");
		tv_neutral_buy_sell.setText(feedback.buy_items + "/"
				+ feedback.sold_items);
		// komentarze negatywne
		feedback = user.negative;
		TextView tv_negative_7 = (TextView) v.findViewById(R.id.tv_negative_7);
		TextView tv_negative_30 = (TextView) v
				.findViewById(R.id.tv_negative_30);
		TextView tv_negative_all = (TextView) v
				.findViewById(R.id.tv_negative_all);
		TextView tv_negative_buy_sell = (TextView) v
				.findViewById(R.id.tv_negative_buy_sell);
		tv_negative_7.setText(feedback.last_week + "");
		tv_negative_30.setText(feedback.last_month + "");
		tv_negative_all.setText(feedback.all + "");
		tv_negative_buy_sell.setText(feedback.buy_items + "/"
				+ feedback.sold_items);
	}

}
