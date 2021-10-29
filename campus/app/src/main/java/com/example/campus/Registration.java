package com.example.campus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import MySQLlite.DBOpenHelper;

public class Registration extends AppCompatActivity implements View.OnClickListener {
    private EditText userid_e;
    private EditText username_e;
    private EditText passwd1_e;
    private EditText passwd2_e;
    private Spinner spinner;
    private Button  excit_b;
    private Button  ensure_b;
    private DBOpenHelper stu_info;
    private String userid_str,username_str,type_str,passwd1_str,passwd2_str;
    private SQLiteDatabase dbReader,dbWriter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initView();
        excit_b.setOnClickListener(this);
        ensure_b.setOnClickListener(this);
    }

    public void initView()
    {
        userid_e=(EditText) findViewById(R.id.Userid_EditText);
        username_e=(EditText) findViewById(R.id.UserName_EditText);
        spinner=(Spinner) findViewById(R.id.Type_Spinner);
        passwd1_e=(EditText) findViewById(R.id.Passwd1_EditText);
        passwd2_e=(EditText) findViewById(R.id.Passwd2_EditText);
        excit_b=(Button) findViewById(R.id.Excit_Button);
        ensure_b=(Button) findViewById(R.id.Ensure_Button);
        stu_info = new DBOpenHelper(Registration.this);
        dbReader = stu_info.getReadableDatabase();
        dbWriter = stu_info.getWritableDatabase();
    }

    @Override
    public void onClick(View v) {
        userid_str=userid_e.getText().toString();
        username_str =username_e.getText().toString();
        type_str=spinner.getSelectedItem().toString();
        passwd1_str=passwd1_e.getText().toString();
        passwd2_str=passwd2_e.getText().toString();
        switch (v.getId())
        {
            case R.id.Excit_Button:
                userid_e.setText("");
                username_e.setText("");
                passwd1_e.setText("");
                passwd2_e.setText("");
                break;
            case R.id.Ensure_Button:
                Cursor cursor;
                cursor = dbReader.query("student_tab", new String[]{"stuno"},"stuno=?",new String[]{userid_str},null,null,null);
                if(cursor.moveToFirst()==false)//判断数据库中是否已经存在该数据
                {
                    if (type_str.equals("学生")&&userid_str.charAt(0)=='S')//如果是学生注册，其账号的第一位必须是大写的S
                    {
                        if(passwd1_str.equals(passwd2_str))//判断两次密码是否一致
                        {
                            stu_info.insert_student(dbWriter,userid_str,username_str,passwd1_str);
                            Toast.makeText(this, "注册成功！", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Registration.this,Login.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(this, "密码输入不一致！", Toast.LENGTH_LONG).show();
                        }
                    }
                    else if((type_str.equals("教师")&&userid_str.charAt(0)=='T'))//如果是学生注册，其账号的第一位必须是大写的T
                    {
                        if(passwd1_str.equals(passwd2_str))
                        {
                            stu_info.insert_teacher(dbWriter,userid_str,username_str,passwd1_str);
                            Toast.makeText(this, "注册成功！", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Registration.this,Login.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(this, "密码输入不一致！", Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(this, "账户名错误！", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(this, "账户已存在！", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }

    }
}