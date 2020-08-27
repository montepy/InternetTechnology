package classFiles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

//import classFiles.Server.ServerThread.PolyAlphabetic;

public class WebRequest extends Thread {
	Socket socket;
	PrintWriter sockWriter;
	BufferedReader reader;
	PrintWriter logWriter;
	HTTP requestHandler = new HTTP();

	public WebRequest(Socket clientSock,PrintWriter logFile) {
		socket = clientSock;
		logWriter = logFile;
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			sockWriter = new PrintWriter(socket.getOutputStream(),true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
	}
	
	public void run() {
		//sockWriter.println("Connection received at " + socket.getInetAddress() + ", Port:" + socket.getPort() + " on " + new Date().toString());
		System.out.println("Connection received at " + socket.getInetAddress() + ", Port:" + socket.getPort() + " on " + new Date().toString());
		//sockWriter.flush();
		String line= "";
		//boolean exception = sockWriter.checkError();
		boolean active = true;
		while (active){
			try {
			
				if (!reader.ready()) continue;
				line = reader.readLine();
				requestHandler.parse(socket.getOutputStream(),line);
				break;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println(e.getMessage());
				break;
			}
		}
		//sockWriter.println("Good Bye!");
		
		//sockWriter.println("Connection closed at " + socket.getInetAddress() + ", Port:" + socket.getPort() + " on " + new Date().toString());
		System.out.println("Connection closed at " + socket.getInetAddress() + ", Port:" + socket.getPort() + " on " + new Date().toString());

		try {
			socket.close();
		} catch(IOException e) {
			System.out.println(e.getMessage());
		} finally {
			sockWriter.close();
			logWriter.close();
		}
		return;
	}
}
