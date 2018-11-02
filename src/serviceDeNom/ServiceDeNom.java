package serviceDeNom;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Objects;

import shared.MyIDentifier;
import shared.NamingServiceInterface;


public class ServiceDeNom implements NamingServiceInterface {
	private static final int RMIREGISTRY_PORT = 5002;
	private static MyIDentifier ip = new MyIDentifier();
	private static final String REPART_LOGINS = "repartitors.txt";
	private MyNamingList namingList = new MyNamingList();

	public static void main(String[] args) {
		
		ServiceDeNom namingList = new ServiceDeNom();
		namingList.run();

	}
	
	protected ServiceDeNom() {
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
			e.printStackTrace();
		}
	}
	
	public boolean verifyRepartitor(String login, String password) throws RemoteException {

		HashMap<String, String> dataHashMap = new HashMap<>();
		try (BufferedReader br = new BufferedReader(new FileReader(REPART_LOGINS))) {
			String line;
			String[] userPass;
			while ((line = br.readLine()) != null) {
				userPass = line.split(":");
				dataHashMap.putIfAbsent(userPass[0].trim(), userPass[1].trim());
			}

			if (dataHashMap.containsKey(login)) {
				//System.out.println("added new repartitor to list");
				return (Objects.equals(dataHashMap.get(login), password));
			}

		} catch (IOException e) {
			System.err.println("Error occurred while reading user data file: " 
								+ e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	/*
	 * Fonctions RMI(non-Javadoc)
	 * @see shared.NamingServiceInterface
	 */
		@Override
		public boolean makeServerAuth(String ipAddr,String uniqueID) throws RemoteException {
			
				return namingList.AddServerToList(uniqueID,ipAddr);
		}
		
		@Override
		public MyNamingList getServerList() throws RemoteException {
			
			return namingList;
		}

		@Override
		public boolean makeRepartitorAuth(String ipAddr, 
				String username, String password)  throws RemoteException {
			
			if(verifyRepartitor(username, password)) {
				return namingList.addRepartitorIfAbsent(username,ipAddr);
			}else {
				System.out.println("Couldn't authenticate Repartitor " + username);
			}
			return false;	
		}

}
