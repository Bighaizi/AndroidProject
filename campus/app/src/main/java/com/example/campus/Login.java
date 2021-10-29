package com.example.campus;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import MySQLlite.DBOpenHelper;

public class Login extends AppCompatActivity implements View.OnClickListener{
    private EditText username_e;
    private EditText passwd_e;
    private Button   login_b;
    private TextView findpasswd_t;
    private TextView register_t;
    private SQLiteDatabase dbReader;
    private DBOpenHelper helper;
    private String userid_str;
    private String passwd_str;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        login_b.setOnClickListener(this);
        findpasswd_t.setOnClickListener(this);
        register_t.setOnClickListener(this);
    }
    //初始化各个控件
    public void initView()
    {
        username_e=(EditText) findViewById(R.id.UserName_EditText);
        passwd_e=(EditText) findViewById(R.id.Passwd_EditText);
        login_b=(Button) findViewById(R.id.Login_Button);
        findpasswd_t=(TextView) findViewById(R.id.Findpasswd_Textview);
        register_t=(TextView) findViewById(R.id.Register_Textview);
        helper=new DBOpenHelper(Login.this);
        dbReader=helper.getReadableDatabase();
    }
    @SuppressLint("Range")
    @Override
    public void onClick(View v) {
        userid_str=username_e.getText().toString();
        passwd_str=passwd_e.getText().toString();
        Intent intent1 = new Intent(Login.this,MainActivity.class);
        Intent intent2 = new Intent(Login.this,Registration.class);
        Intent intent3 = new Intent(Login.this,FindPasswd.class);
            switch (v.getId())
            {
                case R.id.Login_Button:
                    try{
                        if(userid_str.charAt(0)=='S')
                        {
                            Cursor cursor = dbReader.query("student_tab", new String[]{"stuno", "stupasswd"}, "stuno=?", new String[]{userid_str}, null, null, "stupasswd");
                            if (cursor.moveToFirst() == false)//判断数据库中是否有该信息
                            {
                                Toast.makeText(this, "用户不存在！请注册！", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                cursor = dbReader.query("student_tab", new String[]{"stuno", "stupasswd"}, "stuno=?", new String[]{userid_str}, null, null, "stupasswd");
                                while (cursor.moveToNext())
                                {
                                    String password = cursor.getString(cursor.getColumnIndex("stupasswd"));
                                    String stuname = cursor.getString(cursor.getColumnIndex("stuno"));
                                    if (passwd_str.equals(password)&&stuname.equals(userid_str))//判断密码是否正确
                                    {
                                        Toast.makeText(this, "登录成功！", Toast.LENGTH_LONG).show();
                                        intent1.putExtra("userid",userid_str);
                                        startActivity(intent1);
                                        finish();
                                    } else
                                     {
                                        Toast.makeText(this, "用户名或密码不正确！", Toast.LENGTH_LONG).show();
                                      }
                                    break;
                                }
                            }
                        }
                        else if(userid_str.charAt(0)=='T')
                        {
                            Cursor cursor = dbReader.query("teacher_tab", new String[]{"teano", "teapasswd"}, "teano=?", new String[]{userid_str}, null, null, "teapasswd");
                            if (cursor.moveToFirst() == false)//判断数据库中是否有该信息
                            {
                                Toast.makeText(this, "用户不存在！请注册！", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                cursor = dbReader.query("teacher_tab", new String[]{"teano", "teapasswd"}, "teano=?", new String[]{userid_str}, null, null, "teapasswd");
                                while (cursor.moveToNext())
                                {
                                    String password = cursor.getString(cursor.getColumnIndex("teapasswd"));
                                    String stuname = cursor.getString(cursor.getColumnIndex("teano"));
                                    if (passwd_str.equals(password)&&stuname.equals(userid_str))//判断密码是否正确
                                    {
                                        Toast.makeText(this, "登录成功！", Toast.LENGTH_LONG).show();
                                        intent1.putExtra("userid",userid_str);
                                        startActivity(intent1);
                                        finish();
                                    } else
                                    {
                                        Toast.makeText(this, "用户名或密码不正确！", Toast.LENGTH_LONG).show();
                                    }
                                    break;
                                }
                            }
                        }
                    }catch(SQLiteException e){
                        Toast.makeText(this, "系统错误！",Toast.LENGTH_LONG).show();
                    }
                    break;
                case R.id.Register_Textview:
                    startActivity(intent2);
                    break;
                case R.id.Findpasswd_Textview:
                    startActivity(intent3);
                    break;
                default:
                    break;
            }
    }


}