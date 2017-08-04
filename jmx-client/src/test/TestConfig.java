package test;

import org.apache.hadoop.conf.Configuration;

public class TestConfig {
  public static void main (String[] args){
    String key = "hbase.client.metrics.enable", value = "true";
    // System.getProperties().list(System.out);
    System.out.println(System.getenv().toString());
    Configuration.addDefaultResource("bach-site.xml");
//    TestConfig.class.getResource("bach-site.xml").getPath();
    Configuration config = new Configuration();
    //config.set(key, "false");
    System.out.format("%s = %s%n", key, config.get(key));
  }
}
