import java.nio.ByteBuffer;
import java.lang.StringBuilder;

public class Frame {
    private int pin_count;
    private int valdirty;
    private PageId page_chargee;
    private ByteBuffer buffer;
    private boolean dirty;
    
    public Frame() {
        this.pin_count = 0;
        this.valdirty = 0;
        this.page_chargee = null;
        this.buffer = ByteBuffer.allocate((int) DBParams.SGBDPageSize);
    }

    
    public PageId getPageId() {
    	return page_chargee;
    }
    
    public ByteBuffer getByteBuffer() {
    	return buffer;
    }
    public int getPinCount() {
    	return pin_count;
    }
    public int getValDirty() {
    	return valdirty;
    }

    public void updateValdirty(int new_value){
        this.valdirty = new_value;
    }

    public void resetPin_count(){
        this.pin_count = 0;
    }
    
    public void incrPinCount() {
    	this.pin_count ++;
    }

    public void decreasePin_count(){
        this.pin_count --;
    }
    
    public boolean isDirty() {
    	return dirty;
    }
    
    public void setDirty(boolean dirty) {
    	this.dirty = dirty;
    }
    public void loadPage(PageId pageId) {
        this.page_chargee = pageId;
    }
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("Page Chargé : "+page_chargee.toString());
    	return sb.toString();
    }

}
