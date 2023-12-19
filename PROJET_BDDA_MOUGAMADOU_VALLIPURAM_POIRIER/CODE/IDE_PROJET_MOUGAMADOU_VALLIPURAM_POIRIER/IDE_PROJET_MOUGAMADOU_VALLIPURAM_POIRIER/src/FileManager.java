import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
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
		//System.out.println(posDebEspaceLibre+"espacelibrepos");
		//ecriture du buffer
		int tailleRecord = record.WriteToBuffer(buff, posDebEspaceLibre);
	   //positon de pour nombre de record
		int nombreslodir = buff.capacity()-8;//4096-8
		int nombreRecord = buff.getInt(nombreslodir);
		
	    
	   //on fait nombreRecord *2 pour ecrire la taille et le positionnement du record et on on se positonne
		int posiRecordEcrireTaille = nombreslodir-(nombreRecord * 2*4);
		posiRecordEcrireTaille = posiRecordEcrireTaille-4;
		int posiRecordEcrirepos = posiRecordEcrireTaille-8;

	
		buff.position(posiRecordEcrirepos);
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
	
	
	  public List<Record> getRecordsInDataPage(TableInfo tabInfo,PageId pageId) {
	 
		// trouver la page parmis dans les deux les liste chaine
		ByteBuffer buff = BufferManager.getInstance().GetPage(tabInfo.getHeaderPageId());
		if(pageId.equals(tabInfo.getHeaderPageId())){
			System.out.println("La liste de records sera vide car la page coorrspond à la headerpage");
			return null;
		}
		List<Record> listRec = new ArrayList<>(); 
		buff.position(0);
		if(buff.get(0)==pageId.getFileIdx() && buff.get(4)==pageId.getPageIdx()) {
			//PageId est égale à la pageId
			//on rentre dans le buffer de la page 
			ByteBuffer bufPage = BufferManager.getInstance().GetPage(pageId);
			// recuper les records 
			bufPage.position(0);
			int nombreslodir = bufPage.capacity()-8;//4096-8
			int nombreRecord = bufPage.getInt(nombreslodir);
			if(nombreRecord ==0) {
				// Pas de record dans la page 
				return null;
			}
			for(int i = 0 ;i<nombreRecord;i++) {
				int intilisationPositionRecord = nombreslodir -(8*(i+1));
				int positonRecord =bufPage.getInt(intilisationPositionRecord);
				Record rec = new Record(tabInfo);
				int taille = rec.readFromBuffer(bufPage, positonRecord);
		        System.out.println(taille+" : taille record");		
				listRec.add(rec);
			}    
			BufferManager.getInstance().FreePage(pageId, 1);
		}else {
				
             // cas n'est pas dans la header page de liste restant de la place des recorrds 			
			
	
			int a = buff.getInt(0);
			int b = buff.getInt(4);
			PageId pageData = new PageId(a,b);
			ByteBuffer buff2 = BufferManager.getInstance().GetPage(pageData);
			while(buff2.getInt(0)==-1 &&buff2.getInt(4)==0) {
				PageId pagedat = new PageId(buff2.getInt(0),buff2.getInt(4));
				pagedat.toString();
				System.out.println("pageData "+buff2.getInt(0)+"a:"+buff2.getInt(4)+"b : ");
				buff2 = BufferManager.getInstance().GetPage(pagedat);
				// recuper les records 
				buff2.position(0);
				int nombreslodir = buff2.capacity()-8;//4096-8
				int nombreRecord = buff2.getInt(nombreslodir);
				if(nombreRecord ==0) {
					// Pas de record dans la page 
					return null;
				}
				for(int i = 0 ;i<nombreRecord;i++) {
					int intilisationPositionRecord = nombreslodir -(8*(i+1));
					int positonRecord =buff2.getInt(intilisationPositionRecord);
					Record rec = new Record(tabInfo);
					int taille = rec.readFromBuffer(buff2, positonRecord);
			        System.out.println(taille+" : taille record");		
					listRec.add(rec);
				}    
				BufferManager.getInstance().FreePage(pagedat, 1);
			}
			BufferManager.getInstance().FreePage(pageData, 1);
			//recuper la pageI suivante 
			if(buff.get(8)==pageId.getFileIdx() && buff.get(12)==pageId.getPageIdx()){
				//PageID  dans la liste plein  
				
				ByteBuffer bufPage = BufferManager.getInstance().GetPage(pageId);
				// recuper les records 
				bufPage.position(0);
				int nombreslodir = bufPage.capacity()-8;//4096-8
				int nombreRecord = bufPage.getInt(nombreslodir);
				if(nombreRecord ==0) {
					// Pas de record dans la page 
					return null;
				}
				for(int i = 0 ;i<nombreRecord;i++) {
					int intilisationPositionRecord = nombreslodir -(8*(i+1));
					int positonRecord =bufPage.getInt(intilisationPositionRecord);
					Record rec = new Record(tabInfo);
					int taille = rec.readFromBuffer(bufPage, positonRecord);
			        System.out.println(taille+" : taille record");		
					listRec.add(rec);
				}    
				BufferManager.getInstance().FreePage(pageId, 1);
				
			}else {
				
				
				int c = buff.getInt(8);
				int d = buff.getInt(12);
			    PageId pageData2 = new PageId(c,d);
				ByteBuffer buff3 = BufferManager.getInstance().GetPage(pageData2);
				while(buff3.getInt(8)==-1 &&buff3.getInt(12)==0) {
					PageId pagedat = new PageId(buff2.getInt(8),buff2.getInt(12));
					buff3 = BufferManager.getInstance().GetPage(pagedat);
					// recuper les records 
					buff3.position(0);
					int nombreslodir = buff3.capacity()-8;//4096-8
					int nombreRecord = buff3.getInt(nombreslodir);
		
					if(nombreRecord ==0) {
						// Pas de record dans la page 
						return null;
					}
					for(int i = 0 ;i<nombreRecord;i++) {
						int intilisationPositionRecord = nombreslodir -(8*(i+1));
						int positonRecord =buff2.getInt(intilisationPositionRecord);
						Record rec = new Record(tabInfo);
						rec.readFromBuffer(buff2, positonRecord);
						listRec.add(rec);
					}    
					BufferManager.getInstance().FreePage(pagedat, 1);
				}
				BufferManager.getInstance().FreePage(pageData2, 1);
			}
		}
		
			return listRec;
			
		
		}
	  public List<List<PageId>> getDataPage(TableInfo tabInfo){
		  List<List<PageId>> listPageid = new ArrayList<>();
		  ByteBuffer buf = BufferManager.getInstance().GetPage(tabInfo.getHeaderPageId());
		  List<PageId> listA = new ArrayList<>();
		  List<PageId> listB = new ArrayList<>();
		  if(buf.getInt(0)==-1 && buf.getInt(4)==0 && buf.getInt(8)==-1 && buf.getInt(12)==0) {
			  // liste vide ayant aucun pageId 
			  listPageid.add(listA);
			  listPageid.add(listB);
			  return listPageid;
		  }
		  if(buf.getInt(0)==-1 && buf.getInt(4)==0) {
			  // on regarde que l'autre list
			 listPageid.add(listA);
			 PageId p = new PageId(buf.getInt(8),buf.getInt(12));
			 listB.add(p);
			 ByteBuffer buff2 = BufferManager.getInstance().GetPage(p);
			 while(buff2.getInt(8)==-1 && buff2.getInt(12)==0) {
					PageId pagedat = new PageId(buff2.getInt(8),buff2.getInt(12));
					buff2 = BufferManager.getInstance().GetPage(pagedat);
					// recuper les pageId
					listB.add(pagedat);
					BufferManager.getInstance().FreePage(pagedat, 1);
			 }
			 
		  }
		  if(buf.getInt(8)==-1 && buf.getInt(12)==0) {
			  // on regarde que l'autre list
			 listPageid.add(listB);
			 PageId p = new PageId(buf.getInt(0),buf.getInt(4));
			 listA.add(p);
			 ByteBuffer buff2 = BufferManager.getInstance().GetPage(p);
			 while(buff2.getInt(0)==-1 && buff2.getInt(4)==0) {
					PageId pagedat = new PageId(buff2.getInt(0),buff2.getInt(4));
					buff2 = BufferManager.getInstance().GetPage(pagedat);
					// recuper les pageId
					listA.add(pagedat);
					BufferManager.getInstance().FreePage(pagedat, 1);
			 }
		  }
		  
		  return listPageid;
		  
		  
		  
	  }
	  
	  public RecordId  InsertRecordIntoTable(Record record) {
	  }
	  public List<Record> GetAllRecord(TableInfo tabInfo){
		  
	  }


}

	  
	

	
	  
	
