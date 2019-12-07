package com.ssu.readingd.adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ssu.readingd.R;
import com.ssu.readingd.dto.MemoDTO;

import java.util.ArrayList;

public class MemoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private ArrayList<MemoDTO> mData = new ArrayList<>();

    private Context context;

    private int prePosition = -1;

    private SparseBooleanArray selectedItems = new SparseBooleanArray();

    public int adapter_type;

    final int memo_list_main = 0;
    final int memo_search_result = 1;
    final int community_main = 2;


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
        } else {
            return 1;
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
        private ImageView memoImage;
        //private LinearLayout expandedArea;
        private LinearLayout roundLayout;
        private MemoDTO data;
        private int position;

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

        }

        void onBind(MemoDTO data, int position) {
            this.data = data;
            this.position = position;

            Glide.with()



            bookName.setText(data.getBook_name()) ;
            bookWriter.setText(data.getBook_author()) ;
            bookPage.setText(String.valueOf((data.getR_page())));
            bookDate.setText(data.getReg_date()) ;
            memoContent_short.setText(String.valueOf(data.getMemo_text()+ "short text"));
            memoContent_long.setText(String.valueOf(data.getMemo_text()+"long text"));


            changeVisibility(selectedItems.get(position));

            roundLayout.setOnClickListener(this);
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
        private ImageView memoImage;

        //private LinearLayout expandedArea;
        private LinearLayout roundLayout;
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

        }

        void onBind(MemoDTO data, int position) {
            this.data = data;
            this.position = position;

            bookName.setText(data.getBook_name()) ;
            bookWriter.setText(data.getBook_author()) ;
            bookPage.setText(String.valueOf((data.getR_page())));
            memoDate.setText(data.getReg_date()) ;
            memoContent_short.setText(String.valueOf(data.getMemo_text()+ "short text"));
            memoContent_long.setText(String.valueOf(data.getMemo_text()+"long text"));


            changeVisibility(selectedItems.get(position));

            roundLayout.setOnClickListener(this);
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




}
