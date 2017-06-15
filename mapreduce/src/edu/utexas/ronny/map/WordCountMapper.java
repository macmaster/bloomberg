package edu.utexas.ronny.map;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * @author Ronald Macmaster
 * Mapper class for the word count job.
 * maps (lineNum, line) -> list((word, 1))
 */
public class WordCountMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
	
	/* (non-Javadoc)
	 * @see org.apache.hadoop.mapreduce.Mapper#map(java.lang.Object, java.lang.Object, org.apache.hadoop.mapreduce.Mapper.Context)
	 */
	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, LongWritable>.Context context) throws IOException, InterruptedException {
		// emit word count pairs (word, 1)
		String line = value.toString();
		for (String word : line.split("\\W+")) {
			context.write(new Text(word), new LongWritable(1));
		}
	}
	
}
