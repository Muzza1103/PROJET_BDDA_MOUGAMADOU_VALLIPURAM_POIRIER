import java.util.List;

public class SelectCommand {
	
	private String nomDuChamp;
	private String nomDuTableau;
	private List<Conditions> listeDesConditions;
	
	public SelectCommand(String nomDuChamp, String nomDuTableau, List<Conditions> listeDesConditions) {
		this.nomDuChamp = nomDuChamp;
		this.nomDuTableau = nomDuTableau;
		this.listeDesConditions = listeDesConditions;
		
	}
	
}
