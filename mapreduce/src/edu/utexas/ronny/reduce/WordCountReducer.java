package edu.utexas.ronny.reduce;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * @author Ronald Macmaster
 * Reducer class for the word count job.
 * reduces (word, list(count)) -> (word, count)
 */
public class WordCountReducer extends Reducer<Text, LongWritable, Text, LongWritable> {
	
	/* (non-Javadoc)
	 * @see org.apache.hadoop.mapreduce.Reducer#reduce(java.lang.Object, java.lang.Iterable, org.apache.hadoop.mapreduce.Reducer.Context)
	 */
	@Override
	protected void reduce(Text key, Iterable<LongWritable> values, Reducer<Text, LongWritable, Text, LongWritable>.Context context) throws IOException, InterruptedException {
		int count = 0;
		for (LongWritable value : values) {
			count += value.get();
		}
		
		// emit word count.
		context.write(key, new LongWritable(count));
	}
	
}
