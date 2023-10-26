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
        int taille = pos;
        /* 
        if(!recvalues.isEmpty()){
            for (Object object : recvalues) {
                object = null;
            }
        }*/
        
        //extraire la liste des types dans la table
        ArrayList<TypeColonne> types_contenus = new ArrayList<>();
        for (int j=0; j<tabInfo.getColInfoList().size(); j++) {
            types_contenus.add(tabInfo.getColInfo(j).GetTypCol());
        }

        //Ecrire recvalues dans buffer si pas de varstring
        int v =0;
        for(TypeColonne t : types_contenus) {
        	if(t.getType().equals("VARSTRING")) {
        		v= 1;
        	}
        }
        if(v==0) {
        //if(!types_contenus.contains(TypeColonne i)){
            //on utilise le modèle taille fixe
            for(int i=0; i<recvalues.size(); i++){
                //on vérifie le type de la relation
                //gestion pour STRING
                if(tabInfo.getColInfo(i).GetTypCol().getType().equals("STRING")){
                    
                    buffer.position(pos);

                    buffer.put((byte)recvalues.get(i));
                    pos += tabInfo.getColInfo(i).GetT();

                } 
                //gestion pour float & int
                else {
                    buffer.position(pos);
                    
                    if(tabInfo.getColInfo(i).GetTypCol().getType().equals("FLOAT")){
                        float inter_float = (float) recvalues.get(i);
                        buffer.put((byte)inter_float);
                    } 
                    
                    else if (tabInfo.getColInfo(i).GetTypCol().getType().equals("INT")){
                        int inter_int = (int) recvalues.get(i);
                        buffer.put((byte)inter_int);
                    }
                    pos += tabInfo.getColInfo(i).GetT();
                }
            }

            taille = pos - taille;
            
        }

        else {
            //on utilise le modèle taille variable
            ByteBuffer nvbuff;
            //init le offset directory
            buffer.position(0);       
            buffer.put((byte)(recvalues.size()+1));
            
            int pos_index = 1;
            int pos_valeur = recvalues.size()+1;
            
            for(int k = 0; k<recvalues.size()-1; k++){
                buffer.position(pos_index);
                buffer.put((byte)pos_valeur);

                if(tabInfo.getColInfo(k).GetTypCol().getType().equals("FLOAT")){
                    float inter_float_variable = (float) recvalues.get(k);
                    buffer.put((byte)inter_float_variable);
                    pos_index ++;
                    pos_valeur += Float.BYTES;
                }

                else if(tabInfo.getColInfo(k).GetTypCol().getType().equals("INT")){
                    int inter_int_variable = (int) recvalues.get(k);
                    buffer.put((byte)inter_int_variable);
                    pos_index ++;
                    pos_valeur += Integer.BYTES;
                }

                else if(tabInfo.getColInfo(k).GetTypCol().getType().equals("STRING")){
                    buffer.put((byte)recvalues.get(k));
                    pos_index ++;
                    pos_valeur += tabInfo.getColInfo(k).GetT();
                }

                else if(tabInfo.getColInfo(k).GetTypCol().getType().equals("VARSTRING")){
                    String valeur_varstring = (String) recvalues.get(k);
                    buffer.put((byte)recvalues.get(k));
                    pos_index ++;
                    pos_valeur += valeur_varstring.length();
                }
            }

            //ajouter cas du dernier élément
            taille = pos_valeur;
        }
        
        return taille;
    }

    public int readFromBuffer(ByteBuffer buff, int pos){
        int taille=0;
        //vider la liste de valeurs
        if(!recvalues.isEmpty()){
            recvalues.clear();
        }
        

        

        return taille;
    }
}
