package repartiteur;

import java.util.List;

public class LocalServerList {
	public List<String> serverList = null;
	public int nbrServer;
	
	public LocalServerList(List<String> theList) {
		this.serverList = theList;
		this.nbrServer = theList.size();
	}
	
	
}
