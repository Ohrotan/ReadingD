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

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ssu.readingd.BookMemoListActivity;
import com.ssu.readingd.MemoRegisterActivity;
import com.ssu.readingd.R;
import com.ssu.readingd.dto.BookSimpleDTO;
import com.ssu.readingd.util.DBUtil;
import com.ssu.readingd.util.ImageViewFromURL;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class BookDeleteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<BookSimpleDTO> items;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //boolean delete = false;
    //Holder holder;
    private int displayWidth; //화면 크기
    private int size; //이미지 크기
    private int padding; //패딩
    boolean isDeleting = false;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_book_del_btn, null);

        // View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_book, parent, false);
        return new BookDeleteAdapter.ViewHolder_Grid(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final BookSimpleDTO book_model = items.get(position);
        Glide.with(holder.itemView);
        BookDeleteAdapter.ViewHolder_Grid vh_g = (BookDeleteAdapter.ViewHolder_Grid) holder;
        vh_g.onBind(book_model, position);
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
        View itemView;

        ViewHolder_Grid(View itemView) {
            super(itemView);
            this.itemView = itemView;
            // 뷰 객체에 대한 참조. (hold strong reference)
            book_image = itemView.findViewById(R.id.book_img);
            book_name_tv = itemView.findViewById(R.id.book_name);
            book_delete_button = itemView.findViewById(R.id.imagedelete_btn);
            book_delete_button.setVisibility(View.VISIBLE);
            book_layout = itemView.findViewById(R.id.book_layout);

        }

        void onBind(final BookSimpleDTO data, int position) {
            this.data = data;
            this.position = position;
            this.id = data.getId();

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            RelativeLayout deleteLayout = (RelativeLayout) layoutInflater.inflate(R.layout.layout_book_img_delete, null);


            book_name_tv.setText("del" + data.getBook_name());
            //ImageViewFromURL.setImageView((Activity) context, book_image, data.getImg());

            book_delete_button.setVisibility(View.VISIBLE);
            //if (delete) {
            // book_delete_button.setVisibility(View.VISIBLE);
            // Toast.makeText(context, "del", Toast.LENGTH_SHORT).show();
            book_delete_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DBUtil().deleteData("books", data.getId());
                    BookDeleteAdapter.this.notifyDataSetChanged();
                    itemView.setVisibility(View.GONE);
                }

            });

            //ImageView view = deleteLayout.findViewById(R.id.book_deleteBtn);
            //book_layout.addView(deleteLayout);
            //view.setVisibility(View.INVISIBLE);
            //deleteLayout.addView(view);
            if (data.getImg() == null || data.getImg().

                    equals("")) {
                book_image.setImageDrawable(context.getResources().getDrawable(R.drawable.default_book, null));
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ImageViewFromURL.setImageView((Activity) context, book_image, data.getImg());
                    }
                }).start();
            }
            book_name_tv.setText(data.getBook_name());
            // book_image.setOnClickListener(this);
           /* book_image.setOnLongClickListener(new View.OnLongClickListener() {
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
            */


        }

        @Override
        public void onClick(View v) {

            if (System.currentTimeMillis() <= btnPressTime + 1000) {

                Intent intent = new Intent(context, MemoRegisterActivity.class);
                intent.putExtra("book", data);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context.startActivity(intent);

            } else {
                Intent intent = new Intent(v.getContext(), BookMemoListActivity.class);
                intent.putExtra("book", data);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context.startActivity(intent);
            }


        }


    }

    public BookDeleteAdapter(Context context, ArrayList<BookSimpleDTO> list) {
        items = list;
        this.context = context;

    }

    public BookDeleteAdapter(Context context, ArrayList<BookSimpleDTO> list, boolean delete) {
        items = list;
        this.context = context;
        //this.delete = delete;
    }


}
