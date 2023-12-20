
public class SelectCondition {
	private String nom;
	private String op;
	private int valeur;
	private String condition;
	
	public SelectCondition(String condition) {
		condition = condition;
	}


	public void separation(String condition) {
		if(condition.contains("<")&&(!condition.contains("<="))&&(!condition.contains("<>"))){
			String[] conditionVal = condition.split("<");
			op = "<";
			nom = conditionVal[0];
			valeur = Integer.parseInt(conditionVal[1]);
			
		}else if(condition.contains("=")&&(!condition.contains("<="))&&(!condition.contains(">="))){
			String[] conditionVal = condition.split("=");
			op = "=";
			nom = conditionVal[0];
			valeur = Integer.parseInt(conditionVal[1]);
			
		}else if(condition.contains(">")&&(!condition.contains(">="))&&(!condition.contains("<>"))){
			String[] conditionVal = condition.split(">");
			op = ">";
			nom = conditionVal[0];
			valeur = Integer.parseInt(conditionVal[1]);
			
		}else if(condition.contains("<=")&&(!condition.contains("="))&&(!condition.contains("<>"))) {
			String[] conditionVal = condition.split("<=");
			op = "<=";
			nom = conditionVal[0];
			valeur = Integer.parseInt(conditionVal[1]);
			
			
		}else if(condition.contains(">=")&&(!condition.contains("<>"))&&(!condition.contains("="))) {
			String[] conditionVal = condition.split(">=");
			op = ">=";
			nom = conditionVal[0];
			valeur = Integer.parseInt(conditionVal[1]);
			
		}else if(condition.contains("<>")&&(!condition.contains(">"))&&(!condition.contains("<"))) {
			String[] conditionVal = condition.split("<>");
			op = "<>";
			nom = conditionVal[0];
			valeur = Integer.parseInt(conditionVal[1]);
			
		}else {
			System.out.println("pas de condition");
		}
	}

}
;