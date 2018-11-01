package repartiteur;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import shared.MyIPAddress;
import shared.NamingService;
import shared.ServerInterface;



public class Repartitor implements ServerInterface {
	private static final int RMIREGISTRY_PORT = 5001;
	private static MyIPAddress ip = new MyIPAddress();
	private NamingService namingList = new NamingService();
	private RepartiteurServant DistributedOP = new RepartiteurServant(); 


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

			ip.getTheRightIP("192");
			String repartitor_ip_address = ip.ipAddress;
			namingList.AddRepartitorToList(repartitor_ip_address);
			Registry registry = LocateRegistry.getRegistry(repartitor_ip_address , RMIREGISTRY_PORT);
			registry.rebind("repartitor", ClientStub);
			System.out.println("Repartitor ready.");
			System.out.println("Running on " + repartitor_ip_address + ":" + RMIREGISTRY_PORT);
			
		} catch (ConnectException e) {
			System.err.println("Impossible de se connecter au registre RMI. Est-ce que rmiregistry est lancé ?");
			System.err.println();
			System.err.println("Erreur: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Erreur: " + e.getMessage());
		}
	}



	//Fonctions RMI
	@Override
	public int distribute(byte[] operations) throws RemoteException {
		String operationsStr = new String(operations);
		DistributedOP.operationList = new ArrayList<String>(Arrays.asList(operationsStr.split("\n")));
		DistributedOP.RepartitionTache(namingList);
		return 1234;
	}
//	public List<String> getServerList() throws RemoteException {
//		return namingList.serverList;
//	}
	
	public List<String> getRepartitorList() throws RemoteException {
		return namingList.repartitorList;
	}
	
	public void addServerToList(String ServerIP) throws RemoteException {
		namingList.AddServerToList(ServerIP);
	}

	@Override
	public void serverAuth() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

}
