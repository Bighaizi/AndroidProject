package com.example.campus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import MyPages.pageFragment1;
import MyPages.pageFragment2;
import MyPages.pageFragment3;
import MyPages.pageFragment4;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //顶部的Textview
    private TextView Top_t;

    //声明四个Tab的布局文件
    private LinearLayout mTab1;
    private LinearLayout mTab2;
    private LinearLayout mTab3;
    private LinearLayout mTab4;

    //声明四个Tab的ImageButton
    private Button mImg1;
    private Button mImg2;
    private Button mImg3;
    private Button mImg4;

    //声明四个Tab分别对应的Fragment
    private Fragment mFrag1;
    private Fragment mFrag2;
    private Fragment mFrag3;
    private Fragment mFrag4;

    //声明图像数组
    private int[] resId = new int[]{
            R.mipmap.image1,R.mipmap.image2,R.mipmap.image3};
    //图片下标序号
    private int count = 0;
    //定义手势监听对象
    private GestureDetector gestureDetector;
    //定义ImageView对象
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();//初始化控件
        initEvents();//初始化事件
        selectTab(0);//默认选中第一个Tab
    }
    protected void onStart() {
        super.onStart();
        iv = (ImageView)findViewById(R.id.imageView);               //获取ImageView控件id
        gestureDetector = new GestureDetector(onGestureListener);   //设置手势监听由onGestureListener处理
    }

    //首页第一个Fragment中滑动图片处理函数
    //当Activity被触摸时回调
    public boolean onTouchEvent(MotionEvent event){
        return gestureDetector.onTouchEvent(event);
    }
    //自定义GestureDetector的手势识别监听器
    private GestureDetector.OnGestureListener onGestureListener
            = new GestureDetector.SimpleOnGestureListener(){
        //当识别的手势是滑动手势时回调onFinger方法
        public boolean onFling(MotionEvent e1,MotionEvent e2,float velocityX,float velocityY){
            //得到手触碰位置的起始点和结束点坐标 x , y ，并进行计算
            float x = e2.getX()-e1.getX();
            float y = e2.getY()-e1.getY();
            //通过计算判断是向左还是向右滑动
            if(x > 0){
                count++;
                count%=(resId.length);        //想显示多少图片，就把定义图片的数组长度-1
            }else if(x < 0){
                count--;
                count=(count+(resId.length))%(resId.length);
            }
            iv.setImageResource(resId[count]);  //切换imageView的图片
            return true;
        }
    };

    private void initEvents() {
        //初始化四个Tab的点击事件
        mTab1.setOnClickListener(this);
        mTab2.setOnClickListener(this);
        mTab3.setOnClickListener(this);
        mTab4.setOnClickListener(this);
    }

    private void initViews() {
        //初始化四个Tab的布局文件
        mTab1 = (LinearLayout) findViewById(R.id.id_tab1);
        mTab2 = (LinearLayout) findViewById(R.id.id_tab2);
        mTab3 = (LinearLayout) findViewById(R.id.id_tab3);
        mTab4 = (LinearLayout) findViewById(R.id.id_tab4);

        //初始化四个ImageButton
        mImg1 = (Button) findViewById(R.id.id_tab_img1);
        mImg2 = (Button) findViewById(R.id.id_tab_img2);
        mImg3 = (Button) findViewById(R.id.id_tab_img3);
        mImg4 = (Button) findViewById(R.id.id_tab_img4);

        //初始化Top_TextView
        Top_t=(TextView) findViewById(R.id.Top_TextView);
    }

    //处理Tab的点击事件
    @Override
    public void onClick(View v) {
        resetImgs(); //先将四个ImageButton置为灰色
        switch (v.getId()) {
            case R.id.id_tab1:
                selectTab(0);
                break;
            case R.id.id_tab2:
                selectTab(1);
                break;
            case R.id.id_tab3:
                selectTab(2);
                break;
            case R.id.id_tab4:
                selectTab(3);
                break;
        }

    }

    //进行选中Tab的处理
    private void selectTab(int i) {
        //获取FragmentManager对象
        FragmentManager manager = getSupportFragmentManager();
        //获取FragmentTransaction对象
        FragmentTransaction transaction = manager.beginTransaction();
        //先隐藏所有的Fragment
        hideFragments(transaction);
        switch (i) {
            //当选中点击的是第一页的Tab时
            case 0:
                mImg1.setBackground(getResources().getDrawable(R.mipmap.home_b));
                //如果第一页对应的Fragment没有实例化，则进行实例化，并显示出来
                if (mFrag1 == null) {
                    mFrag1 = new pageFragment1();
                    transaction.add(R.id.id_content, mFrag1);
                    Top_t.setText("首页");

                } else {
                    //如果第一页对应的Fragment已经实例化，则直接显示出来
                    transaction.show(mFrag1);
                    Top_t.setText("首页");
                }
                break;
            case 1:
                mImg2.setBackground(getResources().getDrawable(R.mipmap.communication_b));
                if (mFrag2 == null) {
                    mFrag2 = new pageFragment2();
                    transaction.add(R.id.id_content, mFrag2);
                    Top_t.setText("消息");
                } else {
                    transaction.show(mFrag2);
                    Top_t.setText("消息");
                }
                break;
            case 2:
                mImg3.setBackground(getResources().getDrawable(R.mipmap.school_b));
                    if (mFrag3 == null) {
                        mFrag3 = new pageFragment3();
                        transaction.add(R.id.id_content, mFrag3);
                        Top_t.setText("校园");
                    } else {
                        transaction.show(mFrag3);
                        Top_t.setText("校园");
                    }

                break;
            case 3:
                mImg4.setBackground(getResources().getDrawable(R.mipmap.mine_b));
                if (mFrag4 == null) {
                    mFrag4 = new pageFragment4();
                    transaction.add(R.id.id_content, mFrag4);
                    Top_t.setText("我的");
                } else {
                    transaction.show(mFrag4);
                    Top_t.setText("我的");
                }
                break;
        }
        //不要忘记提交事务
        transaction.commit();
    }

    //将四个的Fragment隐藏
    private void hideFragments(FragmentTransaction transaction) {
        if (mFrag1 != null) {
            transaction.hide(mFrag1);
        }
        if (mFrag2 != null) {
            transaction.hide(mFrag2);
        }
        if (mFrag3 != null) {
            transaction.hide(mFrag3);
        }
        if (mFrag4 != null) {
            transaction.hide(mFrag4);
        }
    }

    //将四个ImageButton置为灰色
    private void resetImgs() {
        mImg1.setBackground(getResources().getDrawable(R.mipmap.home_g));
        mImg2.setBackground(getResources().getDrawable(R.mipmap.communication_g));
        mImg3.setBackground(getResources().getDrawable(R.mipmap.school_g));
        mImg4.setBackground(getResources().getDrawable(R.mipmap.mine_g));
    }
}