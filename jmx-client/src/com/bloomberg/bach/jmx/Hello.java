package com.bloomberg.bach.jmx;

import java.lang.management.ManagementFactory;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

/**
 * @author Ronald Macmaster
 * TODO document
 */
public class Hello implements HelloMBean {
	
	public static void main(String[] args)
			throws MalformedObjectNameException, InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException, InterruptedException {
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		ObjectName name = new ObjectName("com.bloomberg.bach.jmx:type=Hello");
		Hello mbean = new Hello();
		mbs.registerMBean(mbean, name);
		Thread.sleep(Long.MAX_VALUE);
	}
	
	private final String name = "Ronny";
	private int cacheSize = 200;
	
	/* (non-Javadoc)
	 * @see com.bloomberg.bach.jmx.HelloMBean#sayHello()
	 */
	@Override
	public void sayHello() {
		System.out.println("Hello world!");
	}
	
	/* (non-Javadoc)
	 * @see com.bloomberg.bach.jmx.HelloMBean#add(int, int)
	 */
	@Override
	public int add(int x, int y) {
		return x + y;
	}
	
	/* (non-Javadoc)
	 * @see com.bloomberg.bach.jmx.HelloMBean#getName()
	 */
	@Override
	public String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see com.bloomberg.bach.jmx.HelloMBean#getCacheSize()
	 */
	@Override
	public int getCacheSize() {
		return cacheSize;
	}
	
	/* (non-Javadoc)
	 * @see com.bloomberg.bach.jmx.HelloMBean#setCacheSize()
	 */
	@Override
	public void setCacheSize(int size) {
		cacheSize = size;
		
	}
	
}
