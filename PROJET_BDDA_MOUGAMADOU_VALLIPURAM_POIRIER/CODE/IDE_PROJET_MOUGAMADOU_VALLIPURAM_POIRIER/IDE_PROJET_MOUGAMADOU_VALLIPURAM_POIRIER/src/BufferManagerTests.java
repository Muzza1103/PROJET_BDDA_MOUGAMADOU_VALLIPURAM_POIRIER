import java.nio.ByteBuffer;

public class BufferManagerTests {
	public static void main(String [] args) {
		DBParams.DBPath = "/users/licence/in07091/PROJET_BDDA_MOUGAMADOU_VALLIPURAM_POIRIER/PROJET_BDDA_MOUGAMADOU_VALLIPURAM_POIRIER/DB/";
		DBParams.SGBDPageSize = 4096;
        DBParams.DMFileCount = 4;
        DBParams.frameCount = 2;
        //TestEcritureEtLecture();
        //TestEcritureEtLecture2();
        TestEcritureEtLecture3();
	}
	
	public static void TestEcritureEtLecture() { //teste la gestion des buffers pour avoir les bons textes écrit dans les bons buffers
		DiskManager dk = DiskManager.getInstance();
		BufferManager bm = BufferManager.getInstance();
		
		PageId pageId = dk.AllocPage();
		PageId pageId2 = dk.AllocPage();
		
		if (pageId != null) {
			System.out.println("Page allouée : " + pageId.toString());
		} else {
			System.out.println("Impossible d'allouer la page.");
		
		}
		if (pageId2 != null) {
			System.out.println("Page allouée : " + pageId2.toString());
		} else {
			System.out.println("Impossible d'allouer la page.");
		
		}
		
		ByteBuffer bb = bm.GetPage(pageId);
		ByteBuffer bb2 = bm.GetPage(pageId2);
		
		System.out.println("1");
		String sb1 = "ksi est le goat !";
		String sb2 = "réel poto";
		
		byte[] sbBytes = sb1.getBytes();
		byte[] sbBytes2 = sb2.getBytes();
		
		if (sbBytes.length <= DBParams.SGBDPageSize ){
			bb.put(sb1.getBytes());
		}
		if (sbBytes2.length <= DBParams.SGBDPageSize ){
			bb2.put(sb2.getBytes());
		}
		
		System.out.println("2");
		dk.WritePage(pageId, bb);
		dk.ReadPage(pageId, bb);
		
		System.out.println("3");
		dk.WritePage(pageId2, bb2);
		System.out.println("4");
		dk.ReadPage(pageId2, bb2);
		
		bb.rewind();
		bb2.rewind();
		
		System.out.println("5");
		byte[] donneeLecture = new byte[bb.remaining()];
		byte[] donneeLecture2 = new byte[bb2.remaining()];
		
		System.out.println("6");
		bb.get(donneeLecture);
		bb2.get(donneeLecture2);
		System.out.println("7");
		
		String lecture = new String(donneeLecture);
		String lecture2 = new String(donneeLecture2);
		
		System.out.println("Lecture de la page : "+pageId.toString());
		System.out.println(lecture);
		System.out.println("Lecture de la page : "+pageId2.toString());
		System.out.println(lecture2);
			
	}
	
	public static void TestEcritureEtLecture2() { // Cas test où 4 pages sont crées tour à tour, vérifie que lorsque les pincount sont égaux et que les nombres d'accées sont égaux la page désalloué est la plus ancienne.
		DiskManager dk = DiskManager.getInstance();
		BufferManager bm = BufferManager.getInstance();
		
		for(int i=0;i<4;i++) {
			PageId pageId = dk.AllocPage();
			if (pageId != null) {
				System.out.println("Page allouée : " + pageId.toString());
			} else {
				System.out.println("Impossible d'allouer la page.");
			
			}
			ByteBuffer bb = bm.GetPage(pageId);
			System.out.println("6");
			String sb1 = "gota";
			byte[] sbBytes = sb1.getBytes();
			System.out.println("7");
			bb.rewind();
			if (sbBytes.length <= DBParams.SGBDPageSize ){
				bb.put(sbBytes);
				System.out.println("8");
			}
			dk.WritePage(pageId, bb);
			System.out.println("9");
			dk.ReadPage(pageId, bb);
			bb.rewind();
			byte[] donneeLecture = new byte[bb.remaining()];
			bb.get(donneeLecture);
			String lecture = new String(donneeLecture);
			System.out.println("Lecture de la page : "+pageId.toString());
			System.out.println(lecture);
			bm.FreePage(pageId, 0);
			bm.afficheFrame();
		}
	}
	
	public static void TestEcritureEtLecture3() { // Teste le cas LFU ou les deux frames ont leur pin count égal a 0, la bufferManager est censé désallouer la page la moins utilisé 
		DiskManager dk = DiskManager.getInstance();
		BufferManager bm = BufferManager.getInstance();
		
		PageId pageId = dk.AllocPage();
		PageId pageId2 = dk.AllocPage();
		PageId pageId3= dk.AllocPage();
		PageId pageId4 = dk.AllocPage();
		if (pageId != null) {
			System.out.println("Page allouée : " + pageId.toString());
		} else {
			System.out.println("Impossible d'allouer la page.");
		}
		if (pageId2 != null) {
			System.out.println("Page allouée : " + pageId2.toString());
		} else {
			System.out.println("Impossible d'allouer la page.");
		}
		if (pageId3 != null) {
			System.out.println("Page allouée : " + pageId3.toString());
		} else {
			System.out.println("Impossible d'allouer la page.");
		}
		if (pageId4 != null) {
			System.out.println("Page allouée : " + pageId4.toString());
		} else {
			System.out.println("Impossible d'allouer la page.");
		}
			
		ByteBuffer bb = bm.GetPage(pageId);
		ByteBuffer bb2 = bm.GetPage(pageId2);
		
		String sb1 = "muzza!";
		byte[] sbBytes = sb1.getBytes();
		bb.rewind();
		
		if (sbBytes.length <= DBParams.SGBDPageSize ){
			bb.put(sbBytes);
		}
			
		dk.WritePage(pageId, bb); // Utilisation répété de PageId pour incrémenter l'utilisation de cette page
		dk.ReadPage(pageId, bb);
		bb.rewind();
		dk.ReadPage(pageId, bb);
		bb.rewind();
		dk.ReadPage(pageId, bb);
		
		bb.rewind();
		byte[] donneeLecture = new byte[bb.remaining()];
		bb.get(donneeLecture);
		String lecture = new String(donneeLecture);
		System.out.println("Lecture de la page : "+pageId.toString());
		System.out.println(lecture);
		bm.afficheFrame();
		bm.FreePage(pageId, 0);
		bm.FreePage(pageId2, 0);
		bm.GetPage(pageId4);
		bm.afficheFrame();
	}
}
