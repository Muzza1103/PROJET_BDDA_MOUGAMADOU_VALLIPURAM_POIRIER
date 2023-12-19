import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class RecordTests {
    public static void runRecordTests(){
        ColInfo Colonne0 = new ColInfo("Nom", "VARSTRING(20)");
        ColInfo Colonne1 = new ColInfo("Age", "INT");
        ColInfo Colonne2 = new ColInfo("Taille", "FLOAT");
        ColInfo Colonne3 = new ColInfo("Genre", "STRING(20)");

        ArrayList<ColInfo> ListeColonnes = new ArrayList<>();
        ListeColonnes.add(Colonne0);
        ListeColonnes.add(Colonne1);
        ListeColonnes.add(Colonne2);
        ListeColonnes.add(Colonne3);

        TableInfo Table0 = new TableInfo("nom-age", 4, ListeColonnes);

        Record Record0 = new Record(Table0);
        
        ArrayList<Object> recValues = new ArrayList<>();
        String nom_0 = "MonPrenom";
        int age_0 = 20;
        float taille_0 = 1.76f;
        String genre_0 = "voila_20_characteres";

        
        recValues.add(nom_0);
        recValues.add(age_0);
        recValues.add(taille_0);
        recValues.add(genre_0);
        System.out.println(recValues);

        Record0.InsertValues(recValues);

        ByteBuffer nvbuffer = ByteBuffer.allocate(200);
        
        Record0.WriteToBuffer(nvbuffer, 0);

        //System.out.println("hi");
        //System.out.println("affichage après écriture");
        //printBuffer(nvbuffer);
        //System.out.println(Arrays.toString(nvbuffer));

        Record0.readFromBuffer(nvbuffer, 0);
        //System.out.println("affichage après lecture du buffer");
        //printBuffer(nvbuffer);
        System.out.println("fin tests");
    }

    public static void testsTailleFixe(){
        ColInfo Colonne0 = new ColInfo("poids", "INT");
        ColInfo Colonne1 = new ColInfo("taille", "FLOAT");
        ColInfo Colonne2 = new ColInfo("nationalite", "STRING(7)");

        ArrayList<ColInfo> ListeColonne = new ArrayList<>();
        
        ListeColonne.add(Colonne0);
        ListeColonne.add(Colonne1);
        ListeColonne.add(Colonne2);

        TableInfo tabInfo = new TableInfo("poids-taille-nat", 3, ListeColonne);

        Record record0 = new Record(tabInfo);

        ArrayList<Object> recValues = new ArrayList<>();
        int poids_0 = 70;
        float taille_0 = 2.07f;
        String nat_0 = "bonjour";

        
        recValues.add(poids_0);
        recValues.add(taille_0);
        recValues.add(nat_0);
        System.out.println(recValues);

        record0.InsertValues(recValues);

        ByteBuffer nvbuffer = ByteBuffer.allocate(80);

        record0.WriteToBuffer(nvbuffer, 0);

        System.out.println("hi");
        System.out.println("affichage après écriture");
        //printBuffer(nvbuffer);
        //System.out.println(Arrays.toString(nvbuffer));

        record0.readFromBuffer(nvbuffer, 0);
        //System.out.println("affichage après lecture du buffer");
        //printBuffer(nvbuffer);
        System.out.println("fin tests");

    }

    //récupéré sur www.java2s.com pour les tests
    public static void printBuffer(ByteBuffer buffer) {
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            System.out.println((String) Arrays.toString(bytes));
    }
}
