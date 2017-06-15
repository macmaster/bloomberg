package com.bloomberg.bach.jmx;

/**
 * @author Ronald Macmaster
 * example interface for an MBean
 */
public interface HelloMBean {
	
	public void sayHello();
	
	public int add(int x, int y);
	
	public String getName();
	
	public int getCacheSize();
	
	public void setCacheSize(int size);
	
}
