import java.nio.ByteBuffer;

public class DiskManagerTests {
	public static void main(String [] args) {
		DBParams.DBPath = "/users/licence/in07091/PROJET_BDDA_MOUGAMADOU_VALLIPURAM_POIRIER/PROJET_BDDA_MOUGAMADOU_VALLIPURAM_POIRIER/DB/";
        DBParams.SGBDPageSize = 4096;
        DBParams.DMFileCount = 4;
		DiskManager dk = new DiskManager();
		PageId page = dk.AllocPage();
		//dk.AllocPage();
		//dk.AllocPage();
		//ByteBuffer buff = ByteBuffer.allocate((int)DBParams.SGBDPageSize);
		//String sb = "Il fait pas beau";
		//buff.put(sb.getBytes());
		//dk.WritePage(page,buff);
		ByteBuffer buff = ByteBuffer.allocate((int)DBParams.SGBDPageSize);
		dk.ReadPage(page, buff);
		System.out.println(buff.toString());
	}
	
	public void TestEcriturePage() {
		
	}
	public void TestLecturePage() {
		
	}
	public static void TestAllocationPage() {
		DiskManager dk = new DiskManager();
		dk.AllocPage();
	}
	public void TestDesAllocPage() {
		
	}

}
