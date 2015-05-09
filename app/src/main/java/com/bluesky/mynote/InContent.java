package com.bluesky.mynote;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by 清晨 on 2015/5/8.
 */
public class InContent extends Activity implements View.OnClickListener {
    private Button del_btn;
    private Button back_btn;
    private ImageView in_img;
    private TextView in_tv;
    private NoteDB noteDB;
    private SQLiteDatabase dbWriter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.incontent);
        initView();
        noteDB= new NoteDB(this);
        dbWriter=noteDB.getWritableDatabase();
        del_btn.setOnClickListener(this);
        back_btn.setOnClickListener(this);
        if(getIntent().getStringExtra(NoteDB.PATH).equals("null")){
            in_img.setVisibility(View.GONE);
        }else {
            in_img.setVisibility(View.VISIBLE);
        }
        in_tv.setText(getIntent().getStringExtra(NoteDB.CONTENT));
        Bitmap bitmap= BitmapFactory.decodeFile(getIntent().getStringExtra(NoteDB.PATH));
        in_img.setImageBitmap(bitmap);
    }

    public void initView(){
        del_btn= (Button) findViewById(R.id.delete);
        back_btn= (Button) findViewById(R.id.back);
        in_img= (ImageView) findViewById(R.id.in_img);
        in_tv= (TextView) findViewById(R.id.in_tv);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.delete:
                delDB();
                finish();
                break;
            case R.id.back:
                finish();
                break;
        }
    }
    public void delDB(){
        dbWriter.delete(NoteDB.TABLE_NAME,"id="+getIntent()
                .getIntExtra(NoteDB.ID,0),null);
    }
}
