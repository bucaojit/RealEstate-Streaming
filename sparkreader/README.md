Commands to run sparkreader example:

Generate some words to count:

/usr/hdp/current/kafka-broker/bin/kafka-console-producer.sh --broker-list myhdp.oliversanalytics:6667 --topic wordcount
Enter Words to count.


Executing the spark-submit, using yarn-cluster
/usr/hdp/current/spark-client/bin/spark-submit --packages org.apache.spark:spark-streaming-kafka_2.10:1.6.0 --class "KafkaWordCount" --master yarn-cluster ~/RealEstate-Streaming/sparkreader/target/scala-2.10/realestate-spark-reader_2.10-1.0.jar localhost:2181 mygroup wordcount 1

-- Building the Code --
sbt package

NOTES: Needed to manually add the snappy 1.1.1.6 jar to .m2 
    
