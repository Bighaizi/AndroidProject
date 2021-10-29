package MySQLlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

import MyPages.User_Lists;

public class DBOpenHelper extends SQLiteOpenHelper {
    public DBOpenHelper(@Nullable Context context) {
        super(context, "JRXY_db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据库sql语句并执行
        //创建了三个数据库，学生信息库，教师信息库，请假管理库
        String sql1="create table student_tab(stuno char(11) primary key,stuname varchar(20),stupasswd varchar(20))";
        String sql2="create table teacher_tab(teano char(11) primary key ,teaname varchar(20),teapasswd varchar(20))";
        String sql3="create table absence_tab(stuno char(11) primary key ,classname varchar(50),reason varchar(50),abtype char(10),starttime varchar(50),endtime varchar(50),address char(10),daddress varchar(50),contactper varchar(20),contactphone char(15),agreetime varchar(20),isagree char(4),abscencetime varchar(50),teachername varchar(20),picture varchar(100))";
        db.execSQL(sql1);
        db.execSQL(sql2);
        db.execSQL(sql3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    //向学生信息库中添加数据,注册的时候需要用到
    public void insert_student(SQLiteDatabase db,String str1,String str2,String str3){
        //插入数据SQL语句
        ContentValues values = new ContentValues();
        values.put("stuno",str1);
        values.put("stuname",str2);
        values.put("stupasswd",str3);
        //执行SQL语句
        db.insert("student_tab",null,values);
        db.close();
    }
    //向教师信息库中添加数据,注册的时候需要用到
    public void insert_teacher(SQLiteDatabase db,String str1,String str2,String str3){
        ContentValues values = new ContentValues();
        values.put("teano",str1);
        values.put("teaname",str2);
        values.put("teapasswd",str3);
        //执行SQL语句
        db.insert("teacher_tab",null,values);
        db.close();
    }

//    public void insert_absence(SQLiteDatabase db,String str1,String str2,String str3,String str4,String str5,String str6,String str7,String str8,String str9,String str10,
//                               String str11){
//        ContentValues values = new ContentValues();
//        values.put("stuno",str1);
//        values.put("classname",str2);
//        values.put("reason",str3);
//        values.put("abtype",str4);
//        values.put("starttime",str5);
//        values.put("endtime",str6);
//        values.put("address",str7);
//        values.put("daddress",str8);
//        values.put("contactper",str9);
//        values.put("contactphone",str10);
//        values.put("abscencetime",str11);
//        //执行SQL语句
//        db.insert("absence_tab",null,values);
//        db.close();
//    }

    //按照学生学号删除请假管理数据库中该学生的信息
    public void delete_absence(SQLiteDatabase db,String str)
    {
        //删除SQL语句
        db.delete("absence_tab","stuno=?",new String[]{str});
        //执行SQL语句
        db.close();
    }
    //更新请假管理数据库,教师同意请假时会用到
    public void update_absence(SQLiteDatabase db,String str1,String str2,String str3,String str4)
    {
        ContentValues values = new ContentValues();
        values.put("isagree",str1);
        values.put("agreetime",str2);
        values.put("teachername",str4);
        db.update("absence_tab",values,"stuno=?",new String[]{str3});
        db.close();
    }
//    //查询请假管理数据库中的部分内容
//    public List<User_Lists> querydata(SQLiteDatabase squteDatabase)
//    {
//        Cursor cursor = squteDatabase.query("absence_tab", new String[]{"stuno","starttime", "endtime", "reason"}, null, null, null, null, "stuno", null);
//        List<User_Lists> list = new ArrayList<User_Lists>();
//        while (cursor.moveToNext())
//        {
//            @SuppressLint("Range") String stuno_str = cursor.getString(cursor.getColumnIndex("stuno"));
//            @SuppressLint("Range") String start_str= cursor.getString(cursor.getColumnIndex("starttime"));
//            @SuppressLint("Range") String end_str= cursor.getString(cursor.getColumnIndex("endtime"));
//            @SuppressLint("Range") String raeson_str=cursor.getString(cursor.getColumnIndex("reason"));
//            list.add(new User_Lists(stuno_str, start_str, end_str, raeson_str));
//        }
//        cursor.close();
//        squteDatabase.close();
//        return list;
//    }

}