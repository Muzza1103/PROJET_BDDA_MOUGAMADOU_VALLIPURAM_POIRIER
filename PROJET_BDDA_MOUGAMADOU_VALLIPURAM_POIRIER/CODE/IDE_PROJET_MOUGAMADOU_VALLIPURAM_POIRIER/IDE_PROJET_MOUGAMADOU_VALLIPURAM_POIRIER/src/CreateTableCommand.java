import java.util.ArrayList;
import java.util.List;

public class CreateTableCommand {
	
	private String nom; 
	private int nbreCol;
	private List<String> nomCol;
	private List<String> typeCol;
	
	
	public CreateTableCommand(String[] mots) {
		List<String> nomColonnes = new ArrayList<>();
		List<String> typeColonnes = new ArrayList<>();
		String ligne = mots[3].substring(1,mots[3].length()-1);
		String[] mots2 = ligne.split("[:,]");
		int nbrColonne;					
		for(int i = 0; i < mots2.length; i += 2 ) {
			nomColonnes.add(mots2[i]);						
			typeColonnes.add(mots2[i+1]);
		}
		nbrColonne = nomColonnes.size();

		this.nom = mots[2];
		this.nbreCol = nbrColonne;
		this.nomCol = nomColonnes;
		this.typeCol = typeColonnes;
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
