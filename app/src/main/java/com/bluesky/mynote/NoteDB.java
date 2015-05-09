package com.bluesky.mynote;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 清晨 on 2015/5/6.
 */
public class NoteDB extends SQLiteOpenHelper {

    public static final String TABLE_NAME="notes";//表名
    public static final String CONTENT="content";//内容
    public static final String ID="id";         //标识每一条数据
    public static final String TIME="time";    //存放添加数据时的时间
    public static final String PATH="path";   //路径,用来存放照片路径

    //构造函数参数保留一个Content即可
    public NoteDB(Context context) {
        super(context, "notes", null, 1);
    }

    //注意属性内的空格 " TEXT NOT NULL,"第一个引号后的空格不能省略 否则名称会变为contentTEXT
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CONTENT+" TEXT NOT NULL,"
                + PATH +" TEXT NOT NULL,"
                + TIME +" TEXT NOT NULL)");
    }

    //不需要更新
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
