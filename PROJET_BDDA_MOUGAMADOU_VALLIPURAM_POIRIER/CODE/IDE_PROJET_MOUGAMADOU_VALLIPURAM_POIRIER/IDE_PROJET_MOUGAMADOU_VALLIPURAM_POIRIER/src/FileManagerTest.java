import java.util.ArrayList;
import java.util.List;

public class FileManagerTest {


  public static PageId TestcreatHeaderPage() {

   FileManager  fm = FileManager.getInstance();
   return fm.createNewHeaderPage();
   }
  
  public  static PageId TestaddDataPage( TableInfo tabInfo) {
      return FileManager.getInstance().addDataPage(tabInfo);	  
  }
  public static  PageId TestgetFreeDataPageId( TableInfo tabInfo,int sizerecord) {
    return FileManager.getInstance().getFreeDataPageId(tabInfo, sizerecord);
    
  }	

  public  static RecordId TestwriteRecordToDataPage(Record record ,PageId pageid) {
	  return FileManager.getInstance().writeRecordToDataPage(record, pageid);
  }

  public  static List<Record> TestgetRecordsInDataPage(TableInfo tabInfo,PageId pageId) {
	  return FileManager.getInstance().getRecordsInDataPage(tabInfo, pageId);
  }
  
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	DBParams.DBPath = "C:\\Users\\thibh\\OneDrive\\Bureau\\Projet_BDDA__MOUGAMADOU_VALLIPURAM_POIRIER\\DB\\";
	DBParams.SGBDPageSize = 4096;
        DBParams.DMFileCount = 4;
        DBParams.frameCount = 2;
        BufferManager bm = BufferManager.getInstance();
        DiskManager dk = DiskManager.getInstance();
        //System.out.println(page.toString());  
        ColInfo Colonne0 = new ColInfo("Nom", "VARSTRING(23)");
        ColInfo Colonne1 = new ColInfo("Age", "INT");
        ColInfo Colonne2 = new ColInfo("Taille", "FLOAT");
        ColInfo Colonne3 = new ColInfo("Genre", "STRING(20)");

        ArrayList<ColInfo> ListeColonnes = new ArrayList<>();
        ListeColonnes.add(Colonne0);
        ListeColonnes.add(Colonne1);
        ListeColonnes.add(Colonne2);
        ListeColonnes.add(Colonne3);
        PageId pageheader = TestcreatHeaderPage();
        TableInfo tab = new TableInfo("nom-age",4,pageheader);
	    tab.setColInfo(ListeColonnes);
        PageId pageData = TestaddDataPage(tab);
        PageId pageDatab = TestaddDataPage(tab);
        System.out.println(pageData.toString());
        System.out.println(pageDatab.toString());
        Record Record0 = new Record(tab);
        
        ArrayList<Object> recValues = new ArrayList<>();
        String nom_0 = "MonPrenom";
        int age_0 = 20;
        float taille_0 = 1.76f;
        String genre_0 = "voila_20_characteres";

        
        recValues.add(nom_0);
        recValues.add(age_0);
        recValues.add(taille_0);
        recValues.add(genre_0);
        //System.out.println(recValues);

        Record0.InsertValues(recValues);

	  RecordId ri = TestwriteRecordToDataPage(Record0,pageData);
	  PageId pagefree = TestgetFreeDataPageId(tab,37);
	  List<Record> ar =  TestgetRecordsInDataPage(tab,pageData);
	  for(int i = 0;i<ar.size();i++) {
		  System.out.println(ar.get(i).getRecValues());
	  }
       
        	
	    

}
}
