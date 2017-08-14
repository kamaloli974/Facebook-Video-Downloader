package com.yomorning.lavafood.facebookvideodownloader;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.Log;

import com.yomorning.lavafood.facebookvideodownloader.Model.VideoModel;

import java.util.ArrayList;

/**
 * Created by KAMAL OLI on 14/08/2017.
 */

public class GetDataForAdapter {
    private Context context;
    private ContentResolver contentResolver;
    private Cursor cursor;
    public GetDataForAdapter(Context context){
        this.context=context;
        contentResolver=context.getContentResolver();


    }
    public ArrayList<VideoModel> getVideoData(){
        ArrayList<VideoModel> models=new ArrayList<>();
        contentResolver=context.getContentResolver();
        String[] projection={MediaStore.Video.Media._ID,MediaStore.Video.Media.SIZE,MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DISPLAY_NAME};
        String[] selectionArgs=new String[]{"FbVideoDownload"};
        cursor= contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,null,null,MediaStore.Video.Media.DATE_TAKEN);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                int id=cursor.getColumnIndex(MediaStore.Video.Media._ID);
                int name=cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME);
                int data=cursor.getColumnIndex(MediaStore.Video.Media.DATA);
                do{
                    VideoModel model=new VideoModel();
                    model.setName(cursor.getString(name));
                    model.setUrl(cursor.getString(data));
                    model.setId(cursor.getInt(id));
                    Bitmap bitmap=MediaStore.Video.Thumbnails.getThumbnail(contentResolver,model.getId(), MediaStore.Images.Thumbnails.MINI_KIND,
                            null);
                    if(bitmap==null){
                        bitmap= ThumbnailUtils.createVideoThumbnail(model.getUrl(), MediaStore.Images.Thumbnails.MINI_KIND);
                    }
                    model.setImageBitmap(bitmap);
                    models.add(model);
                    Log.e("video file name",
                            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
                }while(cursor.moveToNext());
            }
            else{
                Log.e("No media file","Present in the selected directory");
            }
            Log.e("Cursor",cursor.toString());
        }
        else{
            Log.e("No data in Cursor",cursor.toString()+"");
        }
        return models;
    }
}
