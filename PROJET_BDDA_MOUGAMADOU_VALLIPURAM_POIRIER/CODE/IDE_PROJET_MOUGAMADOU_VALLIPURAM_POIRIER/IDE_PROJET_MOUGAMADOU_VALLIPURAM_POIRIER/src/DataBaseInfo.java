import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

public class DataBaseInfo {

    private static DataBaseInfo dbi = null;
    private static ArrayList<TableInfo> listInfos;
    private static int compteurRel;
    
    private DataBaseInfo(){
        this.listInfos = new ArrayList<TableInfo>();
        this.compteurRel = 0;
    }

    public static DataBaseInfo getInstance(){
        if(dbi == null){
            dbi = new DataBaseInfo();
        }
        return dbi;
    }

    public void Init() {
    	String fileName = "DBInfo.save";
    	try {
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			StringBuffer sb = new StringBuffer();
			String line;
			String sep = " ";
			while((line = br.readLine()) != null) {
				sb.append(line);
				String mots[] = sb.toString().split(sep);
				TableInfo tableAdd = new TableInfo(mots[0],Integer.valueOf(mots[1]));
				ArrayList<ColInfo> liste = new ArrayList<>();
				for(int i=2;i<mots.length;i+=3) {
					TypeColonne tp;
					if(mots[i+1].equals("INT") || mots[i+1].equals("FLOAT")) {
						tp = new TypeColonne(mots[i+1]);
					}else {
						tp = new TypeColonne(mots[i+1],Integer.valueOf(mots[i+2])); // Vérifier que le T est bien le nombre de character
					}
					liste.add(new ColInfo(mots[i],tp,Integer.valueOf(mots[i+2])));
				}
				AddTableInfo(tableAdd);
			}
			fr.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	

    }

    public void Finish() {
    	String fileName = "DBInfo.save";
    	File file = new File(DBParams.DBPath+fileName);
    	try {
    		FileOutputStream fos = new FileOutputStream(file);
    		ObjectOutputStream oos = new ObjectOutputStream(fos);
    		for(TableInfo tab : listInfos) {
    			String s = tab.getNom();
    			oos.writeBytes(s);
    			oos.write(tab.getNbColonnes());
    			StringBuffer sb = new StringBuffer();
    			for (int i=0;i<tab.getColInfoList().size();i++) {
    				sb.append(tab.getColInfo(i).GetNomCol());
    				sb.append(" " + tab.getColInfo(i).GetTypCol());
    				sb.append(" " + tab.getColInfo(i).GetT() + " ");
    			}
    			oos.writeBytes(sb.toString());
    		}
    		oos.close();
    	}catch(FileNotFoundException e) {
    		System.out.println("Fichier non existant !");
    		
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }

    public void AddTableInfo(TableInfo tableAdd){
        listInfos.add(tableAdd);
        compteurRel++;
    }

}
