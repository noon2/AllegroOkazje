package com.allegrookazje.item;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.allegrookazje.items.Item_Lista;
import com.allegrookazje.items.Item_User;
 
public class TabsPagerAdapter extends FragmentPagerAdapter {
 
	private Item_Lista item;
	private Item_User user;
    public TabsPagerAdapter(FragmentManager fm,Item_Lista item,Item_User user) {
        super(fm);
        this.item=item;
        this.user=user;
    }
 
    @Override
    public Fragment getItem(int index) {
        switch (index) {
        case 0:
            // Top Rated fragment activity
            return new Item_Desc(item);
        case 1:
            // Games fragment activity
            return new Item_Send(item);
        case 2:
            // Movies fragment activity
            return new Item_Parameters(item);
        case 3:
        	return new Item_Seller(user);
        }
 
        return null;
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 4;
    }
 
}