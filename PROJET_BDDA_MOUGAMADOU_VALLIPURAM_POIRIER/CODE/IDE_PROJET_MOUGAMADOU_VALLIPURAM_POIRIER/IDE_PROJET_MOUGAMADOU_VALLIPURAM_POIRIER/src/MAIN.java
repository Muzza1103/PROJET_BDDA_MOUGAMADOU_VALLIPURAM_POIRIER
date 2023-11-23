import java.util.Scanner;

public class Main {
	public static void main(String [] args) {
		DBParams.DBPath = "/users/licence/in07091/PROJET_BDDA_MOUGAMADOU_VALLIPURAM_POIRIER/PROJET_BDDA_MOUGAMADOU_VALLIPURAM_POIRIER/DB/";
		DBParams.SGBDPageSize = 4096;
        DBParams.DMFileCount = 4;
        
        DiskManager dk = DiskManager.getInstance();
        BufferManager bm = BufferManager.getInstance();
        DataBaseInfo dbi = DataBaseInfo.getInstance();
        DatabaseManager dbm = DatabaseManager.getInstance();
        
        dbm.Init();
        
        try (Scanner sc = new Scanner(System.in)) {
        	System.out.println("EXIT");
        	System.out.println("AUTRE");
        	String s = sc.next();
        	if(s.equals("EXIT")) {
        		dbm.Finish();
        	}else {
        		dbm.ProcessCommand(s);
        	}
        	
		}
        
		System.out.println("Coucou Chef Picsou!");
		
	}
}
