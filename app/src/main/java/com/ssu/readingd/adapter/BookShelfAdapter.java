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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ssu.readingd.BookMemoListActivity;
import com.ssu.readingd.MemoRegisterActivity;
import com.ssu.readingd.R;
import com.ssu.readingd.dto.BookSimpleDTO;
import com.ssu.readingd.dto.MemoDTO;
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
        private long btnPressTime = 0;

        ViewHolder_Grid(View itemView) {
            super(itemView);

            // 뷰 객체에 대한 참조. (hold strong reference)
            book_image = itemView.findViewById(R.id.book_img);
            book_name_tv = itemView.findViewById(R.id.book_name);

        }

        void onBind(final BookSimpleDTO data, int position) {
            this.data = data;
            this.position = position;
            this.id = data.getId();

            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            RelativeLayout deleteLayout = (RelativeLayout)layoutInflater.inflate(R.layout.layout_book_img_delete, null);

            book_name_tv.setText(data.getBook_name());
            ImageViewFromURL.setImageView((Activity) context, book_image, data.getImg());


            if (delete) {
                deleteLayout.setVisibility(View.VISIBLE);
                //book_layout.addView(deleteLayout);
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    ImageViewFromURL.setImageView((Activity) context, book_image, data.getImg());
                }
            }).start();


            //new DBUtil().setImageViewFromDB(context, book_image, data.getImg());
            //book_image.setImageResource(R.drawable.book_1);
            book_image.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if (System.currentTimeMillis() <= btnPressTime + 1000) {

                db.collection("memos")
                        .whereEqualTo("memo", data.getBook_name())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        Intent intent = new Intent(context, MemoRegisterActivity.class);
                                        MemoDTO memo = document.toObject(MemoDTO.class);
                                        intent.putExtra("memo", memo);
                                        context.startActivity(intent);

                                        break;
                                    }
                                } else {
                                    //Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });

            }
            else{
                Intent intent = new Intent(v.getContext(), BookMemoListActivity.class);
                intent.putExtra("book", data);
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
