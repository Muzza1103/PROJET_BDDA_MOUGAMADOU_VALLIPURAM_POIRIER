package TP4;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


class DatabaseInfo implements Serializable {
    private List<TableInfo> tableInfos;
    private int relationCount;

    public DatabaseInfo() {
        tableInfos = new ArrayList<>();
        relationCount = 0;
    }

    public void addTableInfo(TableInfo tableInfo) {
        tableInfos.add(tableInfo);
        relationCount++;
    }

    public void saveToDisk() {
        try (FileOutputStream fileOut = new FileOutputStream("DBInfo.save");
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {
            objectOut.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFromDisk() {
        try (FileInputStream fileIn = new FileInputStream("DBInfo.save");
             ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {
            DatabaseInfo restoredInfo = (DatabaseInfo) objectIn.readObject();
            this.tableInfos = restoredInfo.tableInfos;
            this.relationCount = restoredInfo.relationCount;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DatabaseInfo that = (DatabaseInfo) o;
        return relationCount == that.relationCount &&
               Objects.equals(tableInfos, that.tableInfos);
    }

}
