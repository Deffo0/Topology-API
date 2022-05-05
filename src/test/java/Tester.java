import org.junit.Assert;
import org.junit.Test;

public class Tester {
    @Test
    public void readJSONTest()  {
        API api = new API();
        Assert.assertEquals(1, api.readJSON("C:\\Users\\ahmed\\Topology API\\src\\main\\resources\\topology.json"));
        Assert.assertEquals("res1", api.queryTopologies().get(0).getComponents().get(0).getId());
        Assert.assertEquals(10.0, api.queryTopologies().get(0).getComponents().get(0).getSpecifications().getMin(), 0.00001);
        Assert.assertEquals(1000.0, api.queryTopologies().get(0).getComponents().get(0).getSpecifications().getMax(), 0.00001);
        Assert.assertEquals(100.0, api.queryTopologies().get(0).getComponents().get(0).getSpecifications().getDefault(), 0.00001);
        Assert.assertEquals(2.0, api.queryTopologies().get(0).getComponents().get(1).getSpecifications().getMax(), 0.00001);
        Assert.assertEquals("m1", api.queryTopologies().get(0).getComponents().get(1).getId());
    }
    @Test
    public void writeJSONTest()  {
        API api = new API();
        api.readJSON("C:\\Users\\ahmed\\Topology API\\src\\main\\resources\\topology.json");
        Assert.assertEquals(1, api.writeJSON("top1"));


    }
    @Test
    public void deleteTopologyTest()  {
        API api = new API();
        api.readJSON("C:\\Users\\ahmed\\Topology API\\src\\main\\resources\\topology.json");
        Assert.assertEquals(1, api.queryTopologies().size());
        Assert.assertEquals(1, api.deleteTopology("top1"));
        Assert.assertEquals(0, api.queryTopologies().size());

    }
    @Test
    public void queryDevicesTest()  {
        API api = new API();
        api.readJSON("C:\\Users\\ahmed\\Topology API\\src\\main\\resources\\topology.json");
        api.readJSON("C:\\Users\\ahmed\\Topology API\\src\\main\\resources\\topology2.json");
        Assert.assertEquals(3, api.queryDevices("bottom1").size());
        Assert.assertEquals(2, api.queryDevices("top1").size());


    }
    @Test
    public void queryDevicesWithNetlistNodeTest()  {
        API api = new API();
        api.readJSON("C:\\Users\\ahmed\\Topology API\\src\\main\\resources\\topology2.json");
        Assert.assertEquals(2, api.queryDevicesWithNetlistNode("bottom1", "n2").size());
        Assert.assertEquals("res2", api.queryDevicesWithNetlistNode("bottom1", "n2").get(0).getId());



    }
}
