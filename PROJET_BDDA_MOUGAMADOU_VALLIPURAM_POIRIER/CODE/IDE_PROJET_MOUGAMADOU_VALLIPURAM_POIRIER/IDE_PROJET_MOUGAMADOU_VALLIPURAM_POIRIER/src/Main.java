import java.util.Scanner;

public class Main {
	public static void main(String [] args) {
	DBParams.DBPath = args[0];
	DBParams.SGBDPageSize = 4096;
        DBParams.DMFileCount = 4;
        DBParams.frameCount = 2;
        
        DiskManager dk = DiskManager.getInstance();
        DatabaseManager dbm = DatabaseManager.getInstance();
        
        dbm.Init();
        
        try (Scanner sc = new Scanner(System.in)) {
        	boolean inter=true;
        	while(inter) {
        		System.out.println ("Que voulez vous faire ?");
        		String s = sc.nextLine();
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
