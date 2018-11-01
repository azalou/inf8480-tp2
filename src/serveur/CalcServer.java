package serveur;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import shared.MyIPAddress;
import shared.NamingServiceInterface;
import shared.RepartitorInterface;

public class CalcServer extends Thread {
	private RepartitorInterface repartServer;
	private NamingServiceInterface namingServer;
	private static MyIPAddress ip = new MyIPAddress();
	private Boolean serverUp = true;
	private int Capacite;
	Thread authMe = new Thread() {
		public void run() {
			try {
				while (serverUp) {
					namingServer.makeAuth(ip.ipAddress, "server");
					Thread.sleep(4000);
				}
			} catch (RemoteException e) {
				System.err.println("Erreur d'enregistrement du serveur " + e.getMessage());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	};

	Thread calculate = new Thread() {
		public void run() {

		}

	};

	public static void main(String[] args) {
		ip.getTheRightIP("192");
		String repartitorIP = null;
		byte[] operationList = null;
		int repartitorPort;
		String namingIP = null;
		int namingPort;
		if (args.length == 4) {
			repartitorIP = args[0];
			namingIP = args[2];
			repartitorPort = Integer.parseInt(args[1]);
			namingPort = Integer.parseInt(args[3]);
			CalcServer server = new CalcServer(repartitorIP, repartitorPort, namingIP, namingPort);
		} else {
			System.err.println("mandarory arguments: <RepartitorIP> <RepartitorPORT> <NamingIP> <NamingPORT>");
		}
	}



	public CalcServer(String repartitorIP, int repartitorPort, String NamingIP, int NamingPort) {
		super();
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		repartServer = loadRepartitorStub(repartitorIP, repartitorPort);
		namingServer = loadNamingServiceStub(NamingIP, NamingPort);
		authMe.start();

	}

	private NamingServiceInterface loadNamingServiceStub(String distantHostname, int distantPort) {
		NamingServiceInterface stub = null;
		try {
			Registry registre = LocateRegistry.getRegistry(distantHostname, distantPort);
			stub = (NamingServiceInterface) registre.lookup("nameserver");
		} catch (NotBoundException e) {
			System.err.println("Le nom '" + e.getMessage() + "' n'est pas défini dans le registre.");
		} catch (AccessException e) {
			System.err.println("Erreur: " + e.getMessage());
		} catch (RemoteException e) {
			System.err.println("Erreur: " + e.getMessage());
		}
		return stub;
	}

	private RepartitorInterface loadRepartitorStub(String distantRepartitor, int distantPort) {
		RepartitorInterface stub = null;
		try {
			Registry registre = LocateRegistry.getRegistry(distantRepartitor, distantPort);
			stub = (RepartitorInterface) registre.lookup("repartitor");
		} catch (NotBoundException e) {
			System.err.println("Le nom '" + e.getMessage() + "' n'est pas défini dans le registre.");
		} catch (AccessException e) {
			System.err.println("Erreur: " + e.getMessage());
		} catch (RemoteException e) {
			System.err.println("Erreur: " + e.getMessage());
		}
		return stub;
	}
}
