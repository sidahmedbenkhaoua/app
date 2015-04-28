import dz.sbenkhaoua.app.spec.UtilsRoadTraffic;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sbenkhaoua on 26/04/15.
 */
public class SparkStream {
    public static void main(String args[]) {
        if (args.length != 3) {
            System.out.println("SparkStream <zookeeper_ip> <group_nm> <topic1,topic2,...>");
            System.exit(1);
        }
        Logger.getLogger("org").setLevel(Level.OFF);
        Logger.getLogger("akka").setLevel(Level.OFF);
        Map<String, Integer> topicMap = new HashMap<String, Integer>();
        String[] topic = args[2].split(",");
        for (String t : topic) {
            topicMap.put(t, new Integer(3));
        }
        UtilsRoadTraffic utilsRoadTraffic=new UtilsRoadTraffic();
        /* Connection to Spark */
        utilsRoadTraffic.startStreamingLive(args[0],args[1],topicMap);
    }
}
