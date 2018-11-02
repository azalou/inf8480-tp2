package ca.inf8480.tp2.shared;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class MyIDentifier {
	
	private static final String CHOOSE_CHAR_FROM_LIST = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	
	public String ipAddress = null;
	public String myUniqueID = null;
	
	public void getTheRightIP(String ipFirstOctet) {
		Enumeration<NetworkInterface> enuInterface;
		String ip = "000";
		InetAddress rightInet = null;
		try {
			enuInterface = NetworkInterface.getNetworkInterfaces();
			while (enuInterface.hasMoreElements()) {
				NetworkInterface net = (NetworkInterface) enuInterface.nextElement();
				Enumeration<InetAddress> ee = net.getInetAddresses();
				while (ee.hasMoreElements()) {
					rightInet = (InetAddress) ee.nextElement();
					ip = rightInet.getHostAddress().split("\\.")[0];
					if (ip.equals(ipFirstOctet)) {
						this.ipAddress = rightInet.getHostAddress();
					}
				}
			}
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
	}
	public void randomIdGen () {
		StringBuilder letsMakeID = new StringBuilder();
		for (int i=0; i < 12; i++) {
			int chosen = (int)(Math.random()*CHOOSE_CHAR_FROM_LIST.length());
			letsMakeID.append(CHOOSE_CHAR_FROM_LIST.charAt(chosen));
		}
		this.myUniqueID = letsMakeID.toString();
	}

}
