package com.bluesky.mynote;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by 清晨 on 2015/5/7.
 */
public class MyAdapter extends BaseAdapter {
    private Context mContext;
    private Cursor mCursor;
    private LinearLayout layout;

    public MyAdapter(Context context,Cursor cursor){
        mContext=context;
        mCursor=cursor;
    }
    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        return mCursor.getPosition();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(mContext);
        layout= (LinearLayout) inflater.inflate(R.layout.cell,null);
        TextView content_tv= (TextView) layout.findViewById(R.id.list_content);
        TextView time_tv= (TextView) layout.findViewById(R.id.list_time);
        ImageView img_iv= (ImageView) layout.findViewById(R.id.list_img);
        mCursor.moveToPosition(position);
        String content=mCursor.getString(mCursor.getColumnIndex("content"));
        String time=mCursor.getString(mCursor.getColumnIndex("time"));
        String url=mCursor.getString(mCursor.getColumnIndex("path"));
        content_tv.setText(content);
        time_tv.setText(time);
        img_iv.setImageBitmap(getImageThumbnail(url,200,200));
        return layout;
    }

    //获取缩略图
    public Bitmap getImageThumbnail(String uri,int width,int height){
        Bitmap bitmap=null;
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        bitmap=BitmapFactory.decodeFile(uri,options);
        options.inJustDecodeBounds=false;
        int beWidth=options.outWidth/width;
        int beHeight=options.outHeight/height;
        int be=1;
        if(beWidth<beHeight){
            be=beWidth;
        }else {
            be=beHeight;
        }
        if(be<=0){
            be=1;
        }
        options.inSampleSize=be;
        bitmap=BitmapFactory.decodeFile(uri,options);
        bitmap=ThumbnailUtils.extractThumbnail(bitmap,width,height,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }
}
