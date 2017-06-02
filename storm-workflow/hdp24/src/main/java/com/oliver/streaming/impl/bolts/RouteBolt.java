package com.oliver.streaming.impl.bolts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.oliver.streaming.tools.Property;
import com.oliver.streaming.tools.TruliaParser;

public class RouteBolt extends BaseRichBolt{
	private static final Logger LOG = Logger.getLogger(RouteBolt.class);
	boolean persistAllEvents;
    private OutputCollector outputCollector;

    public RouteBolt(Boolean persistAllEvents) {
        this.persistAllEvents = persistAllEvents;
    }
	@Override
	public void execute(Tuple input) {
		LOG.info("About to process tuple[" + input + "]");
		
		List<Property> properties = new ArrayList<Property>();
		TruliaParser tp = new TruliaParser();
		
		// Process tuple by splitting into individual rows
		String rssfeed = input.getString(0);
		
		properties = tp.processRSS(rssfeed);
		
	    for(Property prop : properties) {
	    	outputCollector.emit(new Values(prop.getTitle(),
	    			                        prop.getLink(),
	    			                        prop.getDescription(),
	    			                        prop.getPubDate(),
	    			                        prop.getThumbnail()));
	    }
		outputCollector.ack(input);
        
	}
	@Override
	public void prepare(Map arg0, TopologyContext arg1, OutputCollector outputCollector) {
        LOG.info("The PersistAllEvents Flag is set to: " + persistAllEvents);
        this.outputCollector = outputCollector;
		
	}
	@Override
	public void declareOutputFields(OutputFieldsDeclarer arg0) {
		arg0.declare(new Fields("title", 
				                "link",
				                "description",
				                "pubDate",
				                "thumbnail"));
	}

}
