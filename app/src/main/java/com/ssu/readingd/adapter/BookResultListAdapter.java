package com.ssu.readingd.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ssu.readingd.dto.BookSimpleDTO;
import com.ssu.readingd.R;

import java.util.ArrayList;

public class BookResultListAdapter extends BaseAdapter {
    Context context;
    ArrayList<BookSimpleDTO> list;
    ViewHolder viewholder;

    public BookResultListAdapter() {
        super();
    }

    public BookResultListAdapter(Context context) {
        super();
        this.context = context;
        list = new ArrayList<>();
        list.add(new BookSimpleDTO("book-1", null, "끌림", "이병률", "", "달", "2005.07.01"));
        list.add(new BookSimpleDTO("book-2", null, "총,균,쇠", "제레드 다이아몬드", "김진준", "문학사상사", "1998.08.08"));
        list.add(new BookSimpleDTO("book-3", null, "끌림", "이병률", "", "달", "2005.07.01"));
        list.add(new BookSimpleDTO("book-4", null, "총,균,쇠", "제레드 다이아몬드", "김진준", "문학사상사", "1998.08.08"));
        list.add(new BookSimpleDTO("book-5", null, "끌림", "이병률", "", "달", "2005.07.01"));
        list.add(new BookSimpleDTO("book-6", null, "총,균,쇠", "제레드 다이아몬드", "김진준", "문학사상사", "1998.08.08"));
        list.add(new BookSimpleDTO("book-7", null, "끌림", "이병률", "", "달", "2005.07.01"));
        list.add(new BookSimpleDTO("book-8", null, "총,균,쇠", "제레드 다이아몬드", "김진준", "문학사상사", "1998.08.08"));
    }

    public BookResultListAdapter(Context context, ArrayList<BookSimpleDTO> list) {
        this.context = context;
        this.list = list;
    }

    public ArrayList<BookSimpleDTO> getList() {
        return list;
    }

    public void setList(ArrayList<BookSimpleDTO> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_book_search_result_item, null);
            viewholder = new ViewHolder();
            viewholder.img = convertView.findViewById(R.id.cover_img);
            viewholder.name = convertView.findViewById(R.id.name_tv);
            viewholder.authors = convertView.findViewById(R.id.authors_tv);
            viewholder.publisher = convertView.findViewById(R.id.publisher_tv);
            viewholder.date = convertView.findViewById(R.id.pub_date_tv);

            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }

       // viewholder.img.setImageResource(list.get(position).getImg());
        viewholder.name.setText(list.get(position).getBookName());
        viewholder.authors.setText(list.get(position).getAuthor() + " 지음, " + list.get(position).getTranslator() + " 옮김");
        viewholder.publisher.setText(list.get(position).getPublisher());
        viewholder.date.setText(list.get(position).getPubDate());

        return convertView;
    }

    private class ViewHolder {

        ImageView img;
        TextView name;
        TextView authors;
        TextView publisher;
        TextView date;


    }
}
