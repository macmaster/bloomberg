package com.bloomberg.bach.hdfs;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

/**
 * @author Ronald Macmaster
 * Sample driver to access an HDFS File.
 */
public class HDFSFile implements Closeable {
	
	public static final Path HADOOP_CONF = new Path("/home/hadoop/hadoop-2.8.0/etc/hadoop", "core-site.xml");
	
	private Path path; // path to file
	
	private Configuration config;
	private FileSystem dfs;
	// private FSDataOutputStream outputStream;
	
	/**
	 * Constructs a new HDFSFile
	 */
	public HDFSFile(Path path) throws IOException {
		this.path = path;
		this.config = new Configuration();
		this.config.addResource(HADOOP_CONF);
		open();
	}
	
	/**
	 * Constructs a new HDFSFile
	 */
	public HDFSFile(String filePath) throws IOException {
		this(new Path(filePath));
	}
	
	/**
	 * prints the file to stdout <br>
	 */
	public void cat() {
		try (FSDataInputStream inputStream = dfs.open(path)) { // attempt to stream the file to stdout.
			IOUtils.copyBytes(inputStream, System.out, config);
		} catch (Exception err) {
			err.printStackTrace();
		}
	}
	
	/**
	 * returns a classical InputStreamReader for the file. <br>
	 */
	public InputStreamReader getStreamReader() throws IOException {
		FSDataInputStream inputStream = dfs.open(path);
		InputStreamReader reader = new InputStreamReader(inputStream);
		return reader;
	}
	
	/**
	 * returns a classical InputStreamReader for the file. <br>
	 */
	public OutputStreamWriter getStreamWriter() throws IOException {
		FSDataOutputStream outputStream = dfs.create(path, () -> {
			System.out.println(".");
		});
		OutputStreamWriter writer = new OutputStreamWriter(outputStream);
		return writer;
	}
	
	/**
	 * call this before using the hdfs file. connects to hdfs. <br>
	 * @throws IOException 
	 */
	private void open() throws IOException {
		this.dfs = FileSystem.get(config);
	}
	
	/* (non-Javadoc)
	 * @see java.io.Closeable#close()
	 */
	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}
	
}
