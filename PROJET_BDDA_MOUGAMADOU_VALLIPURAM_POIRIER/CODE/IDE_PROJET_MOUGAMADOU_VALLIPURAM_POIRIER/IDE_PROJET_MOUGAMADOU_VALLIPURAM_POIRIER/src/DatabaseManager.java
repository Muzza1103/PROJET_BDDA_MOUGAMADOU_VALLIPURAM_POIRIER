
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
				if(mots[1].equals("TABLE")) {
					
					
					
					
					CreateTableCommand ctc = new CreateTableCommand( ,  ,  ,);
					ctc.execute();
				}
				break;
			
			case "RESETDB": // Voir si vraiment besoin de créer une classe ou non 
				
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
