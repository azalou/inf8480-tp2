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
import shared.RepartitorInterface;

public class Client {

	public static void main(String[] args) {
		String distantHostname = null;
		String operationFile = null;
		int distantPort;

		if (args.length == 3) {
			distantHostname = args[0];
			distantPort = Integer.parseInt(args[1]);
			operationFile = args[2];
			try {
				File fic = new File(operationFile);
				byte[] operations = new byte[(int) fic.length()];
				Client client = new Client(distantHostname, distantPort);
				FileInputStream fis = new FileInputStream(operationFile);
				fis.read(operations);
				client.run(operations);
				fis.close();

			} catch (FileNotFoundException e) {
				System.err.println("File not Found");
			} catch (IOException e) {
				System.err.println("Couldn't read file " + e.getMessage());
			}
		} else {
			System.err.println("mandarory arguments: <RepartitorIP> <RepartitorPORT> <File NAME>");
		}

	}

	private RepartitorInterface repartClient;

	public Client(String distantHostname, Integer distantPort) {
		super();
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		repartClient = loadRepartitorStub(distantHostname, distantPort);
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

	private void run(byte[] operations) {
		try {
			int resultat = repartClient.distribute(operations);
			System.out.println("Resultat de calcul: " + resultat);
		} catch (RemoteException e) {
			System.err.println("Erreur: " + e.getMessage());
		}

	}

}
