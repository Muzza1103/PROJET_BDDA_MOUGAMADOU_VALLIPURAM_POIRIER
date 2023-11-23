
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
		
	}
}
