package com.example.campus;

import androidx.annotation.ArrayRes;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import MyPages.User_Lists;
import MySQLlite.DBOpenHelper;

public class TeacherSeeAbsence extends AppCompatActivity {
    private Button agree_b;//同意请假按钮
    private Button reject_b;//拒绝请假按钮
    private EditText studentid_e;
    private String id_str;
    private String teacherid_str;//教师id
    private String sendteachername_str;//教师姓名
    private String agreetime_str;//同意请假的时间
    private DBOpenHelper User;
    private SQLiteDatabase getReader,getWriter;
    private ListView ListView;
    List <User_Lists> user_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_see_absence);
        initView();
        Query_Data();

        //同意请假按钮的事件处理
        agree_b.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                    agree_b=(Button)findViewById(R.id.Agree_Button);
                    agreetime_str=getTime();
                    id_str=studentid_e.getText().toString();
                    //判断EditText是否为空
                    if(id_str.equals(""))
                    {
                        Toast.makeText(TeacherSeeAbsence.this, "未选择学生学号！", Toast.LENGTH_LONG).show();
                    }
                    //按EditText中的内容更新请假管理数据库
                    else
                    {
                        User.update_absence(getWriter,"YES",agreetime_str,id_str,sendteachername_str);
                        getWriter=User.getWritableDatabase();
                        Toast.makeText(TeacherSeeAbsence.this, "已同意！", Toast.LENGTH_LONG).show();
                    }
            }
        });
        //拒绝请假按钮的事件处理
        reject_b=(Button) findViewById(R.id.Reject_Button);
        reject_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studentid_e=(EditText)findViewById(R.id.Student_id_EditText);
                id_str=studentid_e.getText().toString();
                if(id_str.equals(""))
                {
                    Toast.makeText(TeacherSeeAbsence.this, "未选择学生学号！", Toast.LENGTH_LONG).show();
                }
                //如果教师拒绝学生请假，直接将请假管理数据库中的相关学生的请假信息全部删除
                else {
                    User.delete_absence(getWriter,id_str);
                    Toast.makeText(TeacherSeeAbsence.this, "已拒绝！", Toast.LENGTH_LONG).show();
                    refresh();
                }
            }
        });
    }

    //各个控件的绑定
    public void initView()
    {
        //初始化数据库
        User=new DBOpenHelper(TeacherSeeAbsence.this);
        getReader=User.getReadableDatabase();
        getWriter=User.getWritableDatabase();
        //获取前一个页面传递过来的数据
        Intent intent = getIntent();
        teacherid_str=intent.getStringExtra("userid");
        //控件的绑定
        ListView=(ListView)findViewById(R.id.listView);
        user_list=new ArrayList<User_Lists>();
        studentid_e=(EditText)findViewById(R.id.Student_id_EditText);
        agree_b=(Button)findViewById(R.id.Agree_Button);
        //查询教师姓名
        Cursor cursor = getReader.query("teacher_tab", new String[]{"teaname", "teano"}, "teano=?", new String[]{teacherid_str}, null, null, null);
        while (cursor.moveToNext())
        {
            sendteachername_str=cursor.getString(cursor.getColumnIndex("teaname"));
            break;
        }
    }


    //获取系统时间
    public String getTime()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd HH:mm"); //设置时间格式
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+08")); //设置时区
        Date curDate = new Date(System.currentTimeMillis()); //获取当前时间
        String createDate = formatter.format(curDate); //格式转换
        return createDate;
    }

    //页面刷新
    private void refresh()
    {
        finish();
        Intent intent = new Intent(TeacherSeeAbsence.this,TeacherSeeAbsence.class);
        intent.putExtra("userid",teacherid_str);
        startActivity(intent);
    }

    //查询请假管理数据库中的内容，并传递给ListView
    public void Query_Data() {
        Cursor cursor = getWriter.query("absence_tab", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String stuno_str = cursor.getString(cursor.getColumnIndex("stuno"));
             String start_str= cursor.getString(cursor.getColumnIndex("starttime"));
             String end_str= cursor.getString(cursor.getColumnIndex("endtime"));
            String raeson_str=cursor.getString(cursor.getColumnIndex("reason"));
            User_Lists send_list =new User_Lists(stuno_str,start_str,end_str,raeson_str);
            user_list.add(send_list);
        }
        //设置适配器
        ListView.setAdapter(new MyAapter());
    }

    //重写适配器
    class MyAapter extends BaseAdapter {
        //获取集合中有多少条元素,由系统调用
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return user_list.size();
        }
        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }
        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //动态加载布局，显示学生请假详情
            LinearLayout linearLayout = new LinearLayout(TeacherSeeAbsence.this);
            TextView tv1 = new TextView(TeacherSeeAbsence.this);
            TextView tv2 = new TextView(TeacherSeeAbsence.this);
            TextView tv3 = new TextView(TeacherSeeAbsence.this);
            TextView tv4= new TextView(TeacherSeeAbsence.this);

            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(3, 5, 3, 0);
            linearLayout.setLayoutParams(layoutParams);
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.addView(tv1);
            linearLayout.addView(tv2);
            linearLayout.addView(tv3);
            linearLayout.addView(tv4);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            tv1.setLayoutParams(lp);
            tv1.setGravity(Gravity.CENTER); //这是布局文件中的Android：gravity属性 0.0f);//此处我需要均分高度就在heignt处设0,1.0f即设置权重是1，页面还有其他一个控件,1：1高度就均分了
            tv2.setLayoutParams(lp);
            tv2.setGravity(Gravity.CENTER);
            tv3.setLayoutParams(lp);
            tv3.setGravity(Gravity.CENTER);
            tv4.setLayoutParams(lp);
            tv4.setGravity(Gravity.CENTER);
            tv1.setSingleLine(true);
            tv2.setSingleLine(true);
            tv2.setEllipsize(TextUtils.TruncateAt.END);
            tv3.setSingleLine(true);
            tv3.setEllipsize(TextUtils.TruncateAt.END);
            tv4.setSingleLine(true);
            tv4.setEllipsize(TextUtils.TruncateAt.END);
            tv1.setTextColor(0xFF00A9FF);
            tv2.setTextColor(0xFF00A9FF);
            tv3.setTextColor(0xFF00A9FF);
            tv4.setTextColor(0xFF00A9FF);
            tv1.setTextSize(14);
            tv2.setTextSize(14);
            tv3.setTextSize(14);
            tv4.setTextSize(14);
            //获取集合中的元素
            User_Lists getlist = user_list.get(position);
            tv1.setText(getlist.getStuNo());
            tv2.setText(getlist.getStart());
            tv3.setText(getlist.getEnd());
            tv4.setText(getlist.getReason());
            return linearLayout;
        }
    }

    //重写返回按键，使其按指定的Inten跳转
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent myIntent = new Intent();
            myIntent = new Intent(TeacherSeeAbsence.this, MainActivity.class);
            myIntent.putExtra("userid",teacherid_str);
            startActivity(myIntent);
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}