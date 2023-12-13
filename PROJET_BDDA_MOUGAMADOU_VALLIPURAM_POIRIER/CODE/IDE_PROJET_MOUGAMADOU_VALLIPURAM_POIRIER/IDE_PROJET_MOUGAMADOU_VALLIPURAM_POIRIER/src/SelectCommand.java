import java.util.ArrayList;
import java.util.List;

public class SelectCommand {
	
	private TableInfo ti;
	private String[] nomColonne;
	private List<String> listeDesConditions;
	
	public SelectCommand(String [] mots) {
		DataBaseInfo dbi = DataBaseInfo.getInstance();
		this.listeDesConditions = new ArrayList<>();
		this.nomColonne = mots[1].split(",");
		int numTable = -1;
		for(TableInfo ti : dbi.getList()) {
			if(ti.getNom().equals(mots[3])) {
				numTable = dbi.getList().indexOf(ti);
			}
		}
		try {
			if(numTable != -1) {
				this.ti = dbi.getList().get(numTable);
				int colValide=0;
				for (int i=0;i<nomColonne.length;i++) {
					for (ColInfo ci: dbi.getList().get(numTable).getColInfoList()) {
						if (ci.GetNomCol().equals(nomColonne[i])) {
							colValide++;
						}
					}
				}
				if (colValide == nomColonne.length || nomColonne[0].equals("*")) {
					if(mots.length>=6) {
						for(int i=5; i<mots.length;i+=2) {
							listeDesConditions.add(mots[i]);
						}
					}
				}else {
					nomColonne = null;
					throw new Exception("Les colonnes demandées ne sont pas présentes dans cette relation !");
				}
			}else {
				this.ti=null;
				throw new Exception("La table que vous avez entrée n'existe pas !"); // La tableInfo n'existe pas
			}
		}catch(IndexOutOfBoundsException e) {
			System.out.println("Vous n'avez pas rentré de colonne à renvoyer !");
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		
	}
	
	public void execute() {
		if (ti!=null || nomColonne!=null) {
			if(nomColonne[0].equals("*")) {
				
			}else {
				
			}
		}
	}
	
}
