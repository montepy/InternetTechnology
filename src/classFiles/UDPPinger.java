package classFiles;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.net.*;

public class UDPPinger {
	DatagramSocket sock;
	public void sendPing(PingMessage ping) {
		DatagramPacket pack = new DatagramPacket(ping.getPayload().getBytes(),ping.getPayload().getBytes().length,ping.getIP(),ping.getPort());
		try {
			sock = new DatagramSocket();
			sock.send(pack);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public PingMessage receivePing() {
		byte[] buffer = new byte[512];
		DatagramPacket pack = new DatagramPacket(buffer,512);
		try {
			sock.setSoTimeout(1000);
			sock.receive(pack);
		}
		catch (SocketException e) {
			System.out.println(e.getStackTrace()[0]);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getClass()+": "+ e.getMessage());
			return null;
		}
		PingMessage out = new PingMessage(pack.getAddress(),pack.getPort(),new String(pack.getData()));
		
		return out;
		
	}
	
}
