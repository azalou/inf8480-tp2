package serviceDeNom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyNamingList implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7838973735058822379L;
	public List<String> serverList = new ArrayList<String>();
	public List<String> repartitorList = new ArrayList<String>();
	public int nbrServer = this.serverList.size();
	public int nbrRepartitor = this.repartitorList.size();
	
	public boolean AddServerToList (String IP) {
		if (!this.serverList.contains(IP.trim())) {
		System.out.println("added " + IP + " as a Server");
		return this.serverList.add(IP.trim());
		}
		return false;
	}
	public boolean AddRepartitorToList (String IP) {
		if (!this.repartitorList.contains(IP.trim())) {
			System.out.println("added " + IP + " as a Repartitor");
			return this.repartitorList.add(IP.trim());
		}
		return false;
	}
	
	public boolean RemoveServerFromList (String IP) {
		if (this.serverList.contains(IP.trim())) {
			return this.serverList.remove(IP.trim());
		}
		return false;
	}
}
