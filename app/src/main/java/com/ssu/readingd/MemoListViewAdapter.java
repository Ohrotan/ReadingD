package com.ssu.readingd;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ssu.readingd.util.CommunityItem;

import java.util.ArrayList;

public class MemoListViewAdapter extends BaseAdapter {

    private ArrayList<CommunityItem> items = new ArrayList<CommunityItem>() ;

    public void addItem(String title, String writer, int page, String date, String email, Drawable image, String content){

        CommunityItem item = new CommunityItem();

        item.setTitle(title);
        item.setWriter(writer);
        item.setPage(page);
        item.setDate(date);
        item.setEmail(email);
        item.setImage(image);
        item.setContent(content);

        items.add(item);
    }



    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_community, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView imageView = (ImageView) convertView.findViewById(R.id.communityPicture);
        TextView BookNameView = (TextView) convertView.findViewById(R.id.BookTitleCm);
        TextView BookWriterView = (TextView) convertView.findViewById(R.id.BookWriterCm);
        TextView BookPageView = (TextView) convertView.findViewById(R.id.BookPageCm) ;
        TextView BookDateView = (TextView) convertView.findViewById(R.id.BookDateCm) ;
        TextView EmailView = (TextView) convertView.findViewById(R.id.emailCm) ;
        TextView ContentView = (TextView) convertView.findViewById(R.id.communityContent) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        CommunityItem communityItem = items.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        imageView.setImageDrawable(communityItem.getImage());
        BookNameView.setText(communityItem.getTitle());
        BookWriterView.setText(communityItem.getWriter());
        BookPageView.setText(String.valueOf(communityItem.getPage()));
        BookDateView.setText(communityItem.getDate());
        EmailView.setText(communityItem.getEmail());
        ContentView.setText(communityItem.getContent());

        return convertView;
    }




}
