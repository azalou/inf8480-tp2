package repartiteur;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;


public class RepartiteurServant {
	public ArrayList<String> operationList;
	public List<String> serverList = null;
	public int nbrServer;
	
	public void makeServerlist(List<String> remoteList) {
		this.serverList = remoteList;
		this.nbrServer = this.serverList.size();
	}
	
//	public RepartiteurServant(List<String> theList) {
//		this.serverList = theList;
//		this.nbrServer = theList.size();
//	}
	//public int nbrOperations = this.operationList.size();
	
//	void RepartitionTache(MyNamingList namingList) {
//		int nbrServer = namingList.nbrServer;
//		// On 		
//	}

}
