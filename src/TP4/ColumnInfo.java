package TP4;

import java.io.Serializable;

public class ColumnInfo implements Serializable{
    private String columnName;
    private String columnType;

    public ColumnInfo(String columnName, String columnType) {
        this.columnName = columnName;
        this.columnType = columnType;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getColumnType() {
        return columnType;
    }
}

