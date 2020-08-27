package classFiles;
import java.io.*;
import java.net.*;
import java.util.*;


public class WebServer {
	
	public static void main(String argv[]) {
		WebServer serv = new WebServer();
		serv.run();
	}
	
	public void run() {
		ServerSocket ssocket;
		PrintWriter writer;
		FileOutputStream output;
		File file = new File(".");
		for(String fileNames : file.list()) System.out.println(fileNames);
		try {
			ssocket = new ServerSocket(5520);
			output = new FileOutputStream("prog1b.log");
			writer = new PrintWriter(output,true);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			return;
		}
		while (true) {
			Socket sock;
			try {
				sock = ssocket.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
				continue;
			}
			if (sock.isConnected()) {
				WebRequest servThread = new WebRequest(sock,writer);
				servThread.start();
			}
		}
	}

}
