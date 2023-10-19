import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Record {
    private static TableInfo tabInfo;
    private static ArrayList recvalues;
    //CONSTANTE T
    private static int T;

    public Record(TableInfo tabInfo){
        this.tabInfo = tabInfo;
        this.recvalues = null;
    }

    public int WriteToBuffer(ByteBuffer buffer, int pos){
        int taille = pos;
        /* 
        if(!recvalues.isEmpty()){
            for (Object object : recvalues) {
                object = null;
            }
        }*/
        
        //extraire la liste des types dans la table
        ArrayList<String> types_contenus;
        for (int j=0; j<tabInfo.getColInfoList().size(); j++) {
            types_contenus.add(tabInfo.getColInfo(j).GetTypCol());
        }

        //Ecrire recvalues dans buffer si pas de varstring
        if(!types_contenus.contains(VARSTRING(T))){
            //on utilise le modèle taille fixe
            for(int i=0; i<recvalues.size(); i++){
                //on vérifie le type de la relation
                //gestion pour STRING
                if(tabInfo.getColInfo(i).GetTypCol() == STRING(T)){
                    
                    buffer.position(pos);

                    buffer.put((byte)recvalues.get(i));

                    pos += T;

                } 
                //gestion pour float & int
                else {
                    buffer.position(pos);
                    
                    if(tabInfo.getColInfo(i).GetTypCol == FLOAT()){
                        float inter_float = (float) recvalues.get(i);
                        buffer.put((byte)inter_float);
                    } 
                    
                    else if (tabInfo.getColInfo(i).GetTypCol() == INT()){
                        int inter_int = (int) recvalues.get(i);
                        buffer.put((byte)inter_int);
                    }
                    pos += T;
                }
            }
        }

        taille = T-pos;
        return taille;
    }
}
