package com.example.xiaotian.studyrecycleview;

import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;

import com.example.xiaotian.studyrecycleview.adapter.MyAdapter;
import com.example.xiaotian.studyrecycleview.helper.ItemDragCallBack;
import com.example.xiaotian.studyrecycleview.helper.ItemTouchHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<String> mSelector;
    private ArrayList<String> mUnSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.ry_recyclerview);
        initData();
       ;
    }

    private void initData() {
        mSelector = new ArrayList<String>();
        mUnSelector = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            mSelector.add("小田" + i);
        }
        for (int i = 0; i < 10; i++) {
            mUnSelector.add("小王" + i);
        }
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        GridLayoutManager glm = new GridLayoutManager(this,3);
        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0 || position == mSelector.size() + 1) {
                    return 3;
                }
                return 1;
            }

            ;
        });
        ItemDragCallBack callBack = new ItemDragCallBack();
        ItemTouchHelper helper = new ItemTouchHelper(callBack);
        MyAdapter mAdapter = new MyAdapter(mSelector, mUnSelector, this, helper);
        helper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setLayoutManager(glm);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(true);
        mRecyclerView.setAdapter(mAdapter);

    }



}
