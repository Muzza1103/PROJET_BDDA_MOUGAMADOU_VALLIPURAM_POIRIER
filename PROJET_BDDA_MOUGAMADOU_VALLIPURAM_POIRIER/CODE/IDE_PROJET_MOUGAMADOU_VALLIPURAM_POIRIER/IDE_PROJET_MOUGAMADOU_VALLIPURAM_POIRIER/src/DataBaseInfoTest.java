import java.util.ArrayList;

public class DataBaseInfoTest {
	public static void main(String[] args) {
		DBParams.DBPath = "/users/licence/in07091/PROJET_BDDA_MOUGAMADOU_VALLIPURAM_POIRIER/PROJET_BDDA_MOUGAMADOU_VALLIPURAM_POIRIER/DB/";
		testEcritureLecture();
		
	}
	
	public static void testEcritureLecture() {
		DataBaseInfo dbi = DataBaseInfo.getInstance();
		FileManager fm = FileManager.getInstance();
		TableInfo ti =  new TableInfo("Donald", 3, fm.createNewHeaderPage());
		
		ColInfo c1 = new ColInfo("Riri", "INT");
		ColInfo c2 = new ColInfo("Fifi", "FLOAT");
		ColInfo c3 = new ColInfo("Loulou","STRING(10)");
		
		
		ArrayList<ColInfo> liste = new ArrayList<>();
		
		liste.add(c1);
		liste.add(c2);
		liste.add(c3);
		
		ti.setColInfo(liste);
		dbi.addTableInfo(ti);
		dbi.Finish();
		dbi.affichage();
		dbi.flushAll();
		dbi.affichage();
		dbi.Init();
		dbi.affichage();
		
	}
	
	public static void test() {
		DataBaseInfo dbi = DataBaseInfo.getInstance();
		dbi.Init();
	}

}
