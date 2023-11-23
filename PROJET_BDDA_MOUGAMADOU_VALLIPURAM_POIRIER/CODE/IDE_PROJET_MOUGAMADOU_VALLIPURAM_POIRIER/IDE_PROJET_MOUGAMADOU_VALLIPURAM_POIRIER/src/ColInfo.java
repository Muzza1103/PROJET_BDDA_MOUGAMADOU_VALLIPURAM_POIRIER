public class ColInfo {

    private String nom_colonne;
    private String type_colonne;
    
    public ColInfo(String nom_colonne, String type_colonne){
        this.nom_colonne = nom_colonne;
        this.type_colonne = type_colonne;
    }
 
    public String GetNomCol(){
        return this.nom_colonne;
    }

    public String GetTypCol(){
        return this.type_colonne;
    }
}
