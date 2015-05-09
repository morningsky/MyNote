package com.bluesky.mynote;
/*步骤:
    1.
        1.1创建数据库 NoteDB类
            1.1.1创建构造函数
            1.1.2复写onCreat onUpgrade方法
        1.2创建自定义的adapter MyAdapter类
            1.2.1构造函数
            1.2.2复写4个子类方法 注意getView方法
    2.创建视图
        2.1布局主界面 两个按钮 一个listview       activity_main.xml
        2.2 listview每一条数据的视图格式  图片imageview 内容textview 时间textview    cell.xml
        2.3添加内容界面 imageview editext 两个Button  addcontent.xml
        2.4创建详情页视图 与addcontent视图相似 将Editext转换为Textview Button的内容由返回变成删除 incontent.xml
    3.逻辑实现
    MainActivity:
        3.1初始化主界面布局   定义initView方法  给按钮设置监听
        3.7在MainActivity实例化一个SQLiteDatabase 获取读取权限 用于加载listview的内容
        3.8添加查询数据方法selectDB 并在该方法中加载MyAdapter
    AddContent:
        3.2创建添加内容界面的activity 并在AndroidManifest文件中注册该activity 两个activity添加固定竖屏参数
        3.3初始化AddContent界面布局 定义initView方法 给按钮设置监听 实例化SQLiteDatabase 获取写入数据权限
        3.4添加addDB方法获取内容 时间并写入数据库
        3.5添加getTime方法获取系统当前时间
        3.6为按钮添加事件
        3.9增加根据添加文字还是图文加载不同界面的initView逻辑
        4.0添加Intent调用系统相机 实例化一个File存放照片路径
        4.1复写onActivityResult来查看照片效果
        4.2add函数添加图片路径
    MyAdapter:
        4.3添加查看缩略图函数getImageThumbnail listview中显示
        4.5添加用来查询的String path 储存地址
    InContent:
        4.6添加详情页Activity 并注册
        4.7给listview添加监听事件 跳转到详情页 并传入部分数据
        4.8根据图文还是文字加载不同视图 显示文字 图片信息
        4.9实例化一个SQLiteDatabase 获取写入数据权限 用来删除数据
        5.0添加删除数据方法delDB 给按钮加上方法
 */
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    private Button text_btn, img_btn;
    private ListView lv;
    private Intent i;
    private MyAdapter adapter;
    private NoteDB noteDB;
    private SQLiteDatabase dbReader;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        //给按钮加入监听事件
        text_btn.setOnClickListener(this);
        img_btn.setOnClickListener(this);
        noteDB = new NoteDB(this);
        //获取读取权限 用于加载listview的内容
        dbReader = noteDB.getReadableDatabase();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.moveToPosition(position);//游标挪到了position的位置上
                Intent i=new Intent(MainActivity.this,InContent.class);
                i.putExtra(NoteDB.ID,cursor.getInt(cursor.getColumnIndex(NoteDB.ID)));//以便根据ID删除数据
                i.putExtra(NoteDB.CONTENT,cursor.getString(cursor.getColumnIndex(NoteDB.CONTENT)));
                i.putExtra(NoteDB.TIME,cursor.getString(cursor.getColumnIndex(NoteDB.TIME)));
                i.putExtra(NoteDB.PATH,cursor.getString(cursor.getColumnIndex(NoteDB.PATH)));
                startActivity(i);
            }
        });

    }

    //初始化控件
    public void initView() {
        lv = (ListView) findViewById(R.id.list);
        text_btn = (Button) findViewById(R.id.text);
        img_btn = (Button) findViewById(R.id.image);
    }

    //查询数据
    public void selectDB() {
        cursor = dbReader.query(NoteDB.TABLE_NAME,null,null,null,null,null,null,null);
        adapter = new MyAdapter(this,cursor);
        lv.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        i = new Intent(this, AddContent.class);
        switch (v.getId()) {
            case R.id.text:
                i.putExtra("flag", "1");
                startActivity(i);
                break;
            case R.id.image:
                i.putExtra("flag", "2");
                startActivity(i);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectDB();
    }
}
