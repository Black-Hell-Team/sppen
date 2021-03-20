package com.my.newproject2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Tela02Activity extends Activity {
	private Timer _timer;
	private LinearLayout linear1;
	private TimerTask tempo;
	private TextView textview1;
	private TextView textview10;
	private TextView textview11;
	private TextView textview2;
	private TextView textview3;
	private TextView textview4;
	private TextView textview5;
	private TextView textview6;
	private TextView textview7;
	private TextView textview8;
	private TextView textview9;
	private ScrollView vscroll2;

	class AnonymousClass_1 extends TimerTask {
		final /* synthetic */ Tela02Activity this$0;

		class AnonymousClass_1 implements Runnable {
			final /* synthetic */ Tela02Activity.AnonymousClass_1 this$1;

			AnonymousClass_1(Tela02Activity.AnonymousClass_1 r1_Tela02Activity_AnonymousClass_1) {
				super();
				this$1 = r1_Tela02Activity_AnonymousClass_1;
			}

			public void run() {
				this$1.this$0.textview2.setTextColor(-2818048);
			}
		}


		AnonymousClass_1(Tela02Activity r1_Tela02Activity) {
			super();
			this$0 = r1_Tela02Activity;
		}

		public void run() {
			this$0.runOnUiThread(new AnonymousClass_1(this));
		}
	}

	class AnonymousClass_2 extends TimerTask {
		final /* synthetic */ Tela02Activity this$0;

		class AnonymousClass_1 implements Runnable {
			final /* synthetic */ Tela02Activity.AnonymousClass_2 this$1;

			AnonymousClass_1(Tela02Activity.AnonymousClass_2 r1_Tela02Activity_AnonymousClass_2) {
				super();
				this$1 = r1_Tela02Activity_AnonymousClass_2;
			}

			public void run() {
				this$1.this$0.textview3.setTextColor(-2818048);
			}
		}


		AnonymousClass_2(Tela02Activity r1_Tela02Activity) {
			super();
			this$0 = r1_Tela02Activity;
		}

		public void run() {
			this$0.runOnUiThread(new AnonymousClass_1(this));
		}
	}

	class AnonymousClass_3 extends TimerTask {
		final /* synthetic */ Tela02Activity this$0;

		class AnonymousClass_1 implements Runnable {
			final /* synthetic */ Tela02Activity.AnonymousClass_3 this$1;

			AnonymousClass_1(Tela02Activity.AnonymousClass_3 r1_Tela02Activity_AnonymousClass_3) {
				super();
				this$1 = r1_Tela02Activity_AnonymousClass_3;
			}

			public void run() {
				this$1.this$0.textview4.setTextColor(-2818048);
			}
		}


		AnonymousClass_3(Tela02Activity r1_Tela02Activity) {
			super();
			this$0 = r1_Tela02Activity;
		}

		public void run() {
			this$0.runOnUiThread(new AnonymousClass_1(this));
		}
	}

	class AnonymousClass_4 extends TimerTask {
		final /* synthetic */ Tela02Activity this$0;

		class AnonymousClass_1 implements Runnable {
			final /* synthetic */ Tela02Activity.AnonymousClass_4 this$1;

			AnonymousClass_1(Tela02Activity.AnonymousClass_4 r1_Tela02Activity_AnonymousClass_4) {
				super();
				this$1 = r1_Tela02Activity_AnonymousClass_4;
			}

			public void run() {
				this$1.this$0.textview5.setTextColor(-2818048);
			}
		}


		AnonymousClass_4(Tela02Activity r1_Tela02Activity) {
			super();
			this$0 = r1_Tela02Activity;
		}

		public void run() {
			this$0.runOnUiThread(new AnonymousClass_1(this));
		}
	}

	class AnonymousClass_5 extends TimerTask {
		final /* synthetic */ Tela02Activity this$0;

		class AnonymousClass_1 implements Runnable {
			final /* synthetic */ Tela02Activity.AnonymousClass_5 this$1;

			AnonymousClass_1(Tela02Activity.AnonymousClass_5 r1_Tela02Activity_AnonymousClass_5) {
				super();
				this$1 = r1_Tela02Activity_AnonymousClass_5;
			}

			public void run() {
				this$1.this$0.textview6.setTextColor(-2818048);
			}
		}


		AnonymousClass_5(Tela02Activity r1_Tela02Activity) {
			super();
			this$0 = r1_Tela02Activity;
		}

		public void run() {
			this$0.runOnUiThread(new AnonymousClass_1(this));
		}
	}

	class AnonymousClass_6 extends TimerTask {
		final /* synthetic */ Tela02Activity this$0;

		class AnonymousClass_1 implements Runnable {
			final /* synthetic */ Tela02Activity.AnonymousClass_6 this$1;

			AnonymousClass_1(Tela02Activity.AnonymousClass_6 r1_Tela02Activity_AnonymousClass_6) {
				super();
				this$1 = r1_Tela02Activity_AnonymousClass_6;
			}

			public void run() {
				this$1.this$0.textview7.setTextColor(-2818048);
			}
		}


		AnonymousClass_6(Tela02Activity r1_Tela02Activity) {
			super();
			this$0 = r1_Tela02Activity;
		}

		public void run() {
			this$0.runOnUiThread(new AnonymousClass_1(this));
		}
	}

	class AnonymousClass_7 extends TimerTask {
		final /* synthetic */ Tela02Activity this$0;

		class AnonymousClass_1 implements Runnable {
			final /* synthetic */ Tela02Activity.AnonymousClass_7 this$1;

			AnonymousClass_1(Tela02Activity.AnonymousClass_7 r1_Tela02Activity_AnonymousClass_7) {
				super();
				this$1 = r1_Tela02Activity_AnonymousClass_7;
			}

			public void run() {
				this$1.this$0.textview8.setTextColor(-2818048);
			}
		}


		AnonymousClass_7(Tela02Activity r1_Tela02Activity) {
			super();
			this$0 = r1_Tela02Activity;
		}

		public void run() {
			this$0.runOnUiThread(new AnonymousClass_1(this));
		}
	}

	class AnonymousClass_8 extends TimerTask {
		final /* synthetic */ Tela02Activity this$0;

		class AnonymousClass_1 implements Runnable {
			final /* synthetic */ Tela02Activity.AnonymousClass_8 this$1;

			AnonymousClass_1(Tela02Activity.AnonymousClass_8 r1_Tela02Activity_AnonymousClass_8) {
				super();
				this$1 = r1_Tela02Activity_AnonymousClass_8;
			}

			public void run() {
				this$1.this$0.textview10.setTextColor(-2818048);
			}
		}


		AnonymousClass_8(Tela02Activity r1_Tela02Activity) {
			super();
			this$0 = r1_Tela02Activity;
		}

		public void run() {
			this$0.runOnUiThread(new AnonymousClass_1(this));
		}
	}

	class AnonymousClass_9 extends TimerTask {
		final /* synthetic */ Tela02Activity this$0;

		class AnonymousClass_1 implements Runnable {
			final /* synthetic */ Tela02Activity.AnonymousClass_9 this$1;

			AnonymousClass_1(Tela02Activity.AnonymousClass_9 r1_Tela02Activity_AnonymousClass_9) {
				super();
				this$1 = r1_Tela02Activity_AnonymousClass_9;
			}

			public void run() {
				this$1.this$0.textview11.setTextColor(-2818048);
			}
		}


		AnonymousClass_9(Tela02Activity r1_Tela02Activity) {
			super();
			this$0 = r1_Tela02Activity;
		}

		public void run() {
			this$0.runOnUiThread(new AnonymousClass_1(this));
		}
	}


	public Tela02Activity() {
		super();
		_timer = new Timer();
	}

	private void initialize(Bundle r2_Bundle) {
		vscroll2 = (ScrollView) findViewById(R.id.vscroll2);
		linear1 = (LinearLayout) findViewById(R.id.linear1);
		textview1 = (TextView) findViewById(R.id.textview1);
		textview2 = (TextView) findViewById(R.id.textview2);
		textview3 = (TextView) findViewById(R.id.textview3);
		textview4 = (TextView) findViewById(R.id.textview4);
		textview5 = (TextView) findViewById(R.id.textview5);
		textview6 = (TextView) findViewById(R.id.textview6);
		textview7 = (TextView) findViewById(R.id.textview7);
		textview8 = (TextView) findViewById(R.id.textview8);
		textview10 = (TextView) findViewById(R.id.textview10);
		textview11 = (TextView) findViewById(R.id.textview11);
		textview9 = (TextView) findViewById(R.id.textview9);
	}

	private void initializeLogic() {
		tempo = new AnonymousClass_1(this);
		_timer.schedule(tempo, 2010);
		tempo = new AnonymousClass_2(this);
		_timer.schedule(tempo, 2020);
		tempo = new AnonymousClass_3(this);
		_timer.schedule(tempo, 2030);
		tempo = new AnonymousClass_4(this);
		_timer.schedule(tempo, 2040);
		tempo = new AnonymousClass_5(this);
		_timer.schedule(tempo, 2050);
		tempo = new AnonymousClass_6(this);
		_timer.schedule(tempo, 2060);
		tempo = new AnonymousClass_7(this);
		_timer.schedule(tempo, 2070);
		tempo = new AnonymousClass_8(this);
		_timer.schedule(tempo, 2080);
		tempo = new AnonymousClass_9(this);
		_timer.schedule(tempo, 2090);
	}

	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView r7_ListView) {
		ArrayList<Double> r1_ArrayList_Double = new ArrayList();
		SparseBooleanArray r2_SparseBooleanArray = r7_ListView.getCheckedItemPositions();
		int r0i = 0;
		while (r0i < r2_SparseBooleanArray.size()) {
			if (r2_SparseBooleanArray.valueAt(r0i)) {
				r1_ArrayList_Double.add(Double.valueOf((double) r2_SparseBooleanArray.keyAt(r0i)));
			}
			r0i++;
		}
		return r1_ArrayList_Double;
	}

	@Deprecated
	public float getDip(int r4i) {
		return TypedValue.applyDimension(1, (float) r4i, getResources().getDisplayMetrics());
	}

	@Deprecated
	public int getDisplayHeightPixels() {
		return getResources().getDisplayMetrics().heightPixels;
	}

	@Deprecated
	public int getDisplayWidthPixels() {
		return getResources().getDisplayMetrics().widthPixels;
	}

	@Deprecated
	public int getLocationX(View r3_View) {
		int[] r0_int_A = new int[2];
		r3_View.getLocationInWindow(r0_int_A);
		return r0_int_A[0];
	}

	@Deprecated
	public int getLocationY(View r3_View) {
		int[] r0_int_A = new int[2];
		r3_View.getLocationInWindow(r0_int_A);
		return r0_int_A[1];
	}

	@Deprecated
	public int getRandom(int r3i, int r4i) {
		return new Random().nextInt((r4i - r3i) + 1) + r3i;
	}

	protected void onActivityResult(int r1i, int r2i, Intent r3_Intent) {
		super.onActivityResult(r1i, r2i, r3_Intent);
	}

	protected void onCreate(Bundle r2_Bundle) {
		super.onCreate(r2_Bundle);
		setContentView(R.layout.tela02);
		initialize(r2_Bundle);
		initializeLogic();
	}

	@Deprecated
	public void showMessage(String r3_String) {
		Toast.makeText(getApplicationContext(), r3_String, 0).show();
	}
}
