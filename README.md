### RealEstate-Streaming
##### Summary
*RealEstate-Streaming* is a project I started to better understand data engineering components and practices.  

The general idea is to use the Nifi/Kafka/Storm set of components for streaming RSS data from Trulia that is updated regularly with properties coming on the market.  This information is limited to address, price, description, sqft.  In order to obtain additional details, a batch job will be run through Oozie that iterates through the properties that need processing, and pulls data from Zillow that include sold price/date, schools district, neighborhood, and other home facts.

Data will be saved into the Phoenix/HBase database for persisting the detailed property information.  It will also save outputs from reports and metadata from Content Based Filtering that will be used for a recommendation system.

Further processing of the property data and additional static datasets such as schools, safety, nearby stores/restaurants will be done with Spark through applying MLlib functions and other methods.

##### Setup

###### Nifi 
Import template, setup and enable StandardSSLContextService

###### Kafka
Create PROPERTIES topic:
```
cd /usr/hdp/current/kafka-broker/bin
./kafka-topics.sh --zookeeper localhost:2181 --create --partitions 1 --topic properties --replication-factor 1
```
To test that Nifi is writing to Kafka, run the consumer script.  *Note: this is run on the Kafka-broker node*:
```
./kafka-console-consumer.sh --zookeeper localhost:2181 --topic properties --from-beginning
```
###### Storm
Build storm job with Maven: `mvn clean package -DskipTests`
Upload Storm job:
```
$ storm jar target/realestate-streaming-1.0-SNAPSHOT.jar com.oliver.streaming.impl.topologies.KafkaPhoenixTopology
```
###### Phoenix
Make sure Phoenix is enabled in HBase configuration.
The Storm job will create Phoenix tables: PROPERTIES and PROPERTIES_TOPROCESS

###### Zeppelin
Import notebook.
Basic select queries for now:
%jdbc(phoenix)
`select * from PROPERTIES limit 10`

