import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Record {
    private static TableInfo tabInfo;
    private static ArrayList recvalues;

    public Record(TableInfo tabInfo){
        this.tabInfo = tabInfo;
        this.recvalues = null;
    }

    public int WriteToBuffer(ByteBuffer buffer, int pos){
        int taille = 0;
        if(!recvalues.isEmpty()){
            for (Object object : recvalues) {
                object = null;
            }
        }
        
        //Ecrire recvalues dans buffer si pas de varstring
        if(tabInfo.getColInfo().GetTypCol().contains(STRING(T)) || tabInfo.getColInfo().GetTypCol().contains(VARSTRING(T))){
            //on utilise le modèle 
        }
        return taille;
    }
}
