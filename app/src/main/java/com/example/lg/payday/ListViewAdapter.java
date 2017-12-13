package com.example.lg.payday;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by LG on 2017-12-04.
 */

public class ListViewAdapter extends BaseAdapter {
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>() ;

    ListViewAdapter(){

    }

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final int pos = i;
        final Context context = viewGroup.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_listview, viewGroup, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView titleTextView = (TextView) view.findViewById(R.id.tv_name) ;
        TextView totalTextView = (TextView) view.findViewById(R.id.tv_wage);
        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItem listViewItem = listViewItemList.get(i);

        // 아이템 내 각 위젯에 데이터 반영
        titleTextView.setText(listViewItem.getWorkSpace());
        totalTextView.setText(listViewItem.getWage());


        return view;
    }
    public void addItem(String name, String wage){
        ListViewItem item = new ListViewItem();

        item.setWorkSpace(name);
        item.setWage(wage);

        listViewItemList.add(item);
    }
    public void resetItem(){
        listViewItemList.clear();
    }
}
