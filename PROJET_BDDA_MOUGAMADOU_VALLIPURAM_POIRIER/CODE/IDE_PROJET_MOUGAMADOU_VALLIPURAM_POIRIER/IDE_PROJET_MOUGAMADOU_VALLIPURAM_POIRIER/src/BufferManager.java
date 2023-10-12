import java.nio.ByteBuffer;
import java.util.*;
    
public class BufferManager {
    List<Frame> list_frames = Arrays.asList(new Frame[DBParams.FrameCount]);
    
    public ByteBuffer GetPage(PageId ID_page){
        

    }

    public void FreePage (PageId ID_page, int new_valdirty){
        for (Frame frame : list_frames) {
            if(frame.page_chargee == ID_page){
                frame.decreasePin_count();
                frame.valdirty = new_valdirty;
            }
        }
    }

    public void FlushAll(){
        for (Frame frame : list_frames) {
            if(frame.valdirty == 1){
                //ECRITURE
            }
            frame.valdirty = 0;
        } 
    }

}
