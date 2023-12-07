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
    			System.out.println("if(1)");
    			//le cas où la page est deja dans une des frame
    			f.incrPinCount();
    			//incrémentation de pinCount
    			f.getPageId().incrNbreAcces();
    			//incrémente le compteur d'accès à la page
    			return f.getByteBuffer();
    			//renvoie le contenu de la page
    		}else if (f.getPinCount() == 0) {
    			System.out.println("elseif(1)");
                if (f.getPageId() == null) {
                	System.out.println("if(2)");
                	//frame = vide , va etre utilisé pour la page
                    f.incrPinCount();
                    f.loadPage(ID_page);
                    f.getPageId().incrNbreAcces();
                    return f.getByteBuffer();
                } else if (f.getPageId().getNbreAcces() < minAccessCount) {
                	System.out.println("1");
                	//trouve la frame avec le compteur d'acces min
                	//pour respecter le LFU
                    lFU = f;
                    minAccessCount = f.getPageId().getNbreAcces();
                }
    		}
    	}
    //	for(int i = 0; i < listFrames.size(); i++) {
    //		if(listFrames.get(i).getPageId() == null && listFrames.get(i).getByteBuffer() == null ) {
    //			listFrames.get(i).incrPinCount();
    //			listFrames.get(i).getPageId().incrNbreAcces();
    //			return listFrames.get(i).getByteBuffer();
    //		}
    //		else if(listFrames.get(i).getPinCount() == 0 ) {
    //			listFrameZero.add(listFrames.get(i));
    //		}	
    //	}
    	 if (lFU != null) {
    		 this.FreePage(lFU.getPageId(), lFU.getValDirty());
    		 System.out.println("2");
             lFU.loadPage(ID_page);
             System.out.println("3");
             //charge la page dans LFU
             lFU.incrPinCount();
             System.out.println("4");
             lFU.getPageId().incrNbreAcces();
             System.out.println("5");
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
    
    public void afficheFrame() {
    	for (Frame f : listFrames ) {
    		if(f.getPageId() != null) {
    			System.out.println(f.toString());
    		}
    	}
    }
}