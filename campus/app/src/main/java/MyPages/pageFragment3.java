package MyPages;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.campus.Absence;
import com.example.campus.AgreeAbsence;
import com.example.campus.MainActivity;
import com.example.campus.R;
import com.example.campus.Signatures;
import com.example.campus.TeacherSeeAbsence;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import MySQLlite.DBOpenHelper;


public class pageFragment3 extends Fragment {
    private Button absence_button;
    private int intent_flag=0;
    private String tab3userid_str;
    private DBOpenHelper User;
    private SQLiteDatabase getReader,getWriter;
    private Intent intent;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab3, container, false);

        Intent intent =getActivity().getIntent();
        tab3userid_str=intent.getStringExtra("userid");//获取前一个界面传递过来的数据
        absence_button = (Button) view.findViewById(R.id.Absence_Button);
        //初始化数据库
        User=new DBOpenHelper(getActivity());
        getReader=User.getReadableDatabase();
        getWriter=User.getWritableDatabase();
        Intent_is_to();

        //通过intent_flag标志判断下一个跳转界面
        absence_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), Signatures.class);
                Intent intent2 = new Intent(getActivity(), AgreeAbsence.class);
                Intent intent3= new Intent(getActivity(), TeacherSeeAbsence.class);
                switch (intent_flag)
                {
                    case 1:
                        Toast.makeText(getActivity(), "当前暂无请假或教师未同意您的请假！", Toast.LENGTH_LONG).show();
                        intent1.putExtra("userid",tab3userid_str);
                        startActivity(intent1);
                        getActivity().finish();
                        break;
                    case 2:
                        intent2.putExtra("userid",tab3userid_str);
                        startActivity(intent2);
                        getActivity().finish();
                        break;
                    case 3:
                        intent3.putExtra("userid",tab3userid_str);
                        startActivity(intent3);
                        getActivity().finish();
                        break;
                }
            }
        });
        return view;
    }

    //通过前一个界面传递过来的字符串判断是学生还是教师并更改intent_flag标志
    public void Intent_is_to()
    {
         if(tab3userid_str.charAt(0)=='S')//判断是否为学生
         {
            Cursor cursor1 = getReader.query("absence_tab", new String[]{"stuno"}, "stuno=?", new String[]{tab3userid_str}, null, null, "stuno");
            //如果学生未曾请假或者请假被老师拒绝，则请假管理数据库中不会存在该学生的信息，因此需要先判断库中是否存在该学生的信息
             if(cursor1.moveToFirst()==false)//判断查询到的内容是否为空
             {
                 intent_flag=1;
             }
            else
             {
                 intent_flag=2;
             }
            cursor1.close();
         }
         else if(tab3userid_str.charAt(0)=='T')//判断是否为教师
         {
                intent_flag=3;
         }
    }

}
