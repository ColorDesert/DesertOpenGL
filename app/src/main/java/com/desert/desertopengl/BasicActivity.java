package com.desert.desertopengl;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.desert.desertopengl.adapter.BasicAdapter;
import com.desert.desertopengl.callbacks.RecyclerItemClickListener;
import com.desert.desertopengl.constant.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by desert on 2017/12/10
 */

public class BasicActivity extends AppCompatActivity implements RecyclerItemClickListener.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private List<String> mList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mList = new ArrayList<>();
        mList.add("Point");
        mList.add("Line");
        mList.add("Triangle");
        mRecyclerView.setAdapter(new BasicAdapter(this, mList));
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, this));
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, BasicPelActivity.class);
        switch (position) {
            case 0:
                intent.putExtra(Constants.EVENT_BASIC_GRAPH_TYPE, Constants.BASIC_PEL_POINT);
                break;
            case 1:
                intent.putExtra(Constants.EVENT_BASIC_GRAPH_TYPE, Constants.BASIC_PEL_LINE);
                break;
            case 2:
                intent.putExtra(Constants.EVENT_BASIC_GRAPH_TYPE, Constants.BASIC_PEL_TRIANGLE);
                break;
        }
        startActivity(intent);
    }
}
