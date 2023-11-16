import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Record {
    private static TableInfo tabInfo;
    private static ArrayList<Object> recvalues;

    public Record(TableInfo tabInfo){
        this.tabInfo = tabInfo;
        this.recvalues = new ArrayList<>();
    }
    
    public Object getRecvalues() {
    	return recvalues;
    }
    
    public TableInfo getTabInfo() {
    	return tabInfo;
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
        ArrayList<TypeColonne> typesContenus = new ArrayList<>();
        for (int j=0; j<tabInfo.getColInfoList().size(); j++) {
            typesContenus.add(tabInfo.getColInfo(j).GetTypCol());
        }

        //Ecrire recvalues dans buffer si pas de varstring
        int v = 0;
        for(TypeColonne t : typesContenus) {
        	if(t.getType().equals("VARSTRING")) {
        		v = 1;
        	}
        }
        if(v == 0) {
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
                        float interFloat = (float) recvalues.get(i);
                        buffer.put((byte)interFloat);
                    } 
                    
                    else if (tabInfo.getColInfo(i).GetTypCol().getType().equals("INT")){
                        int interInt = (int) recvalues.get(i);
                        buffer.put((byte)interInt);
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
            
            int posIndex = 1;
            int posValeur = recvalues.size()+1;
            
            for(int k = 0; k<recvalues.size()-1; k++){
                buffer.position(posIndex);
                buffer.put((byte)posValeur);

                if(tabInfo.getColInfo(k).GetTypCol().getType().equals("FLOAT")){
                    float interFloatVariable = (float) recvalues.get(k);
                    buffer.put((byte)interFloatVariable);
                    posIndex ++;
                    posValeur += Float.BYTES;
                }

                else if(tabInfo.getColInfo(k).GetTypCol().getType().equals("INT")){
                    int interIntVariable = (int) recvalues.get(k);
                    buffer.put((byte)interIntVariable);
                    posIndex ++;
                    posValeur += Integer.BYTES;
                }

                else if(tabInfo.getColInfo(k).GetTypCol().getType().equals("STRING")){
                    buffer.put((byte)recvalues.get(k));
                    posIndex ++;
                    posValeur += tabInfo.getColInfo(k).GetT();
                }

                else if(tabInfo.getColInfo(k).GetTypCol().getType().equals("VARSTRING")){
                    String valeur_varstring = (String) recvalues.get(k);
                    buffer.put((byte)recvalues.get(k));
                    posIndex ++;
                    posValeur += valeur_varstring.length();
                }
            }

            //ajouter cas du dernier élément
            taille = posValeur;
        }
        
        return taille;
    }

    public int readFromBuffer(ByteBuffer buff, int pos){
        int taille = 0;
        //vider la liste de valeurs
        if(!recvalues.isEmpty()){
            recvalues.clear();
        }
        return taille;
    }
}
