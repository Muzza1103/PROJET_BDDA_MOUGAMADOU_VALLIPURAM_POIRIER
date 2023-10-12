import java.nio.ByteBuffer;
import java.util.*;

public final class BufferManager {
	
	private static BufferManager bm = null;
	private List<Frame> listFrames;
	
	
	private BufferManager() {
		listFrames = Arrays.asList(new Frame[DBParams.frameCount]);
	}
	
	public static BufferManager getInstance() {
		if (bm == null) {
			bm = new BufferManager();
		}
		return bm;
	}
	
    public ByteBuffer GetPage(PageId ID_page){
    	ByteBuffer byteB = ByteBuffer.allocate((int)DBParams.SGBDPageSize);
    	List<Frame> listFrameZero = Arrays.asList(new Frame[DBParams.frameCount]);
    	int cptNbreAccees=0;
    	for(Frame f : listFrames) {
    		if(f.getPageId() == ID_page) {
    			f.incrPinCount();
    			f.getPageId().incrNbreAcces();
    			return f.getByteBuffer();
    		}
    	}
    	for(int i = 0; i < listFrames.size(); i++) {
    		if(listFrames.get(i).getPageId() == null && listFrames.get(i).getByteBuffer() == null ) {
    			listFrames.get(i).incrPinCount();
    			listFrames.get(i).getPageId().incrNbreAcces();
    			return listFrames.get(i).getByteBuffer();
    		}
    		else if(listFrames.get(i).getPinCount() == 0 ) {
    			listFrameZero.add(listFrames.get(i));
    		}	
    	}
    	if (!listFrameZero.isEmpty()) {
    		for (int i=0;i<listFrameZero.size();i++) {
    			if (listFrameZero.get(i).getPageId().getNbreAcces()<listFrameZero.get(i+1).getPageId().getNbreAcces()) {
    				cptNbreAccees = i;
    			}
    		}
    		return listFrameZero.get(cptNbreAccees).getByteBuffer();
    	}
    	
    	return byteB;
    }
    
    
    public void FreePage (PageId ID_page, int valdirty){
        
    }

    public void FlushAll(){

    }

}
