import java.nio.Buffer;

public class RecordIterator {
    private PageId pageID;
    private TableInfo tabInfo;

    public RecordIterator(TableInfo tabInfo, PageId pageID){
        this.pageID = pageID;
        this.tabInfo = tabInfo;
        //demander la page de données au BufferManager
        //position du curseur de l'itérateur au début de la page de données
    }
    
    public Record getNextRecord(){
        Record record = new Record();
        return record;
    }

    public void close(){
        int val = 1;
        //signale qu'on utilise plus l'itérateur 
        //libère la page de donnée auprès du BM
        
    }

    public void reset(){
        //positionne le curseur au début de la page, sans effectuer d'action sur le BM
    }
}
