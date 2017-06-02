package com.oliver.streaming.impl.topologies;

import org.apache.log4j.Logger;
import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import storm.kafka.BrokerHosts;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;

import com.oliver.streaming.impl.bolts.InsertBolt;
import com.oliver.streaming.impl.bolts.RouteBolt;

public class KafkaPhoenixTopology extends BaseKafkaPhoenixTopology{
	private static final Logger LOG = Logger.getLogger(KafkaPhoenixTopology.class);
	
	private static String ROUTE_BOLT = "ROUTE_BOLT";
	
	//String configLocation;
	public KafkaPhoenixTopology(String configLocation) throws Exception{
		
		//this.configLocation = configLocation;
		super(configLocation);
	}
	
    public static void main(String[] args) throws Exception {
        // Empty string for nowj
        // String configFileLocation = args[0];
        String configFileLocation = "config";


        KafkaPhoenixTopology kpTopology = new KafkaPhoenixTopology(configFileLocation);
        kpTopology.buildAndSubmit();
    }
    
    public void buildAndSubmit() throws Exception {
    	 TopologyBuilder builder = new TopologyBuilder();
    	 Config config = new Config();
         config.setDebug(true);
         // String nimbusHost = topologyConfig.getProperty("nimbus.host");
         config.put(Config.NIMBUS_HOST, "localhost");
         
         configureKafkaSpout(builder);
         configureRouteBolt(builder);
         configureInsertBolt(builder);
         
         //builder.setBolt("submitter", new SubmitBolt())
         //   .shuffleGrouping(ROUTE_BOLT);
         
         try {
             StormSubmitter.submitTopology("realestate-topology", config, builder.createTopology());
         } catch (Exception e) {
             LOG.error("Error submiting Topology", e);
         }

    }
    
    public int configureKafkaSpout(TopologyBuilder builder) {
        KafkaSpout kafkaSpout = constructKafkaSpout();

        //int spoutCount = Integer.valueOf(topologyConfig.getProperty("spout.thread.count"));
        //int boltCount = Integer.valueOf(topologyConfig.getProperty("bolt.thread.count"));
        
        int spoutCount = Integer.valueOf(1);
        int boltCount = Integer.valueOf(1);

        builder.setSpout("kafkaSpout", kafkaSpout, spoutCount);
        return boltCount;
    }
    
    private KafkaSpout constructKafkaSpout() {
        KafkaSpout kafkaSpout = new KafkaSpout(constructKafkaSpoutConf());
        return kafkaSpout;
    }

    private SpoutConfig constructKafkaSpoutConf() {
        // BrokerHosts hosts = new ZkHosts(topologyConfig.getProperty("kafka.zookeeper.host.port"));
        BrokerHosts hosts = new ZkHosts("localhost:2181");

        String topic = "properties";
        String zkRoot = "";
        String consumerGroupId = "group1";

        SpoutConfig spoutConfig = new SpoutConfig(hosts, topic, zkRoot, consumerGroupId);
        spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());

        return spoutConfig;
    }
    
    public void configureRouteBolt(TopologyBuilder builder) {
        RouteBolt routeBolt = new RouteBolt(true);
        //Defines new bolt in topology
        builder.setBolt(ROUTE_BOLT, routeBolt, 2).shuffleGrouping("kafkaSpout");
    }
    
    public void configureInsertBolt(TopologyBuilder builder) {
        InsertBolt insertBolt = new InsertBolt();
        //Defines new bolt in topology
        builder.setBolt("InsertBolt", insertBolt, 2).shuffleGrouping(ROUTE_BOLT);
    }



}
