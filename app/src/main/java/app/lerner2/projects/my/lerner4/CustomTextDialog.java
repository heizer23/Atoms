package lerner.lerner;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class CustomTextDialog extends Dialog implements OnClickListener{
	
	Context ourContext;
	LinearLayout lay;
	ScrollView scroll;
	TextView tv;
	public CustomTextDialog(Context context) {
		super(context);
		lay = new LinearLayout(context);
		lay.setPadding(30, 30, 30, 90);
		 scroll = new ScrollView(context);
		 tv = new TextView(context);
		 tv.setPadding(90, 90, 90, 90);
			tv.setGravity(Gravity.CENTER);
			tv.setOnClickListener(this);
			scroll.addView(tv);
			lay.addView(scroll);
			setContentView(lay);
		}
	
	public CustomTextDialog(Context context, String title, String text, int color) {
		super(context);
		// TODO Auto-generated constructor stub
	
	
		LinearLayout lay = new LinearLayout(context);
		ScrollView scroll = new ScrollView(context);
		TextView tv = new TextView(context);
		this.setTitle(title);
		lay.setOrientation(LinearLayout.VERTICAL);		
		if(color!=0){
			tv.setTextColor(color);
		}
	
		tv.setText(text);
		tv.setGravity(Gravity.CENTER);
		tv.setOnClickListener(this);
		scroll.addView(tv);
		lay.addView(scroll);
		setContentView(lay);
	
	}

	public void setTextTitle(String title, String text){
		this.setTitle(title);
		tv.setText(text);
	}
	
	public void onClick(View v) {
		this.cancel();
	}

	public void cancelTimer(int time){
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				cancel();
			}
		}, time);
	}
}
