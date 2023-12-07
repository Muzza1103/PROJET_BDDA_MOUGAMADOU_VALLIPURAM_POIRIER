
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
  
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DBParams.DBPath = "C:\\Users\\thibh\\OneDrive\\Bureau\\Projet_BDDA__MOUGAMADOU_VALLIPURAM_POIRIER\\DB\\";
		DBParams.SGBDPageSize = 4096;
        DBParams.DMFileCount = 4;
        DBParams.frameCount = 2;
        BufferManager bm = BufferManager.getInstance();
        DiskManager dk = DiskManager.getInstance();
        PageId page = dk.AllocPage();
        //System.out.println(page.toString());  
        String nom = "Ti";
        int colonne = 2;
        TableInfo tab = new TableInfo(nom,colonne,page);
        PageId pageheader = TestcreatHeaderPage();
        PageId pageData = TestaddDataPage(tab);
		Record rec = new Record(tab);
		RecordId ri = TestwriteRecordToDataPage(rec,pageData);
		PageId pagefree = TestgetFreeDataPageId(tab,colonne);
       
        	
	    

}
}
