package com.oliver.streaming.impl.bolts;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import org.apache.log4j.Logger;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class PhoenixJDBC extends BaseRichBolt{
	private static final Logger LOG = Logger.getLogger(RouteBolt.class);
	boolean persistAllEvents;
    private OutputCollector outputCollector;
    private Connection conn;

    public PhoenixJDBC(Boolean persistAllEvents) {
        this.persistAllEvents = persistAllEvents;
    }
	@Override
	public void execute(Tuple input) {
		LOG.info("About to process tuple[" + input + "]");
		
	      String sentence = input.getString(0);
	      String[] words = sentence.split(" ");
	      
	      for(String word: words) {
	         word = word.trim();
	         
	         if(!word.isEmpty()) {
	            word = word.toLowerCase();
	            outputCollector.emit(new Values(word));
	         }
	         
	      }
	      
	      outputCollector.ack(input);
        
	}
	@Override
	public void prepare(Map arg0, TopologyContext arg1, OutputCollector outputCollector) {
        LOG.info("The PersistAllEvents Flag is set to: " + persistAllEvents);
        this.outputCollector = outputCollector;
        try {
		conn = getConnection();
		conn.setAutoCommit(true);
		checkTableSchema(conn);
        } catch (SQLException e) {
        	  LOG.info("ISSUE OSB");
        	  e.printStackTrace();
        }
	}
	@Override
	public void declareOutputFields(OutputFieldsDeclarer arg0) {
		arg0.declare(new Fields("word"));
	}
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:phoenix:localhost:2181:/hbase-unsecure");
    }
    private void checkTableSchema(Connection conn) throws SQLException {
        Statement statement = conn.createStatement();
        statement.execute("create table if not exists Insyte.AggregateStg (ID VARCHAR not null primary key,count BIGINT)");
    }

}
