package classFiles;

import java.io.*;
import java.net.*;
import java.util.Date;

public class FileServer {
	ServerSocket sockFack;
	Socket sock;
	BufferedReader reader;
	DataOutputStream out;
	PrintWriter writer;
	public static void main(String[] args) {
		FileServer serv = new FileServer();
		serv.run();
	}
	
	public void run() {
		System.out.println("Server running...");
		try {
			sockFack = new ServerSocket(5520);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (true) {
			try {
				System.out.println("Waiting for connection...");
				sock = sockFack.accept();
				System.out.println("Got a connection: " +new Date().toString());
				System.out.println("Connected to: "+ sock.getInetAddress().getHostAddress()+ " Port: " + sock.getPort());
				reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				writer = new PrintWriter(sock.getOutputStream());
				out = new DataOutputStream(sock.getOutputStream());
				String fileName = getNullTerminatedString();
				System.out.println("File name received: "+ fileName);
				String fileSize = getNullTerminatedString();
				System.out.println("File size received: " + fileSize);
				getFile(fileName,Long.valueOf(fileSize));
				System.out.println("Received File.");
				writer.write("@");
				writer.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				writer.write("File could not be received");
				break;
			}
			try {
				sock.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	private String getNullTerminatedString() {
		String out = "";
		byte buff;
		try {
			while ((buff = sock.getInputStream().readNBytes(1)[0]) != '\0') {
				out += (char)(buff);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return out;
	}
	
	private void getFile(String filename,long size) {
		byte[] buff = new byte[1024];
		int len;
		int loops = (int)size/1024;
		int end = (int) (size %1024);
		try {
			FileOutputStream fis = new FileOutputStream(filename);
			while ((len = sock.getInputStream().read(buff))>0) {
				fis.write(buff,0,len);
				if (len < 1024) break;
			}
			/*while (loops > 0) {
				sock.getInputStream().read(buff);
				fis.write(buff);
				loops--;
			}
			sock.getInputStream().read(buff,0,end);
			fis.write(buff);
			fis.write('\0');*/
			fis.close();
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			writer.write("error reading file.");
		}
		
	}
	
}
