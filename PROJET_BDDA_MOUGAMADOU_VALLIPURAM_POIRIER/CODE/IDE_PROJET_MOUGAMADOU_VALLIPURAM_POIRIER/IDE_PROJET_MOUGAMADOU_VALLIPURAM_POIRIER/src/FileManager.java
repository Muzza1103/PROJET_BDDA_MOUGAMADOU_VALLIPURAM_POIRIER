import java.nio.ByteBuffer;
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
		BufferManager.getInstance().GetPage(pageId);
		String page1 = "-1,0";
		String page2 = "-1,1";
		// correspond aux pages factice
		ByteBuffer  buff1 =  ByteBuffer.allocate((int)DBParams.SGBDPageSize); 
		buff1.put(page1.getBytes());
		buff1.put(page2.getBytes());
		DiskManager.getInstance().WritePage(pageId,buff1);
		
		
		
		BufferManager.getInstance().FreePage(pageId,1);
		return pageId;
	}

}
