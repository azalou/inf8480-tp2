package shared;
import java.util.ArrayList;
import java.util.List;
public class NamingService {
	public List<String> serverList = new ArrayList<String>();
	public List<String> repartitorList = new ArrayList<String>();
	public int nbrServer = this.serverList.size();
	public int nbrRepartitor = this.repartitorList.size();
	
	public void AddServerToList (String IP) {
		this.serverList.add(IP);
	}
	public void AddRepartitorToList (String IP) {
		this.repartitorList.add(IP);
	}

}
