package dumal.net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class OneMore {
	public OneMore() {
		super();
		OneMore r0_OneMore = this;
		OneMore r2_OneMore = r0_OneMore;
	}

	public void writeFile() {
		FileOutputStream r12_FileOutputStream;
		String r5_String;
		int r6i;
		File r12_File = r8_File;
		StringBuffer r12_StringBuffer = r10_StringBuffer;
		File r2_File = r12_File;
		r12_File = r8_File;
		File r3_File = r12_File;
		try {
			r12_FileOutputStream = r8_FileOutputStream;
			r5_String = "0";
			r6i = 0;
			while (true) {
				r12_StringBuffer = r8_StringBuffer;
				r5_String = r12_StringBuffer.append(r5_String).append(String.valueOf(r6i)).toString();
				r12_FileOutputStream.write(r5_String.getBytes());
				r6i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
}
