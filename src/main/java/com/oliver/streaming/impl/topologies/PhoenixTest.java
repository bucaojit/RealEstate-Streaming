package com.oliver.streaming.impl.topologies;

import org.apache.log4j.Logger;
import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.kafka.SpoutConfig;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;

import com.oliver.streaming.impl.bolts.PhoenixJDBC;

import org.apache.storm.kafka.BrokerHosts;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.kafka.StringScheme;
import org.apache.storm.kafka.ZkHosts;

public class PhoenixTest extends BaseKafkaPhoenixTopology{
	private static final Logger LOG = Logger.getLogger(KafkaPhoenixTopology.class);
	
	private static String ROUTE_BOLT = "ROUTE_BOLT";
	
	//String configLocation;
	public PhoenixTest(String configLocation) throws Exception{
		
		//this.configLocation = configLocation;
		super(configLocation);
		LOG.info("OSB PhoenixTest");
	}
	
    public static void main(String[] args) throws Exception {
        String configFileLocation = args[0];


        PhoenixTest kpTopology = new PhoenixTest(configFileLocation);
        kpTopology.buildAndSubmit();
    }
    
    public void buildAndSubmit() throws Exception {
    	 TopologyBuilder builder = new TopologyBuilder();
    	 Config config = new Config();
         config.setDebug(true);
         // String nimbusHost = topologyConfig.getProperty("nimbus.host");
         config.put(Config.NIMBUS_HOST, "localhost");
         
         configureKafkaSpout(builder);
         //configureRouteBolt(builder);
         configurePhoenixTest(builder);
         
         /*
         builder.setBolt("submitter", new SubmitBolt())
            .shuffleGrouping(ROUTE_BOLT);
         */
         
         try {
             StormSubmitter.submitTopology("simple-topology", config, builder.createTopology());
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
        /*
        String topic = topologyConfig.getProperty("kafka.topic");
        String zkRoot = topologyConfig.getProperty("kafka.zkRoot");
        String consumerGroupId = topologyConfig.getProperty("kafka.consumer.group.id");
        */
        String topic = "addresses";
        String zkRoot = "";
        String consumerGroupId = "group1";

        SpoutConfig spoutConfig = new SpoutConfig(hosts, topic, zkRoot, consumerGroupId);
        spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());

        return spoutConfig;
    }
    
    public void configurePhoenixTest(TopologyBuilder builder) {
        PhoenixJDBC phoenixJDBC = new PhoenixJDBC(true);
        //Defines new bolt in topology
        builder.setBolt("PhoenixJDBC", phoenixJDBC, 2).shuffleGrouping("kafkaSpout");
    }



}
