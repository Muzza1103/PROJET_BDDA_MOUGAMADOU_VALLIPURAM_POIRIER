import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
	    System.out.println(bufpage.limit());
	    ByteBuffer buf = BufferManager.getInstance().GetPage(tabInfo.getHeaderPageId());
	    System.out.println(buf.getInt(0));
	    System.out.println(buf.getInt(4));
	    if((buf.getInt(0)==-1 )&&(buf.getInt(4)==0)) {// correspond au cas la liste est vide
	    	// ecrire dans la headerpage   le pageId de la page data 
	    	buf.rewind();
	    	buf.putInt(0,page.getFileIdx());
	    	buf.putInt(4,page.getPageIdx());
	    	bufpage.putInt(-1);
	    	bufpage.putInt(0);
	    	//System.out.println(bufpage.position());
	    	BufferManager.getInstance().FreePage(page, 1);
	    	BufferManager.getInstance().FreePage(tabInfo.getHeaderPageId(), 1);
	    }else {
	  
	    
	    	int a =buf.getInt(0);
	    	int b =buf.getInt(4);
	    	System.out.println(a);
	    	bufpage.position(0);
	    	bufpage.putInt(a);
	    	bufpage.putInt(b);
	    	BufferManager.getInstance().FreePage(page, 1);
		    //Metre à jour dans la headerpage
		    buf.putInt(0,page.getFileIdx());
		    buf.putInt(4,page.getPageIdx());
		    BufferManager.getInstance().FreePage(tabInfo.getHeaderPageId(), 1);
	    
	    
	    	}
	    return page; 
	    	
	    	
	    	
	    }
	    
	    
	public PageId getFreeDataPageId(TableInfo tabInfo,int  sizeRecord){
	 ByteBuffer	 buf = BufferManager.getInstance().GetPage(tabInfo.getHeaderPageId());
	 int a = buf.getInt(0);
	 int b = buf.getInt(4);
	 if((a==-1)&&(b==0)){
		 //page pas existant dans la listelibre
		 return null;
	 }
	 

	 // par courir tous la liste et vérifé l'espace Libre 
	 PageId page = new PageId (a,b);
	 System.out.println(page.toString());
	 ByteBuffer buffparcour = BufferManager.getInstance().GetPage(page);
	 System.out.println(buffparcour.capacity());
	 
	 // calcul espace libre
	buffparcour.position(0);
	int capacite = (int)DBParams.SGBDPageSize;
	//System.out.println(capacite);
	System.out.println(buffparcour.capacity());
	buffparcour.position(capacite-4);
	int posDebEspaceLibre =  buffparcour.getInt();
	
	System.out.println(posDebEspaceLibre);
	//position debut espace disponible 
	int espacelibretableau = capacite-posDebEspaceLibre;
	System.out.println(espacelibretableau);
	int nbslotdir = buffparcour.getInt((int)DBParams.SGBDPageSize -8);
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
	    int m = buff.getInt((int)DBParams.SGBDPageSize-4);
	    m++;
	    buff.allocate(4);
	    buff.putInt((int)DBParams.SGBDPageSize-4,m);
	    if( buff.capacity() == (int)DBParams.SGBDPageSize) {
	    	ByteBuffer buff2 = BufferManager.getInstance().GetPage(record.getTabInfoRecord().getHeaderPageId());
	    	 if((buff2.getInt(8)==-1 )&&(buff2.getInt(12)==0)) {// correspond au cas la liste est vide
	 	    	// ecrire dans la headerpage   le pageId de la page 
	    		buff2.allocate(4);
	 	    	buff2.putInt(8,pageid.getFileIdx());
	 	    	buff2.allocate(4);
	 	    	buff2.putInt(12,pageid.getPageIdx());
	 	    	BufferManager.getInstance().FreePage(record.getTabInfoRecord().getHeaderPageId(), 1);
	 	    }else {
	 	    	int a =buff2.getInt(8);
	 	    	int b =buff2.getInt(12);
	 	    	buff.putInt(8,a);
	 	    	buff.putInt(12,b);
	 	    	BufferManager.getInstance().FreePage(pageid, 1);
	 		    //Metre à jour dans la headerpage  
	 	    	buff2.allocate(4);
	 		    buff2.putInt(8,pageid.getFileIdx());
	 		    buff2.allocate(4);
	 		    buff2.putInt(12,pageid.getPageIdx());
	 		    BufferManager.getInstance().FreePage(record.getTabInfoRecord().getHeaderPageId(), 1);
	    	}
	    }
	    BufferManager.getInstance().FreePage(pageid, 1);
	    RecordId id = new RecordId(pageid,pos);
	    return id;
	}

	}
