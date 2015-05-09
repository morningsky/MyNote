package com.bluesky.mynote;
/*步骤:
    1.创建数据库 NoteDB类
        1.1创建构造函数
        1.2复写onCreat onUpgrade方法
    2.
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
        text_btn.setOnClickListener(this);
        img_btn.setOnClickListener(this);
        noteDB = new NoteDB(this);
        dbReader = noteDB.getReadableDatabase();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.moveToPosition(position);
                Intent i=new Intent(MainActivity.this,InContent.class);
                i.putExtra(NoteDB.ID,cursor.getInt(cursor.getColumnIndex(NoteDB.ID)));
                i.putExtra(NoteDB.CONTENT,cursor.getString(cursor.getColumnIndex(NoteDB.CONTENT)));
                i.putExtra(NoteDB.TIME,cursor.getString(cursor.getColumnIndex(NoteDB.TIME)));
                i.putExtra(NoteDB.PATH,cursor.getString(cursor.getColumnIndex(NoteDB.PATH)));
                startActivity(i);
            }
        });

    }

    public void initView() {
        lv = (ListView) findViewById(R.id.list);
        text_btn = (Button) findViewById(R.id.text);
        img_btn = (Button) findViewById(R.id.image);
    }

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
