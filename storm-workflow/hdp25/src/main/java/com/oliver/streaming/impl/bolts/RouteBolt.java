package com.oliver.streaming.impl.bolts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

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
