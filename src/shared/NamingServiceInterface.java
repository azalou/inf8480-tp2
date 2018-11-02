package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

import serviceDeNom.MyNamingList;

public interface NamingServiceInterface extends Remote {

	boolean makeServerAuth(String ipAddr, String myUniqueID) throws RemoteException;
	MyNamingList getServerList() throws RemoteException;
	boolean makeRepartitorAuth(String ipAddress, String username, String password) throws RemoteException;
	boolean verifyRepartitor(String login, String password) throws RemoteException;
	
	

}
