package repartiteur;


import java.rmi.AccessException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;

import shared.MyIPAddress;
import shared.NamingServiceInterface;
import shared.RepartitorInterface;



public class Repartitor extends Thread implements RepartitorInterface {
	private static final int RMIREGISTRY_PORT = 5001;
	private static MyIPAddress ip = new MyIPAddress();
	//private MyNamingList namingList = new MyNamingList();
	private RepartiteurServant DistributedOP = new RepartiteurServant();
	private static Boolean repartitorUp = false;
	Thread authMe = new Thread() {
		public void run() {
			try {
				while (repartitorUp) {
					namingService.makeAuth(ip.ipAddress, "repartitor");
					Thread.sleep(4000);
				}
			} catch (RemoteException e) {
				System.err.println("Erreur d'enregistrement du Répartiteur " + e.getMessage());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	};

	public static void main(String[] args) {
		//Repartitor repartitor = new Repartitor();
		String namingServiceHostname = null;
		int namingServicePort;
		if (args.length == 2) {
			namingServiceHostname = args[0];
			namingServicePort = Integer.parseInt(args[1]);
			ip.getTheRightIP("192");
			repartitorUp = true;
			Repartitor repartitor = new Repartitor(namingServiceHostname, namingServicePort);
			repartitor.execute();
		
		} else {
			System.err.println("mandarory arguments: <NameServerIP> <NameServerPORT>");
		}
	}
	
	public Repartitor() {
		super();
	}
	
	private NamingServiceInterface namingService;
	public Repartitor(String namingServiceHostname, int distantPort) {
		super();
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		namingService = loadNamingStub(namingServiceHostname, distantPort);
		authMe.start();
	}

	private NamingServiceInterface loadNamingStub(String namingServiceHostname, int distantPort) {
		NamingServiceInterface stub = null;
		try {
			Registry registre = LocateRegistry.getRegistry(namingServiceHostname, distantPort);
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

	private void execute() {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		try {
			RepartitorInterface ClientStub = (RepartitorInterface) UnicastRemoteObject
					.exportObject(this, 0);
			//namingList.AddRepartitorToList(ip.ipAddress);
			Registry registry = LocateRegistry.getRegistry(ip.ipAddress , RMIREGISTRY_PORT);
			registry.rebind("repartitor", ClientStub);
			System.out.println("Repartitor ready.");
			System.out.println("Running on " + ip.ipAddress + ":" + RMIREGISTRY_PORT);
			
		} catch (ConnectException e) {
			System.err.println("Impossible de se connecter au registre de service de Nom.\n Est-ce que rmiregistry est lancé ?");
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
		//DistributedOP.RepartitionTache(namingList);
		return 1234;
	}
//	public List<String> getServerList() throws RemoteException {
//		return namingList.serverList;
//	}
	
//	public List<String> getRepartitorList() throws RemoteException {
//		return namingList.repartitorList;
//	}
//	
//	public void addServerToList(String ServerIP) throws RemoteException {
//		namingList.AddServerToList(ServerIP);
//	}
//
//	@Override
//	public boolean serverAuth(String serverIP) throws RemoteException {
//		Boolean isServerAdded = namingList.AddServerToList(serverIP);
//		return isServerAdded;
//		
//	}


}
