import java.nio.ByteBuffer;
import java.util.*;

public final class BufferManager {
	
	private static BufferManager bm = null;
	private List<Frame> listFrames;
	
	
	private BufferManager() {
		//initialise la liste des frames
		listFrames = new ArrayList<>(DBParams.frameCount);
		for (int i = 0; i < DBParams.frameCount; i++) {
            listFrames.add(new Frame());
        }
	}
	
	public static BufferManager getInstance() {
		if (bm == null) {
			bm = new BufferManager();
		}
		return bm;
	}
	
    public ByteBuffer GetPage(PageId ID_page){
    	
    	//buffer qui va permettre de stocker une page
    	//ByteBuffer byteB = ByteBuffer.allocate((int)DBParams.SGBDPageSize);
    	Frame lFU = null; //frame de la page correspondant à LFU
    	int minAccessCount = Integer.MAX_VALUE;//le compteur du minimum d'accès
    	
    	for(Frame f : listFrames) {
    		if (f.getPageId() != null && f.getPageId().equals(ID_page)) {
    			//le cas où la page est deja dans une des frame
    			f.incrPinCount();
    			//incrémentation de pinCount
    			f.getPageId().incrNbreAcces();
    			//incrémente le compteur d'accès à la page
    			return f.getByteBuffer();
    			//renvoie le contenu de la page
    		}else if (f.getPinCount() == 0) {
                if (f.getPageId() == null) {
                	//frame = vide , va etre utilisé pour la page
                    f.incrPinCount();
                    f.loadPage(ID_page);
                    f.getPageId().incrNbreAcces();
                    return f.getByteBuffer();
                } else if (f.getPageId().getNbreAcces() < minAccessCount) {
                	//trouve la frame avec le compteur d'acces min
                	//pour respecter le LFU
                    lFU = f;
                    minAccessCount = f.getPageId().getNbreAcces();
                }
    		}
    	}
    	 if (lFU != null) {
    		 this.FreePage(lFU.getPageId(), lFU.getValDirty());
             lFU.loadPage(ID_page);
             //charge la page dans LFU
             lFU.incrPinCount();
             lFU.getPageId().incrNbreAcces();
             return lFU.getByteBuffer();
         }
    	
    	return null;
    }
    
    
    public void FreePage (PageId ID_page, int valdirty){
    	 for (Frame f : listFrames) {
             if (f.getPageId() != null && f.getPageId().equals(ID_page)) {
            	 // decremente pinCount et la page devient "dirty" dans certains cas
                 f.decreasePin_count();;
                 if (valdirty == 1) {
                	 DiskManager.getInstance().WritePage(f.getPageId(), f.getByteBuffer());
                     f.setDirty(false);
                 }
                 
             }
    	 }
    }

    public void FlushAll(){
    	 for (Frame frame : listFrames) {
             if (frame.isDirty()) {
                 // Écrire la page sur le disque en appleant DiskManager
                 DiskManager.getInstance().WritePage(frame.getPageId(), frame.getByteBuffer());
                 frame.setDirty(false);
             }
             //Va permettre de reinitialiser le PinCount
             frame.resetPin_count();
         }
    }
    
    public void videListe() {
    	for(int i = 0; i < listFrames.size(); i++) {
    		listFrames.remove(i);
    	}
    }
    
    public void afficheFrame() {
    	for (Frame f : listFrames ) {
    		if(f.getPageId() != null) {
    			System.out.println(f.toString());
    		}
    	}
    }
}
