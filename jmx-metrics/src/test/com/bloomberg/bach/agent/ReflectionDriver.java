
package com.bloomberg.bach.agent;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import com.bloomberg.bach.agent.BachMetricsServer;

/**
 * @author Ronald Macmaster
 * Test driver to test out some of my refecltion methods.
 */
public class ReflectionDriver {
	
	/**
	 * Constructs a new ReflectionDriver
	 */
	public ReflectionDriver() {
		// default constructor.
	}
	
	@Test
	public void testClassRetrival() {
		Class<?> clazz = BachMetricsServer.class;
		System.out.format("%s.class.getDeclaredClasses(): %s%n", clazz.getSimpleName(), Arrays.asList(clazz.getDeclaredClasses()));
		System.out.format("%s.class.getClasses(): %s%n", clazz.getSimpleName(), Arrays.asList(clazz.getClasses()));
		System.out.format("%s.class.getDeclaredFields(): %s%n", clazz.getSimpleName(), Arrays.asList(clazz.getDeclaredFields()));
		System.out.format("%s.class.getDeclaredMethods(): %s%n", clazz.getSimpleName(), Arrays.asList(clazz.getDeclaredMethods()));
		System.out.format("%s.class.getDeclaredConstructors(): %s%n", clazz.getSimpleName(), Arrays.asList(clazz.getDeclaredConstructors()));
		
		System.out.println("\n\nInheritance Path: ");
		System.out.println(getInheritancePath(clazz).stream().map(Object::toString).collect(Collectors.joining(" <= ")).toString());
		
		assertEquals(new byte[1024][1024][1024].getClass(), byte[][][].class);
		assertEquals(void.class, Void.TYPE);
	}
	
	private static List<Class<?>> getInheritancePath(Class<?> clazz) {
		List<Class<?>> path = new ArrayList<Class<?>>();
		while (clazz != null) {
			path.add(clazz);
			clazz = clazz.getSuperclass();
			
		}
		return path;
	}
	
}
