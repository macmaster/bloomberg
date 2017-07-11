package com.bloomberg.bach.metrics;

import java.util.ArrayList;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 * @author Ronald Macmaster
 * TODO document
 */
public class Consumer {
	
	/**
	 * Constructs a new Consumer
	 */
	public static void main(String[] args) {
		Properties properties = new Properties();
		properties.setProperty("group.id", "test");
		properties.setProperty("bootstrap.servers", "localhost:9092");
		properties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		properties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);
		/* @formatter:off */ consumer.subscribe(new ArrayList<String>(){{add("ronny");}}); /* @formatter:on */
		
		int msg = 0;
		while (true) {
			for (ConsumerRecord<String, String> record : consumer.poll(200)) {
				System.out.format("%d) (%s) %s : %s %n", ++msg, record.timestamp(), record.key(), record.value());
			}
		}
		
	}
	
}
