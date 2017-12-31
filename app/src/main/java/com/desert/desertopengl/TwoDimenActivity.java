package com.desert.desertopengl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.desert.desertopengl.constant.Constants;
import com.desert.desertopengl.graph.dimensional.two.AreOctagon;
import com.desert.desertopengl.graph.dimensional.two.Circle;
import com.desert.desertopengl.graph.dimensional.two.CopperCash;
import com.desert.desertopengl.graph.dimensional.two.CopperCashLine;
import com.desert.desertopengl.interfaces.IDrawBasicPel;
import com.desert.desertopengl.view.BasicGLView;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by desert on 2017/12/26
 */

public class TwoDimenActivity extends AppCompatActivity implements IDrawBasicPel {
    private BasicGLView mBasicGLView;
    private Circle mCircle;
    private CopperCashLine mCopperCashLine;
    private CopperCash mCopperCash;
    private AreOctagon mAreOctagon;
    private int mType = Constants.TWO_DIMENSIONAL_CIRCLE;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_dimensional);
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle("2D图形");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mBasicGLView = findViewById(R.id.view);
        mCircle=new Circle();
        mBasicGLView.setIDrawBasicPel(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBasicGLView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBasicGLView.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_two_dimensoinal, menu);
        MenuItem item= menu.getItem(0);
        item.setChecked(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.circle:
                mType = Constants.TWO_DIMENSIONAL_CIRCLE;
                if (mCircle==null){
                    mCircle=new Circle();
                }
                break;
            case R.id.copper_line:
                mType = Constants.TWO_DIMENSIONAl_COPPER_LINE;
                if (mCopperCashLine==null){
                    mCopperCashLine = new CopperCashLine();
                }
                break;
            case R.id.copper:
                mType = Constants.TWO_DIMENSIONAL_COPPER;
                if (mCopperCash==null){
                    mCopperCash=new CopperCash();
                }
                break;
            case R.id.are_octagon:
                mType = Constants.TWO_DIMENSIONAL_ARE_OCTAGON;
                if (mAreOctagon==null){
                    mAreOctagon=new AreOctagon();
                }
                break;
        }
        return true;
    }

    @Override
    public void drawSelf(GL10 gl) {
        switch (mType) {
            case Constants.TWO_DIMENSIONAL_CIRCLE:
                mCircle.drawSelf(gl);
                break;
            case Constants.TWO_DIMENSIONAl_COPPER_LINE:
                mCopperCashLine.drawSelf(gl);
                break;
            case Constants.TWO_DIMENSIONAL_COPPER:
                mCopperCash.drawSelf(gl);
                break;
            case Constants.TWO_DIMENSIONAL_ARE_OCTAGON:
                mAreOctagon.drawSelf(gl);
                break;
        }
        mBasicGLView.requestRender();
    }
}
