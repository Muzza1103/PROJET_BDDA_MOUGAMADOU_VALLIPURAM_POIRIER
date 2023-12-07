import java.util.ArrayList;

public class TableInfo {
    private String nom_rel;
    private int nb_colonnes;
    private ArrayList <ColInfo> col_info;

    public TableInfo(String nom_rel, int nb_colonnes, ArrayList<ColInfo> col_info){
        this.nom_rel = nom_rel;
        this.nb_colonnes = nb_colonnes;
        this.col_info = col_info;
    }

    public TableInfo(){
        
    }

    public String getNomRel(){
        return this.nom_rel;
    }

    public ColInfo getColInfo(int i){
        return this.col_info.get(i);
    }

    public ArrayList<ColInfo> getColInfoList(){
        return this.col_info;
    }

}
