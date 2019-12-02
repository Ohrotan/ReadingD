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

import androidx.recyclerview.widget.RecyclerView;

import com.ssu.readingd.R;
import com.ssu.readingd.dto.MemoListDTO;

import java.util.ArrayList;

public class MemoListAdapter extends RecyclerView.Adapter<MemoListAdapter.ViewHolder> {


    private ArrayList<MemoListDTO> mData = new ArrayList<>();

    private Context context;

    private int prePosition = -1;

    private SparseBooleanArray selectedItems = new SparseBooleanArray();


    public void addItem(MemoListDTO data) {
        // 외부에서 item을 추가시킬 함수입니다.
        mData.add(data);
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public MemoListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        context = parent.getContext() ;
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
//
//        View view = inflater.inflate(R.layout.layout_memo_list_main, parent, false) ;
//        MemoListAdapter.ViewHolder vh = new MemoListAdapter.ViewHolder(view);
//
//        vh.itemView.setTag(vh);
//        return vh ;

        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_memo_list_main, parent, false);
        return new ViewHolder(view);

    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }


    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(MemoListAdapter.ViewHolder holder, int position) {
        holder.onBind(mData.get(position), position);
    }


    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView bookName;
        private TextView bookWriter;
        private TextView bookDate;
        private TextView bookPage;
        private TextView memoContent_short;
        private TextView memoContent_long;
        private ImageView memoImage;
        private LinearLayout expandedArea;
        private LinearLayout roundLayout;
        private MemoListDTO data;
        private int position;

        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            bookName = itemView.findViewById(R.id.BookNameML) ;
            bookWriter = itemView.findViewById(R.id.BookWriterML) ;
            bookDate = itemView.findViewById(R.id.MemoDateML) ;
            bookPage = itemView.findViewById(R.id.BookPageML);
            memoContent_short = itemView.findViewById(R.id.contentML_short);
            memoContent_long = itemView.findViewById(R.id.contentML_long);
            memoImage = itemView.findViewById(R.id.pictureML);
            expandedArea = itemView.findViewById(R.id.memoExpandArea);
            roundLayout = itemView.findViewById(R.id.round_layout);

        }

        void onBind(MemoListDTO data, int position) {
            this.data = data;
            this.position = position;

//            bookName.setText(data.getBookName()) ;
//            bookWriter.setText(data.getAuthor()) ;
//            bookPage.setText(data.getMemoPage()) ;
//            bookDate.setText(data.getMemoRegDate()) ;
//            memoContent.setText(data.getMemoContent());

            bookName.setText("제목") ;
            bookWriter.setText("작가") ;
            bookPage.setText("페이지") ;
            bookDate.setText("날짜") ;
            memoContent_short.setText("내용");
            memoContent_long.setText("내용32352342532535");


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

                //memoContent_short.setVisibility(View.GONE);
                //memoContent_long.setVisibility(View.VISIBLE);

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
                    expandedArea.getLayoutParams().height = value;
                    expandedArea.requestLayout();
                    // imageView가 실제로 사라지게하는 부분
                    expandedArea.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
                    memoContent_short.setVisibility(isExpanded ? View.GONE : View.VISIBLE);
                    memoContent_long.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
                }
            });

            // Animation start
            va.start();
        }


    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    public MemoListAdapter(Context context, ArrayList<MemoListDTO> list) {
        mData = list;
        this.context = context;
    }

    public MemoListAdapter(Context context){
        this.context = context;
    }







}
