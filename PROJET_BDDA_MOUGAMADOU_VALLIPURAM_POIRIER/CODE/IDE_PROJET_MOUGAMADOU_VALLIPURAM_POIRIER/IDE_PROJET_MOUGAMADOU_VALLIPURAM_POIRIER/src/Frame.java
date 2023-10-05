import java.nio.ByteBuffer;

public class Frame {
    public int pin_count;
    public int valdirty;
    public PageId page_chargee;
    public ByteBuffer buffer;
    
    public void initFrame(){
        this.pin_count = 0;
        this.valdirty = 0;
    }

    public void updateValdirty(int new_value){
        this.valdirty = new_value;
    }

    public void resetPin_count(){
        this.pin_count = 0;
    }

    public void decreasePin_count(){
        this.pin_count --;
    }

}
