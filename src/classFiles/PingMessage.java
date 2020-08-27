package classFiles;

import java.net.InetAddress;
import java.util.Date;

public class PingMessage {
	String out;
	InetAddress addr;
	int port;
	String payload;
	public PingMessage(InetAddress addr,int port,String payload) {
		out = addr.getHostAddress()+port+payload;
		this.addr = addr;
		this.port = port;
		this.payload = payload;//check for correctness
	}
	
	public InetAddress getIP() {
		return addr;
	}
	public int getPort() {
		return port;
	}
	
	public String getPayload() {
		return payload;
	}
}
