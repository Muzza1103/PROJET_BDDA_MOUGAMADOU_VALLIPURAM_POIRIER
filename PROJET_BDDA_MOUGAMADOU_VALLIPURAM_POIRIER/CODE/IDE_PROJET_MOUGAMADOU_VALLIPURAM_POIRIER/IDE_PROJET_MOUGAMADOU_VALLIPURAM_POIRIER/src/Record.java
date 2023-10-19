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

        else {
            //on utilise le modèle taille variable
            ByteBuffer nvbuff;
            //init le offset directory
            buffer.position(0);       
            buffer.put((byte)(recvalues.size()+1));
            
            int pos_index = 1;
            int pos_valeur = recvalues.size()+1;
            
            for(int k = 0; k<recvalues.size(); k++){
                buffer.position(pos_index);
                buffer.put((byte)pos_valeur);

                if(tabInfo.getColInfo(k).GetTypCol()==FLOAT()){
                    float inter_float_variable = (float) recvalues.get(k);
                    buffer.put((byte)inter_float_variable);
                    pos_index ++;
                    pos_valeur += Float.BYTES;
                }

                else if(tabInfo.getColInfo(k).GetTypCol()==INT()){
                    int inter_int_variable = (int) recvalues.get(k);
                    buffer.put((byte)inter_int_variable);
                    pos_index ++;
                    pos_valeur += Integer.BYTES;
                }

                else if(tabInfo.getColInfo(k).GetTypCol()==STRING(T)){
                    buffer.put((byte)recvalues.get(k));
                    pos_index ++;
                    pos_valeur += T;
                }

                else if(tabInfo.getColInfo(k).GetTypCol() == VARSTRING()){
                    String valeur_varstring = (String) recvalues.get(k);
                    buffer.put((byte)recvalues.get(k));
                    pos_index ++;
                    pos_valeur += valeur_varstring.length();
                }
            }
        }
        taille = T-pos;
        return taille;
    }
}
