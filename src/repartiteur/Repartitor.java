package repartiteur;


import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Enumeration;

import shared.ServerInterface;

public class Repartitor implements ServerInterface {
	private static final int RMIREGISTRY_PORT = 5001;


	public static void main(String[] args) {
		Repartitor repartitor = new Repartitor();
		repartitor.run();

	}
	
	public Repartitor() {
		super();
	}
	
	private void run() {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		try {
			ServerInterface ClientStub = (ServerInterface) UnicastRemoteObject
					.exportObject(this, 0);

			String repartitor_ip_address = getTheRightIP("192");
			Registry registry = LocateRegistry.getRegistry(repartitor_ip_address , RMIREGISTRY_PORT);
			registry.rebind("repartitor", ClientStub);
			System.out.println("Repartitor ready.");
			System.out.println("Running on " + repartitor_ip_address + " & PORT = " + RMIREGISTRY_PORT);
		} catch (ConnectException e) {
			System.err.println("Impossible de se connecter au registre RMI. Est-ce que rmiregistry est lancé ?");
			System.err.println();
			System.err.println("Erreur: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Erreur: " + e.getMessage());
		}
	}
	
	
	private String getTheRightIP(String ipFirstOctet) {
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
						return rightInet.getHostAddress();
					}
				}
			}
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		return null;
	}



	//Fonctions RMI
	@Override
	public int distribute(byte[] operations) throws RemoteException {
		return 1234;
	}

}
