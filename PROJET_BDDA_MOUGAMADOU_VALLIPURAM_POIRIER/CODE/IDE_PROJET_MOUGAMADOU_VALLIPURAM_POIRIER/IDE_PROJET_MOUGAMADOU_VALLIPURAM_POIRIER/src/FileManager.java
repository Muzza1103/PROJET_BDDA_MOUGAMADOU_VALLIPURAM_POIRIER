import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
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
	int tailleFichier =  buffparcour.capacity(); //4096
	//position de fin donner ecrit
	int espacelibre = buffparcour.capacity()-4;//4096-4
	int posDebEspaceLibre = buffparcour.getInt(espacelibre);
	
   //positon de pour nombre de record
	int nombreslodir = buffparcour.capacity()-8;//4096-8
	int nombreRecord = buffparcour.getInt(nombreslodir);
	//calcul pour la taille du tableau
	int tailleTableauslot = nombreRecord *2;
	//calcul Final pour l'espace libre
	int tailleEspacelibre = tailleFichier-(tailleTableauslot+8+posDebEspaceLibre);
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
		buff.position(0);
		int tailleFichier =  buff.capacity(); //4096
		//position de fin donner ecrit
		int espacelibre = buff.capacity()-4;//4096-4
		int posDebEspaceLibre = buff.getInt(espacelibre);
		//ecriture du buffer
	    record.WriteToBuffer(buff, posDebEspaceLibre);
	   //positon de pour nombre de record
		int nombreslodir = buff.capacity()-8;//4096-8
		int nombreRecord = buff.getInt(nombreslodir);
	    
	   //on fait nombreRecord *2 pour ecrire la taille et le positionnement du record et on on se positonne
		int posiRecordEcrireTaille = (nombreRecord * 2)-nombreslodir;
		int tailleRecord = record.readFromBuffer(buff, posDebEspaceLibre);
		posiRecordEcrireTaille = posiRecordEcrireTaille-tailleRecord;
		buff.position(posiRecordEcrireTaille-4);
		buff.putInt(posDebEspaceLibre);
		buff.position(posiRecordEcrireTaille);
		buff.putInt(tailleRecord);
		
		
		// metre a jour positionnement espacelibre et nombre de record
		
		nombreRecord = nombreRecord+1;
		buff.position(nombreslodir);
		buff.putInt(nombreRecord);
		
		
		int nouvellePositonEspaceLibre = posDebEspaceLibre+tailleRecord;
		buff.position(espacelibre);
		buff.putInt(nouvellePositonEspaceLibre);
		
		
	    if( posDebEspaceLibre ==buff.capacity() ) {// signifie la page est pleine donc on rajoute dans la liste de fichier p
	    	ByteBuffer buff2 = BufferManager.getInstance().GetPage(record.getTabInfoRecord().getHeaderPageId());
	    	 if((buff2.getInt(8)==-1 )&&(buff2.getInt(12)==0)) {// correspond au cas la liste plein qu i ' est vide
	 	    	// ecrire dans la headerpage   le pageId de la page 
	 	    	buff2.putInt(8,pageid.getFileIdx());
	 	    	buff2.putInt(12,pageid.getPageIdx());
	 	    	if(buff2.getInt(0)==pageid.getFileIdx()&&buff2.getInt(4)==pageid.getPageIdx()) {
	 	    		buff.position(0);
	 	    		buff2.position(0);
	 	    		buff.putInt(buff2.getInt(0));
	 	    		buff.putInt(buff2.getInt(4));
	 	    	}
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
	    RecordId id = new RecordId(pageid,posDebEspaceLibre);
	    return id;
	}
	
	/***
	 * public ArrayList<Record> getRecordsInDataPage(TableInfo tabInfo,PageId pageId) {
	 
		// trouver la page parmis dans les deux les liste chaine
		ByteBuffer buff = BufferManager.getInstance().GetPage(tabInfo.getHeaderPageId());
		if(pageId.equals(tabInfo.getHeaderPageId())){
			System.out.println("La liste de records sera vide car la page coorrspond à la headerpage");
			return null;
		}
		buff.position(0);
		if(buff.get(0)==pageId.getFileIdx() && buff.get(4)==pageId.getPageIdx()) {
			//PageId dans la 
		}
		**/
	//}

	}
	
