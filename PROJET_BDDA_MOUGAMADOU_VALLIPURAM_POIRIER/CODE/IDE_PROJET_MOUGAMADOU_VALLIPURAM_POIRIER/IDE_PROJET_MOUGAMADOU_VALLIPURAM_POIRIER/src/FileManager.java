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
	    //System.out.println(bufpage.limit());
	    ByteBuffer buf = BufferManager.getInstance().GetPage(tabInfo.getHeaderPageId());
	    //System.out.println(buf.getInt(0));
	    //System.out.println(buf.getInt(4));
	    if((buf.getInt(0)==-1 )&&(buf.getInt(4)==0)) {// correspond au cas la liste est vide
	    	// ecrire dans la headerpage   le pageId de la page data 
	    	buf.position(0);
	    	buf.putInt(page.getFileIdx());
	    	buf.putInt(page.getPageIdx());
	    	bufpage.position(0);
	    	bufpage.putInt(-1);
	    	bufpage.putInt(0);
	    	int espacelibre = bufpage.capacity()-4;//4096-4
	    	bufpage.position(espacelibre);
	    	bufpage.putInt(8);
	    	int nombredeslotRecord =  bufpage.capacity()-8;//4096-8
	    	bufpage.position(nombredeslotRecord);
	    	bufpage.putInt(0);
	    	//System.out.println(bufpage.position());
	    	BufferManager.getInstance().FreePage(page, 1);
	    	BufferManager.getInstance().FreePage(tabInfo.getHeaderPageId(), 1);
	    }else {
	  
	    
	    	int a =buf.getInt(0);
	    	int b =buf.getInt(4);
	    	bufpage.position(0);
	    	bufpage.putInt(a);
	    	bufpage.putInt(b);
	    	int espacelibre = bufpage.capacity()-4;//4096-4
	    	bufpage.position(espacelibre);
	    	bufpage.putInt(8);
	    	int nombredeslotRecord =  bufpage.capacity()-8;
	    	bufpage.position(nombredeslotRecord);//4096-8
	    	bufpage.putInt(0);
	    	BufferManager.getInstance().FreePage(page, 1);
		    //Metre à jour dans la headerpage
	    	buf.position(0);
		    buf.putInt(page.getFileIdx());
		    buf.putInt(page.getPageIdx());
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
	 ByteBuffer buffparcour = BufferManager.getInstance().GetPage(page);
	 // calcul espace libre
	buffparcour.position(0);
	int espacelibre = buffparcour.capacity()-4;//4096-4
	buffparcour.position(espacelibre);
	int posDebEspaceLibre = buffparcour.getInt(); 
	System.out.println(posDebEspaceLibre+"posDeb");
	int nombreslodir = buffparcour.capacity()-8;
	int nombreRecord = buffparcour.getInt(nombreslodir);
	System.out.println(nombreRecord+"nbr recor");
	int tailleTableauslot = nombreRecord *2;
	int tailleEspacelibre = posDebEspaceLibre-tailleTableauslot;
	System.out.println(tailleEspacelibre+"espace libre taille");
	BufferManager.getInstance().FreePage(page, 1);
	if(tailleEspacelibre >=sizeRecord) {
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
	    buff.putInt((int)DBParams.SGBDPageSize-4,m);
	    if( buff.capacity() == (int)DBParams.SGBDPageSize) {
	    	ByteBuffer buff2 = BufferManager.getInstance().GetPage(record.getTabInfoRecord().getHeaderPageId());
	    	 if((buff2.getInt(8)==-1 )&&(buff2.getInt(12)==0)) {// correspond au cas la liste est vide
	 	    	// ecrire dans la headerpage   le pageId de la page 
	 	    	buff2.putInt(8,pageid.getFileIdx());
	 	
	 	    	buff2.putInt(12,pageid.getPageIdx());
	 	    	BufferManager.getInstance().FreePage(record.getTabInfoRecord().getHeaderPageId(), 1);
	 	    }else {
	 	    	int a =buff2.getInt(8);
	 	    	int b =buff2.getInt(12);
	 	    	buff.putInt(8,a);
	 	    	buff.putInt(12,b);
	 	    	BufferManager.getInstance().FreePage(pageid, 1);
	 		    //Metre à jour dans la headerpage  
	 		    buff2.putInt(8,pageid.getFileIdx());
	 		    buff2.putInt(12,pageid.getPageIdx());
	 		    BufferManager.getInstance().FreePage(record.getTabInfoRecord().getHeaderPageId(), 1);
	    	}
	    }
	    BufferManager.getInstance().FreePage(pageid, 1);
	    RecordId id = new RecordId(pageid,pos);
	    return id;
	}

	}
