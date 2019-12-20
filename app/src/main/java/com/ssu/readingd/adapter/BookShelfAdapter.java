package com.ssu.readingd.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.ssu.readingd.BookMemoListActivity;
import com.ssu.readingd.MemoRegisterActivity;
import com.ssu.readingd.R;
import com.ssu.readingd.dto.BookSimpleDTO;
import com.ssu.readingd.util.ImageViewFromURL;

import java.util.ArrayList;

public class BookShelfAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<BookSimpleDTO> items;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    boolean delete = false;
    //Holder holder;
    private int displayWidth; //화면 크기
    private int size; //이미지 크기
    private int padding; //패딩
    boolean isDeleting = false;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(!delete){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_book, parent, false);
            return new BookShelfAdapter.ViewHolder_Grid(view);
        }
        else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_book, parent, false);
            return new BookShelfAdapter.ViewHolder_Grid_D(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
        BookSimpleDTO m = items.get(position);


        if (!delete) {
            return 0;
        }
        else {
            return 1;
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final BookSimpleDTO book_model = items.get(position);
        Glide.with(holder.itemView);

        if(!delete){
            BookShelfAdapter.ViewHolder_Grid vh_g = (BookShelfAdapter.ViewHolder_Grid) holder;
            vh_g.onBind(book_model, position);
        }
        else{
            BookShelfAdapter.ViewHolder_Grid_D vh_g_d = (BookShelfAdapter.ViewHolder_Grid_D) holder;
            vh_g_d.onBind(book_model, position);
        }



    }



    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder_Grid extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView book_image;
        private ImageView book_delete_button;
        private ConstraintLayout book_layout;
        private TextView book_name_tv;
        private BookSimpleDTO data;
        private int position;
        private String id;
        private long btnPressTime = 0;

        ViewHolder_Grid(View itemView) {
            super(itemView);

            // 뷰 객체에 대한 참조. (hold strong reference)
            book_image = itemView.findViewById(R.id.book_img);
            book_name_tv = itemView.findViewById(R.id.book_name);
            book_delete_button = itemView.findViewById(R.id.imagedelete_btn);
            book_delete_button.setVisibility(View.GONE);
            book_layout = itemView.findViewById(R.id.book_layout);

        }

        void onBind(final BookSimpleDTO data, int position) {
            this.data = data;
            this.position = position;
            this.id = data.getId();

            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            RelativeLayout deleteLayout = (RelativeLayout)layoutInflater.inflate(R.layout.layout_book_img_delete, null);


            book_name_tv.setText(data.getBook_name());
            //ImageViewFromURL.setImageView((Activity) context, book_image, data.getImg());


            if (delete) {
                book_delete_button.setVisibility(View.VISIBLE);
                book_delete_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }

            //ImageView view = deleteLayout.findViewById(R.id.book_deleteBtn);
            //book_layout.addView(deleteLayout);
            //view.setVisibility(View.INVISIBLE);
            //deleteLayout.addView(view);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    ImageViewFromURL.setImageView((Activity) context, book_image, data.getImg());
                }
            }).start();

            book_name_tv.setText(data.getBook_name());
            book_image.setOnClickListener(this);
            book_image.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v) {
                    Log.d("hs_test", "롱클릭");
                    Intent intent = new Intent(context, MemoRegisterActivity.class);
                    intent.putExtra("book", data);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    context.startActivity(intent);
                    return true;
                }
            });


        }

        @Override
        public void onClick(View v) {

            if (System.currentTimeMillis() <= btnPressTime + 1000) {

                Intent intent = new Intent(context, MemoRegisterActivity.class);
                intent.putExtra("book", data);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context.startActivity(intent);

            }
            else{
                Intent intent = new Intent(v.getContext(), BookMemoListActivity.class);
                intent.putExtra("book", data);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context.startActivity(intent);
            }



        }


    }

    public class ViewHolder_Grid_D extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView book_image;
        private ImageView book_delete_button;
        private ConstraintLayout book_layout;
        private TextView book_name_tv;
        private BookSimpleDTO data;
        private int position;
        private String id;
        private long btnPressTime = 0;

        ViewHolder_Grid_D(View itemView) {
            super(itemView);

            // 뷰 객체에 대한 참조. (hold strong reference)
            book_image = itemView.findViewById(R.id.book_img);
            book_name_tv = itemView.findViewById(R.id.book_name);
            book_delete_button = itemView.findViewById(R.id.imagedelete_btn);
            book_delete_button.setVisibility(View.GONE);
            book_layout = itemView.findViewById(R.id.book_layout);

        }

        void onBind(final BookSimpleDTO data, int position) {
            this.data = data;
            this.position = position;
            this.id = data.getId();

            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            RelativeLayout deleteLayout = (RelativeLayout)layoutInflater.inflate(R.layout.layout_book_img_delete, null);

            book_name_tv.setText(data.getBook_name());
            //ImageViewFromURL.setImageView((Activity) context, book_image, data.getImg());
            book_layout.addView(deleteLayout);


            //ImageView view = deleteLayout.findViewById(R.id.book_deleteBtn);
            //book_layout.addView(deleteLayout);
            //view.setVisibility(View.INVISIBLE);
            //deleteLayout.addView(view);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    ImageViewFromURL.setImageView((Activity) context, book_image, data.getImg());
                }
            }).start();

            book_image.setOnClickListener(this);
            book_image.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v) {
                    Log.d("hs_test", "롱클릭");
                    Intent intent = new Intent(context, MemoRegisterActivity.class);
                    intent.putExtra("book", data);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    context.startActivity(intent);
                    return true;
                }
            });


        }

        @Override
        public void onClick(View v) {

            if (System.currentTimeMillis() <= btnPressTime + 1000) {

                Intent intent = new Intent(context, MemoRegisterActivity.class);
                intent.putExtra("book", data);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context.startActivity(intent);

            }
            else{
                Intent intent = new Intent(v.getContext(), BookMemoListActivity.class);
                intent.putExtra("book", data);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context.startActivity(intent);
            }



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
