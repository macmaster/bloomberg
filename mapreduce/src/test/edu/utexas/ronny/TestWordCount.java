package test.edu.utexas.ronny;

import java.io.IOException;
import java.util.Arrays;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Test;

import edu.utexas.ronny.map.WordCountMapper;
import edu.utexas.ronny.reduce.WordCountReducer;

/**
 * @author Ronald Macmaster
 * Test Driver for the Word Count job.
 */
public class TestWordCount {
	
	/**
	 * Constructs a new TestWordCount
	 */
	public TestWordCount() {
		// TODO Auto-generated constructor stub
	}
	
	@Test
	public void testMap() throws IOException {
		Text input = new Text("the quick brown fox jumped over the lazy dog. hahaha!");
		new MapDriver<LongWritable, Text, Text, LongWritable>() //
				.withMapper(new WordCountMapper()) //
				.withInput(new LongWritable(0), input) // 
				.withOutput(new Text("the"), new LongWritable(1)) //
				.withOutput(new Text("quick"), new LongWritable(1)) //
				.withOutput(new Text("brown"), new LongWritable(1)) //
				.withOutput(new Text("fox"), new LongWritable(1)) //
				.withOutput(new Text("jumped"), new LongWritable(1)) //
				.withOutput(new Text("over"), new LongWritable(1)) //
				.withOutput(new Text("the"), new LongWritable(1)) //
				.withOutput(new Text("lazy"), new LongWritable(1)) //
				.withOutput(new Text("dog"), new LongWritable(1)) //
				.withOutput(new Text("hahaha"), new LongWritable(1)) //
				.runTest(true);
	}
	
	@Test
	public void testReduce() throws IOException {
		new ReduceDriver<Text, LongWritable, Text, LongWritable>() //
				.withReducer(new WordCountReducer()) //
				.withInput(new Text("the"), Arrays.asList(new LongWritable(1), new LongWritable(1))) //
				.withInput(new Text("quick"), Arrays.asList(new LongWritable(1))) //
				.withInput(new Text("brown"), Arrays.asList(new LongWritable(1))) //
				.withInput(new Text("fox"), Arrays.asList(new LongWritable(1))) //
				.withInput(new Text("jumped"), Arrays.asList(new LongWritable(1))) //
				.withInput(new Text("over"), Arrays.asList(new LongWritable(1))) //
				.withInput(new Text("lazy"), Arrays.asList(new LongWritable(1))) //
				.withInput(new Text("dog"), Arrays.asList(new LongWritable(1))) //
				.withInput(new Text("hahaha"), Arrays.asList(new LongWritable(1))) //
				.withOutput(new Text("the"), new LongWritable(2)) //
				.withOutput(new Text("quick"), new LongWritable(1)) //
				.withOutput(new Text("brown"), new LongWritable(1)) //
				.withOutput(new Text("fox"), new LongWritable(1)) //
				.withOutput(new Text("jumped"), new LongWritable(1)) //
				.withOutput(new Text("over"), new LongWritable(1)) //
				.withOutput(new Text("lazy"), new LongWritable(1)) //
				.withOutput(new Text("dog"), new LongWritable(1)) //
				.withOutput(new Text("hahaha"), new LongWritable(1)) //
				.runTest(true);
		
	}
	
}
