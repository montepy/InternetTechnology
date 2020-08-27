package classFiles;

import java.net.*;
import java.util.Date;

public class PingClient extends UDPPinger {
	int[] rtts = new int[10];
	
	public static void main(String argv[]) {
		PingClient client = new PingClient();
		client.run();
	}
	public void run() {
		try {
			PingMessage ping;
			int counter = 0;
			sock = new DatagramSocket();
			sock.setSoTimeout(1000);
			long sendTime;
			String host = "localhost";
			int port = 5520;
			System.out.println("Contacting host: "+host+" at port "+ port);
			for (int i = 0;i<10;i++) {

				sendTime = new Date().getTime();
				String payload = "PING " + i+" "+sendTime;
				ping = new PingMessage(InetAddress.getByName(host),port,payload);
				sendPing(ping);
				if (i==8) sock.setSoTimeout(5000);
				if ((ping = receivePing()) != null ) {
					counter++;
					String[] arr = ping.getPayload().split(" ");
					rtts[i] = (int)(new Date().getTime() - sendTime);
					System.out.println("Received packet from "+ping.getIP().getHostAddress()+" "+port+" "+new Date().toString());
				} else {
					rtts[i] = 1000;
				}
				
			}
			/*if (counter < 10) {
				sock.setSoTimeout(5000);
				ping= receivePing(); 
			}*/
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getClass()+": " + e.getMessage());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int min =1000,max=0, average=0;
		for (int i = 0;i<10;i++) {
			String truth = rtts[i] == 1000 ? "false":"true";
			System.out.println("PING " + i+ ":"+truth+ " RTT: "+rtts[i]);
			if (rtts[i]<min) {
				min = rtts[i];
			}
			if (rtts[i] > max) {
				max = rtts[i];
			}
			average += rtts[i];
		}
		System.out.println("Minimum = " + min +"ms, Maximum = "+ max+"ms, Average = " + average/10+ "ms.");
		
		
		
	}
}
