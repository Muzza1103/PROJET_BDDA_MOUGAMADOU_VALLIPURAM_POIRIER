import java.nio.ByteBuffer;

public class Frame {
    private int pin_count;
    private int valdirty;
    private PageId page_chargee;
    private ByteBuffer buffer;
    
    public void initFrame(){
        this.pin_count = 0;
        this.valdirty = 0;
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

}
