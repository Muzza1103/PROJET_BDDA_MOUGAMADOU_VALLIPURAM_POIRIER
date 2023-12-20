import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class Record {
    private TableInfo tabInfo;
    private ArrayList<Object> recvalues;
    //CONSTANTE T
    //private static int T = 20;

    public Record(){

    }

    public Record(TableInfo tabInfo){
        this.tabInfo = tabInfo;
        this.recvalues = null;
    }

    public void InsertValues(ArrayList<Object> recvalues){
        this.recvalues = recvalues;
    }

    //on crée un méthode pour extraire les types de la tabinfo
    public ArrayList<String> extraireTypes(TableInfo table){
        ArrayList<String> listeTypes = new ArrayList<>();
        for(int i=0; i<table.getColInfoList().size(); i++){
            //if(!listeTypes.contains(table.getColInfo(i).GetTypCol())){
                listeTypes.add(table.getColInfo(i).GetTypCol());
            //}
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

        boolean contient_var = false;
        for(int ite_type_cont = 0; ite_type_cont<types_contenus.size();ite_type_cont++){
            if(types_contenus.get(ite_type_cont).contains("VAR")){
                contient_var = true;
            }
        }

        //Ecrire recvalues dans buffer si pas de varstring
        if(!contient_var){
            
            //on utilise le modèle taille fixe
            for(int i=0; i<recvalues.size(); i++){
                //on vérifie le type de la relation
                //gestion pour STRING
                if(tabInfo.getColInfo(i).GetTypCol().contains("STRING") && !tabInfo.getColInfo(i).GetTypCol().contains("VAR")){
                    System.out.println("wr_STRING_taillefixe");
                    String valeur_string = (String) recvalues.get(i);
                    writeInBufferString(buffer, pos, valeur_string);
                    pos += tabInfo.getColInfo(i).getSizeString();
                 
                } 

                //gestion pour float & int
                else {                    
                    if(tabInfo.getColInfo(i).GetTypCol().equals("FLOAT")){
                        float inter_float = (float) recvalues.get(i);
                        writeInBufferFloat(buffer, pos, inter_float);
                        
                    } 
                    else if (tabInfo.getColInfo(i).GetTypCol().equals("INT")){
                        System.out.println("On passe par la nouvelle commande");
                        int inter_int = (int) recvalues.get(i);
                        writeInBufferInt(buffer, pos, inter_int);
                        
                    }
                    pos += Integer.BYTES;
                }
            }

            taille = pos-taille;
        }

        else {
            System.out.println("writebuffer_passage_taille_var");
            //on utilise le modèle taille variable
            //ByteBuffer nvbuff;
            //init le offset directory
            buffer.position(pos);       
            //buffer.put((byte)(recvalues.size()*4+4));
            
            int pos_debut= pos;
            int pos_valeur = recvalues.size()*4+4;

            int positionLastValue = 0;

            int tabPositions[] = new int[recvalues.size()];
            int k;
            int position_valeur = pos + recvalues.size()*4 ;
            
            //boucle pour insérer les valeurs de positions
            /*
            buffer.position(pos);
            buffer.putInt(position_valeur);
            buffer.position(pos+=4);
            tabPositions[0] = position_valeur;
            for(int iteration=1; iteration<recvalues.size();iteration++){
                //On cherche la taille du String ici pour le garder en mémoire
                int T = tabInfo.getColInfo(iteration).getSizeString();

                if(tabInfo.getColInfo(iteration).GetTypCol().equals("FLOAT")||tabInfo.getColInfo(iteration).GetTypCol().equals("INT")){
                    buffer.putInt(position_valeur+=4);
                    buffer.position(pos+=4);
                    tabPositions[iteration] = position_valeur;
                }
                else if(tabInfo.getColInfo(iteration).GetTypCol().contains("STRING") && !tabInfo.getColInfo(iteration).GetTypCol().contains("VAR")){
                    buffer.putInt(position_valeur+=T);
                    buffer.position(pos+=4);
                    tabPositions[iteration] = position_valeur;

                } else if(tabInfo.getColInfo(iteration).GetTypCol().contains("VARSTRING")){
                    String elementAInserer = (String) recvalues.get(iteration);
                    buffer.putInt(position_valeur+=elementAInserer.length());
                    System.out.println(elementAInserer.length());
                    buffer.position(pos+=4);
                    tabPositions[iteration] = position_valeur;
                }
            }*/
            
            buffer.position(pos);
            //buffer.putInt(position_valeur);
            //buffer.position(pos+=4);
            //tabPositions[0] = position_valeur;
            for(int iteration=0; iteration<recvalues.size();iteration++){
                //On cherche la taille du String ici pour le garder en mémoire
                int T = tabInfo.getColInfo(iteration).getSizeString();

                if(tabInfo.getColInfo(iteration).GetTypCol().equals("FLOAT")||tabInfo.getColInfo(iteration).GetTypCol().equals("INT")){
                    buffer.putInt(position_valeur);
                    buffer.position(pos+=4);
                    tabPositions[iteration] = position_valeur;
                    position_valeur+=4;
                }
                else if(tabInfo.getColInfo(iteration).GetTypCol().contains("STRING") && !tabInfo.getColInfo(iteration).GetTypCol().contains("VAR")){
                    buffer.putInt(position_valeur);
                    buffer.position(pos+=4);
                    System.out.println("kdfjslkfsfkjsdlfk");
                    tabPositions[iteration] = position_valeur;
                    position_valeur+=T;

                } else if(tabInfo.getColInfo(iteration).GetTypCol().contains("VARSTRING")){
                    String elementAInserer = (String) recvalues.get(iteration);
                    buffer.putInt(position_valeur);
                    buffer.position(pos+=4);
                    tabPositions[iteration] = position_valeur;
                    position_valeur+=elementAInserer.length();
                }
            }
            /*
            buffer.position(pos);
            buffer.putInt(position_valeur);
            buffer.position(pos+=4);
            tabPositions[0] = position_valeur;
            for(int iteration=0; iteration<recvalues.size()-1;iteration++){
                //On cherche la taille du String ici pour le garder en mémoire
                int T = tabInfo.getColInfo(iteration).getSizeString();

                if(tabInfo.getColInfo(iteration).GetTypCol().equals("FLOAT")||tabInfo.getColInfo(iteration).GetTypCol().equals("INT")){
                    buffer.putInt(position_valeur+=4);
                    buffer.position(pos+=4);
                    tabPositions[iteration+1] = position_valeur;
                }
                else if(tabInfo.getColInfo(iteration).GetTypCol().contains("STRING") && !tabInfo.getColInfo(iteration).GetTypCol().contains("VAR")){
                    buffer.putInt(position_valeur+=T);
                    buffer.position(pos+=4);
                    tabPositions[iteration+1] = position_valeur;

                } else if(tabInfo.getColInfo(iteration).GetTypCol().contains("VARSTRING")){
                    String elementAInserer = (String) recvalues.get(iteration);
                    buffer.putInt(position_valeur+=elementAInserer.length());
                    System.out.println(elementAInserer.length());
                    buffer.position(pos+=4);
                    tabPositions[iteration+1] = position_valeur;
                }
            }*/
            
            /*
            //gestion du dernier element
            if(tabInfo.getColInfo(recvalues.size()-1).GetTypCol().equals("FLOAT") || tabInfo.getColInfo(recvalues.size()-1).GetTypCol().equals("INT")){
                buffer.putInt(position_valeur+=4);
            } else if (tabInfo.getColInfo(recvalues.size()-1).GetTypCol().contains("STRING") && !tabInfo.getColInfo(recvalues.size()-1).GetTypCol().contains("VAR")){
                buffer.putInt(position_valeur+tabInfo.getColInfo(recvalues.size()-1).getSizeString());
            } else if (tabInfo.getColInfo(recvalues.size()-1).GetTypCol().contains("VARSTRING")){
                String elementAInserer = (String) recvalues.get(recvalues.size()-1);
                buffer.putInt(position_valeur+elementAInserer.length());
            }
            */
          //gestion du dernier element
            if(tabInfo.getColInfo(recvalues.size()-1).GetTypCol().equals("FLOAT") || tabInfo.getColInfo(recvalues.size()-1).GetTypCol().equals("INT")){
                buffer.putInt(position_valeur);
            } else if (tabInfo.getColInfo(recvalues.size()-1).GetTypCol().contains("STRING") && !tabInfo.getColInfo(recvalues.size()-1).GetTypCol().contains("VAR")){
                buffer.putInt(position_valeur);
            } else if (tabInfo.getColInfo(recvalues.size()-1).GetTypCol().contains("VARSTRING")){
                //String elementAInserer = (String) recvalues.get(recvalues.size()-1);
                buffer.putInt(position_valeur);
            }

            System.out.println(tabPositions.toString());
            
            for(k=0; k<recvalues.size(); k++){
                System.out.println("envoi info");
                //buffer.position(pos_index);
                //buffer.put((byte)pos_valeur);

                int positionInsertion = 0;



                if(tabInfo.getColInfo(k).GetTypCol().equals("FLOAT")){
                    System.out.println("writebuffer_FLOAT_taillevar");
                    float inter_float_variable = (float) recvalues.get(k);
                    positionInsertion = tabPositions[k];
                    writeInBufferFloat(buffer, positionInsertion, inter_float_variable);
                }

                else if(tabInfo.getColInfo(k).GetTypCol().equals("INT")){
                    System.out.println("PASSAGE PAR INT//TAILLE VARIABLE");
                    int inter_int_variable = (int) recvalues.get(k);
                    positionInsertion = tabPositions[k];
                    writeInBufferInt(buffer, positionInsertion, inter_int_variable);
                }

                else if(tabInfo.getColInfo(k).GetTypCol().contains("STRING") && !tabInfo.getColInfo(k).GetTypCol().contains("VAR")){
                    System.out.println("wr_STRING_taillevar");
                    String valeur_string = (String) recvalues.get(k);
                    positionInsertion = tabPositions[k];
                    writeInBufferString(buffer, positionInsertion, valeur_string);
                }

                else if(tabInfo.getColInfo(k).GetTypCol().contains("VARSTRING")){
                    System.out.println("writebuffer_VARSTRING_taillevariable");
                    String valeur_varstring = (String) recvalues.get(k);
                    positionInsertion = tabPositions[k];
                    writeInBufferString(buffer, positionInsertion, valeur_varstring);
                }

            }

            taille = buffer.position()-pos_debut;
        }
        //printBuffer(buffer);
        return taille;
    }

    public void writeInBufferInt(ByteBuffer buff, int pos, int aEcrire){
        System.out.println("écrit dans le buffer:"+ aEcrire);
        try {
            buff.position(pos);
            buff.putInt(aEcrire);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Le int "+aEcrire+" a causé une erreur");
            System.out.println(e);
        }
    }

    public void writeInBufferFloat(ByteBuffer buff, int pos, float aEcrire){
        System.out.println("écrit dans le buffer:"+ aEcrire);
        try{
            buff.position(pos);
            buff.putFloat(aEcrire);
        } catch (Exception e){
            System.out.println("Le float "+aEcrire+" a causé une erreur");
            System.out.println(e);
        }
    }

    //Cette classe permet d'écrire un String dans le buffer
    public void writeInBufferString(ByteBuffer buff, int pos, String aEcrire){
        System.out.println("écrit dans le buffer:"+aEcrire);
        try {
            buff.position(pos);
            byte[] tabByte = aEcrire.getBytes();
            buff.put(tabByte);
            
            /*
            for(int wr_str_tfix = 0; wr_str_tfix<aEcrire.length(); wr_str_tfix++){
                buff.position(pos);
                buff.putChar(aEcrire.charAt(wr_str_tfix));
                pos++;
                //System.out.print("caractère écrit: "+aEcrire.charAt(wr_str_tfix)+" ");
            } 
            
            */   
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Le string "+aEcrire+" a causé une erreur");
            System.out.println(e);
        }
    }

    public int readFromBuffer(ByteBuffer buff, int pos){
        
        //extraire la liste des types dans la table
        ArrayList<String> types_content = new ArrayList<>();
        types_content = extraireTypes(tabInfo);

        boolean contient_var = false;
        for(int ite_type_cont = 0; ite_type_cont<types_content.size();ite_type_cont++){
            if(types_content.get(ite_type_cont).contains("VAR")){
                contient_var = true;
            }
        }
        
        //variables pour 
        String intermediaire="";
        int bufferposmove=pos;
        int bufferposz;
        
        int taille=0;
        if(recvalues==null) {
        	recvalues = new ArrayList<Object>();
        }
        //vider la liste de valeurs
        if(!recvalues.isEmpty()){
            recvalues.clear();
        }

        //on verifie si il y a des varstrings dans les recvalues
        //si oui, taille variable
        if(contient_var){
            for(int ite = 0; ite<tabInfo.getColInfoList().size(); ite++){
                //on donne à la valeur T la taille du string
                int T = tabInfo.getColInfo(ite).getSizeString();

                if(tabInfo.getColInfo(ite).GetTypCol().contains("STRING") && !tabInfo.getColInfo(ite).GetTypCol().contains("VAR")){
                    //on récupère la valeur indiquée à la position pos
                	
                	bufferposz = buff.get(bufferposmove);
                	buff.position(bufferposz);
                	byte[] string = new byte[T];
                	buff.get(string);
                	intermediaire = new String(string);
                	recvalues.add(intermediaire);
                	System.out.println("Valeur intérmédiaire:" +intermediaire);
                	bufferposmove++;
                	
                	/*
                    bufferposz = buff.get(bufferposmove);
                    for(int iteite = 0; iteite<T; iteite++){
                        intermediaire += buff.getChar(bufferposz);
                        bufferposz++;
                    }
                    System.out.println("Valeur intérmédiaire:" +intermediaire);
                    recvalues.add(intermediaire);
                    intermediaire="";
                    bufferposmove++;
               	*/
               	}	
                
                if(tabInfo.getColInfo(ite).GetTypCol().contains("INT")){
                    bufferposz = buff.get(bufferposmove);
                    buff.position(bufferposz);
                    recvalues.add(buff.getInt());
                    
                    bufferposmove++;
                }
                if(tabInfo.getColInfo(ite).GetTypCol().contains("FLOAT")){
                    bufferposz = buff.get(bufferposmove);
                    buff.position(bufferposz);
                    recvalues.add(buff.getFloat());
                    
                    bufferposmove++;
                }if(tabInfo.getColInfo(ite).GetTypCol().contains("VARSTRING")){
                	int taille_du_varstring;
                	bufferposz = buff.get(bufferposmove);
                	buff.position(bufferposz);
                	//if (ite<tabInfo.getColInfoList().size()-1) {
                		taille_du_varstring = buff.getInt(bufferposmove+1)-bufferposz;
                	//}else {
                		taille_du_varstring = tabInfo.getColInfo(ite).getSizeString();
                	//}
                	byte[] string = new byte[taille_du_varstring];
                	buff.position(bufferposz);
                	buff.get(string);
                	intermediaire = new String(string);
                	recvalues.add(intermediaire);
                	System.out.println("Valeur intérmédiaire:" +intermediaire);
                	bufferposmove++;
                	
                	/*
                    bufferposz = buff.get(bufferposmove);
                    buff.position(bufferposz);
                    int taille_init_var = bufferposz;
                    bufferposmove++;
                    int taille_du_varstring = buff.get(bufferposmove);
                    for(int iteite = 0; iteite<taille_du_varstring - taille_init_var; iteite++){
                        intermediaire += buff.get(bufferposz);
                        //bufferposz++;
                    }
                    recvalues.add(intermediaire);
                    intermediaire = "";
                    //bufferposmove++;
                     */
                }
            }
        } 
        //si non, taille fixe
        else {
            
            for(int m = 0; m<tabInfo.getColInfoList().size(); m++){
                //T = taillestring
                int T = tabInfo.getColInfo(m).getSizeString();

                if(tabInfo.getColInfo(m).GetTypCol().contains("STRING(T)")){
                    readStringFromBuffer(buff, bufferposmove, T);
                    bufferposmove+=T;
                }
                else if(tabInfo.getColInfo(m).GetTypCol().contains("INT")){
                    readIntFromBuffer(buff, bufferposmove);
                    bufferposmove++;
                } 
                else if(tabInfo.getColInfo(m).GetTypCol().contains("FLOAT")){
                    readFloatFromBuffer(buff, bufferposmove);
                    bufferposmove++;
                } /*else {
                    for(int ii=0;ii<T; ii++){
                        intermediaire += buff.get(bufferposmove);
                        bufferposmove++;
                    }
                    recvalues.add(intermediaire);
                    intermediaire="";
                    buff.position(bufferposmove+T);
                }*/
            }
        }
        System.out.println("recvalues: "+recvalues);
        return taille;
    }

    public void readIntFromBuffer(ByteBuffer buffer, int pos){
        try {
            int inter = buffer.getInt(pos);
            recvalues.add(inter);
            System.out.println("Int intégré à recvalues :"+inter);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Exception caused by :"+e);
        }
    }

    public void readFloatFromBuffer(ByteBuffer buffer, int pos){
        try {
            float inter = buffer.getFloat(pos);
            recvalues.add(inter);
            System.out.println("Float intégré à recvalues :"+inter);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Exception caused by :"+e);
        }
    }
    
    public void readStringFromBuffer(ByteBuffer buffer, int pos, int taille){
        try {
        	buffer.position(4096);
        	buffer.flip();
        	buffer.position(pos);
        	byte[] string = new byte[taille];
        	buffer.get(string);
        	String inter = new String(string);
        	recvalues.add(inter);
        	System.out.println("Valeur intérmédiaire:" +inter);
        	
        	
        	/*
            String inter = "";
            for(int i = 0; i<taille; i++){
                inter += buffer.getChar(pos);
                pos++;
            }
            recvalues.add(inter);
            System.out.println("String intégré à recvalues :"+inter);
            */
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Exception caused by :"+e);
        }
    }

    //récupéré sur www.java2s.com pour les tests
    public static void printBuffer(ByteBuffer buffer) {
    	buffer.position(0);
    	/*
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        System.out.println((String) Arrays.toString(bytes));
        */
    	while (buffer.hasRemaining()) {
            int element = buffer.getInt();
            System.out.print(element+" ");
        }
    }

    TableInfo getTabInfoRecord() {
    	return this.tabInfo;
    }
    
    public ArrayList<Object> getRecValues(){
    	return recvalues;
    }
}
