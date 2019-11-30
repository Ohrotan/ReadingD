package com.ssu.readingd.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ssu.readingd.R;
import com.ssu.readingd.dto.BookSimpleDTO;
import com.ssu.readingd.util.ImageViewFromURL;

import java.util.ArrayList;

public class BookResultListAdapter extends BaseAdapter {
    Activity activity;
    ArrayList<BookSimpleDTO> list;
    ViewHolder viewholder;

    public BookResultListAdapter() {
        super();
    }

    public BookResultListAdapter(Activity activity) {
        super();
        this.activity = activity;
        list = new ArrayList<>();
    }

    public BookResultListAdapter(Activity activity, ArrayList<BookSimpleDTO> list) {
        this.activity = activity;
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
            convertView = LayoutInflater.from(activity).inflate(R.layout.layout_book_search_result_item, null);
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
        if (list.get(position).getImg() != null) {
            ImageViewFromURL.setImageView(activity, viewholder.img, list.get(position).getImg());
        }
        viewholder.name.setText(list.get(position).getBookName());
        viewholder.authors.setText(list.get(position).getAuthor() + " \n " + list.get(position).getTranslator() + " ");
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
