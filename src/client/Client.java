package client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import shared.ServerInterface;


public class Client {
	
	
	public static void main(String[] args) {
		String distantHostname = null;
		String operationFile = null;
		int distantPort;

		if (args.length > 2) {
			distantHostname = args[0];
			distantPort = Integer.parseInt(args[1]);
			operationFile = args[2];
			try {
				
				byte[] operations;
				File fic = new File(operationFile);
				operations = new byte[(int) fic.length()];
				Client client = new Client(distantHostname, distantPort);
				FileInputStream fis = new FileInputStream(fic);
				fis.read(operations, 0, operations.length);
				client.run(operations);
				fis.close();
				
				
			} catch (FileNotFoundException e) {
				System.err.println("File not Found");
			} catch (IOException e) {
				System.err.println("Couldn't read file " + e.getMessage());
			}
		} else {
			System.err.println("mandarory arguments: <IP> <PORT> <File NAME>");
		}

		
	}
	
	private ServerInterface repartiteur;
	
	public Client(String distantHostname, Integer distantPort) {
		super();
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		repartiteur = loadServerStub(distantHostname, distantPort);
		
	}

	private ServerInterface loadServerStub(String distantRepartitor, int distantPort) {
		ServerInterface stub = null;
		
		try {
			Registry registre = LocateRegistry.getRegistry(distantRepartitor , distantPort);
			stub = (ServerInterface) registre.lookup("repartitor");
		} catch (NotBoundException e) {
			System.err.println("Le nom '" + e.getMessage() + 
					"' n'est pas défini dans le registre.");
		} catch (AccessException e) {
			System.err.println("Erreur: " + e.getMessage());
		} catch (RemoteException e) {
			System.err.println("Erreur: " + e.getMessage());
		}
		
		return stub;
	}

	private void run(byte[] operations) {
		try {
			int resultat = repartiteur.distribute(operations);
			System.out.println(resultat);
		} catch (RemoteException e) {
			System.err.println("Erreur: " + e.getMessage());
		}
		
	}
	
	

}
