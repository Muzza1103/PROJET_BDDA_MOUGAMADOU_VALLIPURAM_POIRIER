import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class InsertCommand {
	
	private Record rec;
	
	public InsertCommand(String [] mots) {
		List<String> motColonnes = new ArrayList<>();
		DataBaseInfo dbi = DataBaseInfo.getInstance();
		String nomRelation = mots[2];
		int numTable = -1;
		String ligneInsert = mots[4].substring(1, mots[4].length()-1);
		String[] mots3 = ligneInsert.split(",");
		for(TableInfo ti : dbi.getList()) {
			if(ti.getNom().equals(nomRelation)) {
				numTable = dbi.getList().indexOf(ti);
			}
		}
		Record rec = new Record(null);
		try {
			if (numTable != -1) {
				rec = new Record(dbi.getList().get(numTable));
				ArrayList<Object> recvalues = new ArrayList<>();
				ArrayList<String> types = rec.extraireTypes(dbi.getList().get(numTable));
				if (dbi.getList().get(numTable).getNbColonnes() == mots3.length) {
					for(int i = 0; i < mots3.length; i++) {
						if (types.get(i)=="VARCHAR"||types.get(i)=="STRING") {
							if (getType(mots3[i]).equals("STRING") && mots3[i].length()< dbi.getList().get(numTable).getColInfo(i).getSizeString()) {
								recvalues.add(mots3[i]);
							}
						}else if (types.get(i)=="INT") {
							if (getType(mots3[i]).equals(types.get(i))) {
								recvalues.add(Integer.parseInt(mots3[i]));
							}
						}else if (types.get(i)=="FLOAT"){
							if (getType(mots3[i]).equals(types.get(i))) {
								recvalues.add(Float.parseFloat(mots3[i]));
							}
						}
					}
					if(recvalues.size() == dbi.getList().get(numTable).getNbColonnes()) {
						rec.InsertValues(recvalues);
					}else {
						throw new Exception("Les types des valeurs entrées ne correspondent pas à celle de la table !");// Nbr d'élements correcte mais les types ne correspondent pas
					}
				}else {
					throw new Exception("Le nombre d'éléments rentré ne correspond pas au nombre d'élément de la table !");// Nbr d'éléments différent du nombre de colonne
				}
			}else {
				throw new Exception("La table que vous avez entrée n'existe pas !"); // La tableInfo n'existe pas
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}finally {
			this.rec = rec;
		}
	}
	
	public String getType(String mot) {
		if (isInteger(mot)) {
			return "INT";
		}
		if (isFloat(mot)) {
			return "FLOAT";
		}
		return "STRING";
		
	}
	
	private static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isFloat(String s) {
        try {
            Float.parseFloat(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
	
	public void execute() {
		if(rec.getTabInfoRecord()!=null){
			int taille = 0;
			for (int i=0;i<rec.getRecValues().size();i++) {
				taille+=(byte)rec.getRecValues().get(i);
			}
			PageId pageId = fm.getFreeDataPageId(rec.getTabInfoRecord(), taille);
			fm.writeRecordToDataPage(rec, pageId);
			//Compléter
		}
	}
		
	
}
