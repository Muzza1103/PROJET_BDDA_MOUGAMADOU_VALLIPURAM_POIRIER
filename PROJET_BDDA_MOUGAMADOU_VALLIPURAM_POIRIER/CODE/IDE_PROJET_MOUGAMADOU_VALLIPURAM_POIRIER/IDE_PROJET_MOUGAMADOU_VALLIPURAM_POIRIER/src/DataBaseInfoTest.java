import java.util.ArrayList;

public class DataBaseInfoTest {
	public static void main(String[] args) {
		DBParams.DBPath = "/users/licence/in07091/PROJET_BDDA_MOUGAMADOU_VALLIPURAM_POIRIER/PROJET_BDDA_MOUGAMADOU_VALLIPURAM_POIRIER/DB/";
		testEcritureLecture();
		
	}
	
	public static void testEcritureLecture() {
		DataBaseInfo dbi = DataBaseInfo.getInstance();
		TableInfo ti =  new TableInfo("Donald", 3);
		
		TypeColonne t1 = new TypeColonne("INT");
		TypeColonne t2 = new TypeColonne("FLOAT");
		TypeColonne t3 = new TypeColonne("STRING", 10);
		//TypeColonne t4 = new TypeColonne("VARSTRING", 10);
		
		ColInfo c1 = new ColInfo("Riri", t1);
		ColInfo c2 = new ColInfo("Fifi", t2);
		ColInfo c3 = new ColInfo("Loulou", t3);
		
		
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
