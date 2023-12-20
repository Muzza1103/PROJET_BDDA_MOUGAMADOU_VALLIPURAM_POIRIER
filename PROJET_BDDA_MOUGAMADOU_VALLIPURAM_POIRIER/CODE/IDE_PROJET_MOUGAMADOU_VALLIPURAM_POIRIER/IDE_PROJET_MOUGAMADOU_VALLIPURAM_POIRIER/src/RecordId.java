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

    
    public int WriteToBuffer(ByteBuffer buffer, int pos) {
    int taille = pos;

    ArrayList<String> types_contenus = extraireTypes(tabInfo);
    boolean contient_var = types_contenus.stream().anyMatch(type -> type.contains("VAR"));

    if (!contient_var) {
        for (int i = 0; i < recvalues.size(); i++) {
            if (tabInfo.getColInfo(i).GetTypCol().contains("STRING") && !tabInfo.getColInfo(i).GetTypCol().contains("VAR")) {
                String valeur_string = (String) recvalues.get(i);
                writeInBufferString(buffer, pos, valeur_string);
                pos += tabInfo.getColInfo(i).getSizeString();
            } else {
                if (tabInfo.getColInfo(i).GetTypCol().equals("FLOAT")) {
                    float inter_float = (float) recvalues.get(i);
                    writeInBufferFloat(buffer, pos, inter_float);
                } else if (tabInfo.getColInfo(i).GetTypCol().equals("INT")) {
                    int inter_int = (int) recvalues.get(i);
                    writeInBufferInt(buffer, pos, inter_int);
                }
                pos += Integer.BYTES;
            }
        }
        taille = pos - taille;
    } else {
        int[] tabPositions = new int[recvalues.size()];

        for (int i = 0; i < recvalues.size(); i++) {
            int T = tabInfo.getColInfo(i).getSizeString();

            if (tabInfo.getColInfo(i).GetTypCol().equals("FLOAT") || tabInfo.getColInfo(i).GetTypCol().equals("INT")) {
                tabPositions[i] = pos;
                pos += 4;
            } else if (tabInfo.getColInfo(i).GetTypCol().contains("STRING") && !tabInfo.getColInfo(i).GetTypCol().contains("VAR")) {
                tabPositions[i] = pos;
                pos += T;
            } else if (tabInfo.getColInfo(i).GetTypCol().contains("VARSTRING")) {
                String elementAInserer = (String) recvalues.get(i);
                tabPositions[i] = pos;
                pos += elementAInserer.length();
            }
        }

        for (int k = 0; k < recvalues.size(); k++) {
            int positionInsertion = tabPositions[k];

            if (tabInfo.getColInfo(k).GetTypCol().equals("FLOAT")) {
                float inter_float_variable = (float) recvalues.get(k);
                writeInBufferFloat(buffer, positionInsertion, inter_float_variable);
            } else if (tabInfo.getColInfo(k).GetTypCol().equals("INT")) {
                int inter_int_variable = (int) recvalues.get(k);
                writeInBufferInt(buffer, positionInsertion, inter_int_variable);
            } else if (tabInfo.getColInfo(k).GetTypCol().contains("STRING") && !tabInfo.getColInfo(k).GetTypCol().contains("VAR")) {
                String valeur_string = (String) recvalues.get(k);
                writeInBufferString(buffer, positionInsertion, valeur_string);
            } else if (tabInfo.getColInfo(k).GetTypCol().contains("VARSTRING")) {
                String valeur_varstring = (String) recvalues.get(k);
                writeInBufferString(buffer, positionInsertion, valeur_varstring);
            }
        }

        taille = buffer.position() - taille;
    }

    return taille;
}
    
    public void writeInBufferInt(ByteBuffer buff, int pos, int aEcrire){
        System.out.println("écrit dans le buffer:"+ aEcrire);
        try {
            buff.position(pos);
            buff.putInt(aEcrire);
        } catch (Exception e) {
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
            byte[] tabByte = aEcrire.getBytes();
            buff.put(tabByte);
        } catch (Exception e) {
            System.out.println("Le string "+aEcrire+" a causé une erreur");
            System.out.println(e);
        }
    }

    public int readFromBuffer(ByteBuffer buff, int pos) {
        ArrayList<String> types_content = extraireTypes(tabInfo);
        boolean contient_var = types_content.stream().anyMatch(type -> type.contains("VAR"));

        if (recvalues == null) {
            recvalues = new ArrayList<>();
        }

        recvalues.clear(); // Vider la liste de valeurs

        int bufferposmove = pos;

        if (contient_var) {
            for (int ite = 0; ite < tabInfo.getColInfoList().size(); ite++) {
                if (tabInfo.getColInfo(ite).GetTypCol().contains("STRING") && !tabInfo.getColInfo(ite).GetTypCol().contains("VAR")) {
                    int bufferposz = buff.get(bufferposmove);
                    buff.position(bufferposz);
                    int T = tabInfo.getColInfo(ite).getSizeString();
                    byte[] stringBytes = new byte[T];
                    buff.get(stringBytes);
                    String intermediaire = new String(stringBytes);
                    recvalues.add(intermediaire);
                    bufferposmove++;
                } else if (tabInfo.getColInfo(ite).GetTypCol().contains("INT")) {
                    int bufferposz = buff.get(bufferposmove);
                    buff.position(bufferposz);
                    recvalues.add(buff.getInt());
                    bufferposmove++;
                } else if (tabInfo.getColInfo(ite).GetTypCol().contains("FLOAT")) {
                    int bufferposz = buff.get(bufferposmove);
                    buff.position(bufferposz);
                    recvalues.add(buff.getFloat());
                    bufferposmove++;
                } else if (tabInfo.getColInfo(ite).GetTypCol().contains("VARSTRING")) {
                    int bufferposz = buff.get(bufferposmove);
                    buff.position(bufferposz);
                    int taille_du_varstring = buff.getInt(bufferposmove + 1) - bufferposz;
                    byte[] stringBytes = new byte[taille_du_varstring];
                    buff.get(stringBytes);
                    String intermediaire = new String(stringBytes);
                    recvalues.add(intermediaire);
                    bufferposmove++;
                }
            }
        } else {
            for (int m = 0; m < tabInfo.getColInfoList().size(); m++) {
                if (tabInfo.getColInfo(m).GetTypCol().contains("STRING(T)")) {
                    int T = tabInfo.getColInfo(m).getSizeString();
                    readStringFromBuffer(buff, bufferposmove, T);
                    bufferposmove += T;
                } else if (tabInfo.getColInfo(m).GetTypCol().contains("INT")) {
                    readIntFromBuffer(buff, bufferposmove);
                    bufferposmove++;
                } else if (tabInfo.getColInfo(m).GetTypCol().contains("FLOAT")) {
                    readFloatFromBuffer(buff, bufferposmove);
                    bufferposmove++;
                }
            }
        }

        System.out.println("recvalues: " + recvalues);
        return bufferposmove - pos;
    }

    public void readIntFromBuffer(ByteBuffer buffer, int pos) {
        try {
            int inter = buffer.getInt(pos);
            recvalues.add(inter);
            System.out.println("Int intégré à recvalues :" + inter);
        } catch (Exception e) {
            System.out.println("Exception caused by :" + e);
        }
    }

    public void readFloatFromBuffer(ByteBuffer buffer, int pos) {
        try {
            float inter = buffer.getFloat(pos);
            recvalues.add(inter);
            System.out.println("Float intégré à recvalues :" + inter);
        } catch (Exception e) {
            System.out.println("Exception caused by :" + e);
        }
    }

    public void readStringFromBuffer(ByteBuffer buffer, int pos, int taille) {
        try {
            byte[] stringBytes = new byte[taille];
            buffer.position(pos);
            buffer.get(stringBytes);
            String inter = new String(stringBytes);
            recvalues.add(inter);
            System.out.println("Valeur intérmédiaire:" + inter);
        } catch (Exception e) {
            System.out.println("Exception caused by :" + e);
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
