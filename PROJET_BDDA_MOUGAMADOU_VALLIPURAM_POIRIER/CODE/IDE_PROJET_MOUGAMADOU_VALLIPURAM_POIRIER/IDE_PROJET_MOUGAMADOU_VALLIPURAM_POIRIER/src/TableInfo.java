import java.util.ArrayList;

public class TableInfo {
    private static String nom_rel;
    private static int nb_colonnes;
    private static ArrayList <ColInfo> col_info;

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
