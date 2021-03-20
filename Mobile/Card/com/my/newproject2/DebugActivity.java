package com.my.newproject2;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;

public class DebugActivity extends Activity {
	String[] errMessage;
	String[] exceptionType;

	class AnonymousClass_1 implements OnClickListener {
		final /* synthetic */ DebugActivity this$0;

		AnonymousClass_1(DebugActivity r1_DebugActivity) {
			super();
			this$0 = r1_DebugActivity;
		}

		public void onClick(DialogInterface r2_DialogInterface, int r3i) {
			this$0.finish();
		}
	}


	public DebugActivity() {
		super();
		String[] r0_String_A = new String[5];
		r0_String_A[0] = "StringIndexOutOfBoundsException";
		r0_String_A[1] = "IndexOutOfBoundsException";
		r0_String_A[2] = "ArithmeticException";
		r0_String_A[3] = "NumberFormatException";
		r0_String_A[4] = "ActivityNotFoundException";
		exceptionType = r0_String_A;
		r0_String_A = new String[5];
		r0_String_A[0] = "Invalid string operation\n";
		r0_String_A[1] = "Invalid list operation\n";
		r0_String_A[2] = "Invalid arithmetical operation\n";
		r0_String_A[3] = "Invalid toNumber block operation\n";
		r0_String_A[4] = "Invalid intent operation";
		errMessage = r0_String_A;
	}

	protected void onCreate(Bundle r8_Bundle) {
		String r0_String;
		Builder r1_Builder;
		int r2i = 0;
		super.onCreate(r8_Bundle);
		Intent r0_Intent = getIntent();
		String r1_String = "";
		if (r0_Intent != null) {
			r0_String = r0_Intent.getStringExtra("error");
			String[] r3_String_A = r0_String.split("\n");
			while (true) {
				try {
				} catch (Exception e) {
					r0_String = r1_String;
				}
				if (r2i >= exceptionType.length) {
					if (!r1_String.isEmpty()) {
						r1_Builder = new Builder(this);
						r1_Builder.setTitle("An error occured");
						r1_Builder.setMessage(r0_String);
						r1_Builder.setNeutralButton("End Application", new AnonymousClass_1(this));
						r1_Builder.create().show();
					}
					r0_String = r1_String;
				} else {
					int r4i = 0;
					if (r3_String_A[r4i].contains(exceptionType[r2i])) {
						r1_String = new StringBuilder(String.valueOf(errMessage[r2i])).append(r3_String_A[0].substring(exceptionType[r2i].length() + r3_String_A[0].indexOf(exceptionType[r2i]), r3_String_A[0].length())).toString();
						if (!r1_String.isEmpty()) {
							r0_String = r1_String;
						}
					} else {
						r2i++;
					}
				}
			}
		} else {
			r0_String = r1_String;
		}
		r1_Builder = new Builder(this);
		r1_Builder.setTitle("An error occured");
		r1_Builder.setMessage(r0_String);
		r1_Builder.setNeutralButton("End Application", new AnonymousClass_1(this));
		r1_Builder.create().show();
	}
}
