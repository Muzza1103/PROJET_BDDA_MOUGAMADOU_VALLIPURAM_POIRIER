import java.util.Scanner;

public class Main {
	public static void main(String [] args) {
		DBParams.DBPath = "/users/licence/in07091/PROJET_BDDA_MOUGAMADOU_VALLIPURAM_POIRIER/PROJET_BDDA_MOUGAMADOU_VALLIPURAM_POIRIER/DB/";
		DBParams.SGBDPageSize = 4096;
        DBParams.DMFileCount = 4;
        
        DiskManager dk = DiskManager.getInstance();
        DatabaseManager dbm = DatabaseManager.getInstance();
        
        dbm.Init();
        
        try (Scanner sc = new Scanner(System.in)) {
        	boolean inter=true;
        	while(inter) {
        		System.out.println ("Que voulez vous faire ?");
        		String s = sc.next();
        		if(s.equals("EXIT")) {
        			dbm.Finish();
        			inter=false;
        		}else {
        			dbm.ProcessCommand(s);
        		}
        	}
		}
        
		
	}
}
