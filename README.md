### RealEstate-Streaming
Stream data from Trulia api using Nifi and insert into Phoenix -- Components: Nifi, Kafka, Storm, Phoenix, Zeppelin

Trulia (data source) -> Nifi -> Kafka -> Storm -> Phoenix -> Zeppelin (UI)

#####Setup

######Nifi 
Import template, setup and enable StandardSSLContextService

######Kafka
Create PROPERTIES topic:
./kafka-topics.sh --zookeeper localhost:2181 --create --partitions 1 --topic properties --replication-factor 1

######Storm
Build storm job with Maven: mvn clean package -DskipTests
Upload Storm job:
$ storm jar target/realestate-streaming-1.0-SNAPSHOT.jar com.oliver.streaming.impl.topologies.KafkaPhoenixTopology  <any file>

######Phoenix
Make sure Phoenix is enabled in HBase configuration

######Zeppelin
Import notebook.
Basic select queries for now:
%jdbc(phoenix)
select title, description from PROPERTIES limit 10

