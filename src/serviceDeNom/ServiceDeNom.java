package serviceDeNom;
import java.rmi.ConnectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import shared.MyIPAddress;
import shared.NamingServiceInterface;


public class ServiceDeNom implements NamingServiceInterface {
	private static final int RMIREGISTRY_PORT = 5002;
	private static MyIPAddress ip = new MyIPAddress();
	private MyNamingList namingList = new MyNamingList();

	public static void main(String[] args) {
		
		ServiceDeNom namingList = new ServiceDeNom();
		namingList.run();

	}
	
	public ServiceDeNom() {
		super();
	}

	private void run() {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		try {
			NamingServiceInterface namingStub = (NamingServiceInterface) UnicastRemoteObject
					.exportObject(this, 0);
			ip.getTheRightIP("192");
			Registry registry = LocateRegistry.getRegistry(ip.ipAddress , RMIREGISTRY_PORT);
			registry.rebind("nameserver", namingStub);
			System.out.println("Naming Service ready.");
			System.out.println("Running on " + ip.ipAddress + ":" + RMIREGISTRY_PORT);
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//Fonctions RMI
		@Override
		public boolean makeAuth(String ipAddr, String type) throws RemoteException {
			
			if (type.equals("server")) {
				return namingList.AddServerToList(ipAddr);
			}else if (type.equals("repartitor")){
				return namingList.AddRepartitorToList(ipAddr);
			}
			return false;
			
		}

}
