package classFiles;

import java.io.*;
import java.util.*;


public class HTTP {

	boolean fileExists = false;
	String origfile;
	DataOutputStream datastream;
	public HTTP() {
		
	}
	
	
	public void parse(OutputStream out,String entry) {

		StringTokenizer token = new StringTokenizer(entry);
		String method;
		if (token.hasMoreTokens())
			 method = token.nextToken();
		else {
			System.out.println("bad input");return;
		}
		String fileName;
		if (token.hasMoreTokens())
		 fileName = token.nextToken();
		else {
			System.out.println("bad input");return;
		}
		origfile = fileName;
		fileExists = true;
		FileInputStream file = null;
		try {
		file = new FileInputStream("." +fileName); 
		} catch (Exception e) {
			fileExists = false;
		}
		//if (token.hasMoreTokens())
		//String version = token.nextToken();
		System.out.println(fileName);
		
		responseWrite(out,file);
		
		
	}
	public void responseWrite(OutputStream out,FileInputStream file) {
		String data= "HTTP/1.0";
		if (fileExists) {
			data = data + " 200" + " OK";
		} else {
			data = data + " 404" + " Not Found";
		}
		//PrintWriter writer = new PrintWriter(out);
		data += "\r\n";
		data += "Content-type: " + contentType(origfile) + "\r\n";
		datastream = new DataOutputStream(out);
		try {
			datastream.writeBytes(data);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println(e1.getMessage());
		}
		byte[] buffer = new byte[1024];
		if (fileExists) {

			try {
			while (file.read(buffer) != -1) {
					out.write(buffer,0,1024);
			}
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
		} else {
			try {
				datastream.writeBytes("<HTML>" + "<HEAD><TITLE>Not Found</TITLE></HEAD>"+ "<BODY>Not Found</BODY></HTML>");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private String contentType(String fileName) {
		if (fileName.endsWith("htm") ||fileName.endsWith("html")) {
			return "text/html\r\n";
		}
		else if (fileName.endsWith("gif")) {
			return "image/gif\r\n";
		} else if (fileName.endsWith("bmp")) {
			return "image/bmp\r\n";
		} else if (fileName.endsWith("jpeg")|| fileName.endsWith("jpe")||fileName.endsWith("jpg")) {
			return "image/jpeg\r\n";
		} else if (fileName.endsWith("pdf")) {
			return "application/pdf\r\n";
		}
		return "application/octetstream";
		
	}
	
}
