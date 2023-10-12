public class TableInfo {
    private static String nom_rel;
    private static int nb_colonnes;
    private static ColInfo col_info;

    public TableInfo(){

    }

    public String getNomRel(){
        return this.nom_rel;
    }

    public ColInfo getColInfo(){
        return this.col_info;
    }

}
