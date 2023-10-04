
import java.util.Stack;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.io.File;
import java.io.IOException;

public class DiskManager {
	
	private Stack<PageId> pilePageLibre = new Stack<>();
	private Stack<PageId> pilePageOccupé = new Stack<>();
	
	public PageId AllocPage() {
		PageId pageAlloué = null;
		if (pilePageLibre.empty()) {
			for (int pageIdx = 0; ;pageIdx ++){
				for (int fileIdx = 0; fileIdx <DBParams.DMFileCount; fileIdx++){
					PageId pageCrée = new PageId(fileIdx, pageIdx);
					if (!pilePageOccupé.contains(pageCrée)){
						pageAlloué = pageCrée;
						String fileName = "F"+pageAlloué.getFileIdx()+".txt";
						try{
							File file = new File("c:\\"+DBParams.DBPath+"\\"+fileName+".txt");
							if (file.createNewFile()){
								System.out.println("Création du fichier "+fileName+" !");
							}else{
								System.out.println("Ajout d'une page à "+fileName+" !");
							}
						}catch (IOException e){
							e.printStackTrace();
						}
						pilePageOccupé.add(pageAlloué);
						return pageAlloué;
					}
				}
			}
		}else {
			pageAlloué = pilePageLibre.pop();
			pilePageOccupé.add(pageAlloué);
			return pageAlloué;
		}
		
	}
	
	public void ReadPage (PageId pageId, ByteBuffer buff) {
		if(pilePageOccupé.contains(pageId) || pilePageLibre.contains(pageId)){ //en considérant que les pages libres peuvent ne pas être vide
			String fileName = "F"+pageId.getFileIdx()+".txt";
			try{
				RandomAccessFile rf = new RandomAccessFile(fileName, "r");
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
			System.out.println("La page n'existe pas !");
		}
	}

	public void WritePage(PageId pageId, ByteBuffer buff) {
        if (pilePageOccupé.contains(pageId)) {
            String fileName = "F" + pageId.getFileIdx() + ".txt";
            try {
				RandomAccessFile rf = new RandomAccessFile(fileName, "rw");
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
			System.out.println("La page n'existe pas !");
		}
	}

	public void DeallocPage (PageId pageId){
		if (pilePageOccupé.contains(pageId)){
			pilePageLibre.add(pageId);
			pilePageOccupé.remove(pageId);
		}else{
			System.out.println("Cette page n'a pas été alloué !");
		}

	}
	

	public int GetCurrentCountAllocPages(){
		return pilePageOccupé.size();
	}

}
