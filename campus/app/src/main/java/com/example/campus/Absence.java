package com.example.campus;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.google.android.material.button.MaterialButtonToggleGroup;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import MySQLlite.DBOpenHelper;

public class Absence extends AppCompatActivity {
    private EditText class_name_e;
    private EditText reason_e;
    private Spinner type_sp;
    private EditText starttime_e;
    private EditText endtime_e;
    private Spinner   address_sp;
    private EditText  daddress_e;
    private EditText  contactpersn_e;
    private EditText  contactphone_e;
    private Button    next_b;

    private String className_str;//班级专业姓名
    private String reason_str;//请假原因
    private String type_str;//请假类型
    private String starttime_str;//请假开始时间
    private String endtime_str;//请假结束时间
    private String address_str;//去往校内还是校外
    private String daddress_str;//去往的详细地址
    private String contactper_str;//紧急联系人姓名
    private String contactnum_str;//紧急联系人电话
    private String userid_str;//获取前一个界面传递过来的数据
    private String absencetime_str;//发出请假时的系统时间

    private DBOpenHelper User;
    private SQLiteDatabase getWriter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absence);
        initView();
        //“下一步”按钮事件处理
        next_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getString();//获取各个控件的内容
                ContentValues cv = new ContentValues();
                cv.put("classname", className_str);
                cv.put("reason", reason_str);
                cv.put("abtype", type_str);
                cv.put("starttime", starttime_str);
                cv.put("endtime", endtime_str);
                cv.put("address", address_str);
                cv.put("daddress", daddress_str);
                cv.put("contactper", contactper_str);
                cv.put("contactphone", contactnum_str);
                cv.put("abscencetime", absencetime_str);
                cv.put("isagree","NO");
                //将请假信息更新到请假管理数据库中,并跳转到下一界面
                getWriter.update("absence_tab", cv,"stuno=?",new String[]{userid_str}); //向数据表中追加数据，第二个参数是对空列的填充处理策略
                getWriter.close();
                Intent intent = new Intent(Absence.this,AgreeAbsence.class);
                intent.putExtra("userid",userid_str);
                startActivity(intent);
                finish();
            }
        });
    }
    //各个控件的绑定，以及数据库的初始化
    public void initView()
    {
        class_name_e=(EditText)findViewById(R.id.Class_Name_EditText);
        reason_e=(EditText)findViewById(R.id.Reason_EdiText);
        type_sp=(Spinner)findViewById(R.id.SelectTypes_Spinner);
        starttime_e=(EditText)findViewById(R.id.StartDateTime_EditText);
        endtime_e=(EditText)findViewById(R.id.EndDateTime_EditText);
        address_sp=(Spinner)findViewById(R.id.SelectAddress_Spinner);
        daddress_e=(EditText)findViewById(R.id.DetailsAddress_EditText);
        contactpersn_e=(EditText)findViewById(R.id.ContactPerson_EditText);
        contactphone_e=(EditText)findViewById(R.id.ContactPhone_EditText);
        next_b=(Button) findViewById(R.id.Next_Button);
        User=new DBOpenHelper(Absence.this);
        getWriter=User.getWritableDatabase();
        Intent intent = getIntent();
        userid_str=intent.getStringExtra("userid");
    }

    //获取控件中的内容
    public void getString()
    {
        className_str=class_name_e.getText().toString();
        reason_str=reason_e.getText().toString();
        type_str=type_sp.getSelectedItem().toString();
        starttime_str=starttime_e.getText().toString();
        endtime_str=endtime_e.getText().toString();
        address_str=address_sp.getSelectedItem().toString();
        daddress_str=daddress_e.getText().toString();
        contactper_str=contactpersn_e.getText().toString();
        contactnum_str=contactphone_e.getText().toString();
        absencetime_str=getTime();
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

    //重写返回按键，使其按指定的Inten跳转
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent myIntent = new Intent();
            myIntent = new Intent(Absence.this, Signatures.class);
            myIntent.putExtra("userid",userid_str);
            startActivity(myIntent);
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}