
public class TestDataBaseManager {
	public static void main(String[] args) {
		DBParams.DBPath = "/users/licence/in07091/PROJET_BDDA_MOUGAMADOU_VALLIPURAM_POIRIER/PROJET_BDDA_MOUGAMADOU_VALLIPURAM_POIRIER/DB/";
		DBParams.SGBDPageSize = 4096;
        DBParams.DMFileCount = 4;
        DBParams.frameCount = 2;
        DatabaseManager dbm = DatabaseManager.getInstance();
        dbm.Init();
		testCreate();
		testInsert();
		//testReset();
		dbm.Finish();
	}
	
	public static void testCreate() {
		DatabaseManager dbm = DatabaseManager.getInstance();
		DataBaseInfo dbi = DataBaseInfo.getInstance();
		dbm.ProcessCommand("CREATE TABLE Profs (Nom:STRING(10),Matière:STRING(10))");
		dbi.affichage();
	}
	public static void testReset() {
		DatabaseManager dbm = DatabaseManager.getInstance(); 
		dbm.ProcessCommand("RESETDB");
	}
	public static void testInsert() {
		DatabaseManager dbm = DatabaseManager.getInstance(); 
		dbm.ProcessCommand("INSERT INTO Profs VALUES (Ileana,BDDA)");
	}
	
	

}
