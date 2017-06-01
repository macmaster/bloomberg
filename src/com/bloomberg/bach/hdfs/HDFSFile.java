package com.bloomberg.bach.hdfs;

import java.io.Closeable;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DistributedFileSystem;

/**
 * @author Ronald Macmaster
 * Sample driver to access an HDFS File.
 */
public class HDFSFile implements Closeable {
	
	public static final Path HADOOP_CONF = new Path("/home/hadoop/hadoop-2.8.0/etc/hadoop", "core-site.xml");
	
	private Configuration config;
	private FileSystem dfs;
	
	/**
	 * Constructs a new HDFSFile
	 */
	public HDFSFile(String path) {
		dfs = new DistributedFileSystem();
		config = new Configuration();
		config.addResource(HADOOP_CONF);
		open();
	}
	
	/**
	 * call this before using the hdfs file. connects to hdfs. <br>
	 */
	private void open() {
		
	}
	
	/* (non-Javadoc)
	 * @see java.io.Closeable#close()
	 */
	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}
	
}
