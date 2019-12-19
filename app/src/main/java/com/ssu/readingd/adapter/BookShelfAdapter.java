package com.ssu.readingd.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ssu.readingd.BookMemoListActivity;
import com.ssu.readingd.R;
import com.ssu.readingd.dto.BookSimpleDTO;
import com.ssu.readingd.util.ImageViewFromURL;

import java.util.ArrayList;

public class BookShelfAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<BookSimpleDTO> items;
    boolean delete = false;
    //Holder holder;
    private int displayWidth; //화면 크기
    private int size; //이미지 크기
    private int padding; //패딩

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_book, parent, false);
        return new BookShelfAdapter.ViewHolder_Grid(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final BookSimpleDTO book_model = items.get(position);
        Glide.with(holder.itemView);
        BookShelfAdapter.ViewHolder_Grid vh_g = (BookShelfAdapter.ViewHolder_Grid) holder;
        vh_g.onBind(book_model, position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder_Grid extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView book_image;
        private ConstraintLayout book_layout;
        private TextView book_name_tv;
        private BookSimpleDTO data;
        private int position;
        private String id;

        ViewHolder_Grid(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            book_image = itemView.findViewById(R.id.book_img);
            book_layout = itemView.findViewById(R.id.book_layout);
            book_name_tv = itemView.findViewById(R.id.book_name);

        }

        void onBind(BookSimpleDTO data, int position) {
            this.data = data;
            this.position = position;
            this.id = data.getId();

            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            RelativeLayout deleteLayout = (RelativeLayout)layoutInflater.inflate(R.layout.layout_book_img_delete, null);

            book_name_tv.setText(data.getBook_name());
            ImageViewFromURL.setImageView((Activity) context, book_image, data.getImg());
            //new DBUtil().setImageViewFromDB(context, book_image, data.getImg());
            //book_image.setImageResource(R.drawable.book_1);
            book_image.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(v.getContext(), BookMemoListActivity.class);
            intent.putExtra("book", data);
            context.startActivity(intent);

        }


    }

    public BookShelfAdapter(Context context, ArrayList<BookSimpleDTO> list) {
        items = list;
        this.context = context;

    }

    public BookShelfAdapter(Context context, ArrayList<BookSimpleDTO> list, boolean delete) {
        items = list;
        this.context = context;
        this.delete = delete;
    }


}
