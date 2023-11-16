import java.util.ArrayList;

public class TableInfo {
    private static String nomRel;
    private static int nbColonnes;
    private static ArrayList <ColInfo> colInfo;

    public TableInfo(){

    }

    public String getNomRel(){
        return this.nomRel;
    }

    public ColInfo getColInfo(int i){
        return this.colInfo.get(i);
    }

    public ArrayList<ColInfo> getColInfoList(){
        return this.colInfo;
    }

}
