package com.dataeng;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

public class PropertyDetailProcessor {
	DBInterface dbInterface;
	RESTInterface restInterface;
	
	
	public PropertyDetailProcessor() throws SQLException{
		dbInterface = new DBInterface();
		restInterface = new RESTInterface();
	}
	
	private ResultSet getPropertiesToProcess() {
		String query = "SELECT TITLE FROM PROPERTIES_TOPROCESS";
		try {
			return dbInterface.executeSelectStatement(query);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			return null;
		}
		
	}
	
	private Map<String,String> getPropDetails(String property) {
		return restInterface.getDetailsForProperty(property);
	}
	
	private void upsertRow(Map<String, String> row) throws SQLException {
		String insert = compileInsertString(row);
		dbInterface.executeInsertStatement(insert);
	}
	
	private void process() throws IOException {
		ResultSet properties = getPropertiesToProcess();
		try {
			while(properties.next()) {
				Map<String, String> rowval = getPropDetails(properties.getString(1));
				upsertRow(rowval);
			}
		
			
		}catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}
	
	public String compileInsertString(Map<String,String> property) {
		// TODO: Determine Table Column Names based on XML.  
		//       For the names in string, add to string compile
		Set<String> kvProps = property.keySet();
		StringBuilder firstHalf = new StringBuilder(), secondHalf = new StringBuilder();
		
		for(String kvProp : kvProps) {
			switch (kvProp) {
			case "Column1":
				firstHalf.append(kvProp).append(",");
				secondHalf.append(property.get(kvProp)).append(",");
				break;
			default:
				break;
			}
		}
		return firstHalf.toString() + secondHalf.toString();
	}
	
	
	public static void main(String args[]) throws SQLException{
		PropertyDetailProcessor pdp = new PropertyDetailProcessor();
		try {
			pdp.process();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
