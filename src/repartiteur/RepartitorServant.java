package repartiteur;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import shared.ServerInterface;


public class RepartitorServant implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5292671218095986112L;
	private List<String> serverList = null;
	private List<ServerInterface> serverStubList = new ArrayList<ServerInterface>();
	private String username = "repartitor1";
	private String password = "repartitor1";
	
	public ArrayList<String> operationList;
	public int nbrServer;
	public int nbrOperations;
	public HashMap<String, String> serverHashMap;
	
	public void makeServerlist(List<String> remoteList) {
		this.serverHashMap = new HashMap<>();
		this.serverList = remoteList;
		this.nbrServer = this.serverList.size();
		String[] IDIP;
		for (String temp : this.serverList) {
			IDIP = temp.split(":");
			serverHashMap.putIfAbsent(IDIP[0].trim(), IDIP[1].trim());
		}
	}
	
	public int sendOpToServer() throws RemoteException, NotBoundException {
		//Connection RMI à l'ensemble de serveur et tentatives d'Envoie des opérations
		int somme = 0; 
		for (String key: this.serverHashMap.keySet()) {
			Registry registre = LocateRegistry.getRegistry(this.serverHashMap.get(key), 5003);
			this.serverStubList.add((ServerInterface) registre.lookup(key));
		}
		
		//Repartition des operations et envoie aux serveurs divisions de tache égale
		int nbrOpEnv = Math.floorDiv(this.nbrOperations, this.nbrServer);
		this.serverStubList.get(0);
		
		for (int iter=0; iter< this.nbrServer; iter++) {
			int startlist = Math.multiplyExact(iter, nbrOpEnv);
			int endlist = Math.multiplyExact(iter+1, nbrOpEnv);
			ServerInterface stub = this.serverStubList.get(iter);
			List<String> subListofOp = new ArrayList<String>(this.operationList.subList(startlist, endlist));
			somme+= stub.calculeThis(subListofOp,username, password);
		}
		
		return somme;
		
	}
	

}
