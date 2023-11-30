import java.io.Serializable;
import java.util.ArrayList;

public class TableInfo implements Serializable{
    private static String nom;
    private static int nbColonnes;
    private static ArrayList <ColInfo> colInfo;
    private static PageId headerPageId;

    public TableInfo(String nom, int nbColonnes,PageId headerPageId){
    	this.nom = nom;
    	this.nbColonnes = nbColonnes;
    	this.colInfo = new ArrayList<>();
    	this.headerPageId=headerPageId;

    }

    public String getNom(){
        return this.nom;
    }
 
    public ColInfo getColInfo(int i){
        return this.colInfo.get(i);
    }

    public ArrayList<ColInfo> getColInfoList(){
        return this.colInfo;
    }
    
    public int getNbColonnes() {
    	return nbColonnes;
    }

    public void setColInfo(ArrayList<ColInfo> l) {
    	colInfo = l;
    }
    
    public PageId getHeaderPageId() {
    	return headerPageId;
    }
    
}
