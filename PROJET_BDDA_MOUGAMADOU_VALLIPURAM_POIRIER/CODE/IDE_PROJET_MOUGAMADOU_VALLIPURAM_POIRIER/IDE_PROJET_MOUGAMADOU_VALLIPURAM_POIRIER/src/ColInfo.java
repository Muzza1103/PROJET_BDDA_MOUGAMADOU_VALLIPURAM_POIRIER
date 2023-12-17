public class ColInfo {

    private String nom_colonne;
    private String type_colonne;
    private int stringSize;
    
    //On utilise ce constructeur pour float et int
    public ColInfo(String nom_colonne, String type_colonne){
        this.nom_colonne = nom_colonne;
        this.type_colonne = type_colonne;
        if(type_colonne.contains("STRING")){
            stringSize = Integer.parseInt(type_colonne.substring(type_colonne.indexOf("(")+1, type_colonne.indexOf(")")));
            System.out.println(stringSize);
        }
    }

    public String GetNomCol(){
        return this.nom_colonne;
    }

    public String GetTypCol(){
        return this.type_colonne;
    }

    public int getSizeString(){
        return this.stringSize;
    }
}
