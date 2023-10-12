package TP4;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Créez et utilisez des objets DatabaseInfo, TableInfo et ColInfo
        TableInfo tableInfo = new TableInfo("MyTable", 3,
                List.of("Column1", "Column2", "Column3"),
                List.of("INT", "STRING(10)", "FLOAT")
        );

        DatabaseInfo databaseInfo = new DatabaseInfo();
        databaseInfo.addTableInfo(tableInfo);

        // Sauvegardez la base de données dans un fichier
        databaseInfo.saveToDisk();

        // Restaurez la base de données depuis le fichier
        DatabaseInfo restoredDatabaseInfo = new DatabaseInfo();
        restoredDatabaseInfo.loadFromDisk();

        if (databaseInfo.equals(restoredDatabaseInfo)) {
            System.out.println("Les objets sont identiques.");
        } else {
            System.out.println("Les objets ne correspondent pas.");
        }

    }
}
