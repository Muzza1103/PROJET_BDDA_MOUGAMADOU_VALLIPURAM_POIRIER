
import java.util.Stack;
import java.nio.ByteBuffer;

public class DiskManager {
	
	private Stack<PageId> pilePageLibre = new Stack<>();
	private Stack<PageId> pilePageOccupé = new Stack<>();
	
	public PageId AllocPage() {
		PageId pageAlloué = null;
		if (pilePageLibre.empty()) {
			//A compléter
		}else {
			pageAlloué = pilePageLibre.pop();
			pilePageOccupé.add(pageAlloué);
		}
		return pageAlloué;
		
	}
	
	public void ReadPage (PageId pageId, ByteBuffer buff) {
		
	}

	
}
