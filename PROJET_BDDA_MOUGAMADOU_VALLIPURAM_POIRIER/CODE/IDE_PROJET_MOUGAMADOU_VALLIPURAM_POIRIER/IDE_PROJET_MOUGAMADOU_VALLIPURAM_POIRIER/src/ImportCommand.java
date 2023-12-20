import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.io.BufferedReader;

public class ImportCommand{
	private Record record;
	private String nomRelation;
	private File file;
	private FileReader fileReader;
	private int indexTable;
	
	public ImportCommand(String[] mots) throws FileNotFoundException {
		this.nomRelation = mots[2];
		this.file = new File("/Users/vthar/Documents/S.csv");
		this.fileReader = new FileReader(file);
		DataBaseInfo dbi = DataBaseInfo.getInstance();
		this.indexTable = -1;
		record = null;
		for(TableInfo ti : dbi.getList()) {
			if(ti.getNom().equals(nomRelation)) {
				indexTable = dbi.getList().indexOf(ti);
			}
		}
		if(indexTable != -1) {
			record = new Record(dbi.getList().get(indexTable));
			
		}
	}
	public void execute() throws IOException {
		try(BufferedReader  bufferReader = new BufferedReader(fileReader)){
			String ligne;
			String[] separateur;
			ArrayList<Object> recvalues = new ArrayList<>();
			while((ligne = bufferReader.readLine()) != null) {
				separateur = ligne.split(",");
				for(int i = 0; i < separateur.length; i++) {
					recvalues.add(separateur[i]);
					System.out.println(separateur[i]);
				}
			}
			record.InsertValues(recvalues);
		}
	}

}
