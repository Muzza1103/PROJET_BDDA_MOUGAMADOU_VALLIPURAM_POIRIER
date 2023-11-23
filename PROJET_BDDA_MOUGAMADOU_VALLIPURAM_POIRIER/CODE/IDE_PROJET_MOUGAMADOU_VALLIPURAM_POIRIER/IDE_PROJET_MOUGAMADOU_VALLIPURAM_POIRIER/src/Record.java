import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Record {
    private TableInfo tabInfo;
    private ArrayList<Object> recvalues;
    //CONSTANTE T
    private static int T;

    public Record(TableInfo tabInfo){
        this.tabInfo = tabInfo;
        this.recvalues = null;
    }

    //on crée un méthode pour extraire les types de la tabinfo
    public ArrayList<String> extraireTypes(TableInfo table){
        ArrayList<String> listeTypes = new ArrayList<>();
        for(int i=0; i<table.getColInfoList().size(); i++){
            if(!listeTypes.contains(table.getColInfo(i).GetTypCol())){
                listeTypes.add(table.getColInfo(i).GetTypCol().getType());
            }
        }
        return listeTypes;
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
        ArrayList<String> types_contenus = new ArrayList<>();
        types_contenus = extraireTypes(tabInfo);

        //Ecrire recvalues dans buffer si pas de varstring
        if(!types_contenus.contains("VARSTRING(T)")){
            
            //on utilise le modèle taille fixe
            for(int i=0; i<recvalues.size(); i++){
                //on vérifie le type de la relation
                //gestion pour STRING
                if(tabInfo.getColInfo(i).GetTypCol().getType().equals("STRING")){
                    
                    buffer.position(pos);

                    buffer.put((byte)recvalues.get(i));

                    pos += T;

                } 
                //gestion pour float & int
                else {
                    buffer.position(pos);
                    
                    if(tabInfo.getColInfo(i).GetTypCol().getType().equals("FLOAT")){
                        float inter_float = (float) recvalues.get(i);
                        buffer.put((byte)inter_float);
                    } 
                    
                    else if (tabInfo.getColInfo(i).GetTypCol().getType() == "INT"){
                        int inter_int = (int) recvalues.get(i);
                        buffer.put((byte)inter_int);
                    }
                    pos += Integer.BYTES;
                }
            }

            taille = pos-taille;
        }

        else {
            //on utilise le modèle taille variable
            ByteBuffer nvbuff;
            //init le offset directory
            buffer.position(0);       
            buffer.put((byte)(recvalues.size()+1));
            
            int pos_index = 1;
            int pos_valeur = recvalues.size()+1;

            int positionLastValue = 0;

            int k;
            
            for(k=0; k<recvalues.size()-1; k++){
                buffer.position(pos_index);
                buffer.put((byte)pos_valeur);

                if(tabInfo.getColInfo(k).GetTypCol().getType().equals("FLOAT")){
                    float inter_float_variable = (float) recvalues.get(k);
                    buffer.position(pos_valeur);
                    buffer.put((byte)inter_float_variable);
                    buffer.position(pos_index);
                    buffer.put((byte)pos_valeur);
                    pos_index ++;
                    pos_valeur += Float.BYTES;
                }

                else if(tabInfo.getColInfo(k).GetTypCol().getType().equals("INT")){
                    int inter_int_variable = (int) recvalues.get(k);
                    buffer.position(pos_valeur);
                    buffer.put((byte)inter_int_variable);
                    buffer.position(pos_index);
                    buffer.put((byte)pos_valeur);
                    pos_index ++;
                    pos_valeur += Integer.BYTES;
                }

                else if(tabInfo.getColInfo(k).GetTypCol().getType().equals("STRING")){
                    buffer.position(pos_valeur);
                    buffer.put((byte)recvalues.get(k));
                    buffer.position(pos_index);
                    buffer.put((byte)pos_valeur);
                    pos_index ++;
                    pos_valeur += T;
                }

                else if(tabInfo.getColInfo(k).GetTypCol().getType().equals("VARSTRING")){
                    String valeur_varstring = (String) recvalues.get(k);
                    buffer.position(pos_valeur);
                    buffer.put((byte)recvalues.get(k));
                    buffer.position(pos_index);
                    buffer.put((byte)pos_valeur);
                    pos_index ++;
                    pos_valeur += valeur_varstring.length();
                }

                positionLastValue = k;

            }
            //dernier element
            if(tabInfo.getColInfo(positionLastValue+1).GetTypCol().getType().equals("VARSTRING")){
                String valeur_varstring = (String) recvalues.get(positionLastValue+1);
                buffer.position(pos_valeur);
                buffer.put((byte)recvalues.get(positionLastValue+1));
                buffer.position(pos_index);
                buffer.put((byte)pos_valeur);
                pos_index ++;
                buffer.position(pos_index);
                buffer.put((byte)(positionLastValue+1+valeur_varstring.length()));
                pos_valeur += valeur_varstring.length();
            } else if(tabInfo.getColInfo(positionLastValue+1).GetTypCol().getType().equals("STRING")){
                buffer.position(pos_valeur);
                buffer.put((byte)recvalues.get(positionLastValue+1));
                buffer.position(pos_index);
                buffer.put((byte)pos_valeur);
                pos_index ++;
                buffer.position(pos_index);
                buffer.put((byte)(positionLastValue+1+T));
            } else if(tabInfo.getColInfo(positionLastValue+1).GetTypCol().getType().equals("INT")){
                int inter_int_variable = (int) recvalues.get(k);
                buffer.position(pos_valeur);
                buffer.put((byte)inter_int_variable);
                buffer.position(pos_index);
                buffer.put((byte)pos_valeur);
                pos_index ++;
                buffer.position(pos_index);
                buffer.put((byte)(positionLastValue+1+Integer.BYTES));
            } else if(tabInfo.getColInfo(positionLastValue+1).GetTypCol().getType().equals("FLOAT")){
                float inter_float_variable = (float) recvalues.get(k);
                buffer.position(pos_valeur);
                buffer.put((byte)inter_float_variable);
                buffer.position(pos_index);
                buffer.put((byte)pos_valeur);
                pos_index ++;
                buffer.position(pos_index);
                buffer.put((byte)(positionLastValue+1+Float.BYTES)); 
            }



            taille = pos_valeur;
        }
        
        return taille;
    }

    public int readFromBuffer(ByteBuffer buff, int pos){
        
        //extraire la liste des types dans la table
        ArrayList<String> types_content = new ArrayList<>();
        types_content = extraireTypes(tabInfo);
        
        //variables pour 
        String intermediaire="";
        int bufferposmove=pos;
        int bufferposz;
        
        int taille=0;
        //vider la liste de valeurs
        if(!recvalues.isEmpty()){
            recvalues.clear();
        }

        //on verifie si il y a des varstrings dans les recvalues
        //si oui, taille variable
        if(types_content.contains("VARSTRING(T)")){
            for(int ite = 0; ite<tabInfo.getColInfoList().size(); ite++){
                if(tabInfo.getColInfo(ite).GetTypCol().getType().contains("STRING")){
                    //on récupère la valeur indiquée à la position pos
                    bufferposz = buff.get(bufferposmove);
                    for(int iteite = 0; iteite<T; iteite++){
                        intermediaire += buff.get(bufferposz);
                        bufferposz++;
                    }
                    recvalues.add(intermediaire);
                    intermediaire="";
                    bufferposmove++;
                }
                if(tabInfo.getColInfo(ite).GetTypCol().getType().contains("INT")){
                    bufferposz = buff.get(bufferposmove);
                    buff.position(bufferposz);
                    recvalues.add(buff.getInt());
                }
            }
        } 
        //si non, taille fixe
        else {
            
            for(int m = 0; m<tabInfo.getColInfoList().size(); m++){
                if(tabInfo.getColInfo(m).GetTypCol().getType().contains("STRING(T)")){
                    for(int ii=0;ii<T; ii++){
                        intermediaire += buff.get(bufferposmove);
                        bufferposmove++;
                    }
                    recvalues.add(intermediaire);
                    intermediaire="";
                    buff.position(bufferposmove+T);
                }
                else if(tabInfo.getColInfo(m).GetTypCol().getType().contains("INT")){
                    recvalues.add(buff.getInt());
                    buff.position(bufferposmove+Integer.BYTES);
                } else if(tabInfo.getColInfo(m).GetTypCol().getType().contains("FLOAT")){
                    recvalues.add(buff.getFloat());
                    buff.position(bufferposmove+Integer.BYTES);
                } else {
                    for(int ii=0;ii<T; ii++){
                        intermediaire += buff.get(bufferposmove);
                        bufferposmove++;
                    }
                    recvalues.add(intermediaire);
                    intermediaire="";
                    buff.position(bufferposmove+T);
                }
            }
        }
        

        

        return taille;
    }
}