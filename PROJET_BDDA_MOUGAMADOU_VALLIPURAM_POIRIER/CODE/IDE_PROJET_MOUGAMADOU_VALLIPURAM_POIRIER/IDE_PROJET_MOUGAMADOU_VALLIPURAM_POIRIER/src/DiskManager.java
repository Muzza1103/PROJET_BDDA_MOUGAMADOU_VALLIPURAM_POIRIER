
import java.util.Stack;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.io.File;
import java.io.IOException;

public final class DiskManager {
	
	private static DiskManager dk = null;
	private Stack<PageId> pilePageLibre ;
	private Stack<PageId> pilePageOccupe ;
	private static final int PAGEIDMAX = 999;
	private int[] taille = new int[DBParams.DMFileCount];

	private DiskManager() {
		pilePageLibre = new Stack<PageId>();
		pilePageOccupe = new Stack<PageId>();
	}
	
	public static DiskManager getInstance() {
		if (dk == null) {
			dk = new DiskManager();
		}
		return dk;
	}

	
	public PageId AllocPage() {
		PageId pageAlloue = null;
		if (pilePageLibre.isEmpty()) {
			try {
				for (int i=0;i<DBParams.DMFileCount;i++) {
					File file = new File(DBParams.DBPath+"F"+i+".bin");
				if (file.exists()) {
					taille[i]=(int)file.length();
					}
				}
				int indiceMin = 0;
				for (int i = 1; i < taille.length; i++) {
					if (taille[i] < taille[indiceMin]) {
						indiceMin = i; // Met à jour l'indice du minimum
					}
				}
				File file = new File(DBParams.DBPath+"F"+indiceMin+".bin");
				if (file.exists()) {
					pageAlloue = new PageId(indiceMin, (int) (file.length() / DBParams.SGBDPageSize) );
				
					//System.out.println("Ajout d'une page à F" +indiceMin + ".bin");
				}else {
					file.createNewFile();
					pageAlloue = new PageId(indiceMin, (int) (file.length() / DBParams.SGBDPageSize) );
				
					//System.out.println("Création du fichier " + indiceMin + ".bin");
				}
				RandomAccessFile raf = new RandomAccessFile(file, "rw");
				raf.seek(raf.length());
				byte[] pageContent = new byte[(int)DBParams.SGBDPageSize];
				raf.write(pageContent);
				raf.close();
				pilePageOccupe.add(pageAlloue);
				return pageAlloue;
			}catch (IOException e){
				e.printStackTrace();
			}
		}else{
			pageAlloue = pilePageLibre.pop();
			pilePageOccupe.add(pageAlloue);
			//System.out.println("La page libre "+pageAlloue.toString()+" a été alloué !");
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
            	//pageId.setOctetTaille((int)rf.length());
				buff.flip();
				rf.close();
				pageId.incrNbreAcces();
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
				rf.seek(pageId.getPageIdx() * DBParams.SGBDPageSize /*+ pageId.getOctetTaille()*/ );
				buff.position((int)DBParams.SGBDPageSize);
				buff.flip();
				byte[] pageData = new byte[buff.remaining()]; //Permet d'initialiser pageData avec la même taille que les données de l'écriture
				buff.get(pageData); // Copie les données du buffer dans pageData
				rf.write(pageData); // Écrire le contenu de pageData dans le fichier
				rf.close();
				pageId.incrNbreAcces();
				
            		
			} catch (Exception e) {
				e.printStackTrace();
				//System.out.println("Erreur lors de l'écriture de la page.");
			}
		} else {
			System.out.println("La page "+pageId.toString()+" n'existe pas !");
		}
	}

	public void DeallocPage (PageId pageId){
		if (pilePageOccupe.contains(pageId)){
			try {
			File file = new File(DBParams.DBPath+"F"+pageId.getFileIdx()+".bin");
				RandomAccessFile raf = new RandomAccessFile(file, "rw");
				raf.seek(pageId.getPageIdx() * DBParams.SGBDPageSize);
				byte[] pageContent = new byte[(int)DBParams.SGBDPageSize];
				raf.write(pageContent);
				raf.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
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
