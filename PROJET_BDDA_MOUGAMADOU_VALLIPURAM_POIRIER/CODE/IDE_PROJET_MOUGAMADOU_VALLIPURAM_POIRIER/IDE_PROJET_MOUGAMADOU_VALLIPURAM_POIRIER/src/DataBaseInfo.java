import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

public class DataBaseInfo implements Serializable {

    private static DataBaseInfo dbi = null;
    private ArrayList<TableInfo> listInfos;
    private int compteurRel;
    
    private DataBaseInfo(){
        this.listInfos = new ArrayList<TableInfo>();
        this.compteurRel = 0;
    }
    
    public ArrayList<TableInfo> getList() {
    	return listInfos;
    }
    
    private int getCompteur() {
    	return compteurRel;
    }
    
    private void setList(ArrayList<TableInfo> listInfos) {
    	this.listInfos = listInfos;
    }
    
    private void setCompteur(int compteur) {
    	this.compteurRel = compteur;
    }
    
    public void flushAll() {
    	this.setCompteur(0);
    	this.setList(new ArrayList<TableInfo>());
    }
    

    public static DataBaseInfo getInstance(){
        if(dbi == null){
            dbi = new DataBaseInfo();
        }
        return dbi;
    }

    public void Init() {
    	String fileName = DBParams.DBPath +"DBInfo.save";
    	File file = new File(fileName);
    	try {
    		/*if(!file.exists()) {
				file.createNewFile();
    		}*/
    		if(file.exists()) {
    			FileInputStream input = new FileInputStream(file);
    		
    			ObjectInputStream ois = new ObjectInputStream(input);
    		
    			DataBaseInfo dbi = (DataBaseInfo) ois.readObject();
    			this.setList(dbi.getList());
    			this.setCompteur(getCompteur());
    			ois.close();
    		}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	

    }

    public void Finish() {
    	String fileName = "DBInfo.save";
    	
    	File file = new File(DBParams.DBPath + fileName);
    	try {
    		FileOutputStream fos = new FileOutputStream(file);
    		ObjectOutputStream oos = new ObjectOutputStream(fos);
    		oos.writeObject(this);
    		oos.close();
    	}catch(FileNotFoundException e) {
    		System.out.println("Fichier non existant !");
    		
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }

    public void addTableInfo(TableInfo tableAdd){
        listInfos.add(tableAdd);
        compteurRel++;
    }
    
    public void affichage() { // Pour les tests 
    	StringBuffer sb = new StringBuffer();
    	if(listInfos!=null) {
    	for(TableInfo tab : listInfos) {
        	String s = tab.getNom();
       		sb.append(s + " " + tab.getNbColonnes() + " \n");
       		if(!tab.getColInfoList().isEmpty()) {
       		for (int i = 0; i < tab.getColInfoList().size(); i++) {
      			sb.append("Nom " + tab.getColInfo(i).GetNomCol());
        		sb.append(" Type " + tab.getColInfo(i).GetTypCol());
        		sb.append(" Taille " + tab.getColInfo(i).getSizeString() + "\n");
        		}
        	}
    	}
      		System.out.println(sb.toString());
    	}else {
    		sb.append(this.getCompteur());
    		System.out.println(sb.toString());
    	}
    }

}
