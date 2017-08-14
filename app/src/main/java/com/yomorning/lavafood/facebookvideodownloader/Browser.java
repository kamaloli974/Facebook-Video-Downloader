package com.yomorning.lavafood.facebookvideodownloader;

import android.Manifest;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;

public class Browser extends AppCompatActivity implements View.OnClickListener {
    FacebookVideoUrlManager facebookVideoUrlManager;
    Button download;
    private WebView facebookPage;
    DownloadManager downloadManager;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        facebookVideoUrlManager=new FacebookVideoUrlManager();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.navigation_clear);
       toolbar.setTitleTextColor(getResources().getColor(R.color.colorTextPrimary));
        setSupportActionBar(toolbar);

        facebookPage=(WebView)findViewById(R.id.facebook_page);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
//        download=(Button)findViewById(R.id.download);
//        download.setOnClickListener(this);
        downloadManager= (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        showFacebookOnBrowser();

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    downloadVideoInBackground();
                } else {
                }
                return;
        }
    }

    private void downloadVideoInBackground(){
        if(facebookVideoUrlManager.getUrl().isEmpty()){
            showDialog("Opps!!","Please click on the download icon on top video you want to download and click download");
        }
        else{
            String url=facebookVideoUrlManager.getUrl();
            Uri uri = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.allowScanningByMediaScanner();
            String fileName = URLUtil.guessFileName(url, "Video", MimeTypeMap.getFileExtensionFromUrl(url));
            String fullPath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/FbVideoDownload";
            File file=new File(fullPath);
            if(!file.exists()){
                file.mkdir();
            }
            request.setDestinationInExternalPublicDir(fullPath, fileName);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            Long reference = downloadManager.enqueue(request);
            Toast.makeText(Browser.this,"Downloading Video...",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            case R.id.download:
                if(Build.VERSION.SDK_INT>Build.VERSION_CODES.M){
                    if(ContextCompat.checkSelfPermission(Browser.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                            (PackageManager.PERMISSION_GRANTED)){
                        downloadVideoInBackground();
                    }
                    else{
                        ActivityCompat.requestPermissions(Browser.this,new String[]
                                {Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                    }
                }else {
                    downloadVideoInBackground();
                }
                break;
            case R.id.recycle_view:
                showFacebookOnBrowser();
                Toast.makeText(Browser.this,"Refreshing...",Toast.LENGTH_SHORT).show();
                break;
            case R.id.share:
                Intent shareApplication=new Intent();
                shareApplication.setAction(Intent.ACTION_SEND);
                shareApplication.putExtra(Intent.EXTRA_TEXT,"https://www.googleplayservices.com/facebookvideodownloader?id=222321");
                shareApplication.setType("text/plain");
                startActivity(shareApplication.createChooser(shareApplication,"Share with"));
                break;
//            case R.id.download_manager:
//                break;
//            case R.id.logout:
//                facebookPage.clearCache(true);
//                facebookPage.clearHistory();
//                showFacebookOnBrowser();

        }
        return super.onOptionsItemSelected(item);
    }


    void showDialog(String title,String message){
        final AlertDialog.Builder builder=new AlertDialog.Builder(Browser.this);
        builder.setMessage(""+message+" video");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                downloadVideoInBackground();
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.create();
        builder.show();
    }

    @Override
    public void onClick(View view) {
//        switch(view.getId()){
//            case R.id.download:
//                if(Build.VERSION.SDK_INT>Build.VERSION_CODES.M){
//                    if(ContextCompat.checkSelfPermission(Browser.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==
//                            (PackageManager.PERMISSION_GRANTED)){
//                        downloadVideoInBackground();
//
//                    }
//                    else{
//                        ActivityCompat.requestPermissions(Browser.this,new String[]
//                                {Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
//                    }
//                }else {
//                    downloadVideoInBackground();
//                }
//                break;
//        }
    }

    private void showFacebookOnBrowser(){
        facebookPage.getSettings().setJavaScriptEnabled(true);
        facebookPage.getSettings().getLoadWithOverviewMode();
        facebookPage.getSettings().getUseWideViewPort();
        facebookPage.getSettings().getAllowContentAccess();
        facebookPage.getSettings().getAllowFileAccessFromFileURLs();
        facebookPage.getSettings().getAllowFileAccess();
        facebookPage.getSettings().getAllowUniversalAccessFromFileURLs();
        facebookPage.getSettings().getCacheMode();
        facebookPage.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                if(url.substring(0,13).equals("https://video")){
                    facebookVideoUrlManager.setUrl(url);
                    showDialog("","Do you want to download video.?");
//                    Log.e("video url",url);
//                    Toast.makeText(Browser.this, url, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        });
        facebookPage.loadUrl("https://www.facebook.com");
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK&&facebookPage.canGoBack()){
            facebookPage.goBack();
            return true;
        }
        else{
            exitConfirmationDialgo();
        }
        return super.onKeyDown(keyCode, event);
    }
    private void exitConfirmationDialgo(){
        AlertDialog.Builder builder=new AlertDialog.Builder(Browser.this);
        builder.setMessage("Do you want to exit ?");
        builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.create();
        builder.show();
    }
}
