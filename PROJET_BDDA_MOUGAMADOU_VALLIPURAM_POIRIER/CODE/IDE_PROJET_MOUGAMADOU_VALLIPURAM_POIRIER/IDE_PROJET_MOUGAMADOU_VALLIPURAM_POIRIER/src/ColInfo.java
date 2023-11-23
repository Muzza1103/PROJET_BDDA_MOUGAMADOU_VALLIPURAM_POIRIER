public class ColInfo {

    private String nomColonne;
    private TypeColonne typeColonne;
    private int t;
    
    public ColInfo(String nomColonne, TypeColonne typeColonne){
        this.nomColonne = nomColonne;
        this.typeColonne = typeColonne;
        //this.t = t;
        this.t = typeColonne.getOctet();
        
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

