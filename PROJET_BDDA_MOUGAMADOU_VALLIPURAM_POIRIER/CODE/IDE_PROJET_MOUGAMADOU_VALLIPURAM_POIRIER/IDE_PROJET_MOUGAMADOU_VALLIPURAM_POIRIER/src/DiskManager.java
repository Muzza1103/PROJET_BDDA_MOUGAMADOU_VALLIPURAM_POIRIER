
import java.util.Stack;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.io.File;
import java.io.IOException;

public class DiskManager {
	
	private Stack<PageId> pilePageLibre ;
	private Stack<PageId> pilePageOccupe ;
	private static final int PAGEIDMAX = 999;

	public DiskManager() {
		pilePageLibre = new Stack<PageId>();
		pilePageOccupe = new Stack<PageId>();
	}

	
	public PageId AllocPage() {
		PageId pageAlloue = null;
		if (pilePageLibre.empty()) {
			for (int pageIdx = 0; pageIdx< PAGEIDMAX ;pageIdx ++){
				for (int fileIdx = 0; fileIdx <DBParams.DMFileCount; fileIdx++){
					PageId pageCree = new PageId(fileIdx, pageIdx);
					if (!pilePageOccupe.contains(pageCree)){
						pageAlloue = pageCree;
						String fileName = "F"+pageAlloue.getFileIdx();
						try{
							File file = new File(DBParams.DBPath+fileName+".bin");
							if (file.exists()) {
								System.out.println("Ajout d'une page à " + fileName + ".bin");
							} else {
								if (file.createNewFile()) {
									System.out.println("Création du fichier " + fileName + ".bin");
								}
							}
						}catch (IOException e){
							e.printStackTrace();
						}
						pilePageOccupe.add(pageAlloue);
						return pageAlloue;
					}
				}
			}
		}else {
			pageAlloue = pilePageLibre.pop();
			pilePageOccupe.add(pageAlloue);
			System.out.println("La page libre "+pageAlloue.toString()+" a été alloué !");
			return pageAlloue;
		}
		return pageAlloue; 
	}
	
	public void ReadPage (PageId pageId, ByteBuffer buff) {
		if(pilePageOccupe.contains(pageId) || pilePageLibre.contains(pageId)){ //en considérant que les pages libres peuvent ne pas être vide
			String fileName = "F"+pageId.getFileIdx()+".bin";
			try{
				RandomAccessFile rf = new RandomAccessFile(DBParams.DBPath+fileName, "r");
				rf.seek(pageId.getPageIdx()*DBParams.SGBDPageSize);
				buff.clear();
				byte[] pageData = new byte[(int)DBParams.SGBDPageSize];
				rf.read(pageData); //pageData prend les données de la lecture
            	buff.put(pageData); //on met les données de la lecture dans le buffer
				buff.flip();
				rf.close();
			}catch (Exception e) {
				e.printStackTrace();
				System.out.println("Erreur lors de la lecture de la page.");
			}
		}else{
			System.out.println("La page "+pageId.toString()+" n'existe pas !");
		}
	}

	public void WritePage(PageId pageId, ByteBuffer buff) {
        if (pilePageOccupe.contains(pageId)) {
            String fileName = "F" + pageId.getFileIdx() + ".bin";
            try {
            	//File file = new File(DBParams.DBPath+fileName);
				RandomAccessFile rf = new RandomAccessFile(DBParams.DBPath+fileName, "rw");
				rf.seek(pageId.getPageIdx() * DBParams.SGBDPageSize);
				if (buff.remaining() <= DBParams.SGBDPageSize) { // Pour être sur que la quantité de données que l'on va écrire est plus petite que la taille de la page 
					buff.flip();
					byte[] pageData = new byte[buff.remaining()]; //Permet d'initialiser pageData avec la même taille que les données de l'écriture
					buff.get(pageData); // Copie les données du buffer dans pageData
					rf.write(pageData); // Écrire le contenu de pageData dans le fichier
					rf.close();
				} else {
					System.out.println("La quantité de données restantes est supérieure à la taille de la page.");
				}
            		
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Erreur lors de l'écriture de la page.");
			}
		} else {
			System.out.println("La page "+pageId.toString()+" n'existe pas !");
		}
	}

	public void DeallocPage (PageId pageId){
		if (pilePageOccupe.contains(pageId)){
			pilePageLibre.add(pageId);
			pilePageOccupe.remove(pageId);
			System.out.println("La page "+pageId.toString()+" a été désalloué !");
		}else{
			System.out.println("La page "+pageId.toString()+" n'a pas été alloué !");
		}

	}
	

	public int GetCurrentCountAllocPages(){
		return pilePageOccupe.size();
	}

}

