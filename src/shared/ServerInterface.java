package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ServerInterface extends Remote {
	int distribute(byte[] operations) throws RemoteException;
	void serverAuth() throws RemoteException;
	public List<String> getRepartitorList() throws RemoteException;
}
