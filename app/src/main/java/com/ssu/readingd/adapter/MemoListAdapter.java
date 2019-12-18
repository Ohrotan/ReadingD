package com.ssu.readingd.adapter;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ssu.readingd.R;
import com.ssu.readingd.dto.BookSimpleDTO;
import com.ssu.readingd.dto.MemoDTO;
import com.ssu.readingd.util.DBUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MemoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private ArrayList<MemoDTO> mData = new ArrayList<>();
    private ArrayList<BookSimpleDTO> bookData = new ArrayList<>();

    private Context context;

    private int prePosition = -1;
    Dialog dialog;

    private SparseBooleanArray selectedItems = new SparseBooleanArray();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public int adapter_type;
    final int memo_list_main = 0; // adapter_type = 0이면 layout_memo_list_main 레이아웃 뷰홀더 사용
    final int memo_search_result = 1; // adapter_type = 1이면 layout_memo_search_result 레이아웃 뷰홀더 사용
    final int community_main = 2; // adapter_type = 2이면 layout_community 레이아웃 뷰홀더 사용
    final int book_shelf = 3;


    public void addItem(MemoDTO data) {
        // 외부에서 item을 추가시킬 함수입니다.
        mData.add(data);
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        switch (adapter_type) {
            case memo_list_main:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_memo_list_main, parent, false);
                return new ViewHolder_MemoList(view);

            case memo_search_result:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_memo_search_result, parent, false);
                return new ViewHolder_SearchResult(view);

            case community_main:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_community, parent, false);
                return new ViewHolder_Community(view);


        }

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_memo_list_main, parent, false);
        return new ViewHolder_MemoList(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final MemoDTO model = mData.get(position);
        Glide.with(holder.itemView);

        switch(adapter_type) {
            case memo_list_main:
                ViewHolder_MemoList vh_m = (ViewHolder_MemoList) holder;
                vh_m.onBind(model, position);
                break;

            case memo_search_result:
                ViewHolder_SearchResult vh_r = (ViewHolder_SearchResult) holder;
                vh_r.onBind(model, position);
                break;

            case community_main:
                ViewHolder_Community vh_c = (ViewHolder_Community) holder;
                vh_c.onBind(model, position);
                break;

        }


        //holder.onBind(mData.get(position), position);
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }


    @Override
    public int getItemViewType(int position) {
        MemoDTO m = mData.get(position);


        if ( adapter_type == memo_list_main ) {
            return 0;
        } else if ( adapter_type == memo_search_result) {
            return 1;
        }
        else if( adapter_type == community_main){
            return 2;
        }
        else{
            return 0;
        }


    }



    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder_MemoList extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView bookName;
        private TextView bookWriter;
        private TextView bookDate;
        private TextView bookPage;
        private TextView memoContent_short;
        private TextView memoContent_long;
        public ImageView memoImage;
        public int imgIndex;
        public int imgcnt;
        private Spinner memoEditSpn;
        //private LinearLayout expandedArea;
        private LinearLayout roundLayout;
        private MemoDTO data;
        private int position;
        private String memo_id;

        ViewHolder_MemoList(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            bookName = itemView.findViewById(R.id.BookNameML) ;
            bookWriter = itemView.findViewById(R.id.BookWriterML) ;
            bookDate = itemView.findViewById(R.id.MemoDateML) ;
            bookPage = itemView.findViewById(R.id.BookPageML);
            memoContent_short = itemView.findViewById(R.id.contentML_short);
            memoContent_long = itemView.findViewById(R.id.contentML_long);
            memoImage = itemView.findViewById(R.id.pictureML);
            //expandedArea = itemView.findViewById(R.id.memoExpandArea);
            roundLayout = itemView.findViewById(R.id.round_layout);
            memoEditSpn = itemView.findViewById(R.id.memoEditSpinner);


        }

        @SuppressLint("ClickableViewAccessibility")
        void onBind(final MemoDTO data, int position) {
            this.data = data;
            this.position = position;
            this.memo_id = data.getMemo_id();

            final MemoDTO memoDTO = data;

            bookName.setText(data.getBook_name()) ;
            bookWriter.setText(data.getBook_author()) ;
            bookPage.setText(String.valueOf((data.getR_page())));
            bookDate.setText(data.getReg_date()) ;
            memoContent_short.setText(String.valueOf(data.getMemo_text()+ "short text"));
            memoContent_long.setText(String.valueOf(data.getMemo_text()+"long text"));
            imgIndex = 0;
            imgcnt = 0;
            if(data.getImg()!=null)
                imgcnt = data.getImg().size();
            setImageSwitcher(context, memoImage, imgIndex, data);

            memoImage.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    float x = event.getX();
                    float width = v.getX() + v.getWidth()/2;
                    if(x > width){
                        if(imgIndex < imgcnt -1)
                            imgIndex++;
                        setImageSwitcher(context, memoImage, imgIndex, data);
                    }
                    else{
                        if(imgIndex > 0)
                            imgIndex--;
                        setImageSwitcher(context, memoImage, imgIndex, data);
                    }
                    return true;
                }
            });


            changeVisibility(selectedItems.get(position));

            roundLayout.setOnClickListener(this);

            memoEditSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position ==0){
                        //수정일 때

                        //인텐트 보내서 수정화면으로 보내기

                    }
                    else{
                        //삭제일 때
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View dialogView = inflater.inflate(R.layout.layout_memo_delete, null);
                        Button memoDeleteCancelBtn = dialogView.findViewById(R.id.memoDeleteCancelBtn);
                        Button memoDeleteBtn = dialogView.findViewById(R.id.memoDeleteBtn);
                        builder.setView(dialogView);

                        memoDeleteCancelBtn.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(v.getContext(), "수정 버튼 클릭", Toast.LENGTH_SHORT).show();
                                Log.d("hs_test", "메모 ... 스피너 삭제 --> 취소 버튼 클릭");
                                dialog.dismiss();
                            }
                        });

                        memoDeleteBtn.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                new DBUtil().DeleteMemo(memo_id);
                                Toast.makeText(v.getContext(), "삭제 버튼 클릭", Toast.LENGTH_SHORT).show();
                                Log.d("hs_test", "메모 ... 스피너 삭제--> 확인 버튼 클릭");
                                dialog.dismiss();
                            }
                        });

                        dialog = builder.create();
                        dialog.show();



                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }

            });

        }

        @Override
        public void onClick(View v) {
            if(v==memoImage){
                if(imgIndex < imgcnt -1)
                    imgIndex++;
                else
                    imgIndex = 0;
                setImageSwitcher(context, memoImage, imgIndex, data);
                return;
            }

            else if (selectedItems.get(position)) {
                // 펼쳐진 Item을 클릭 시
                selectedItems.delete(position);

            } else {
                // 직전의 클릭됐던 Item의 클릭상태를 지움
                selectedItems.delete(prePosition);
                // 클릭한 Item의 position을 저장
                selectedItems.put(position, true);


            }
            // 해당 포지션의 변화를 알림
            if (prePosition != -1) notifyItemChanged(prePosition);
            notifyItemChanged(position);
            // 클릭된 position 저장
            prePosition = position;

        }

        private void changeVisibility(final boolean isExpanded) {
            // height 값을 dp로 지정해서 넣고싶으면 아래 소스를 이용
            int dpValue = 150;
            float d = context.getResources().getDisplayMetrics().density;
            int height = (int) (dpValue * d);

            // ValueAnimator.ofInt(int... values)는 View가 변할 값을 지정, 인자는 int 배열
            ValueAnimator va = isExpanded ? ValueAnimator.ofInt(0, height) : ValueAnimator.ofInt(height, 0);

            // Animation이 실행되는 시간, n/1000초
            va.setDuration(600);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    // value는 height 값
                    int value = (int) animation.getAnimatedValue();
                    // imageView의 높이 변경
                    //expandedArea.getLayoutParams().height = value;
                    //expandedArea.requestLayout();

                    //memoContent_long.getLayoutParams().height = value;
                    memoContent_long.requestLayout();

                    memoImage.getLayoutParams().height = value;
                    memoImage.requestLayout();
                    // imageView가 실제로 사라지게하는 부분

                    //expandedArea.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
                    memoContent_short.setVisibility(isExpanded ?  View.GONE :View.VISIBLE);
                    memoContent_long.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
                    memoImage.setVisibility(isExpanded ? View.VISIBLE : View.GONE);


                }
            });

            // Animation start
            va.start();
        }


    }


    public class ViewHolder_SearchResult extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView bookName;
        private TextView bookWriter;
        private TextView memoDate;
        private TextView bookPage;

        private TextView memoContent_short;
        private TextView memoContent_long;
        public ImageView memoImage;
        int imgIndex, imgcnt;

        //private LinearLayout expandedArea;
        private LinearLayout roundLayout;
        private Spinner memoEditSpinner;
        private String memo_id;
        private MemoDTO data;
        private int position;

        ViewHolder_SearchResult(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            bookName = itemView.findViewById(R.id.memoSearchResultTitle) ;
            bookWriter = itemView.findViewById(R.id.memoSearchResultAuthor) ;
            memoDate = itemView.findViewById(R.id.memoSearchResultDate) ;
            bookPage = itemView.findViewById(R.id.memoSearchResultPage);
            memoContent_short = itemView.findViewById(R.id.contentMSR_short);
            memoContent_long = itemView.findViewById(R.id.contentMSR_long);
            memoImage = itemView.findViewById(R.id.pictureML);
            //expandedArea = itemView.findViewById(R.id.memoExpandArea);
            roundLayout = itemView.findViewById(R.id.round_layout);
            memoEditSpinner = itemView.findViewById(R.id.memoEditSpinner);


        }

        @SuppressLint("ClickableViewAccessibility")
        void onBind(final MemoDTO data, int position) {
            this.data = data;
            this.position = position;
            this.memo_id = memo_id;

            bookName.setText(data.getBook_name()) ;
            bookWriter.setText(data.getBook_author()) ;
            bookPage.setText(String.valueOf((data.getR_page())));
            memoDate.setText(data.getReg_date()) ;
            memoContent_short.setText(String.valueOf(data.getMemo_text()+ "short text"));
            memoContent_long.setText(String.valueOf(data.getMemo_text()+"long text"));
            imgIndex = 0;
            imgcnt = 0;
            if(data.getImg()!=null)
                imgcnt = data.getImg().size();
            setImageSwitcher(context, memoImage, imgIndex, data);

            memoImage.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    float x = event.getX();
                    float width = v.getX() + v.getWidth()/2;
                    if(x > width){
                        if(imgIndex < imgcnt -1)
                            imgIndex++;
                        setImageSwitcher(context, memoImage, imgIndex, data);
                    }
                    else{
                        if(imgIndex > 0)
                            imgIndex--;
                        setImageSwitcher(context, memoImage, imgIndex, data);
                    }
                    return true;
                }
            });


            changeVisibility(selectedItems.get(position));

            roundLayout.setOnClickListener(this);

            memoEditSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position ==0){
                        //수정일 때

                        //인텐트 보내서 수정화면으로 보내기

                    }
                    else{
                        //삭제일 때
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View dialogView = inflater.inflate(R.layout.layout_memo_delete, null);
                        Button memoDeleteCancelBtn = dialogView.findViewById(R.id.memoDeleteCancelBtn);
                        Button memoDeleteBtn = dialogView.findViewById(R.id.memoDeleteBtn);
                        builder.setView(dialogView);

                        memoDeleteCancelBtn.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(v.getContext(), "수정 버튼 클릭", Toast.LENGTH_SHORT).show();
                                Log.d("hs_test", "메모 ... 스피너 삭제 --> 취소 버튼 클릭");
                                dialog.dismiss();
                            }
                        });

                        memoDeleteBtn.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                new DBUtil().DeleteMemo(memo_id);
                                Toast.makeText(v.getContext(), "삭제 버튼 클릭", Toast.LENGTH_SHORT).show();
                                Log.d("hs_test", "메모 ... 스피너 삭제--> 확인 버튼 클릭");
                                dialog.dismiss();
                            }
                        });

                        dialog = builder.create();
                        dialog.show();



                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }

            });
        }

        @Override
        public void onClick(View v) {

            if(v==memoImage){
                if(imgIndex < imgcnt -1)
                    imgIndex++;
                else
                    imgIndex=0;
                setImageSwitcher(context, memoImage, imgIndex, data);
                return;
            }

            else if (selectedItems.get(position)) {
                // 펼쳐진 Item을 클릭 시
                selectedItems.delete(position);

            } else {
                // 직전의 클릭됐던 Item의 클릭상태를 지움
                selectedItems.delete(prePosition);
                // 클릭한 Item의 position을 저장
                selectedItems.put(position, true);


            }
            // 해당 포지션의 변화를 알림
            if (prePosition != -1) notifyItemChanged(prePosition);
            notifyItemChanged(position);
            // 클릭된 position 저장
            prePosition = position;

        }

        private void changeVisibility(final boolean isExpanded) {
            // height 값을 dp로 지정해서 넣고싶으면 아래 소스를 이용
            int dpValue = 150;
            float d = context.getResources().getDisplayMetrics().density;
            int height = (int) (dpValue * d);

            // ValueAnimator.ofInt(int... values)는 View가 변할 값을 지정, 인자는 int 배열
            ValueAnimator va = isExpanded ? ValueAnimator.ofInt(0, height) : ValueAnimator.ofInt(height, 0);

            // Animation이 실행되는 시간, n/1000초
            va.setDuration(600);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    // value는 height 값
                    int value = (int) animation.getAnimatedValue();
                    // imageView의 높이 변경
                    //expandedArea.getLayoutParams().height = value;
                    //expandedArea.requestLayout();

                    //memoContent_long.getLayoutParams().height = value;
                    memoContent_long.requestLayout();

                    memoImage.getLayoutParams().height = value;
                    memoImage.requestLayout();
                    // imageView가 실제로 사라지게하는 부분

                    //expandedArea.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
                    memoContent_short.setVisibility(isExpanded ?  View.GONE :View.VISIBLE);
                    memoContent_long.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
                    memoImage.setVisibility(isExpanded ? View.VISIBLE : View.GONE);


                }
            });

            // Animation start
            va.start();
        }


    }

    public class ViewHolder_Community extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView BookNameView;
        TextView BookWriterView;
        TextView BookPageView;
        TextView BookDateView;
        TextView EmailView;
        TextView contentView;

        //private LinearLayout expandedArea;
        private LinearLayout roundLayout;
        private MemoDTO data;
        private int position;

        ViewHolder_Community(View itemView) {
            super(itemView) ;

            roundLayout = itemView.findViewById(R.id.round_layout);
            imageView = itemView.findViewById(R.id.communityPicture);
            BookNameView = itemView.findViewById(R.id.BookTitleCm);
            BookWriterView = itemView.findViewById(R.id.BookWriterCm);
            BookPageView = itemView.findViewById(R.id.BookPageCm) ;
            BookDateView = itemView.findViewById(R.id.BookDateCm) ;
            EmailView = itemView.findViewById(R.id.emailCm) ;
            contentView = itemView.findViewById(R.id.communityContent) ;

        }

        void onBind(MemoDTO data, int position) {
            this.data = data;
            this.position = position;

            BookNameView.setText(data.getBook_name()) ;
            BookWriterView.setText(data.getBook_author()) ;
            BookPageView.setText(String.valueOf((data.getR_page())));
            BookDateView.setText(data.getReg_date()) ;
            contentView.setText(data.getMemo_text());

            //changeVisibility(selectedItems.get(position));
            //roundLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (selectedItems.get(position)) {
                // 펼쳐진 Item을 클릭 시
                selectedItems.delete(position);
            } else {
                // 직전의 클릭됐던 Item의 클릭상태를 지움
                selectedItems.delete(prePosition);
                // 클릭한 Item의 position을 저장
                selectedItems.put(position, true);
            }
            // 해당 포지션의 변화를 알림
            if (prePosition != -1) notifyItemChanged(prePosition);
            notifyItemChanged(position);
            // 클릭된 position 저장
            prePosition = position;
        }
    }




    // 생성자에서 데이터 리스트 객체를 전달받음.
    public MemoListAdapter(Context context, ArrayList<MemoDTO> list, int adapter_type) {
        mData = list;
        this.context = context;
        this.adapter_type = adapter_type;


    }


    public MemoListAdapter(Context context, int adapter_type){
        this.context = context;
        this.adapter_type = adapter_type;
    }


    public void setImageSwitcher(final Context con, ImageView imageview, int imgIndex, MemoDTO data){
        List<String> imgs = data.getImg();
        String imgname = "default_image.jpg";
        int imgcnt = 0;
        imageview.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        if(imgs != null){
            imgcnt = imgs.size();
        }
        if(imgcnt != 0)
            imgname = imgs.get(imgIndex);
        if(!imgname.contains("jpg"))
            imgname = imgname+".PNG";
        StorageReference httpsReference = FirebaseStorage.getInstance()
                .getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/ssu-readingd.appspot.com/o/" + imgname);

        Task<Uri> uritask = httpsReference.getDownloadUrl();
        while(!uritask.isSuccessful()){;}
        Uri uri = uritask.getResult();
        Glide.with(con).load(uri).override(600,400).into(imageview);
    }


}
