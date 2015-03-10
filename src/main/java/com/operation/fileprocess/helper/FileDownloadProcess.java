package com.operation.fileprocess.helper;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class FileDownloadProcess {

	
	public static String downloadFile() throws IOException {
		String DESKPATH = (System.getProperty("user.home") + "/Desktop")
				.replace("\\", "/");
		new File(DESKPATH + "/XOANON_SURESH").mkdir();
		String partialFileName = DESKPATH + "/XOANON_SURESH/";
		String fileName = DESKPATH + "/XOANON_SURESH/DownloadedFile.txt";
		System.out.println("Folder created in desktop successfully!");
		System.out.println("Dowloaded file location is : " + fileName);
		URL link = new URL("http://www.gutenberg.org/cache/epub/2600/pg2600.txt");
		InputStream in = new BufferedInputStream(link.openStream());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[2048];
		int n = 0;
		while (-1 != (n = in.read(buf))) {
			out.write(buf, 0, n);
		}
		out.close();
		in.close();
		byte[] response = out.toByteArray();

		FileOutputStream fos = new FileOutputStream(fileName);
		fos.write(response);
		fos.close();

		System.out.println("File download complete!");

		return partialFileName;


}
}
