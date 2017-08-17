package com.cry.cryUtils.gen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class CommonUtils {

	public static void main(String[] args) {

	}

	
	
	public static void genFile(XWPFDocument doc) throws IOException{
		String formatDate = DateFormatUtils.format(new Date(),"yyyyMMddHHmmss");
		String fileName = "C://recycle/t1"+formatDate+".docx";
		System.out.println(fileName);
		File file = new File(fileName);
		file.createNewFile();
		OutputStream out = new FileOutputStream(file);
		try {
			doc.write(out);
		} finally {
			out.close();
		}
	}
}
