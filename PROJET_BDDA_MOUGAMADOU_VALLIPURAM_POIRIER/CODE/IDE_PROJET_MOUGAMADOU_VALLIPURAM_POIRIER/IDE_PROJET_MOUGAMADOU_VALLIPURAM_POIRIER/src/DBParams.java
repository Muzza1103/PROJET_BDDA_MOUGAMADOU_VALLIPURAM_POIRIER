import java.nio.ByteBuffer;
import java.util.*;

public class DBParams {

    public static String DBPath;
    public static long SGBDPageSize;
    public static int DMFileCount;
    public static int FrameCount;

    public static void main (String[] args){
        System.out.println("test");
        DBPath = "/users/licence/in07091/PROJET_BDDA_MOUGAMADOU_VALLIPURAM_POIRIER/PROJET_BDDA_MOUGAMADOU_VALLIPURAM_POIRIER/DB/";
        SGBDPageSize = 4096;
        DMFileCount = 4;
        FrameCount = 2;

        ColInfo Colonne0 = new ColInfo("Nom", "VARSTRING(T)");
        ColInfo Colonne1 = new ColInfo("Age", "INT");

        ArrayList<ColInfo> ListeColonnes = new ArrayList<>();

        TableInfo Table0 = new TableInfo("nom-age", 2, ListeColonnes);

        Record Record0 = new Record(Table0);
        
        ArrayList<Object> recValues = new ArrayList<>();
        String nom_0 = "Matthieu";
        int age_0 = 2;
        
        recValues.add(nom_0);
        recValues.add(age_0);

        Record0.InsertValues(recValues);

        ByteBuffer nvbuffer = ByteBuffer.allocate(80);

        Record0.WriteToBuffer(nvbuffer, 0);

        System.out.println("hi");
        System.out.println(nvbuffer);

        Record0.readFromBuffer(nvbuffer, 0);



    }

}
