import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

	private static DatabaseManager dbm = null;
	
	private DatabaseManager() {
		
	}
	
	public static DatabaseManager getInstance() {
		if (dbm== null) {
			dbm = new DatabaseManager();
		}
		return dbm;
	}
	
	public void Init() {//ca va pas marcher car getPage != Init
		DataBaseInfo dbi = DataBaseInfo.getInstance();
		BufferManager bm = BufferManager.getInstance();
		
		dbi.Init();
		bm.GetPage(null);
	}
	
	public void Finish() {
		DataBaseInfo dbi = DataBaseInfo.getInstance();
		BufferManager bm = BufferManager.getInstance();
		
		dbi.Finish();
		bm.FlushAll();
	}
	
	public void remiseZero() {
		DataBaseInfo dbi = DataBaseInfo.getInstance();
		BufferManager bm = BufferManager.getInstance();
		dbi.flushAll();
		bm.FlushAll();
		
	}
	public void ProcessCommand(String commande) {

		String[] mots = commande.split(" ");
		
		List<String> nomColonnes = new ArrayList<>();
		List<TypeColonne> typeColonnes = new ArrayList<>();
		DataBaseInfo dbi = DataBaseInfo.getInstance();
		switch (mots[0]){

			case "CREATE":
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
					nbrColonne = nomColonnes.size();
					CreateTableCommand ctc = new CreateTableCommand(mots[2], nbrColonne, nomColonnes, typeColonnes);
					ctc.execute(); 
				}
				break;
			
			case "RESETDB":
				for(int i = 0; i < 5; i++) {
					String chemin = DBParams.DBPath + "F" + i + ".bin"; 
					File fichierASupprimer = new File(chemin);
					if(fichierASupprimer.exists()) {
						fichierASupprimer.delete();
					}
				}
				remiseZero();
				break;
			
			case "INSERT":
				Record record = new Record(null);
				List<String> motColonnes = new ArrayList<>();
				
				/*
				 * Ajout du nom de la relation dans la relation
				 */
				String nomRelation = mots[2];
				int numTable = -1;
				String ligneInsert = mots[4].substring(1, mots[4].length()-1);
				String[] mots3 = ligneInsert.split("(,)");
				for(TableInfo ti : dbi.getList()) {
					if(ti.getNom().equals(nomRelation)) {
						numTable = dbi.getList().indexOf(ti);
					}
				}
				if (numTable != -1) {
					Record rec = new Record(dbi.getList().get(numTable));
					ArrayList<Object> recvalues = new ArrayList<>();
					for(int i = 0; i < mots3.length; i++) {
						typ //Changer pour vérifier le type
						recvalues.add(mots3[i]);
					}
					int nbrColloneValide = 0;
					for (int i=0;i<dbi.getList().get(numTable).getNbColonnes();i++) {
						if()
					}
				}
				
				
				ArrayList<Object> recvalues = new ArrayList<>();
				for (int i=0;i<)
				
				for(int i = 0; i < mots3.length; i++) {
					motColonnes.add(mots3[i]);
				}
				
				
				
				
				break;
				
			case "SELECT":
				
				break;
				
			default:
				System.out.println("Cette commande n'existe pas !");
				break;
		}
	}
}
