import java.util.ArrayList;
import java.util.List;

public class CreateTableCommand {
	
	private String nom; 
	private int nbreCol;
	private List<String> nomCol;
	private List<TypeColonne> typeCol;
	
	
	public CreateTableCommand(String[] mots) {
		List<String> nomColonnes = new ArrayList<>();
		List<TypeColonne> typeColonnes = new ArrayList<>();
		String ligne = mots[3].substring(1,mots[3].length()-1);
		String[] mots2 = ligne.split("[:,]");
		int nbrColonne;					
		for(int i = 0; i < mots2.length; i += 2 ) {
			nomColonnes.add(mots2[i]);						
			if( mots2[i+1].contains("STRING") || mots2[i+1].contains("VARSTRING") ) {
				System.out.println(mots2[i+1]);
				int startIndex = mots2[i+1].indexOf("(");
				int endIndex = mots2[i+1].indexOf(")");
				String subString = mots2[i+1].substring(startIndex + 1,endIndex);
				typeColonnes.add(new TypeColonne(mots2[i+1],Integer.parseInt(subString)));
			}else {
				typeColonnes.add(new TypeColonne(mots2[i+1]));
			
			}
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
