#!/bin/bash

set -x

ARGS=()
while [[ $# -gt 0 ]]; do
  case $1 in
    -v|--verbose)
      echo "verbose"
      shift
      ;;
    -a|--admin)
      admin=$2
      shift; shift
      ;;
    -f|--file)
      csv=$2
      shift; shift
      ;;
    -c|--columns)
      columns=$2
      shift; shift
      ;;
    -u|--user)
      user=$2
      shift; shift
      ;;
    *)
      ARGS+=($1)
      shift
      ;;
  esac
done

set -- ${POSITIONAL[@]}

csv=${csv-overdose.csv}
columns=${columns-$(head -n1 $csv | sed -e 's/^,//')}

echo "csv: $csv"
echo "columns: $columns"

if [[ -n $admin && -n $user ]]; then
  sudo -u $admin hbase shell <<<"
    grant '$user', 'RWCXA'
  "
fi

# create the hbase table
hbase shell <<<"
disable 'overdose'
drop 'overdose'
create 'overdose', 'metadata', 'personal', 'residence', 'death', 'drug', 'manner'
exit
"

# import the hbase table from hdfs
overdose_csv="/tmp/$csv"
hdfs dfs -copyFromLocal -f $csv $overdose_csv 
hbase org.apache.hadoop.hbase.mapreduce.ImportTsv \
  -Dimporttsv.separator=, -Dimporttsv.columns="HBASE_ROW_KEY,$columns" overdose hdfs://Test-Laptop:$overdose_csv
