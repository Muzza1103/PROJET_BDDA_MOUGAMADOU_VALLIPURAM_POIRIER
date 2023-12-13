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
		//bm.GetPage(null);
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
		
		DataBaseInfo dbi = DataBaseInfo.getInstance();
		switch (mots[0]){

			case "CREATE":
				if (mots[1].equals("TABLE")) {
					CreateTableCommand ctc = new CreateTableCommand(mots);
					ctc.execute();
				}else {
					System.out.println("La commande rentrée comporte une erreur de syntaxe !");
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
				if (mots[1].equals("TABLE") && mots[3].equals("VALUES")) {
					InsertCommand ic = new InsertCommand(mots);
					ic.execute();
				}else {
					System.out.println("La commande rentrée comporte une erreur de syntaxe !");
				}
				break;
				
			case "SELECT":
				
				break;
			case "IMPORT":
				if(mots[1].equals("INTO")) {
					ImportCommand ic = new ImportCommand(mots);
					i
				}
				break;
				
			default:
				System.out.println("Cette commande n'existe pas !");
				break;
		}
	}
}
