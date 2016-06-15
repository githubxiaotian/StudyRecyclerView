package com.example.xiaotian.studyrecycleview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xiaotian.studyrecycleview.R;
import com.example.xiaotian.studyrecycleview.helper.ItemTouchHelper;
import com.example.xiaotian.studyrecycleview.helper.OnItemMoveListener;

import java.util.ArrayList;

/**
 * Created by xiaotian on 2016/6/13.
 */
public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnItemMoveListener {
    private ArrayList<String> mSelector;
    private ArrayList<String> mUnSelector;
    private static final int TYPE_TITLE_ITEM = 0;
    private static final int TYPE_ENTITY_ITEM = 1;
    private Context mContext;
    private ItemTouchHelper mHelper;
    private boolean mShowDeleteIcon;

    public MyAdapter(ArrayList<String> mSelector, ArrayList<String> mUnSelector, Context mContext, ItemTouchHelper mHelper) {
        this.mSelector = mSelector;
        this.mUnSelector = mUnSelector;
        this.mContext = mContext;
        this.mHelper = mHelper;
        mHelper.setOnItemLongPressListener(new ItemTouchHelper.OnItemLongPressListener() {
            @Override
            public void onItemLongPress(MotionEvent e) {
                mShowDeleteIcon = true;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ENTITY_ITEM:
                View entity = LayoutInflater.from(mContext).inflate(R.layout.entity_item, parent, false);
                EntityViewHolder entityViewHolder = new EntityViewHolder(entity);
                return entityViewHolder;
            case TYPE_TITLE_ITEM:
                RelativeLayout title = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.title_item, parent, false);
                return new TitleViewHolder(title);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (position == 0) {
            ((TitleViewHolder) holder).mTitleHeader.setText("我的收藏");
            if (mShowDeleteIcon) {
                ((TitleViewHolder) holder).mCompletion.setVisibility(View.VISIBLE);
            }
        } else if (position == mSelector.size() + 1) {
            ((TitleViewHolder) holder).mTitleHeader.setText("未收藏");
        } else if (position > 0 && position < mSelector.size() + 1) {

            EntityViewHolder holder1 = (EntityViewHolder) holder;
            String text = mSelector.get(position - 1);
            holder1.mTitle.setText(text);
            holder1.mDeleteIcon.setTag(text);
            Log.d("xiaotian", "onBindViewHolder: ----go?");
            if (mShowDeleteIcon) {
                holder1.mDeleteIcon.setVisibility(View.VISIBLE);
            }
        } else if (position > mSelector.size() + 1) {
            ((EntityViewHolder) holder).mTitle.setText(mUnSelector.get(position - mSelector.size() - 2));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == mSelector.size() + 1) {
            return TYPE_TITLE_ITEM;
        }
        return TYPE_ENTITY_ITEM;
    }

    @Override
    public int getItemCount() {
        return mSelector.size() + mUnSelector.size() + 2;
    }


    @Override
    public void onMove(int fromPosition, int toPosition) {
        if (fromPosition < mSelector.size() + 1 && toPosition < mSelector.size() + 1 && toPosition > 0) {
            String s = mSelector.get(fromPosition - 1);
            mSelector.remove(fromPosition - 1);
            mSelector.add(toPosition - 1, s);
            notifyItemMoved(fromPosition, toPosition);
        } else if (fromPosition < mSelector.size() + 1 && toPosition > mSelector.size() + 1) {
            String s = mSelector.get(fromPosition - 1);
            mSelector.remove(fromPosition - 1);
            mUnSelector.add(toPosition - mSelector.size() - 2, s);
            notifyItemMoved(fromPosition, toPosition);
        } else if (fromPosition > mSelector.size() + 1 && toPosition > mSelector.size() + 1) {
            String s = mUnSelector.get(fromPosition - mSelector.size() - 2);
            mUnSelector.remove(fromPosition - mSelector.size() - 2);
            mUnSelector.add(toPosition - mSelector.size() - 2, s);
            notifyItemMoved(fromPosition, toPosition);
        } else if (fromPosition > mSelector.size() + 1 && toPosition < mSelector.size() + 1 && toPosition > 0) {
            String s = mUnSelector.get(fromPosition - mSelector.size() - 2);
            mUnSelector.remove(fromPosition - mSelector.size() - 2);
            mSelector.add(toPosition - 1, s);
            notifyItemMoved(fromPosition, toPosition);
        }
    }

    private class EntityViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle;
        ImageView mDeleteIcon;

        public EntityViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            mTitle = (TextView) itemView.findViewById(R.id.tv_title);
            mDeleteIcon = (ImageView) itemView.findViewById(R.id.iv_delete_icon);
            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (!mShowDeleteIcon) {
                        mHelper.startDrag(EntityViewHolder.this);
                    }
                    return false;
                }
            });
            mDeleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tag = (String) mDeleteIcon.getTag();
                    Log.d("xiaotian", "tag-------------->" + tag);
                    mDeleteIcon.setVisibility(View.INVISIBLE);
                    mSelector.remove(tag);
                    mUnSelector.add(tag);

                    notifyDataSetChanged();
                }
            });
        }

    }

    private class TitleViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitleHeader;
        public TextView mCompletion;

        public TitleViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            mTitleHeader = (TextView) itemView.findViewById(R.id.tv_title);
            mCompletion = (TextView) itemView.findViewById(R.id.tv_completion);
            mCompletion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mShowDeleteIcon = false;
                    mCompletion.setVisibility(View.INVISIBLE);
                    notifyItemRangeChanged(1, mSelector.size(), null);
                }
            });
        }
    }
}
