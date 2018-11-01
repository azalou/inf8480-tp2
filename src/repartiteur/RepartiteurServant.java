package repartiteur;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import shared.NamingService;

public class RepartiteurServant {
	public ArrayList<String> operationList;
	
	void RepartitionTache(NamingService namingList) {
		
		long taille_de_liste = namingList.serverList.size();
		
	}

}
