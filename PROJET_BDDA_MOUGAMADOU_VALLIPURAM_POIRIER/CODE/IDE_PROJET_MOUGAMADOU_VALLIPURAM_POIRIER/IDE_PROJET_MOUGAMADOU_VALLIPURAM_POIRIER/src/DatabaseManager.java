import java.io.File;
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
	
	public void ProcessCommand(String commande) {

		String[] mots = commande.split(" ");
		switch (mots[0]){

			case "CREATE":
					List<String> nomColonne = new ArrayList<>();
					List<TypeColonne> typeColonne = new ArrayList<>();
					String ligne = mots[3].substring(1,mots[3].length()-1);
					String[] mots2 = ligne.split("[:,]");;
					int nbrColonne;
					for(int i = 0; i < mots2.length; i+=2 ) {
						nomColonne.add(mots2[i]);
						//System.out.println(mots2[i]);							
						if(mots2[i+1].contains("STRING")||mots2[i+1].contains("VARSTRING")) {
							System.out.println(mots2[i+1]);
							int startIndex = mots2[i+1].indexOf("(");
							int endIndex = mots2[i+1].indexOf(")");
							//System.out.println(startIndex);
							//System.out.println(endIndex);
							String subString = mots2[i+1].substring(startIndex + 1,endIndex);
							typeColonne.add(new TypeColonne(mots2[i+1],Integer.parseInt(subString)));
						}else {
							//System.out.println(mots2[i+1]);
							typeColonne.add(new TypeColonne(mots2[i+1]));
					
						}
					nbrColonne = nomColonne.size();
					CreateTableCommand ctc = new CreateTableCommand(mots[2], nbrColonne, nomColonne, typeColonne);
					ctc.execute(); 
				}
				break;
			
			case "RESETDB":
				for(int i=0;i<5;i++) {
					String chemin = DBParams.DBPath+"F"+i+".bin"; 
					File fichierASupprimer = new File(chemin);
					if(fichierASupprimer.exists()) {
						fichierASupprimer.delete();
					}
				}
				break;
			
			case "INSERT":

				break;
				
			case "SELECT":
				
				break;
				
			default:
				System.out.println("Cette commande n'existe pas !");
				break;
		}
	}
}
