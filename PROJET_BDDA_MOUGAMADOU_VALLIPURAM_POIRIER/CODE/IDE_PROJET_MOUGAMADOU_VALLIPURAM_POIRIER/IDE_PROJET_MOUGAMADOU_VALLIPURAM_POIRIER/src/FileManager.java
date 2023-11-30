import java.nio.ByteBuffer;
public class FileManager {
	private static FileManager fm =null;
	
	private FileManager() {
		
	}
	
	public static FileManager getInstance() {
		if(fm == null) {
			fm = new FileManager();
		}
		return fm;
	}
	
	
	public PageId createNewHeaderPage() {
		PageId pageId = DiskManager.getInstance().AllocPage();
		ByteBuffer buf = BufferManager.getInstance().GetPage(pageId);
		// correspond aux pages factice
		
		buf.putInt(-1);
		buf.putInt(0);
		buf.putInt(-1);
		buf.putInt(0);
		BufferManager.getInstance().FreePage(pageId,1);
		return pageId;
	}
	
	
	public PageId addDataPage(TableInfo tabInfo) {
		PageId page = DiskManager.getInstance().AllocPage();// correspond a la datapage 
		ByteBuffer bufpage = BufferManager.getInstance().GetPage(page);
	    ByteBuffer buf = BufferManager.getInstance().GetPage(tabInfo.getHeaderPageId());
	    if((buf.getInt(0)==-1 )&&(buf.getInt(1)==0)) {// correspond au cas la liste est vide
	    	// ecrire dans la headerpage   le pageId de la page data 
	    	buf.putInt(0,page.getFileIdx());
	    	buf.putInt(1,page.getPageIdx());
	    	BufferManager.getInstance().FreePage(tabInfo.getHeaderPageId(), 1);
	    }else {
	    	int a =buf.getInt(0);
	    	int b =buf.getInt(1);
	    	bufpage.putInt(a);
	    	bufpage.putInt(b);
	    	BufferManager.getInstance().FreePage(page, 1);
		    //Metre à jour dans la headerpage  
		    buf.putInt(page.getFileIdx());
		    buf.putInt(page.getPageIdx());
		    BufferManager.getInstance().FreePage(tabInfo.getHeaderPageId(), 1);
	    	}
	    return page;
	    	
	    	
	    	
	    }
	    
	    
	public PageId getFreeDataPageId(TableInfo tabInfo,int  sizeRecord){
	ByteBuffer	 buf = BufferManager.getInstance().GetPage(tabInfo.getHeaderPageId());
	 int a = buf.getInt(0);
	 int b = buf.getInt(1);
	 if((a==-1)&&(b==0)){
		 //page pas existant dans la listelibre
		 return null;
	 }
	 // par courir tous la liste est vérifé l'espace Libre 
	  
		 
	}
	public Recordid writeRecordToDataPage(Record record ,PageId pageid) {
		
	}

}
