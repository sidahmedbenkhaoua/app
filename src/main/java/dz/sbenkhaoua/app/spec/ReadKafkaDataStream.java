package dz.sbenkhaoua.app.spec;

import com.datastax.driver.core.*;
import com.datastax.driver.mapping.MappingSession;
import com.google.common.collect.Lists;
import dz.sbenkhaoua.app.model.CarMapper;
import dz.sbenkhaoua.app.model.CarRoadMapper;
import dz.sbenkhaoua.app.model.RoadMapper;
import dz.sbenkhaoua.app.tools.PKeyGenerator;
import dz.sbenkhaoua.app.tools.Segment;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;


import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.codehaus.jackson.map.ObjectMapper;
import scala.Tuple2;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;


import static com.datastax.spark.connector.japi.CassandraJavaUtil.javaFunctions;
import static com.datastax.spark.connector.japi.CassandraJavaUtil.mapToRow;
import com.datastax.spark.connector.japi.CassandraJavaUtil;
import com.datastax.spark.connector.japi.CassandraRow;
import com.datastax.spark.connector.japi.SparkContextJavaFunctions;

// Import Row.


/**
 * Created by sbenkhaoua on 27/04/15.
 */
public class ReadKafkaDataStream implements Serializable {
    private List<CarMapper> carMapperList;
    private ObjectMapper mapper;
    private CarMapper carMapper;
    private Session session;
    private JavaSparkContext jsc;
    private static final  String SERVER_IP = "127.0.0.1";
    private static final  String KEY_SPACE="roadtraffic";
    private Cluster cluster;
    private MappingSession mappingSession;

    public ReadKafkaDataStream() {
//        cluster = Cluster.builder().addContactPoints(SERVER_IP)
//                .build();
//        session = cluster.connect(KEY_SPACE);
//        mappingSession = new MappingSession("roadtraffic", session);
    }

    public void reviceKafkaDataStream(JavaPairReceiverInputDStream dStream,  JavaSparkContext sparkContext) {

        JavaDStream<CarMapper> data = dStream.map(new Function<Tuple2<String, String>, CarMapper>() {
                                                      public CarMapper call(Tuple2<String, String> message) throws IOException {
                                                          mapper = new ObjectMapper();
                                                          carMapper = new CarMapper();
                                                          carMapper = mapper.readValue(message._2(), CarMapper.class);
                                                          showmessage(carMapper);
                                                          return carMapper;
                                                      }
                                                  }
        );

        //checkAndInsertInCarRoad(carMapper, sparkContext);
        System.out.println("krahte !!");
        //data.print();
        javaFunctions(data).writerBuilder("roadtraffic", "car", mapToRow(CarMapper.class)).saveToCassandra();


    }
    public  void showmessage(CarMapper carMapper) {
        cluster = Cluster.builder().addContactPoints(SERVER_IP)
                .build();
        session = cluster.connect(KEY_SPACE);
        mappingSession = new MappingSession("roadtraffic", session);
        System.out.printf(carMapper.getCarName());
        ResultSet rs = session.execute("SELECT * FROM road");
        List<RoadMapper> result = mappingSession.getFromResultSet(RoadMapper.class, rs);
        for (RoadMapper road : result) {
            Map<String, Double> mapRoad = new HashMap<String, Double>();
            mapRoad.put("source_side_x", Double.parseDouble(road.getSourceSideX()));
            mapRoad.put("target_side_x", Double.parseDouble(road.getTargetSideX()));
            mapRoad.put("source_side_y", Double.parseDouble(road.getSourceSideY()));
            mapRoad.put("targer_side_y", Double.parseDouble(road.getTargetSideY()));
            Map<String, Double> mapCar = new HashMap<String, Double>();
            mapCar.put("posX", Double.parseDouble(carMapper.getPosX()));
            mapCar.put("posY", Double.parseDouble(carMapper.getPosY()));
            //begin test i am bad programmer
            System.out.println("start analyse !");
            Segment segmentX=new Segment(mapRoad.get("source_side_x"),mapRoad.get("target_side_x"));
            Segment segmentY=new Segment(mapRoad.get("source_side_y"),mapRoad.get("targer_side_y"));
            if(segmentX.appartient(mapCar.get("posX")) && segmentY.appartient(mapCar.get("posY"))){
                System.out.println("find it ");
                System.out.println("save start");
                Timestamp myTimestamp=new Timestamp(new Date().getTime());
                String dateInsert = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(myTimestamp);
                CarRoadMapper carRoadMapper =new CarRoadMapper();
                carRoadMapper.setCarId(carMapper.getId());
                carRoadMapper.setId(PKeyGenerator.get());
                carRoadMapper.setRoadId(road.getId());
                carRoadMapper.setLeaveDate("not_yet");
                carRoadMapper.setInsertDate(dateInsert);
                mappingSession.save(carRoadMapper);
                System.out.println("save end");
            }
//            if (mapCar.get("posX") > mapRoad.get("source_side_x") && mapCar.get("posX") < mapRoad.get("target_side_x")) {
//                if (mapCar.get("posY") > mapRoad.get("source_side_y") && mapCar.get("posY") < mapRoad.get("targer_side_y")) {
//                    System.out.println("trouvé");
//                }
//            } else {
//                if (mapCar.get("posX") < mapRoad.get("source_side_x") && mapCar.get("posX") > mapRoad.get("target_side_x")) {
//                    if (mapCar.get("posY") < mapRoad.get("source_side_y") && mapCar.get("posY") > mapRoad.get("targer_side_y")) {
//                        System.out.println("trouvé");
//                    }
//                }
//                System.out.println(road.getRoadName());
//            }
            System.out.println("end analyse !");
        }


    }

    public void checkAndInsertInCarRoad(CarMapper carMapper, JavaSparkContext sparkContext) {
        SparkContextJavaFunctions functions = CassandraJavaUtil.javaFunctions(sparkContext);
        JavaRDD<CassandraRow> rdd = functions.cassandraTable("roadtraffic", "road");
        JavaPairRDD<String, Integer> sizes = rdd.groupBy( new Function<CassandraRow, String>() {

            public String call(CassandraRow row) throws Exception {
                return row.getString("road_name");
            }
        }).
                mapToPair(new PairFunction<Tuple2<String, Iterable<CassandraRow>>, String, Integer>() {

                    public Tuple2<String, Integer> call(Tuple2<String, Iterable<CassandraRow>> t) throws Exception {
                        return new Tuple2<String, Integer>(t._1(), Lists.newArrayList(t._2()).size());
                    }
                });
        sizes.cache();

        List<Tuple2<String, Integer>> sizesResults = sizes.collect();
        System.out.println("logger ");
        for(Tuple2<String, Integer> tuple : sizesResults){
            System.out.println(tuple._1() + " : " + tuple._2());
        }



//        JavaCassandraSQLContext cassandraSQLContext = new JavaCassandraSQLContext(sparkContext);
//       // JavaRDD teenagers = sqlContext.sql("SELECT name FROM people WHERE age >= 13 AND age <= 19");
//        JavaSchemaRDD metricRDD = cassandraSQLContext.sql("select road_name from roadtraffic.road where road_name like 'road%'");
//        List<String> teenagerNames = (List<String>) (List<String>) metricRDD.map(new Function<Row, String>() {
//
//            public String call(Row row) throws Exception {
//                System.out.println("road "+row.getString(0));
//                return "Name: " + row.getString(0);
//            }
//        });

    }
}
