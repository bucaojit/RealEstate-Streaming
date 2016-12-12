package com.oliver.streaming.impl.bolts;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;

import com.oliver.streaming.tools.Property;


public class InsertBolt implements IRichBolt{
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private static final Logger LOG = Logger.getLogger(InsertBolt.class);
   // Map<String, Integer> counters;
   private OutputCollector collector;
   private Connection conn;
   
   @Override
   public void prepare(Map stormConf, TopologyContext context,
   OutputCollector collector) {
   
      this.collector = collector;
      try {
		conn = getConnection();
		checkTableSchema(conn);
		conn.setAutoCommit(true);
      } catch (SQLException e) {
      	  LOG.info("Unable to obtain connection");
      	  e.printStackTrace();
      }
   }

   @Override
   public void execute(Tuple input) {
	  Property prop = new Property();
      prop.setTitle(input.getString(0));
      prop.setLink(input.getString(1));
      prop.setDescription(input.getString(2));
      prop.setPubDate(input.getString(3));
      prop.setThumbnail(input.getString(4));
      
      insertRow(prop);
      
      collector.ack(input);
   }
   
   private void insertRow(Property prop) {
	   try {
		   Statement insert = conn.createStatement();
		   String insertStatement = "UPSERT INTO PROPERTIES VALUES ( '" 
		           + prop.getTitle() + "','" 
                   + prop.getLink()  + "','" 
                   + prop.getDescription() + "','" 
                   + prop.getPubDate() + "','" 
                   + prop.getThumbnail()+ "')";
		   LOG.info("INSERTING: " + insertStatement);
		   insert.execute(insertStatement);
		   
	   } catch (SQLException sqle) {
		   sqle.printStackTrace();
	   }
	   
   }

   @Override
   public void cleanup() {

   }

   @Override
   public void declareOutputFields(OutputFieldsDeclarer declarer) {
   
   }

   @Override
   public Map<String, Object> getComponentConfiguration() {
      return null;
   }
   private Connection getConnection() throws SQLException {
       return DriverManager.getConnection("jdbc:phoenix:localhost:2181:/hbase-unsecure");
   }
   
   private void checkTableSchema(Connection conn) throws SQLException {
       Statement statement = conn.createStatement();
       statement.execute("CREATE TABLE IF NOT EXISTS PROPERTIES (TITLE VARCHAR not null primary key," +
                                                                "LINK  VARCHAR, " +
    		                                                    "DESCRIPTION VARCHAR, " +
                                                                "PUBDATE VARCHAR, " +
    		                                                    "THUMBNAIL VARCHAR)");
   }
}
