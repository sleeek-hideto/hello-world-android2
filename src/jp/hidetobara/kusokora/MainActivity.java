package jp.hidetobara.kusokora;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

public class MainActivity extends Activity {

	ImageButton button_a, button_b, button_c;
    ImageButton button_stop;
 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

        button_a = (ImageButton) findViewById(R.id.button_car_a);
        button_b = (ImageButton) findViewById(R.id.button_car_b);
        button_c = (ImageButton) findViewById(R.id.button_car_c);
        button_stop = (ImageButton) findViewById(R.id.button_close);
         
        final OnClickListener onStartButton = new OnClickListener() {
            @Override
            public void onClick(View v) {
            	stopService(new Intent(MainActivity.this, LayerService.class));
            	
            	Intent intent = new Intent(MainActivity.this, LayerService.class);
            	int index = 0;
            	if(v == button_b) index = 1;
            	else if(v == button_c) index = 2;
            	intent.putExtra("index", index);
                startService(intent);
            }
        };
        button_a.setOnClickListener(onStartButton);
        button_b.setOnClickListener(onStartButton);
        button_c.setOnClickListener(onStartButton);
 
        final OnClickListener onStopButton = new OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(MainActivity.this, LayerService.class));
            }
        };
        button_stop.setOnClickListener(onStopButton);
        
        AdView ad = (AdView)findViewById(R.id.ad_view);
        ad.loadAd(new AdRequest.Builder().build());
    }
}
