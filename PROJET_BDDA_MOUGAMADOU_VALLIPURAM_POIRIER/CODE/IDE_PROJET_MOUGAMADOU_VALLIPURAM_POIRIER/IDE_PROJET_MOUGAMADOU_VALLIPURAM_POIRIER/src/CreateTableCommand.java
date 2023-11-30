import java.util.ArrayList;
import java.util.List;

public class CreateTableCommand {
	
	private String nom; 
	private int nbreCol;
	private List<String> nomCol;
	private List<TypeColonne> typeCol;
	
	
	public CreateTableCommand(String nom, int nbreCol, List<String> nomCol, List<TypeColonne> typeCol ) {
		this.nom = nom;
		this.nbreCol = nbreCol;
		this.nomCol = nomCol;
		this.typeCol = typeCol;
	}
	
	private ArrayList<ColInfo> getColInfo(){
		ArrayList <ColInfo> colInfo = new ArrayList<>();
		for(int i = 0; i < nomCol.size(); i++) {
			colInfo.add(new ColInfo(nomCol.get(i), typeCol.get(i)));
		}
		return colInfo;
	}
	
	public void execute() {
		FileManager fm = FileManager.getInstance();
		DataBaseInfo dbi = DataBaseInfo.getInstance();
		
		PageId pageId = fm.createNewHeaderPage();
		TableInfo tableInfo = new TableInfo(nom, nbreCol, pageId);
		tableInfo.setColInfo(getColInfo());
		dbi.addTableInfo(tableInfo);
	}
	
	
}
