package classFiles;
import java.io.*;
import java.net.*;
import java.util.*;

//import classFiles.ServerThread.PolyAlphabetic;
public class Server {
	PrintWriter logWriter;
	
	public static void main(String argv[]) {
		Server serv = new Server();
		serv.run();
	}
	
	public void run() {
		ServerSocket ssocket;
		PrintWriter writer;
		FileOutputStream output;
		try {
			ssocket = new ServerSocket(5520);
			output = new FileOutputStream("prog1b.log");
			writer = new PrintWriter(output,true);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		while (true) {
			Socket sock;
			try {
				sock = ssocket.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				writer.println(e.getMessage());
				continue;
			}
			if (sock.isConnected()) {
				ServerThread servThread = new ServerThread(sock,writer);
				servThread.start();
			}
		}
	}
	
	private class ServerThread extends Thread {
		Socket socket;
		PrintWriter sockWriter;
		BufferedReader reader;

		public ServerThread(Socket clientSock,PrintWriter logFile) {
			socket = clientSock;
			logWriter = logFile;
			try {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				sockWriter = new PrintWriter(socket.getOutputStream(),true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logWriter.println(e.getMessage());
			}
		}
		
		public void run() {
			//sockWriter.println("Connection received at " + socket.getInetAddress() + ", Port:" + socket.getPort() + " on " + new Date().toString());
			logWriter.println("Connection received at " + socket.getInetAddress() + ", Port:" + socket.getPort() + " on " + new Date().toString());
			//sockWriter.flush();
			String line= "";
			//boolean exception = sockWriter.checkError();
			PolyAlphabetic crypto = new PolyAlphabetic();
			boolean active = true;
			while (active){
				try {
				
					if (!reader.ready()) continue;
					line = reader.readLine();
					if (line.equals("quit")) break;
					logWriter.println(line);
					String out = crypto.encryptMessage(line);
					sockWriter.println(out);
					logWriter.println(out);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logWriter.println(e.getMessage());
					break;
				}
			}
			sockWriter.println("Good Bye!");
			
			//sockWriter.println("Connection closed at " + socket.getInetAddress() + ", Port:" + socket.getPort() + " on " + new Date().toString());
			logWriter.println("Connection closed at " + socket.getInetAddress() + ", Port:" + socket.getPort() + " on " + new Date().toString());

			try {
				socket.close();
			} catch(IOException e) {
				logWriter.println(e.getMessage());
			} finally {
				sockWriter.close();
				logWriter.close();
			}
			return;
		}
		
		private class PolyAlphabetic {
			int off1,off2;
			Random rand =new Random();
			public PolyAlphabetic() {
				off1 = rand.nextInt(24)+1;
				do {
					off2 = rand.nextInt(24)+1;
				} while (off1==off2);
			}
			public String encryptMessage(String entry) {
				String output = entry;
				for (int i = 0;i<output.length();i++) {
					int cipherCheck = i%5,offset;
					if (cipherCheck == 0 || cipherCheck==3||cipherCheck == 4) {
						offset = off1;
					}else {
						offset = off2;
					}
					char edit = output.charAt(i);
					int ednum = (int)(edit);//101
					if (!Character.isAlphabetic(edit))continue;
					if (Character.isLowerCase(edit)) {
						if ((ednum+offset) >122	) {//101+24 = 125
							offset = Math.abs(122-ednum-offset);//24-(122-101) == 3
							edit = (char)(offset+97);
						} else {
							edit += offset;
						}
					} else {

						if ((ednum+offset) >90) {
							offset = Math.abs(90-ednum-offset);
							edit = (char)(offset+65);
						} else {
							edit += offset;
						}
					}
					output = output.substring(0,i) + Character.toString(edit) + output.substring(i+1);
				}
				return output;
			}
		}
	}
}

