package shared;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class MyIPAddress {
	public String ipAddress = null;
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

}
