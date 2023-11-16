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

    }

    public void Finish() {

    }

    //A VERIFIER
    public void AddTableInfo(TableInfo tableAdd){
        listInfos.add(tableAdd);
        compteurRel++;
    }

    public TableInfo GetTableInfo (String nomRelation){
        TableInfo sampleTable = new TableInfo();
        for (TableInfo tableInfo : listInfos) {
            if(tableInfo.getNomRel().equals(nomRelation)){
                sampleTable = tableInfo;
            }
        }
        return sampleTable;
    }
}
