package TP4;

import java.util.List;
import java.util.Objects;
import java.io.Serializable;

class TableInfo implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String tableName;
    int numColumns;
    List<String> columnNames;
    List<String> columnTypes;

    public TableInfo(String tableName, int numColumns, List<String> columnNames, List<String> columnTypes) {
        this.tableName = tableName;
        this.numColumns = numColumns;
        this.columnNames = columnNames;
        this.columnTypes = columnTypes;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableInfo tableInfo = (TableInfo) o;
        return numColumns == tableInfo.numColumns &&
               Objects.equals(tableName, tableInfo.tableName) &&
               Objects.equals(columnNames, tableInfo.columnNames) &&
               Objects.equals(columnTypes, tableInfo.columnTypes);
    }

}

