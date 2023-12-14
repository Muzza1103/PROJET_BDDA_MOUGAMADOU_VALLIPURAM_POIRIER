import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


/*
 * Loin d'avoir fini mais oklm on a la santé
 */

public class ImportCommand{
	private Record record;
	private String nomRelation;
	private File file;
	private FileReader fileReader;
	private DataBaseInfo dbi;
	private int indexTable;
	
	public ImportCommand(String[] mots) throws FileNotFoundException {
		this.nomRelation = mots[2];
		this.file = new File("/users/licence/in07091/PROJET_BDDA_MOUGAMADOU_VALLIPURAM_POIRIER/PROJET_BDDA_MOUGAMADOU_VALLIPURAM_POIRIER/");
		this.fileReader = new FileReader(file + "S.csv");
		this.dbi = DataBaseInfo.getInstance();
		this.indexTable = -1;
		record = null;
		for(TableInfo ti : dbi.getList()) {
			if(ti.getNom().equals(nomRelation)) {
				indexTable = dbi.getList().indexOf(ti);
			}
		}
	}
	
	public void execute() throws IOException {
		try(Scanner scanner = new Scanner(fileReader)){
			while(scanner.hasNextLine()) {
				String ligne = scanner.nextLine();
				String[] separateur = ligne.split(",");
				ArrayList<Object> recvalues = new ArrayList<>();
				
				for(int i = 0; i < separateur.length; i++) {
					recvalues.add(separateur[i]);
				}
				if(indexTable != -1) {
					record = new Record(dbi.getList().get(indexTable));
					record.InsertValues(recvalues);	
				}
			}
		}
	}

}
