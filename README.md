# app
this is simple applicatopn apache spark with apache kafka with mode streaming and datastax for connection cassandra with java and apache spark
for start  test
---mvn package--- 
run in spark home
bin/spark-submit --master spark://Sbenaoua-PC:7077  --class  SparkStream  app-1.0-SNAPSHOT-jar-with-dependencies.jar 127.0.0.1 0 page_visits
