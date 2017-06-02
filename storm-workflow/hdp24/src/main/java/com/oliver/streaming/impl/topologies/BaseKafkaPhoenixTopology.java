package com.oliver.streaming.impl.topologies;

//  Derived from:
// https://github.com/jkpchang/storm_kafka_tutorial/blob/master/src/main/java/com/hortonworks/tutorials/tutorial4/BaseTruckEventTopology.java
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public abstract class BaseKafkaPhoenixTopology {
  private static final Logger LOG = Logger.getLogger(BaseKafkaPhoenixTopology.class);
  protected Properties topologyConfig;

  public BaseKafkaPhoenixTopology(String configFileLocation) throws Exception {
    topologyConfig = new Properties();
    try {
      topologyConfig.load(new FileInputStream(configFileLocation));
    } catch (FileNotFoundException e) {
      LOG.error("Encountered error while reading configuration properties: "
          + e.getMessage());
      throw e;
    } catch (IOException e) {
      LOG.error("Encountered error while reading configuration properties: "
          + e.getMessage());
      throw e;
    }
  }
}
