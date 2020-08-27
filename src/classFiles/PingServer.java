package classFiles;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Random;


public class PingServer {

	static final double LOSS_RATE = 0.3;
	static final int AVERAGE_DELAY = 100;
	
	public static void main(String argv[]) {
		PingServer server = new PingServer();
		server.run();
	}
	
	public void run() {
		DatagramSocket udpSocket;
		Random rand = new Random(new Date().getTime());
		int port = 5520;
		//int count = 0;
		try {
			udpSocket = new DatagramSocket(port);
			System.out.println("Ping Server running...");
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		while (true) {
			try {
				byte[] buff=new byte[512];
				DatagramPacket pack = new DatagramPacket(buff,512);
				System.out.println("Waiting for UDP packet...");
				udpSocket.receive(pack);
				//count++;
				System.out.println("Received from: "+ pack.getAddress().getHostAddress() + new String(pack.getData()));
				if (rand.nextDouble() < LOSS_RATE) {
					System.out.println("Packet loss..., reply not sent.");
					continue;
				}
				Thread.sleep((int) (rand.nextDouble()*2*AVERAGE_DELAY));
				DatagramPacket outpack = new DatagramPacket(pack.getData(),512,InetAddress.getLocalHost(),pack.getPort());
				udpSocket.send(outpack);
				System.out.println("Reply sent.");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
				continue;
			}
		}

		
	}
}
