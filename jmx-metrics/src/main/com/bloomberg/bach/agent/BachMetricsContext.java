package com.bloomberg.bach.agent;

import java.io.IOException;
import java.lang.instrument.Instrumentation;

import org.apache.hadoop.metrics2.lib.DefaultMetricsSystem;

/**
 * @author Ronald Macmaster
 * Context Package for Bach Metrics package.
 * Manages a singleton context.
 */
public class BachMetricsContext {

  // singleton context object.
  private static volatile BachMetricsContext INSTANCE = null;

  // metrics reporting via JMX and metrics2
  private static BachMetricsServer server = null;

  /**
   * Agent Method.
   * Called before the program's main method is called when run in agent mode.
   * Run as an agent by passing -javaagent:jarpath[=options] as an agent.
   */
  public static void premain(String args, Instrumentation inst) {
    System.out.println("Running the Bach Metrics Context in agent mode...");
    BachMetricsContext.start();
  }

  private BachMetricsContext() {
    try{
      DefaultMetricsSystem.initialize("client");
    } catch (NoClassDefFoundError error){
      System.err.println("Hadoop Libraries not loaded.");
      System.err.println("Could not load DefaultMetricsSystem class.");
      System.err.println("Continuing anyway...");
    }
  }

  private static synchronized BachMetricsContext registerContext() {
    // return singleton context.
    return (INSTANCE == null) ? (INSTANCE = new BachMetricsContext()) : INSTANCE;
  }

  /**
   * Start metrics JSON service on localhost and an arbitrary port.  <br>
   */
  public static void start() {
    start(null, null);
  }

  /**
   * Start the metrics JSON service on the given host and port. <br>
   */
  public static void start(String host, Integer port) {
    try {
      // System.getProperties().list(System.out);		
      server = new BachMetricsServer(host, port);
      server.start();

      // initialize metrics system.
      BachMetricsContext.registerContext();
      System.out.format("Bach Metrics Context: started on  <%s, %d> %n", server.getHost(), server.getPort());
    } catch (IOException error) {
      System.err.println("Failed to start Bach Metrics Context!");
      error.printStackTrace();
    }
  }

  /**
   * Stop the metrics context. <br>
   */
  public static void stop() throws IOException {
    server.stop();
  }

  /**
   * returns the current port of operation. <br>
   */
  public static Integer getPort() {
    if (server != null) {
      return server.getPort();
    } else {
      throw new IllegalStateException("Metrics Context has not been started!");
    }
  }

  /**
   * returns the current host of operation. <br>
   */
  public static String getHost() {
    if (server != null) {
      return server.getHost();
    } else {
      throw new IllegalStateException("Metrics Context has not been started!");
    }
  }

}
