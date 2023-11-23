
public class TypeColonne {
	
	private String type;
	private int nbOctet;
	
	public TypeColonne(String s) {
		if(s.equals("INT") || s.equals("FLOAT")) {
			this.nbOctet = 4;
			type = s;
			
		}
	}
	
	public TypeColonne(String s,int T) {
		if(s.equals("STRING") || s.equals("VARSTRING")) {
			this.nbOctet = T;
			type = s;
		}
	}
	
	public String getType() {
		return type;
	}
	
	public int getOctet() {
		return nbOctet;
	}
	
}