package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
	int distribute(byte[] operations) throws RemoteException;

}
