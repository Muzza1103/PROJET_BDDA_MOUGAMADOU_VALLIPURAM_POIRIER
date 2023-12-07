import java.nio.ByteBuffer;
import java.util.Scanner;
/* 
public class DiskManagerTests {
	
	public static void main(String [] args) {
		DBParams.DBPath = "/users/licence/in07091/PROJET_BDDA_MOUGAMADOU_VALLIPURAM_POIRIER/PROJET_BDDA_MOUGAMADOU_VALLIPURAM_POIRIER/DB/";
		DBParams.SGBDPageSize = 4096;
        	DBParams.DMFileCount = 4;
		
		TestAllocationPage();

		//TestDesAllocPage();

		//TestEcritureLecturePage();
	}
	

	public static void TestAllocationPage() { // Vérifier que l'ordre de création des fichiers et de leurs pages est correcte, et que cela alloue bien en priorié les pages de la pile PilePageLibre
		DiskManager dk = new DiskManager();
		Scanner sc = new Scanner(System.in);
		System.out.println("Combien de pages voulez vous alloué ?");
		int nbrPage = sc.nextInt();
		sc.close();
		for (int i = 0; i < nbrPage; i++) {
			PageId pageId = dk.AllocPage();
			if (pageId != null) {
				System.out.println("Page allouée : " + pageId.toString());
			} else {
				System.out.println("Impossible d'allouer la page.");
			}
		}

		PageId pageDesalocalise = new PageId(0,0);
		dk.DeallocPage(pageDesalocalise);
		System.out.println("Désallocation de la page : "+pageDesalocalise.toString());
		PageId pageAllocDePilePageLibre = dk.AllocPage();
		System.out.println("Allocation de la page : "+pageAllocDePilePageLibre.toString());

	}
	
	public static void TestEcritureLecturePage() { // Vérifier que l'écriture et la lecture fonctionne 

		DiskManager dk = new DiskManager();	
		PageId pageAlloue = dk.AllocPage();

		//Ecriture
		ByteBuffer buff1 = ByteBuffer.allocate((int)DBParams.SGBDPageSize);
		String sb = "Il faisait pas beau ce matin !";
		byte[] sbBytes = sb.getBytes();
		if (sbBytes.length <= DBParams.SGBDPageSize ){
			buff1.put(sb.getBytes());
		dk.WritePage(pageAlloue,buff1);
		System.out.println("Ecriture dans la page : "+pageAlloue.toString());
		}else{
			System.out.println("La quantité de données restantes à écrire est supérieure à la taille de la page.");
		}
		
		//Lecture
		ByteBuffer buff2 = ByteBuffer.allocate((int)DBParams.SGBDPageSize);
		dk.ReadPage(pageAlloue, buff2);
		buff2.rewind();
		byte[] donneeLecture = new byte[buff2.remaining()];
		buff2.get(donneeLecture);
		String lecture = new String(donneeLecture);
		System.out.println("Lecture de la page : "+pageAlloue.toString());
		System.out.println(lecture);

	}


	public static void TestDesAllocPage() { // Essaie de désallouer une page déjà désalloué ou qui n'a pas été crée

		DiskManager dk = new DiskManager();
		PageId pageAlloue = dk.AllocPage();
		dk.DeallocPage(pageAlloue);
		dk.DeallocPage(pageAlloue);

		PageId pageNonCree = new PageId(1, 0);
		dk.DeallocPage(pageNonCree);

	}

}*/
