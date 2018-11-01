package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NamingServiceInterface extends Remote {

	boolean makeAuth(String ipAddr, String type) throws RemoteException;
	
	

}
