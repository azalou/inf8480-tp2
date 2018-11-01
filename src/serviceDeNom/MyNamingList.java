package serviceDeNom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyNamingList implements Serializable {
	/**
	 * This object can be loaded by the repartitor
	 */
	private static final long serialVersionUID = -7838973735058822379L;
	public List<String> serverList = new ArrayList<String>();
	public List<String> repartitorList = new ArrayList<String>();
	public int nbrServer = this.serverList.size();
	public int nbrRepartitor = this.repartitorList.size();
	
	public boolean AddServerToList (String uniqueID, String IP) {
		if (!this.serverList.contains(uniqueID + ":" + IP.trim())) {
		System.out.println("added " + uniqueID + ":" + IP.trim() + " as a Server");
		return this.serverList.add(uniqueID + ":" + IP.trim());
		}
		return false;
	}
	public boolean addRepartitorIfAbsent (String login, String IP) {
		if (!this.repartitorList.contains(login.trim() + ":" + IP.trim())) {
			System.out.println("added " + login.trim() + ":" + IP.trim() + " as a Repartitor");
			return this.repartitorList.add(login.trim() + ":" + IP.trim());
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
