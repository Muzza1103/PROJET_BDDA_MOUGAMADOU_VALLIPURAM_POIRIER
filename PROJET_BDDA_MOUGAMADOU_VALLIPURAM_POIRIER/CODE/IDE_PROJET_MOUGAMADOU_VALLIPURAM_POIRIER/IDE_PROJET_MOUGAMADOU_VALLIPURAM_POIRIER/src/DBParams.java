import java.nio.ByteBuffer;
import java.util.*;

public class DBParams {

    public static String DBPath;
    public static long SGBDPageSize;
    public static int DMFileCount;
    public static int frameCount;


    public static void main (String[] args){
        System.out.println("test");
        DBPath = "/users/licence/in07091/PROJET_BDDA_MOUGAMADOU_VALLIPURAM_POIRIER/PROJET_BDDA_MOUGAMADOU_VALLIPURAM_POIRIER/DB/";
        SGBDPageSize = 4096;
        DMFileCount = 4;
        frameCount = 2;

        ColInfo Colonne0 = new ColInfo("Nom", "VARSTRING(T)");
        ColInfo Colonne1 = new ColInfo("Age", "INT");
        ColInfo Colonne2 = new ColInfo("Taille", "FLOAT");
        ColInfo Colonne3 = new ColInfo("Genre", "STRING(T)");

        ArrayList<ColInfo> ListeColonnes = new ArrayList<>();
        ListeColonnes.add(Colonne0);
        ListeColonnes.add(Colonne1);
        ListeColonnes.add(Colonne2);
        ListeColonnes.add(Colonne3);

        TableInfo Table0 = new TableInfo("nom-age", 2, ListeColonnes);

        Record Record0 = new Record(Table0);
        
        ArrayList<Object> recValues = new ArrayList<>();
        String nom_0 = "Matthieu";
        int age_0 = 2;
        float taille_0 = 1.07f;
        String genre_0 = "M";

        
        recValues.add(nom_0);
        recValues.add(age_0);
        recValues.add(taille_0);
        recValues.add(genre_0);
        System.out.println(recValues);

        Record0.InsertValues(recValues);

        ByteBuffer nvbuffer = ByteBuffer.allocate(80);
        
        Record0.WriteToBuffer(nvbuffer, 0);

        System.out.println("hi");
        printBuffer(nvbuffer);
        //System.out.println(Arrays.toString(nvbuffer));

        Record0.readFromBuffer(nvbuffer, 0);
        System.out.println("fin tests");
    }

    //récupéré sur www.java2s.com pour les tests
    public static void printBuffer(ByteBuffer buffer) {
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            System.out.println(Arrays.toString(bytes));
        }

}
