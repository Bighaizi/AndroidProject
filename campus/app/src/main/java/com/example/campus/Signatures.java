package com.example.campus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.icu.util.TimeZone;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import MySQLlite.DBOpenHelper;



//注意：本页面下的代码为第三方
//目的：获取签名，通过用户手写姓名，获取截图，保存到本地相册供后期调用
//源码地址1：https://blog.csdn.net/jifenglie/article/details/100033539
//源码地址2：https://github.com/gcacace/android-signaturepad
//使用该源码时，需要加载框架
public class Signatures extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private SignaturePad mSignaturePad;
    private Button mClearButton;
    private Button mSaveButton;
    private String setuserid_str;
    private String pictureaddress_str;//获取到的截图名称
    private DBOpenHelper User;
    private SQLiteDatabase getWriter,getReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verifyStoragePermissions(this);
        setContentView(R.layout.activity_signatures);
        //61-66为获取前一个界面传递过来的信息，和初始化数据库
        Intent getintent = getIntent();
        setuserid_str=getintent.getStringExtra("userid");
        User=new DBOpenHelper(Signatures.this);
        getReader=User.getReadableDatabase();
        getWriter=User.getWritableDatabase();

        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                //Toast.makeText(Signatures.this, "OnStartSigning", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSigned() {
                mSaveButton.setEnabled(true);
                mClearButton.setEnabled(true);
            }

            @Override
            public void onClear() {
                mSaveButton.setEnabled(false);
                mClearButton.setEnabled(false);
            }
        });

        mClearButton = (Button) findViewById(R.id.clear_button);
        mSaveButton = (Button) findViewById(R.id.save_button);

        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignaturePad.clear();
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
                if (addPngSignatureToGallery(signatureBitmap)) {
                    //102-107行代码为本人添加，目的是将获取的截图的名称保存到请假管理数据库中，为后期调用
                    ContentValues cv = new ContentValues();
                    cv.put("stuno",setuserid_str);
                    cv.put("picture",pictureaddress_str);
                    getWriter.insert("absence_tab", null, cv);
                    getWriter.close();
                    Toast.makeText(Signatures.this, "签名已保存", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Signatures.this, "签名未保存", Toast.LENGTH_SHORT).show();
                }
//                if (addSvgSignatureToGallery(mSignaturePad.getSignatureSvg())) {
//                    Toast.makeText(Signatures.this, "SVG Signature saved into the Gallery", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(Signatures.this, "Unable to store the SVG signature", Toast.LENGTH_SHORT).show();
//                }
                Intent intent = new Intent(Signatures.this, Absence.class);
                intent.putExtra("userid",setuserid_str);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(Signatures.this, "Cannot write images to external storage", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created");
        }
        return file;
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }

    public boolean addPngSignatureToGallery(Bitmap signature) {
        boolean result = false;
        try {
            //按照指定格式命名获取到的截图
            pictureaddress_str=String.format("Signature_%d.png", System.currentTimeMillis());
            //在内部存储空间中创建了一个名为“SignaturePad”的文件夹
            File photo = new File(getAlbumStorageDir("SignaturePad"), pictureaddress_str);
            saveBitmapToJPG(signature, photo);
            scanMediaFile(photo);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        Signatures.this.sendBroadcast(mediaScanIntent);
    }

//    public boolean addSvgSignatureToGallery(String signatureSvg) {
//        boolean result = false;
//        try {
//            File svgFile = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.svg", System.currentTimeMillis()));
//            OutputStream stream = new FileOutputStream(svgFile);
//            OutputStreamWriter writer = new OutputStreamWriter(stream);
//            writer.write(signatureSvg);
//            writer.close();
//            stream.flush();
//            stream.close();
//            scanMediaFile(svgFile);
//            result = true;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }

    /**
     * Checks if the app has permission to write to device storage
     * <p/>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity the activity from which permissions are checked
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent myIntent = new Intent();
            myIntent = new Intent(Signatures.this, MainActivity.class);
            myIntent.putExtra("userid",setuserid_str);
            startActivity(myIntent);
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}