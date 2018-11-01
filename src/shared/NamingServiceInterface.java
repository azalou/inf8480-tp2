package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import serviceDeNom.MyNamingList;

public interface NamingServiceInterface extends Remote {

	boolean makeAuth(String ipAddr, String type) throws RemoteException;
	MyNamingList getServerList() throws RemoteException;
	
	

}
