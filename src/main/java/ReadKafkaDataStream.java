import dz.sbenkhaoua.mapper.model.CarMapper;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.codehaus.jackson.map.ObjectMapper;
import scala.Tuple2;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.datastax.spark.connector.japi.CassandraJavaUtil.javaFunctions;
import static com.datastax.spark.connector.japi.CassandraJavaUtil.mapToRow;

/**
 * Created by sbenkhaoua on 27/04/15.
 */
public class ReadKafkaDataStream implements Serializable {
    private List<CarMapper> carMapperList;
    private ObjectMapper mapper;
    private CarMapper carMapper;
    public ReadKafkaDataStream(){
        carMapperList=new ArrayList<CarMapper>();
    }

    public void  reviceKafkaDataStream(JavaPairReceiverInputDStream dStream,JavaSparkContext sparkContext){
        carMapperList=new ArrayList<CarMapper>();
        JavaDStream<CarMapper> data=  dStream.map(new Function< Tuple2<String,String>, CarMapper >()
                    {
                        public CarMapper call(Tuple2<String, String> message) throws IOException {
                            mapper = new ObjectMapper();
                            carMapper = new CarMapper();
                            carMapper = mapper.readValue(message._2(), CarMapper.class);
                            return  carMapper;
                        }
                    }
        );
            if(carMapper!=null){
                checkAndInsertInCarRoad(carMapper,sparkContext);
            }
            javaFunctions(data).writerBuilder("roadtraffic", "car", mapToRow(CarMapper.class)).saveToCassandra();
            data.print();


    }
   public void  checkAndInsertInCarRoad(CarMapper carMapper,JavaSparkContext sparkContext){


   }
}
