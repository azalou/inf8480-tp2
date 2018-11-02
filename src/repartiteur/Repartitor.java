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

import shared.MyIDentifier;
import shared.NamingServiceInterface;
import shared.RepartitorInterface;

public class Repartitor extends Thread implements RepartitorInterface {
	private static final int RMIREGISTRY_PORT = 5001;
	private String username = "repartitor1";
	private String password = "repartitor1";
	private int resultat = 0; 
	private static MyIDentifier ip = new MyIDentifier();
	//private MyNamingList namingList = new MyNamingList();
	private RepartitorServant DistributedOP = new RepartitorServant();
	private static Boolean repartitorUp = false;
	Thread authMe = new Thread() {
		public void run() {
			try {
				while (repartitorUp) {
					namingService.makeRepartitorAuth(ip.ipAddress, username, password);
					Thread.sleep(4000);
				}
			} catch (RemoteException e) {
				System.err.println("Erreur d'enregistrement du Répartiteur " + e.getMessage() + "\n le service de nom est il joignable?" );
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	};
	
	Thread sendOp = new Thread() {
		public void run() {
			System.out.println("distributing the list of OP");
			try {
				DistributedOP.makeServerlist(namingService.getServerList().serverList);
				resultat=DistributedOP.sendOpToServer();
				
			} catch (RemoteException e) {
				e.printStackTrace();
				System.err.println("Erreur: " + e.getMessage());
				
			} catch (NotBoundException e) {
				e.printStackTrace();
				System.err.println("Le nom '" + e.getMessage() + "' n'est pas défini dans le registre.");
			}
		}
	};

	public static void main(String[] args) {
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
			RepartitorInterface repartitorStub = (RepartitorInterface) UnicastRemoteObject
					.exportObject(this, 0);
			//namingList.AddRepartitorToList(ip.ipAddress);
			Registry registry = LocateRegistry.getRegistry(ip.ipAddress , RMIREGISTRY_PORT);
			registry.rebind("repartitor", repartitorStub);
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
		DistributedOP.nbrOperations = DistributedOP.operationList.size();
		sendOp.start();
		
		try {
			sendOp.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return resultat;
	}


}
