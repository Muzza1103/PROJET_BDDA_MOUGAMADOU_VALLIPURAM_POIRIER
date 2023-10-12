import java.util.*;

public class DataBaseInfo {

    private static DataBaseInfo dbi = null;
    private static ArrayList<TableInfo> list_infos;
    private static int compteur_rel;
    
    private DataBaseInfo(){
        this.list_infos = new ArrayList<TableInfo>();
        this.compteur_rel = 0;
    }

    public static DataBaseInfo getInstance(){
        if(dbi == null){
            dbi = new DataBaseInfo();
        }
        return dbi;
    }

    public void Init() {

    }

    public void Finish() {

    }

    //A VERIFIER
    public void AddTableInfo(TableInfo table_add){
        list_infos.add(table_add);
        compteur_rel++;
    }

    public TableInfo GetTableInfo (String nom_relation){
        TableInfo sample_table = new TableInfo();
        for (TableInfo tableInfo : list_infos) {
            if(tableInfo.getNomRel().equals(nom_relation)){
                sample_table = tableInfo;
            }
        }
        return sample_table;
    }
}
