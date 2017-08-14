package com.yomorning.lavafood.facebookvideodownloader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yomorning.lavafood.facebookvideodownloader.Controllers.VideoFilesAdapters;
import com.yomorning.lavafood.facebookvideodownloader.Model.VideoModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    RecyclerView recyclerView;
    Button browseFacebook;
    ArrayList<VideoModel> dataList;
    GetDataForAdapter dataForAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dataForAdapter=new GetDataForAdapter(MainActivity.this);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view_for_video);
        dataList=new ArrayList<>();
        browseFacebook=(Button)findViewById(R.id.browse_facebook);
        browseFacebook.setOnClickListener(this);
        setRecyclerView();
    }
    private ArrayList<VideoModel> getDataList(){
        for(int i=0;i<20;i++){
            VideoModel model=new VideoModel();
            model.setName("Video Downloaded From Facebook");
            dataList.add(model);
        }
        return dataList;
    }
    private void setRecyclerView(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    !=PackageManager.PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE},1);
            }
            else{
                recyclerViewPart();
            }
        }
        else{
            recyclerViewPart();
        }

    }
    public void recyclerViewPart(){
        VideoFilesAdapters adapters=new VideoFilesAdapters(MainActivity.this,dataForAdapter.getVideoData());
        recyclerView.setAdapter(adapters);
        GridLayoutManager manager=new GridLayoutManager(MainActivity.this,2);
        recyclerView.setLayoutManager(manager);
        registerForContextMenu(recyclerView);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       switch (requestCode){
           case 1:
               if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                   setRecyclerView();
               }
               else{
//                   AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
//                   builder.setTitle("Permission Denied");
//                   builder.setMessage("Storage Permission is denied. Please exit and reopen the application if you want to
                   Log.e("Permission Denied","True");
               }
               break;
       }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.browse_facebook:
                Intent intent=new Intent(MainActivity.this,Browser.class);
                startActivity(intent);
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
        switch (item.getItemId()){
            case R.id.more_apps:
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT,"https://www.googleplayservices.com/facebookvideodownloader?id=222321");
                intent.setType("text/plain");
                startActivity(intent.createChooser(intent,"Open Url"));
                break;
            case R.id.recycle_view:
                setRecyclerView();
                break;
            case R.id.share:
                Intent shareApplication=new Intent();
                shareApplication.setAction(Intent.ACTION_SEND);
                shareApplication.putExtra(Intent.EXTRA_TEXT,"https://www.googleplayservices.com/facebookvideodownloader?id=222321");
                shareApplication.setType("text/plain");
                startActivity(shareApplication.createChooser(shareApplication,"Share with"));
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
