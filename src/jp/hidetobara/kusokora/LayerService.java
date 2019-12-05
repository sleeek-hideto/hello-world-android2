package jp.hidetobara.kusokora;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

public class LayerService extends Service {
    View _view;
    WindowManager _wm;
     
    @Override
    public int onStartCommand(Intent intent, int flag, int startId) {
        super.onStartCommand(intent, flag, startId);
        
        OpenCarView.imagesIndex = intent.getIntExtra("index", 0);
        
        // Viewからインフレータを作成する
        LayoutInflater layoutInflater = LayoutInflater.from(this);
 
        // 重ね合わせするViewの設定を行う
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);
         
        // WindowManagerを取得する
        _wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
         
        // レイアウトファイルから重ね合わせするViewを作成する
        _view = layoutInflater.inflate(R.layout.overlay, null);
 
        // Viewを画面上に重ね合わせする
        _wm.addView(_view, params);
        
        return START_NOT_STICKY;
    }
 
    @Override
    public void onCreate() {
        super.onCreate();
    }
 
    @Override
    public void onDestroy() {
        super.onDestroy();
         
        // サービスが破棄されるときには重ね合わせしていたViewを削除する
        if(_wm != null) _wm.removeView(_view);
        _wm = null;
    }
 
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
}