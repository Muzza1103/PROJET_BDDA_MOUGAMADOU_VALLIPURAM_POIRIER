
import java.util.Stack;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.io.File;
import java.io.IOException;

public class DiskManager {
	
	private Stack<PageId> pilePageLibre;
	private Stack<PageId> pilePageOccupe;
	
	public DiskManager() {
		pilePageLibre = new Stack<>();
		pilePageOccupe = new Stack<>();
	}
	
	public PageId AllocPage() {
		PageId pageAlloue = null;
		System.out.println("ok");
		if (pilePageLibre.empty()) {
			System.out.println("pasok");
			for (int pageIdx = 0;pageIdx<3 ;pageIdx ++){
				System.out.println("Beh");
				for (int fileIdx = 0; fileIdx <DBParams.DMFileCount; fileIdx++){
					System.out.println("quoicou");
					if(!pilePageOccupe.empty()) {
						System.out.println(pilePageOccupe.get(0).getFileIdx());
						System.out.println(pilePageOccupe.get(0).getPageIdx());
						System.out.println(pilePageOccupe.get(pilePageOccupe.size()-1).getPageIdx());
					}
					PageId pageCree = new PageId(fileIdx, pageIdx);
					int nbr = pilePageOccupe.size();
					int comp = 0;
					for (PageId p : pilePageOccupe) {
						if (p.getFileIdx() !=fileIdx || p.getPageIdx() != pageIdx) {
							comp++;
						}
					}
					if (comp==nbr) {
					//if (!pilePageOccupe.contains(pageCree)){
						pageAlloue = pageCree;
						String fileName = "F"+pageAlloue.getFileIdx()+".txt";
						try{
							File file = new File(DBParams.DBPath+fileName);
							if (file.createNewFile()){
								System.out.println("Création du fichier "+fileName+" !");
							}else{
								System.out.println("Ajout d'une page à "+fileName+" !");
							}
						}catch (IOException e){
							e.printStackTrace();
						}
						System.out.println("Yes");
						pilePageOccupe.add(pageAlloue);
						return pageAlloue;
					}
				}
			}
		}else {
			pageAlloue = pilePageLibre.pop();
			pilePageOccupe.add(pageAlloue);
			return pageAlloue;
		}
		return pageAlloue;
		
	}
	
	public void ReadPage (PageId pageId, ByteBuffer buff) {
		if(pilePageOccupe.contains(pageId) || pilePageLibre.contains(pageId)){ //en considérant que les pages libres peuvent ne pas être vide
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
		System.out.println(pilePageOccupe.contains(pageId));
        if (pilePageOccupe.contains(pageId)) {
            String fileName = "F" + pageId.getFileIdx() + ".txt";
            try {
				RandomAccessFile rf = new RandomAccessFile(DBParams.DBPath+fileName, "rw");
				FileChannel channel = rf.getChannel(); // A revoir
				rf.seek(pageId.getPageIdx() * DBParams.SGBDPageSize); // Voir si le curseur fonctionne
				if (buff.remaining() <= DBParams.SGBDPageSize) {// Pour être sur que la quantité de données que l'on va écrire est plus petite que la taille de la page 
					System.out.println("az");
					buff.flip();
					channel.write(buff);
					//rf.write(buff.array());
					rf.close();
					channel.close();
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
		if (pilePageOccupe.contains(pageId)){
			pilePageLibre.add(pageId);
			pilePageOccupe.remove(pageId);
		}else{
			System.out.println("Cette page n'a pas été alloué !");
		}

	}
	

	public int GetCurrentCountAllocPages(){
		return pilePageOccupe.size();
	}

}
