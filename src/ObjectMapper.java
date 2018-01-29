import java.lang.reflect.Field;

// class to map classes and their fields to SQL table 
public class ObjectMapper implements iORM {
	
	private Object pObj = null;
	
	public ObjectMapper(Object object){
		this.pObj = object;
	}
	
	public String create(){
		
		//creating empty strings, where names and values for SQL statements would be added
		String names = "";
		String values = "";
				
		Field[] fields = pObj.getClass().getDeclaredFields();
				
		for (int i = 0; i < fields.length; i++){
			try{
				if(fields[i].get(pObj) == null){
					continue;
				}
				
				//replace apostrophe if has it
				String name = fields[i].getName();
				String value = fields[i].get(pObj).toString().replace("'", "''");
				
						
				// skipping adding comma before the first element
				if(i!=0){
					names += ",";
					values += ",";
				}
				if(fields[i].get(pObj) instanceof String){
					values += "'"+value+"'";
				}
				else{
					values += value;
				}
			names += name; 
			}catch (IllegalAccessException e){
				System.out.println(e.getMessage());
			}
		}
		// returns string of correct SQL format
		return "INSERT INTO "+pObj.getClass().getSimpleName().toLowerCase().concat("s")+"("+names+") VALUES ("+values+");"; 
		
	}
	
	public String update(){
		String updateQuery = "";
		try {
            String update_cols = "";
            int counter = 0;
            
            
            Class<?> objClass = pObj.getClass();
            String db_tableName = objClass.getSimpleName() + "s";
            Field[] fields = objClass.getFields();

            //setting length -1 so it won't update last field value, which is unique intID
            for (int i = 0; i < fields.length - 1; i++) {
                String name = fields[i].getName();
                String fieldValue = "";
                Object value = fields[i].get(pObj);
                if(value==null){
                	continue;
                }
                if(value instanceof String){
                	fieldValue = name + " = '" + value.toString().replace("'", "''") + "'";
				}
				else{
					fieldValue = name + " = " + value.toString();
				}

                if (counter == 0) {
                    update_cols = fieldValue;
                } else {
                    update_cols = update_cols + ", " + fieldValue;
                }
                counter += 1;
            }
            updateQuery = "UPDATE " + db_tableName + " SET " + update_cols + " WHERE intID = " + "'" +((Patient) pObj).getIntID()+"'" + ";";

        } catch (Exception e) {
            e.printStackTrace();
        }
		
		return updateQuery;
	}
}
