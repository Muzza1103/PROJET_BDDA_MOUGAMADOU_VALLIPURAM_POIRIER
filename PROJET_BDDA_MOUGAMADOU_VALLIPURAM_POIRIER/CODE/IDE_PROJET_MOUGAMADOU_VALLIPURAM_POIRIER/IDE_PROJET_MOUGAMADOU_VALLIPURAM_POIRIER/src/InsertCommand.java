import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
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
		try {
			if (numTable != -1) {
				rec = new Record(dbi.getList().get(numTable));
				ArrayList<Object> recvalues = new ArrayList<>();
				ArrayList<String> types = rec.extraireTypes(dbi.getList().get(numTable));
				for(int i=0;i<types.size();i++) {
					System.out.println(types.get(i));
				}
				System.out.println("0.1");
				if (dbi.getList().get(numTable).getNbColonnes() == mots3.length) {
					System.out.println("0.2");
					for(int i = 0; i < mots3.length; i++) {
						if (types.get(i).contains("STRING")) {
							if (getType(mots3[i]).contains("STRING") && mots3[i].length()< dbi.getList().get(numTable).getColInfo(i).getSizeString()) {
								System.out.println("1");
								recvalues.add(mots3[i]);
								System.out.println("2");
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
					for(int i=0; i < recvalues.size();i++) {
						System.out.println(recvalues.get(i).toString());
					}
					if(recvalues.size() == dbi.getList().get(numTable).getNbColonnes()) {
						rec.InsertValues(recvalues);
						this.rec = rec;
					
					}else {
						throw new Exception("Les types des valeurs entrées ne correspondent pas à celle de la table !");// Nbr d'élements correcte mais les types ne correspondent pas
					}
				}else {
					throw new Exception("Le nombre d'éléments rentré ne correspond pas au nombre d'élément de la table !");// Nbr d'éléments différent du nombre de colonne
				}
			}else {
				Record rec = new Record(null);
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
			FileManager fm = FileManager.getInstance();
			int taille = 0; //Modifier pour modifier la taille dans le cas ou il ya un VARSTRING et possiblement faire *2 pour la taille des char et des string
			for (int i=0;i<rec.extraireTypes(rec.getTabInfoRecord()).size();i++) {
				if (rec.getTabInfoRecord().getColInfo(i).GetTypCol().equals("INT")||rec.getTabInfoRecord().getColInfo(i).GetTypCol().equals("FLOAT")){
					taille +=4;
				}else{
					taille += rec.getTabInfoRecord().getColInfo(i).getSizeString();
				}
			}
			PageId pageId = fm.getFreeDataPageId(rec.getTabInfoRecord(), taille);
			if (pageId == null) {
				fm.addDataPage(rec.getTabInfoRecord());
				pageId = fm.getFreeDataPageId(rec.getTabInfoRecord(), taille);
			}
			/*
			for(int i=0; i < rec.getRecValues().size();i++) {
				System.out.println(rec.getRecValues().get(i).toString());
			}*/
			ByteBuffer buff = BufferManager.getInstance().GetPage(pageId);
			printBuffer(buff);
			BufferManager.getInstance().FreePage(pageId, 1);
			fm.writeRecordToDataPage(rec, pageId);
			ByteBuffer buff2 = BufferManager.getInstance().GetPage(pageId);
			printBuffer(buff2);
			BufferManager.getInstance().FreePage(pageId, 1);
			//InsertRecordIntoTable(rec);
		}
	}
	
	public static void printBuffer(ByteBuffer buffer) {
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        System.out.println((String) Arrays.toString(bytes));
    }
		
	
}
