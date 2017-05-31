package com.bloomberg.bach.hbase;

import java.io.Closeable;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * @author Ronald Macmaster
 * Interface to HBase table of city locations.
 */
public class LocationTable implements Closeable {
	
	// configuration files.
	public static final Path HBASE_CONF = new Path("/home/hadoop/hbase-1.3.1/conf", "hbase-site.xml");
	public static final Path HADOOP_CONF = new Path("/home/hadoop/hadoop-2.8.0/etc/hadoop", "core-site.xml");
	
	// HBase table connection.
	private static final TableName TABLE_NAME = TableName.valueOf("Locations");
	private Table table;
	private Connection conn;
	private Configuration config;
	
	/**
	 * Constructs a new LocationTable. 
	 * Call connect before using.
	 * @throws IOException 
	 */
	public LocationTable() throws IOException {
		config = HBaseConfiguration.create();
		config.addResource(HBASE_CONF);
		config.addResource(HADOOP_CONF);
		connect();
	}
	
	/**
	 * store a location in the database. <br>
	 */
	public void putLocation(Location location) throws IOException {
		Put put = new Put(Bytes.toBytes(location.getRowTag()));
		put.addColumn(Bytes.toBytes("name"), Bytes.toBytes("country"), Bytes.toBytes(location.getCountryCode()));
		put.addColumn(Bytes.toBytes("name"), Bytes.toBytes("ascii"), Bytes.toBytes(location.getAsciiName()));
		put.addColumn(Bytes.toBytes("name"), Bytes.toBytes("city"), Bytes.toBytes(location.getCityName()));
		put.addColumn(Bytes.toBytes("name"), Bytes.toBytes("region"), Bytes.toBytes(location.getRegion()));
		
		put.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("population"), Bytes.toBytes(location.getPopulation()));
		put.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("longitude"), Bytes.toBytes(location.getLongitude()));
		put.addColumn(Bytes.toBytes("meta"), Bytes.toBytes("latitude"), Bytes.toBytes(location.getLatitude()));
		table.put(put);
	}
	
	/**
	 * call this before performing table operations.
	 * @throws IOException 
	 */
	private void connect() throws IOException {
		conn = ConnectionFactory.createConnection(config);
		table = conn.getTable(TABLE_NAME);
	}
	
	/**
	 * call this when finished with table. cleans up resources.
	 * @throws IOException 
	 */
	@Override
	public void close() throws IOException {
		conn.close();
	}
	
}
