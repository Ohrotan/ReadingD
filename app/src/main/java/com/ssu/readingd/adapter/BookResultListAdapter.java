package com.ssu.readingd.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
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
import java.util.List;

public class BookResultListAdapter extends BaseAdapter {
    Activity activity;
    ArrayList<BookSimpleDTO> list;
    ViewHolder viewholder;
    BookSimpleDTO selec;

    public BookResultListAdapter() {
        super();
    }

    public BookResultListAdapter(Activity activity) {
        super();
        this.activity = activity;
        list = new ArrayList<>();
    }

    public BookResultListAdapter(Activity activity, List<BookSimpleDTO> list) {
        this.activity = activity;
        this.list = (ArrayList<BookSimpleDTO>) list;
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

    public BookSimpleDTO getSelec() {
        return selec;
    }


    public void setSelec(int pos) {
        this.selec = list.get(pos);
    }

    public void setSelec(BookSimpleDTO selec) {
        this.selec = selec;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.layout_book_search_result_item, null);
        }
        viewholder = new ViewHolder();
        viewholder.img = convertView.findViewById(R.id.cover_img);
        viewholder.name = convertView.findViewById(R.id.name_tv);
        viewholder.authors = convertView.findViewById(R.id.authors_tv);
        viewholder.publisher = convertView.findViewById(R.id.publisher_tv);
        viewholder.date = convertView.findViewById(R.id.pub_date_tv);

        if (selec != null) {
            Log.v("selec", list.get(position).getBook_name() + "/" + selec.getBook_name());
        }
        if (list.get(position) != selec) {
            convertView.setBackgroundColor(Color.WHITE);
        } else {
            convertView.setBackgroundColor(Color.LTGRAY);
        }
        // viewholder.img.setImageResource(list.get(position).getImg());
        if (list.get(position).getImg() != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ImageViewFromURL.setImageView(activity, viewholder.img, list.get(position).getImg());
                }
            }).start();

        } else {
            viewholder.img.setImageResource(R.drawable.default_book);
        }
        viewholder.name.setText(list.get(position).getBook_name());
        String auth = list.get(position).getAuthor();
        if (list.get(position).getTranslator() != null)
            auth = auth + " \n " + list.get(position).getTranslator();
        viewholder.authors.setText(auth);
        viewholder.publisher.setText(list.get(position).getPublisher());
        viewholder.date.setText(list.get(position).getPub_date());

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
