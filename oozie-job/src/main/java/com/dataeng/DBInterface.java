package com.dataeng;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HConstants;

public class DBInterface {
	String phxZKHostname;
	Integer phxZKPort;
	String hbaseRootDir;

	String jdbcString = "jdbc:phoenix";
	
	Configuration hbaseConfig;
	Connection dbConn;
	
	public DBInterface() throws SQLException {
		// Get details from configuration
		hbaseConfig = HBaseConfiguration.create();
		phxZKHostname = hbaseConfig.get(HConstants.ZOOKEEPER_QUORUM);
		phxZKPort = hbaseConfig.getInt(HConstants.ZOOKEEPER_CLIENT_PORT, HConstants.DEFAULT_ZOOKEPER_CLIENT_PORT);
		hbaseRootDir = hbaseConfig.get(HConstants.ZOOKEEPER_ZNODE_PARENT);
		
		dbConn = getConnection();
	}
	
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcString + ":" + 
                                           phxZKHostname + ":" +
                                           phxZKPort + ":" + 
                                           hbaseRootDir);
    }
    
    public ResultSet executeSelectStatement(String sql) throws SQLException {
        Statement statement = dbConn.createStatement();
        return statement.executeQuery(sql);
    }
    
    public boolean executeInsertStatement(String sql) throws SQLException {
        Statement statement = dbConn.createStatement();
        return statement.execute(sql);
    }
    
    public void getT1() {
    	try {
    		ResultSet rs = executeSelectStatement("Select * from T1");
    		while(rs.next()) {
    			System.out.println(rs.getString(1));
    		}
    	} catch(SQLException se) {
    		se.printStackTrace();
    	}
    	
    }
    
    public static void main(String[] args) throws SQLException {
    	DBInterface dbi = new DBInterface();
    	dbi.getT1();
    }
    
}
