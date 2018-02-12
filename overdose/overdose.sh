#!/bin/bash

cat <<EOT > overdose.hsh
disable 'overdose'
drop 'overdose'
create 'overdose', 'metadata', 'personal', 'residence', 'death', 'drug', 'manner'
exit
EOT

columns=`head -n1 overdose.csv` && columns=${columns#,}
hdfs dfs -copyFromLocal -f overdose.csv /tmp/overdose.csv 
hbase shell overdose.hsh
hbase org.apache.hadoop.hbase.mapreduce.ImportTsv \
  -Dimporttsv.separator=, -Dimporttsv.columns="HBASE_ROW_KEY,$columns" overdose hdfs://Test-Laptop:/tmp/overdose.csv

rm overdose.hsh
