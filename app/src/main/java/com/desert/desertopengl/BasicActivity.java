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
        mList.add("基本图元");
        mList.add("2D图形");
        mList.add("3D图形");
        mList.add("纹理和光照");
        mList.add("Obj模型");
        mList.add("飘动效果");
        mRecyclerView.setAdapter(new BasicAdapter(this, mList));
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, this));
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent;
        switch (position) {
            case 0:
                intent = new Intent(this, BasicPelActivity.class);
                break;
            case 1:
                intent = new Intent(this, TwoDimenActivity.class);
                break;
            case 2:
                intent = new Intent(this, ThreeDimenActivity.class);
                break;
            case 3:
                intent = new Intent(this, TextureActivity.class);
                break;
            case 4:
                intent = new Intent(this, ObjActivity.class);
                break;
            case 5:
                intent = new Intent(this, EffectActivity.class);
                break;
            default:
                intent = new Intent(this, BasicPelActivity.class);
                break;
        }
        startActivity(intent);
    }
}
