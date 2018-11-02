package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ServerInterface  extends Remote {

	int calculeThis(List<String> subList, String username, String password) throws RemoteException;

}
