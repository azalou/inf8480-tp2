package serveur;

import java.rmi.AccessException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import shared.MyIDentifier;
import shared.NamingServiceInterface;
import shared.RepartitorInterface;
import shared.ServerInterface;

public class CalcServer extends Thread implements ServerInterface {
	private RepartitorInterface repartServer;
	private NamingServiceInterface namingServer;
	private static MyIDentifier myID = new MyIDentifier();
	private Boolean serverUp = true;
	private String serverID = null;
	private int Capacite = 4;
	private static final int RMIREGISTRY_PORT = 5003;
	
	Thread authMe = new Thread() {
		public void run() {
			try {
				while (serverUp) {
					namingServer.makeServerAuth(myID.ipAddress,myID.myUniqueID);
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
			System.out.println("Server ready to accept Operations");
		}

	};

	public static void main(String[] args) {
		myID.getTheRightIP("192");
		myID.randomIdGen();
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
			server.execute();
		} else {
			System.err.println("mandarory arguments: <RepartitorIP> <RepartitorPORT> <NamingIP> <NamingPORT>");
		}
	}



	private void execute() {
		
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		try {
			ServerInterface serverStub = (ServerInterface) UnicastRemoteObject
					.exportObject(this, 0);
			//namingList.AddRepartitorToList(ip.ipAddress);
			Registry registry = LocateRegistry.getRegistry(myID.ipAddress , RMIREGISTRY_PORT);
			registry.rebind(myID.myUniqueID , serverStub);
			System.out.println("Repartitor ready.");
			System.out.println("Running on " + myID.ipAddress + ":" + RMIREGISTRY_PORT);
			
		} catch (ConnectException e) {
			System.err.println("Impossible de se connecter au registre de service de Nom.\n Est-ce que rmiregistry est lancé ?");
			System.err.println();
			System.err.println("Erreur: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Erreur: " + e.getMessage());
		}
		

		authMe.start();
		calculate.start();
	}



	public CalcServer(String repartitorIP, int repartitorPort, String NamingIP, int NamingPort) {
		super();
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		//repartServer = loadRepartitorStub(repartitorIP, repartitorPort);
		namingServer = loadNamingServiceStub(NamingIP, NamingPort);

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

//	private RepartitorInterface loadRepartitorStub(String distantRepartitor, int distantPort) {
//		RepartitorInterface stub = null;
//		try {
//			Registry registre = LocateRegistry.getRegistry(distantRepartitor, distantPort);
//			stub = (RepartitorInterface) registre.lookup("repartitor");
//		} catch (NotBoundException e) {
//			System.err.println("Le nom '" + e.getMessage() + "' n'est pas défini dans le registre.");
//		} catch (AccessException e) {
//			System.err.println("Erreur: " + e.getMessage());
//		} catch (RemoteException e) {
//			System.err.println("Erreur: " + e.getMessage());
//		}
//		return stub;
//	}



	@Override
	public int calculeThis(List<String> subList, String repartitorUsername, String repartitorPassword) throws RemoteException {
		int somme = 0;
		if (namingServer.verifyRepartitor(repartitorUsername, repartitorPassword)) {
			System.out.println("repartitor is legit");
			//finalement on y va pour les caluls
			
			for (String temp: subList) {
				String[] operationAndValue = temp.split(" ");
				int value = Integer.parseInt(operationAndValue[1]);
				String type = operationAndValue[0];
				Operations op = new Operations();
				if (type.equals("pell")) {
					somme += op.pell(value) % 4000;
					somme = somme %4000;
				}
				if (type.equals("prime")) {
					somme += op.prime(value) % 4000;
					somme = somme %4000;
				}	
			}	
		}
		return somme; 
	}
}
