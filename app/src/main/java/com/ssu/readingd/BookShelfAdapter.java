package com.ssu.readingd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class BookShelfAdapter extends BaseAdapter {

    Context context;
    ArrayList<BookItem> items;
    Holder holder;
    private int displayWidth; //화면 크기
    private int size; //이미지 크기
    private int padding; //패딩
    private int margin;

    public BookShelfAdapter(){
        super();
    }

    public BookShelfAdapter(Context context, int displayWidth) {
        super();

        this.context = context;
        items = new ArrayList<BookItem>();

        this.displayWidth = displayWidth;
        size = displayWidth/3 ;  //화면크기를 / 3으로 나누어서 이미지 사이즈를 구한다.
        padding = 50;
        margin = 10;
        System.out.println("size="+size);
    }

    public BookShelfAdapter(Context context, ArrayList<BookItem> items, int displayWidth) {
        this.context = context;
        this.items = items;

        this.displayWidth = displayWidth;
        size = displayWidth/3 ;  //화면크기를 / 3으로 나누어서 이미지 사이즈를 구한다.
        padding = 20;
        margin = 10;
        System.out.println("size="+size);

    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void addItem(BookItem item){
        items.add(item);
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
        ImageView imageView;
        if(convertView == null){
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(size, size)); //85,85
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(padding, padding, padding, padding);


        }else{
            imageView = (ImageView) convertView;
        }
        //이미지뷰에 주어진 위치의 이미지를 설정함
        imageView.setImageResource(items.get(position).getImage());
        return imageView;

    }

    public class Holder {

        public ImageView img;


    }

}
