import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class Record {
    private TableInfo tabInfo;
    private ArrayList<Object> recvalues;
    //CONSTANTE T
    private static int T = 3;

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
            if(!listeTypes.contains(table.getColInfo(i).GetTypCol())){
                listeTypes.add(table.getColInfo(i).GetTypCol());
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
                System.out.println("ICI LA VALEUR i à RECORD:52");
                System.out.println(i);
                if(tabInfo.getColInfo(i).GetTypCol().equals("STRING(T)")){
                    System.out.println("wr_STRING_taillefixe");
                    String valeur_string = (String) recvalues.get(i);
                    writeInBufferString(buffer, pos, valeur_string);
                    pos += T;
                } 

                //gestion pour float & int
                else {                    
                    if(tabInfo.getColInfo(i).GetTypCol() == "FLOAT"){
                        float inter_float = (float) recvalues.get(i);
                        writeInBufferFloat(buffer, pos, inter_float);
                        pos++;
                    } 
                    else if (tabInfo.getColInfo(i).GetTypCol() == "INT"){
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
            ByteBuffer nvbuff;
            //init le offset directory
            buffer.position(0);       
            buffer.put((byte)(recvalues.size()+1));
            
            int pos_index = 1;
            int pos_valeur = recvalues.size()+1;

            int positionLastValue = 0;

            int tabPositions[] = new int[recvalues.size()];
            int k;
            int position_valeur = recvalues.size()+1;
            //boucle pour insérer les valeurs de positions
            buffer.position(pos);
            buffer.putInt(position_valeur);
            buffer.position(pos++);
            tabPositions[0] = position_valeur;
            for(int iteration=1; iteration<recvalues.size();iteration++){
                if(tabInfo.getColInfo(iteration).GetTypCol()=="FLOAT"||tabInfo.getColInfo(iteration).GetTypCol()=="INT"){
                    buffer.putInt(position_valeur++);
                    buffer.position(pos++);
                    tabPositions[iteration] = position_valeur;
                }
                else if(tabInfo.getColInfo(iteration).GetTypCol()=="STRING(T)"){
                    buffer.putInt(position_valeur+T);
                    buffer.position(pos+T);
                    tabPositions[iteration] = position_valeur;
                } else if(tabInfo.getColInfo(iteration).GetTypCol()=="VARSTRING(T)"){
                    String elementAInserer = (String) recvalues.get(iteration);
                    buffer.putInt(position_valeur+elementAInserer.length());
                    buffer.position(pos+elementAInserer.length());
                    tabPositions[iteration] = position_valeur;
                }
            }
            //gestion du dernier element
            if(tabInfo.getColInfo(recvalues.size()-1).GetTypCol()=="FLOAT" || tabInfo.getColInfo(recvalues.size()-1).GetTypCol()=="INT"){
                buffer.putInt(position_valeur++);
            } else if (tabInfo.getColInfo(recvalues.size()-1).GetTypCol()=="STRING(T)"){
                buffer.putInt(position_valeur+T);
            } else if (tabInfo.getColInfo(recvalues.size()-1).GetTypCol()=="VARSTRING(T)"){
                String elementAInserer = (String) recvalues.get(recvalues.size()-1);
                buffer.putInt(position_valeur+elementAInserer.length());
            }

            System.out.println(tabPositions.toString());
            
            for(k=0; k<recvalues.size(); k++){
                System.out.println("envoi info");
                buffer.position(pos_index);
                buffer.put((byte)pos_valeur);

                int positionInsertion = 0;



                if(tabInfo.getColInfo(k).GetTypCol()=="FLOAT"){
                    System.out.println("writebuffer_FLOAT_taillevar");
                    float inter_float_variable = (float) recvalues.get(k);
                    positionInsertion = tabPositions[k];
                    writeInBufferFloat(buffer, positionInsertion, inter_float_variable);
                }

                else if(tabInfo.getColInfo(k).GetTypCol()=="INT"){
                    System.out.println("PASSAGE PAR INT//TAILLE VARIABLE");
                    int inter_int_variable = (int) recvalues.get(k);
                    positionInsertion = tabPositions[k];
                    writeInBufferInt(buffer, positionInsertion, inter_int_variable);
                }

                else if(tabInfo.getColInfo(k).GetTypCol()=="STRING(T)"){
                    System.out.println("wr_STRING_taillevar");
                    String valeur_string = (String) recvalues.get(k);
                    positionInsertion = tabPositions[k];
                    writeInBufferString(buffer, positionInsertion, valeur_string);
                }

                else if(tabInfo.getColInfo(k).GetTypCol() == "VARSTRING(T)"){
                    System.out.println("writebuffer_VARSTRING_taillevariable");
                    String valeur_varstring = (String) recvalues.get(k);
                    positionInsertion = tabPositions[k];
                    writeInBufferString(buffer, positionInsertion, valeur_varstring);
                }

            }

            taille = pos_valeur;
        }
        printBuffer(buffer);
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

    public void writeInBufferString(ByteBuffer buff, int pos, String aEcrire){
        System.out.println("écrit dans le buffer:"+aEcrire);
        try {
            buff.position(pos);
            for(int wr_str_tfix = 0; wr_str_tfix<aEcrire.length(); wr_str_tfix++){
                buff.putChar(aEcrire.charAt(wr_str_tfix));
                //System.out.print("caractère écrit: "+aEcrire.charAt(wr_str_tfix)+" ");
            }    
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
                if(tabInfo.getColInfo(ite).GetTypCol().contains("STRING(T)")){
                    //on récupère la valeur indiquée à la position pos
                    bufferposz = buff.get(bufferposmove);
                    for(int iteite = 0; iteite<T; iteite++){
                        intermediaire += buff.getChar(bufferposz);
                        bufferposz++;
                    }
                    System.out.println("Valeur intérmédiaire:" +intermediaire);
                    recvalues.add(intermediaire);
                    intermediaire="";
                    bufferposmove++;
                }
                if(tabInfo.getColInfo(ite).GetTypCol().contains("INT")){
                    bufferposz = buff.get(bufferposmove);
                    buff.position(bufferposz);
                    recvalues.add(buff.getInt());
                }
                if(tabInfo.getColInfo(ite).GetTypCol().contains("FLOAT")){
                    bufferposz = buff.get(bufferposmove);
                    buff.position(bufferposz);
                    recvalues.add(buff.getFloat());
                }if(tabInfo.getColInfo(ite).GetTypCol().contains("VARSTRING(T)")){
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
                }
            }
        } 
        //si non, taille fixe
        else {
            
            for(int m = 0; m<tabInfo.getColInfoList().size(); m++){
                if(tabInfo.getColInfo(m).GetTypCol().contains("STRING(T)")){
                    for(int ii=0;ii<T; ii++){
                        intermediaire += buff.getChar(bufferposmove);
                        bufferposmove++;
                    }
                    System.out.println("valeur string: "+intermediaire);
                    recvalues.add(intermediaire);
                    intermediaire="";
                    buff.position(bufferposmove+T);
                }
                else if(tabInfo.getColInfo(m).GetTypCol().contains("INT")){
                    int inter_int_fix_read = buff.getInt(bufferposmove);
                    recvalues.add(inter_int_fix_read);
                    buff.position(bufferposmove+Integer.BYTES);
                } else if(tabInfo.getColInfo(m).GetTypCol().contains("FLOAT")){
                    recvalues.add(buff.getFloat());
                    buff.position(bufferposmove+Integer.BYTES);
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
        return taille;
    }

    //récupéré sur www.java2s.com pour les tests
    public static void printBuffer(ByteBuffer buffer) {
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        System.out.println((String) Arrays.toString(bytes));
    }
}
