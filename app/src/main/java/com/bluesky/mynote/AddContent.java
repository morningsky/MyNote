package com.bluesky.mynote;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.VideoView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 清晨 on 2015/5/6.
 */
public class AddContent extends Activity implements View.OnClickListener {
    private NoteDB noteDB;
    private SQLiteDatabase dbWriter;
    private String flag; //接受从mainactivity传来的标识 用于判定加载不同的添加内容界面(图文或者纯文字)
    private EditText editText;
    private Button save_btn,cancel_btn;
    private ImageView c_img;
    private File imgfile;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcontent);
        flag=getIntent().getStringExtra("flag");
        initView();
        save_btn.setOnClickListener(this);
        cancel_btn.setOnClickListener(this);
        noteDB=new NoteDB(this);
        dbWriter=noteDB.getWritableDatabase();//获取写入数据库权限
    }

    public void initView(){
        editText= (EditText) findViewById(R.id.ettext);
        save_btn= (Button) findViewById(R.id.save);
        cancel_btn= (Button) findViewById(R.id.cancel);
        c_img= (ImageView) findViewById(R.id.c_img);
        if(flag.equals("1")){
            c_img.setVisibility(View.GONE);//隐藏imageview
        }
        if(flag.equals("2")){
            c_img.setVisibility(View.VISIBLE);//显示imageview
            //启动系统相机拍照
            Intent getImg=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //图片是放在存储卡中 路径存在数据库中 以时间命名图片 避免重名
            imgfile=new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath()+"/"+getTime()+".jpg");
            getImg.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imgfile));
            startActivityForResult(getImg,1);//便于立即查看效果


        }
    }

    //获取内容并写入数据库
    public void addDB(){
        ContentValues cv=new ContentValues();
        cv.put(NoteDB.CONTENT,editText.getText().toString());
        cv.put(NoteDB.TIME,getTime());
        cv.put(NoteDB.PATH,imgfile + "");
        dbWriter.insert(NoteDB.TABLE_NAME,null,cv);
    }

    //获取系统当前时间
    public String getTime(){
        SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate=new Date();
        String str=format.format(curDate);
        return str;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save:
                addDB();
                finish();
                break;
            case R.id.cancel:
                finish();
                break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1){
            Bitmap bitmap= BitmapFactory.decodeFile(imgfile.getAbsolutePath());
            c_img.setImageBitmap(bitmap);
        }
    }
}
