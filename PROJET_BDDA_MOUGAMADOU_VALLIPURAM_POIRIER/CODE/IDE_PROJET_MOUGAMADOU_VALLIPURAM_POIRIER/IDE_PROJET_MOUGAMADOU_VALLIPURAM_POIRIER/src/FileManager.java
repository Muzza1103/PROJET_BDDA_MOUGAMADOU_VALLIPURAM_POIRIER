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
	    	bufpage.putInt(0,a);
	    	bufpage.putInt(1,b);
	    	BufferManager.getInstance().FreePage(page, 1);
		    //Metre à jour dans la headerpage  
		    buf.putInt(0,page.getFileIdx());
		    buf.putInt(1,page.getPageIdx());
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
	 PageId page = new PageId (a,b);
	 ByteBuffer buffparcour = BufferManager.getInstance().GetPage(page);
	 // calcul espace libre
	int posDebEspaceLibre =  buffparcour.getInt((int)DBParams.SGBDPageSize);
	//position debut espace disponible 
	int espacelibretableau = (int)DBParams.SGBDPageSize-posDebEspaceLibre;
	int nbslotdir = buffparcour.getInt((int)DBParams.SGBDPageSize -1);
	int tailletableauslot = nbslotdir *2;
	int tailleespacelibre = espacelibretableau-tailletableauslot;
	BufferManager.getInstance().FreePage(page, 1);
	if(tailleespacelibre >=sizeRecord) {
		return page;
	}else {
		return null;
	}
	 
	
	
	 
	 
	}
	public RecordId writeRecordToDataPage(Record record ,PageId pageid) {
		ByteBuffer buff = BufferManager.getInstance().GetPage(pageid);
        int pos  = buff.getInt((int)DBParams.SGBDPageSize);
	    record.WriteToBuffer(buff, pos);
	    if( buff.capacity() == (int)DBParams.SGBDPageSize) {
	    	ByteBuffer buff2 = BufferManager.getInstance().GetPage(record.getTabInfoRecord().getHeaderPageId());
	    	 if((buff2.getInt(2)==-1 )&&(buff2.getInt(3)==0)) {// correspond au cas la liste est vide
	 	    	// ecrire dans la headerpage   le pageId de la page 
	 	    	buff2.putInt(2,pageid.getFileIdx());
	 	    	buff2.putInt(3,pageid.getPageIdx());
	 	    	BufferManager.getInstance().FreePage(record.getTabInfoRecord().getHeaderPageId(), 1);
	 	    }else {
	 	    	int a =buff2.getInt(2);
	 	    	int b =buff2.getInt(3);
	 	    	buff.putInt(2,a);
	 	    	buff.putInt(3,b);
	 	    	BufferManager.getInstance().FreePage(pageid, 1);
	 		    //Metre à jour dans la headerpage  
	 		    buff2.putInt(pageid.getFileIdx());
	 		    buff2.putInt(pageid.getPageIdx());
	 		    BufferManager.getInstance().FreePage(record.getTabInfoRecord().getHeaderPageId(), 1);
	    	}
	    }
	    BufferManager.getInstance().FreePage(pageid, 1);
	    RecordId id = new RecordId(pageid,pos);
	    return id;
	}

}
