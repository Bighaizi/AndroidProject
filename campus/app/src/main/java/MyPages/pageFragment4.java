package MyPages;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.campus.Login;
import com.example.campus.R;

import MySQLlite.DBOpenHelper;


public class pageFragment4 extends Fragment {
    private TextView tab4userid_t;
    private String tab4userid_str;
    private Button excit_b;
    private DBOpenHelper User;
    private SQLiteDatabase getReader,getWriter;
    @SuppressLint("Range")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.tab4, container, false);
        Intent intent =getActivity().getIntent();
        tab4userid_str=intent.getStringExtra("userid");
        tab4userid_t=(TextView) view.findViewById(R.id.Tab4Userid_TextView);
        excit_b=(Button)view.findViewById(R.id.Excit_Login_Button);
        User=new DBOpenHelper(getActivity());
        getReader=User.getReadableDatabase();
        getWriter=User.getWritableDatabase();
        if(tab4userid_str.charAt(0)=='T')
        {
            Cursor cursor = getReader.query("teacher_tab", new String[]{"teaname", "teano"}, "teano=?", new String[]{tab4userid_str}, null, null, "teano");
            while (cursor.moveToNext())
            {
                tab4userid_t.setText(cursor.getString(cursor.getColumnIndex("teaname")));
                break;
            }
        }
        else if(tab4userid_str.charAt(0)=='S')
        {
            Cursor cursor = getReader.query("student_tab", new String[]{"stuname", "stuno"}, "stuno=?", new String[]{tab4userid_str}, null, null, "stuno");
            while (cursor.moveToNext())
            {
                tab4userid_t.setText(cursor.getString(cursor.getColumnIndex("stuname")));
                break;
            }
        }

        excit_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Login.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }
}
