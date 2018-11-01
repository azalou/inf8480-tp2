package serveur;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import shared.MyIPAddress;
import shared.ServerInterface;

public class CalcServer extends Thread {
	private ServerInterface repartServer;
	private static MyIPAddress ip = new MyIPAddress();
	Thread authMe = new Thread() {
		
		public void run() {
			try {
				repartServer.serverAuth();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	};
	
Thread calculate = new Thread() {
		
		public void run() {
			try {
				repartServer.serverAuth();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	};
	
	public static void main(String[] args) {
		String distantHostname = null;
		byte[] operationList = null;
		ip.getTheRightIP("192");
		String server_ip = ip.ipAddress;
		int distantPort;
		
		if (args.length == 2) {
			distantHostname = args[0];
			distantPort = Integer.parseInt(args[1]);
			CalcServer server = new CalcServer(distantHostname, distantPort);
		} else {
			System.err.println("mandarory arguments: <IP> <PORT>");
		}
	}

	

	public CalcServer(String distantHostname, int distantPort) {
		super();
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		repartServer = loadServerStub(distantHostname, distantPort);// TODO Auto-generated constructor stub
	}

	private ServerInterface loadServerStub(String distantRepartitor, int distantPort) {
		ServerInterface stub = null;
		try {
			Registry registre = LocateRegistry.getRegistry(distantRepartitor, distantPort);
			stub = (ServerInterface) registre.lookup("repartitor");
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
