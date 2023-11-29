import java.util.List;

public class CreateTableCommand {
	
	private String nom;
	private int nbreCol;
	private List<String> nomCol;
	private List<String> typeCol;
	
	
	public CreateTableCommand(String nom, int nbreCol, List<String> nomCol, List<String> typeCol ) {
		this.nom = nom;
		this.nbreCol = nbreCol;
		this.nomCol = nomCol;
		this.typeCol = typeCol;
	}
	
	
	public void execute() {
		
	}
	
	
}
