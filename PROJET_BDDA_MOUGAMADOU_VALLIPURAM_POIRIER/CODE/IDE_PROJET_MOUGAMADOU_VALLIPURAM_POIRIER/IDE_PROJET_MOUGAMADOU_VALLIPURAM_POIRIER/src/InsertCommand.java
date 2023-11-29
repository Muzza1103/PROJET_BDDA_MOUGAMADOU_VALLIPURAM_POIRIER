import java.util.List;

public class InsertCommand {
	
	private String nomRelation;
	//private int nbrValeur;
	private List<Object> valeurs;
	
	public InsertCommand(String nomRelation, List<Object> valeurs ) {
		this.nomRelation = nomRelation;
		this.valeurs = valeurs;
	}
	
	public void execute() {
		
	}
		
	
}
