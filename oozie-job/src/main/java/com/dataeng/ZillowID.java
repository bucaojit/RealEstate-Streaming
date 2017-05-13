package com.dataeng;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ZillowID {
	public static String getID() {
		String statementZWSID = "SELECT ZWSID from ZWSID limit 1";
		try {
			DBInterface dbInterface = new DBInterface();
			ResultSet result = dbInterface.executeSelectStatement(statementZWSID);
			if (result.getFetchSize() == 0) {
				throw new SQLException("No rows returned for ZWSID");
			}
			
			return result.getString("ZWSID");
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
