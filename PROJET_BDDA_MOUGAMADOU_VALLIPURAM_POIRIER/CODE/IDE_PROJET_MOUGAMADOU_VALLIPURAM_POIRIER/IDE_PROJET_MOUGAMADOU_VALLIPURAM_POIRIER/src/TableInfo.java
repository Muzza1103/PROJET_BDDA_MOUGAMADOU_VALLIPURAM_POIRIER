import java.util.ArrayList;

public class TableInfo {
    private static String nom;
    private static int nbColonnes;
    private static ArrayList <ColInfo> colInfo;

    public TableInfo(String nom, int nbColonnes){
    	this.nom = nom;
    	this.nbColonnes = nbColonnes;
    	this.colInfo = new ArrayList<>();

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
    
}
