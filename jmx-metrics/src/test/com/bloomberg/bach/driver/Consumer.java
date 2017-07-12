package com.bloomberg.bach.driver;

import java.util.ArrayList;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 * @author Ronald Macmaster
 * TODO document
 */
public class Consumer {
	
	private static KafkaConsumer<String, String> consumer = null;
	
	/**
	 * Constructs a new Consumer
	 */
	@SuppressWarnings("serial")
	public static void main(String[] args) {
		Properties properties = new Properties();
		properties.setProperty("group.id", "test");
		properties.setProperty("bootstrap.servers", "localhost:9092");
		properties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		properties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		consumer = new KafkaConsumer<String, String>(properties);
		/* @formatter:off */ consumer.subscribe(new ArrayList<String>(){{add("ronny");}}); /* @formatter:on */
		Runtime.getRuntime().addShutdownHook(new ShutdownHandler());
		
		int msg = 0;
		while (true) {
			for (ConsumerRecord<String, String> record : consumer.poll(200)) {
				System.out.format("%d) (%s) %s : %s %n", ++msg, record.timestamp(), record.key(), record.value());
			}
		}
	}
	
	private static class ShutdownHandler extends Thread {
		
		public void run() {
			System.out.println("shutting down...");
			consumer.close();
		}
	}
	
}
