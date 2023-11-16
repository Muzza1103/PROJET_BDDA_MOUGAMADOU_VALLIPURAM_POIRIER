public class ColInfo {

    private static String nomColonne;
    private static TypeColonne typeColonne;
    private static int t;
    
    public ColInfo(String nomColonne, TypeColonne typeColonne, int t){
        this.nomColonne = nomColonne;
        this.typeColonne = typeColonne;
        this.t = t;
        
    }

    public String GetNomCol(){
        return nomColonne;
    }

    public TypeColonne GetTypCol(){
        return typeColonne;
    }
    
    public int GetT() {
    	return t;
    }
}

