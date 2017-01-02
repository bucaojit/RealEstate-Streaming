package com.oliver.streaming.impl.bolts;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;

import org.apache.log4j.Logger;


public class SubmitBolt implements IRichBolt{
   private static final Logger LOG = Logger.getLogger(SubmitBolt.class);
   Map<String, Integer> counters;
   private OutputCollector collector;
   
   @Override
   public void prepare(Map stormConf, TopologyContext context,
   OutputCollector collector) {
      this.counters = new HashMap<String, Integer>();
      this.collector = collector;
   }

   @Override
   public void execute(Tuple input) {
      String str = input.getString(0);
      
      if(!counters.containsKey(str)){
         counters.put(str, 1);
      }else {
         Integer c = counters.get(str) +1;
         counters.put(str, c);
      }
   
      collector.ack(input);
   }

   @Override
   public void cleanup() {
      for(Map.Entry<String, Integer> entry:counters.entrySet()){
         System.out.println(entry.getKey()+" : " + entry.getValue());
         LOG.info("TO PRINT: " + entry.getKey()+" : " + entry.getValue());
      }
   }

   @Override
   public void declareOutputFields(OutputFieldsDeclarer declarer) {
   
   }

   @Override
   public Map<String, Object> getComponentConfiguration() {
      return null;
   }
}
