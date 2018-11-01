package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RepartitorInterface extends Remote {
	int distribute(byte[] operations) throws RemoteException;
	//public List<String> getRepartitorList() throws RemoteException;
	//boolean serverAuth(String serverIP) throws RemoteException;
}
