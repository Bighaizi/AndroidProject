package com.example.campus;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import MySQLlite.DBOpenHelper;

public class AgreeAbsence extends AppCompatActivity {
    private ImageView image;
    private TextView  reason_t;
    private TextView  abseccetime_t;
    private TextView  studentname1_t;
    private TextView studentname2_t;
    private TextView  teachername_t;
    private TextView  agreetime_t;
    private TextView tittle_t;
    private TextView isaggree_t;
    private TextView isjudje_t;
    private TextView isexict_t;

    private String className_str;//班级专业姓名
    private String reason_str;//请假原因
    private String type_str;//请假类型
    private String Sdate_time_str;//请假开始时间
    private String Edate_time_str;//请假结束时间
    private String address_str;//去往校内还是校外
    private String daddress_str;//去往的详细地址
    private String contactper_str;//紧急联系人姓名
    private String contactnum_str;//紧急联系人电话
    private String absencetime_str;//发出请假时的系统时间
    private String agreetime_str;//教师同意的时间
    private String teachername_str;//教师的姓名
    private String isagree_str;//是否同意
    private String getuserid_str;//学生id
    private String getpicture_str;//签名截图的名称

    private DBOpenHelper User;
    private SQLiteDatabase getReader;
    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agree_absence);
        initView();
        get_Absence_info();
        get_Student_info();
        display_Info();
    }


    //按照传递过来截图的绝对路径，将该截图通过image显示出来
    public void displayImage(String imagePath){
        if (imagePath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            image.setImageBitmap(bitmap);
        }
    }


    //各个控件的初始化
    public void initView()
    {
        image=(ImageView)findViewById(R.id.Signed_ImageView);
        Intent intent = getIntent();
        getuserid_str=intent.getStringExtra("userid");
        reason_t=(TextView)findViewById(R.id.Reason_TextView);
        studentname1_t=(TextView)findViewById(R.id.StudentName_TextView);
        studentname2_t=(TextView)findViewById(R.id.StudentName2_TextView);
        teachername_t=(TextView)findViewById(R.id.TeacherName_Textview);
        abseccetime_t=(TextView)findViewById(R.id.AbsenceTime_TextView);
        agreetime_t=(TextView)findViewById(R.id.AgreeTime_TextView);
        tittle_t=(TextView)findViewById(R.id.title);
        isaggree_t=(TextView)findViewById(R.id.IsAgree_TextView);
        isjudje_t=(TextView)findViewById(R.id.IsJudje_TextView);
        isexict_t=(TextView)findViewById(R.id.IsExict_TextView);
        User=new DBOpenHelper(AgreeAbsence.this);
        getReader=User.getReadableDatabase();
    }

    //按照学号获取请假管理数据库中的内容
    public void get_Absence_info()
    {
        Cursor cursor1 = getReader.query("absence_tab",null,"stuno=?", new String[]{getuserid_str}, null, null, null);
        while (cursor1.moveToNext())
        {
            className_str= cursor1.getString( cursor1.getColumnIndex("classname"));
            reason_str= cursor1.getString( cursor1.getColumnIndex("reason"));
            type_str = cursor1.getString( cursor1.getColumnIndex("abtype"));
            Sdate_time_str = cursor1.getString( cursor1.getColumnIndex("starttime"));
            Edate_time_str = cursor1.getString( cursor1.getColumnIndex("endtime"));
            address_str = cursor1.getString( cursor1.getColumnIndex("address"));
            daddress_str = cursor1.getString( cursor1.getColumnIndex("daddress"));
            contactper_str = cursor1.getString( cursor1.getColumnIndex("contactper"));
            contactnum_str = cursor1.getString( cursor1.getColumnIndex("contactphone"));
            agreetime_str= cursor1.getString( cursor1.getColumnIndex("agreetime"));
            absencetime_str = cursor1.getString( cursor1.getColumnIndex("abscencetime"));
            teachername_str=cursor1.getString(cursor1.getColumnIndex("teachername"));
            isagree_str=cursor1.getString(cursor1.getColumnIndex("isagree"));
            getpicture_str=cursor1.getString(cursor1.getColumnIndex("picture"));
            break;
        }
    }
    //按照学号获取学生信息数据库中的内容
    public void get_Student_info()
    {
        Cursor cursor2 = getReader.query("student_tab", new String[]{"stuname"}, "stuno=?", new String[]{getuserid_str}, null, null, "stuno");
        while (cursor2.moveToNext())
        {
            studentname1_t.setText(cursor2.getString(cursor2.getColumnIndex("stuname")));
            studentname2_t.setText(cursor2.getString(cursor2.getColumnIndex("stuname")));
            break;
        }
    }

    //根据请假管理数据库中的信息显示相应的内容
    public void display_Info()
    {
        if(isagree_str.equals("NO"))
        {
            tittle_t.setText("待同意详情");
            isaggree_t.setText("请假申请待审批，请等教师同意");
            isjudje_t.setText("待审批");
            teachername_t.setText("夏天宇");
            abseccetime_t.setText(absencetime_str);
            isexict_t.setText("销假申请");
        }
        else
        {
            tittle_t.setText("待销假详情");
            isaggree_t.setText("请假申请已被通过，请结束后完成销假");
            isjudje_t.setText("审批请假");
            isexict_t.setText("销假申请");
            teachername_t.setText(teachername_str);
            agreetime_t.setText(agreetime_str);
            abseccetime_t.setText(absencetime_str);
        }
        reason_t.setText(Html.fromHtml("\u3000\u3000本人" + "<font color='#0098E5'>" + className_str + "</font>"+"因"+"<font color='#0098E5'>"+reason_str+"</font>"+"需请"+"<font color='#0098E5'>"+type_str+"</font>"+"，从"+"<font color='#0098E5'>"+Sdate_time_str+"</font>"+"至"+"<font color='#0098E5'>"+Edate_time_str+"</font>"+"，不能参加累计"+"<font color='#0098E5'>"+"0"+"</font>"+"节课程的学习。请假期间，本人将去往"
                +"<font color='#0098E5'>"+address_str+"</font>"+"，详细地址为："+"<font color='#0098E5'>"+daddress_str+"</font>"+"，"+"联系人："+"<font color='#0098E5'>"+contactper_str+"</font>"+"，"+"联系电话："+"<font color='#0098E5'>"+contactnum_str+"</font>"));

        //在获取签名那一界面中，会在内部存储中创建一个名为“SignaturePad”文件夹，截图就存放在该文件夹下
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "SignaturePad");
        File photo = new File(file, getpicture_str);//根据数据库中存放的截图的名称查找截图的位置
        String path = photo.toString();//path就是获取到的对应截图的绝对路径
        displayImage(path);//将该路径传递给显示图片的函数
    }

    //重写返回按键，使其按指定的Inten跳转
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent myIntent = new Intent();
            myIntent = new Intent(AgreeAbsence.this, MainActivity.class);
            myIntent.putExtra("userid",getuserid_str);
            startActivity(myIntent);
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}