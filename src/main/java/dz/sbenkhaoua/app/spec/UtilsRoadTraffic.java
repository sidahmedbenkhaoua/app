package dz.sbenkhaoua.app.spec;

import com.datastax.spark.connector.cql.CassandraConnector;
import dz.sbenkhaoua.app.model.CarMapper;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sbenkhaoua on 27/04/15.
 */
public class UtilsRoadTraffic implements Serializable {

    public UtilsRoadTraffic(){

    }

    public void startStreamingLive(String a1,String a2,Map<String, Integer> a3){
        SparkConf conf = new SparkConf();
        conf.set("spark.cassandra.connection.host", "localhost");
        conf.set("spark.driver.allowMultipleContexts","true");
        JavaSparkContext sparkContext = new JavaSparkContext("local[4]", "Spark Streaming Save Car Data", conf);
        JavaStreamingContext jssc = new JavaStreamingContext(sparkContext, new Duration(1000));
        ReadKafkaDataStream rfds = new ReadKafkaDataStream();
        /* connection to cassandra */
        CassandraConnector connector = CassandraConnector.apply(sparkContext.getConf());
        final List<CarMapper> carMappers = new ArrayList<CarMapper>();
        /* Receive Kafka streaming inputs */
        JavaPairReceiverInputDStream<String, String> messages = KafkaUtils.createStream(jssc, a1, a2, a3);
        rfds.reviceKafkaDataStream(messages,sparkContext);
        jssc.start();
        jssc.awaitTermination();
    }
}
