package jp.hidetobara.kusokora;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;
import android.graphics.PorterDuff;

public class OpenCarView extends SurfaceView implements Callback, Runnable {
	Thread _thread;
    SurfaceHolder _holder;
    //画像読み込み
    Resources _res = this.getContext().getResources();
    Bitmap _car;

    public static int imagesIndex = 0;
    final int[] _images = new int[] { R.drawable.car_a, R.drawable.car_b, R.drawable.car_c };
    
    final int LOOP_MAX = 180;
    int _loop = 0;
    
    public OpenCarView(Context context) {
        super(context);
        initialize();
    }
    public OpenCarView(Context context, AttributeSet attrs){
        super(context);
        initialize();
    	
    }
    public OpenCarView(Context context, AttributeSet attrs, int defStyle){
        super(context);
        initialize();    	
    }
    
    private void initialize(){
        _holder = getHolder();
    	// 半透明を設定
        _holder.setFormat(PixelFormat.TRANSLUCENT);
        //callbackメソッドを登録
        _holder.addCallback(this);  
        // フォーカス可
        setFocusable(true);
        // このViewをトップにする
        setZOrderOnTop(true);
        
        change(imagesIndex);
    }
    
    public void change(int index){
    	if(_car != null){
    		_car.recycle();
    	}
    	
    	if(_images.length < index) index = 0;
    	_car = BitmapFactory.decodeResource(_res, _images[index]);
    }

    //サーフェイス変化で実行される
    @Override
    public void surfaceChanged(SurfaceHolder holder, int f, int w, int h) {
    }

    //サーフェイス生成で実行される
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    	callDraw(holder);
    	
        _thread = new Thread(this);
        _thread.start();
    }

    //サーフェイス破棄で実行される
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    	_thread = null;
    }

    @Override
    public void run() {
    	while(true)
    	{
    		try
    		{
    			if(_thread == null) break;
    			
    			callDraw(_holder);
    			Thread.sleep(30);
    		}
    		catch(Exception e)
    		{
    			break;
    		}
    	}
    }
    
    private void callDraw(SurfaceHolder holder)
    {
    	if(_car == null) return;
    	
    	int width = _car.getWidth();
    	int height = _car.getHeight();
    	float scale = (float)getWidth() / (float)width;
    	
    	float theta = (float)_loop / (float)LOOP_MAX * (float)Math.PI * 2.0f;
    	float cos1 = (float)Math.cos( theta );
    	float sin4 = (float)Math.sin( theta * 4.0f );
    	float bias = cos1 * cos1 * 20.0f + sin4 * sin4 * 5.0f;
    	
    	Rect src = new Rect(0, 0, width, height);
    	RectF dst = new RectF(0, getHeight() - height * scale + bias, getWidth(), getHeight() + bias);
        //描画処理
		Canvas c = holder.lockCanvas();
		c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
		c.drawBitmap(_car, src, dst, null);
		holder.unlockCanvasAndPost(c);
		
		_loop = (++_loop) % LOOP_MAX;
    }
}