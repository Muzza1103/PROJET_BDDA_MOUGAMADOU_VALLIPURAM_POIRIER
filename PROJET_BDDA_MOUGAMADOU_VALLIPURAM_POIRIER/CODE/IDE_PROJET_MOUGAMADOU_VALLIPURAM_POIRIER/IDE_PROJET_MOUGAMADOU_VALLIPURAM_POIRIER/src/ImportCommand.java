import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

public class ImportCommand{
	private String nomRelation;
	private File file;
	private FileReader fileReader;
	
	public ImportCommand(String[] mots) throws FileNotFoundException {
		this.nomRelation = mots[2];
		String fileName = mots[3];
		this.file = new File("/users/licence/in07091/PROJET_BDDA_MOUGAMADOU_VALLIPURAM_POIRIER/PROJET_BDDA_MOUGAMADOU_VALLIPURAM_POIRIER/" + fileName);
		this.fileReader = new FileReader(file);
	}
	public void execute() throws IOException {
		DatabaseManager dbm = DatabaseManager.getInstance(); 
		try(BufferedReader  bufferReader = new BufferedReader(fileReader)){
			String ligne;
			while((ligne = bufferReader.readLine()) != null) {
				String values = "(" + ligne + ")";
				//System.out.println(values);
				dbm.ProcessCommand("INSERT INTO "+ nomRelation + " VALUES "+ values);
			}
		}
	}

}
