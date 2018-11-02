package ca.inf8480.tp2.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RepartitorInterface extends Remote {
	int distribute(byte[] operations) throws RemoteException;
	//public List<String> getRepartitorList() throws RemoteException;
	//boolean serverAuth(String serverIP) throws RemoteException;
}
