package dz.sbenkhaoua.app.spec;


import com.datastax.spark.connector.CassandraRow;
import com.datastax.spark.connector.japi.CassandraJavaUtil;
import com.datastax.spark.connector.japi.SparkContextJavaFunctions;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;


import java.io.Serializable;
import java.util.List;

import static com.datastax.spark.connector.japi.CassandraJavaUtil.*;

/**
 * Created by sbenkhaoua on 29/04/15.
 */
public class CarRoadCount implements Serializable {

    public void countCarByRoud(JavaSparkContext sc) {

        SparkContextJavaFunctions functions = CassandraJavaUtil.javaFunctions(sc);
        JavaRDD<com.datastax.spark.connector.japi.CassandraRow> rdd = functions.cassandraTable("roadtraffic", "car_road");
        rdd.cache();
        //khkhk start to filter all rdd with num_save is actual number
        JavaRDD<com.datastax.spark.connector.japi.CassandraRow> rdd1=rdd.filter(new Function<com.datastax.spark.connector.japi.CassandraRow, Boolean>() {
            @Override
            public Boolean call(com.datastax.spark.connector.japi.CassandraRow row) throws Exception {

                return row.getString("insert_order").equals("0") ? true:false;
            }
        });
        JavaPairRDD<String,String> roadCar1=  rdd1.mapToPair(new PairFunction<com.datastax.spark.connector.japi.CassandraRow, String, String>() {
            @Override
            public Tuple2 call(com.datastax.spark.connector.japi.CassandraRow row) throws Exception {
                return new Tuple2<String, String>(row.getString("road_id"), row.getString("car_id"));
            }
        });
        JavaPairRDD<String,Integer> roadByCarGroup=roadCar1.groupBy(new Function<Tuple2<String, String>, String>() {
            @Override
            public String call(Tuple2<String, String> stringStringTuple2) throws Exception {
                return stringStringTuple2._1();
            }
        }).mapToPair(new PairFunction<Tuple2<String, Iterable<Tuple2<String, String>>>, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(Tuple2<String, Iterable<Tuple2<String, String>>> stringIterableTuple2) throws Exception {
                return new Tuple2<String, Integer>(stringIterableTuple2._1(), Lists.newArrayList(stringIterableTuple2._2()).size());
            }
        });
        roadCar1.cache();
        List<Tuple2<String, Integer>> sizesResults = roadByCarGroup.collect();
        for(Tuple2<String, Integer> tuple : sizesResults){
            System.out.println(tuple._1() + " : " + tuple._2());
        }
        //start to get pairRDD for road with number insertion

        /*
        *
        * */

 //        JavaPairRDD<String, Integer> sizes = rdd.groupBy(new Function<com.datastax.spark.connector.japi.CassandraRow, String>() {
//            @Override
//            public String call(com.datastax.spark.connector.japi.CassandraRow row) throws Exception {
//             return  row.getString("road_id");
//            }
//        }).mapToPair(new PairFunction<Tuple2<String, Iterable<com.datastax.spark.connector.japi.CassandraRow>>, String, Integer>() {
//            @Override
//            public Tuple2<String, Integer> call(Tuple2<String, Iterable<com.datastax.spark.connector.japi.CassandraRow>> t) throws Exception {
//                return new Tuple2<String, Integer>(t._1(), Lists.newArrayList(t._2()).size());
//            }
//                });
//        sizes.cache();
//        List<Tuple2<String, Integer>> sizesResults = sizes.collect();
//        System.out.println("road _id");
//        for(Tuple2<String, Integer> tuple : sizesResults){
//            System.out.println(tuple._1() + " : " + tuple._2());
//        }


    }
}
