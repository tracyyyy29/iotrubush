package com.example.du.deepPart;

import java.util.List;

import com.example.du14.R;
import com.example.du.deepPart.*;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * NK2023 Embedded System Design Body IV Design Deep Part
 * 
 * ListView adapter: choice for different clocks
 * 
 * 
 * @author achdu0000<achdu0000@163.com>
 * 
 */
public class ListViewAdapter extends ArrayAdapter<ListViewItem>{
	private int resourceId;

	public ListViewAdapter(Context context, int textViewResourceId, List<ListViewItem> objects) {
		super(context, textViewResourceId, objects);
		resourceId=textViewResourceId;
	}
	public View getView(int position, View convertView, ViewGroup parent) {

		ListViewItem clockItem=getItem(position);
		View view=LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
		ImageView itemImage=(ImageView) view.findViewById(R.id.item_image);
		TextView itemText=(TextView) view.findViewById(R.id.item_text);
		itemImage.setImageResource(clockItem.getImageId());
		itemText.setText(clockItem.getName());
		if(Vars.getInstance().getMyListViewItemChoicePosition()==position){
			view.setBackgroundColor(Color.BLUE);
		}
		return view;
	}

}
