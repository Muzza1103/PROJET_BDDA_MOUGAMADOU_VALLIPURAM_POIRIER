public class ColInfo {

    private static String nom_colonne;
    private static TypeColonne type_colonne;
    private static int T;
    
    public ColInfo(String nom_colonne, TypeColonne type_colonne, int T){
        this.nom_colonne = nom_colonne;
        this.type_colonne = type_colonne;
        this.T= T;
        
    }

    public String GetNomCol(){
        return this.nom_colonne;
    }

    public TypeColonne GetTypCol(){
        return this.type_colonne;
    }
    
    public int GetT() {
    	return T;
    }
}

